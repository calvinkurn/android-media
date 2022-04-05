package com.tokopedia.tokopatch

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.tokopatch.domain.data.DataResponse
import com.tokopedia.tokopatch.domain.data.DataStatus
import com.tokopedia.tokopatch.domain.data.RobustDatabase
import com.tokopedia.tokopatch.domain.repository.PatchRepository
import com.tokopedia.tokopatch.model.Patch
import com.tokopedia.tokopatch.model.Tester
import com.tokopedia.tokopatch.patch.PatchExecutors
import com.tokopedia.tokopatch.utils.*
import kotlinx.coroutines.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


/**
 * Author errysuprayogi on 23,March,2020
 */
class PatchService(val context: Context) {

    private var repository: PatchRepository
    private var logger: PatchLogger
    private val packageName = Utils.packageName(context)
    private val versionName = Utils.versionName(context)
    private val buildNumber = Utils.versionCode(context)
    private val dataDao = RobustDatabase.getDatabase(context).dataDao()
    private var isProd: Boolean = false
    private val remoteConfig: FirebaseRemoteConfigImpl by lazy { FirebaseRemoteConfigImpl(context) }
    private val gson = GsonBuilder().create()

    init {
        logger = PatchLogger.instance
        repository = PatchRepository.getInstance(dataDao, Utils.versionCode(context))
    }

    companion object {
        private const val PATCHES_CLASS_FULL_NAME = "com.tokopedia.stability.patch.PatchesInfoImpl"
        private const val PROD = "prod"
        private const val TEST = "test"
        private const val DEVICE_ID = "Device ID"
        private const val CONFIG_KEY = "android_robust_tester"
        fun startService(app: Application) {
            val patchService: PatchService by lazy { PatchService(app.applicationContext) }
            patchService.getPatch()
        }
    }

    private fun getPatch() {
        GlobalScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    logger.onStart()
                    val deviceId = Utils.getDeviceId(context)
                    var status = dataDao.getStatus(deviceId)
                    getEnvironment()?.let { environment ->
                        if (status == null) {
                            status = DataStatus(id = deviceId, env = environment)
                            refreshToken(status)
                        } else if (status.expired.toLong() < Date().time) {
                            status.env = environment
                            refreshToken(status)
                        }
                        isProd = environment.equals(PROD)
                        repository.getPatchV2(
                            status.tokenId,
                            packageName,
                            versionName,
                            buildNumber,
                            environment,
                            this@PatchService::onSuccessGetPatch,
                            this@PatchService::onErrorGetPatch
                        )
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                ServerLogger.log(
                    Priority.P1, "ROBUST", mapOf(
                        "type" to "service",
                        "err" to t.message.toString()
                    )
                )
            }
        }
    }

    private suspend fun refreshToken(status: DataStatus) {
        status.tokenId = repository.oauth2(Jwt(status).token(context))
        dataDao.setStatus(status)
    }

    fun getEnvironment(): String? {
        val testers = gson.fromJson<Tester>(
            remoteConfig.getString(CONFIG_KEY),
            object : TypeToken<Tester>() {}.type
        )
        val deviceId = Utils.getDeviceId(context)
        logger.logMessage(context, true,"${DEVICE_ID} : ${deviceId}")
        testers?.let {
            if(it.list.find { item -> item.deviceId == deviceId } != null) {
                logger.showToaster(context, context.getString(R.string.tester_message))
                return TEST
            } else {
                return PROD
            }
        }
        return null
    }

    private fun onErrorGetPatch(t: Throwable) {
        repository.allData?.let {
            val patchList: MutableList<Patch> = mutableListOf()
            it.forEachIndexed { index, result ->
                decodeData(result, patchList)
            }
            PatchExecutors.getInstance(context, patchList, logger).start()
            logger.exceptionNotify(context, t, context.getString(R.string.applied_from_local))
        }
        t.printStackTrace()
    }

    private fun onSuccessGetPatch(data: DataResponse) {
        GlobalScope.launch {
            repository.flush()
            data.result?.let {
                val patchList: MutableList<Patch> = mutableListOf()
                it.forEachIndexed { index, result ->
                    result.uid = index
                    repository.insert(result)
                    decodeData(result, patchList)
                }
                PatchExecutors.getInstance(context, patchList, logger).start()
            }
        }
    }

    private fun decodeData(result: DataResponse.Result, patchList: MutableList<Patch>) {
        try {
            val file = File.createTempFile(result.versionName, ".zip", context.cacheDir)
            val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
            val decodedBytes = Decoder.decrypt(result.signature, result.data)
            decodedBytes?.let {
                bufferedOutputStream.write(decodedBytes)
                val p = Patch()
                p.version = result.versionName
                p.name = result.description
                p.md5 = result.signature
                p.patchesInfoImplClassFullName = PATCHES_CLASS_FULL_NAME
                p.tempPath = file.absolutePath
                p.debug = !isProd
                patchList.add(p)
            }
            try {
                bufferedOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}