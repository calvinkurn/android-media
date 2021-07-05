package androidx.core.app

import android.app.Application
import android.content.Intent
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.tokopatch.domain.data.DataResponse
import com.tokopedia.tokopatch.domain.data.RobustDatabase
import com.tokopedia.tokopatch.domain.repository.PatchRepository
import com.tokopedia.tokopatch.model.Patch
import com.tokopedia.tokopatch.patch.PatchExecutors
import com.tokopedia.tokopatch.utils.Decoder
import com.tokopedia.tokopatch.utils.PatchLogger
import com.tokopedia.tokopatch.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Author errysuprayogi on 23,March,2020
 */
class PatchService : JobIntentService() {

    private lateinit var repository: PatchRepository
    private lateinit var logger: PatchLogger

    companion object {
        private const val JOB_ID = 12131415
        private const val PATCHES_CLASS_FULL_NAME = "com.meituan.robust.patch.PatchesInfoImpl"

        fun startService(app: Application) {
            try {
                val work = Intent(app.applicationContext, PatchService::class.java)
                enqueueWork(
                        app.applicationContext, PatchService::class.java,
                        JOB_ID, work
                )
            } catch (e: Exception){
                ServerLogger.log(Priority.P1, "ROBUST", mapOf("type" to "service",
                        "err" to e.message.toString()))
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        logger = PatchLogger.instance
        val dataDao = RobustDatabase.getDatabase(applicationContext).dataDao()
        repository = PatchRepository.getInstance(dataDao, Utils.versionCode(applicationContext))
    }

    override fun onHandleWork(intent: Intent) {
        val packageName = Utils.packageName(applicationContext)
        val versionName = Utils.versionName(applicationContext)
        val buildNumber = Utils.versionCode(applicationContext)
        repository.getPatch(
                packageName,
                versionName,
                buildNumber,
                this::onSuccessGetPatch,
                this::onErrorGetPatch
        )
    }

    private fun onErrorGetPatch(t: Throwable) {
        repository.allData?.let {
            val patchList: MutableList<Patch> = mutableListOf()
            it.forEachIndexed { index, result ->
                decodeData(result, patchList)
            }
            PatchExecutors.getInstance(applicationContext, patchList, logger).start()
        }
        logger.exceptionNotify(this, t, "Applied patch from local")
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
                PatchExecutors.getInstance(applicationContext, patchList, logger).start()
            }
        }
    }

    private fun decodeData(result: DataResponse.Result, patchList: MutableList<Patch>) {
        val file = File.createTempFile(result.versionName, ".zip", applicationContext.cacheDir)
        val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
        try {
            val decodedBytes = Decoder.decrypt(result.signature, result.data)
            decodedBytes?.let {
                bufferedOutputStream.write(decodedBytes)
                val p = Patch()
                p.version = result.versionName
                p.name = result.description
                p.md5 = result.signature
                p.patchesInfoImplClassFullName = PATCHES_CLASS_FULL_NAME
                p.tempPath = file.absolutePath
                patchList.add(p)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bufferedOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    internal override fun dequeueWork(): GenericWorkItem? {
        try {
            return super.dequeueWork()
        } catch (ex: SecurityException){
            ex.printStackTrace()
        }
        return null
    }

}