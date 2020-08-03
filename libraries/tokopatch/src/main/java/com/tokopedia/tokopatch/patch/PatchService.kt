package com.tokopedia.tokopatch.patch

import android.app.Application
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.tokopatch.domain.data.DataResponse
import com.tokopedia.tokopatch.domain.data.RobustDatabase
import com.tokopedia.tokopatch.domain.repository.PatchRepository
import com.tokopedia.tokopatch.model.Patch
import com.tokopedia.tokopatch.utils.Decoder
import com.tokopedia.tokopatch.utils.PatchLogger
import com.tokopedia.tokopatch.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Author errysuprayogi on 23,March,2020
 */
class PatchService : JobIntentService() {

    private lateinit var allResult: LiveData<List<DataResponse.Result>>
    private lateinit var repository: PatchRepository
    private lateinit var logger: PatchLogger

    companion object {
        private const val JOB_ID = 12131415
        private const val PATCHES_CLASS_FULL_NAME = "com.meituan.robust.patch.PatchesInfoImpl"

        fun startService(app: Application) {
            val work = Intent(app.applicationContext, PatchService::class.java)
            enqueueWork(
                    app.applicationContext, PatchService::class.java,
                    JOB_ID, work
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        logger = PatchLogger()
        logger.logMessage(applicationContext,"P1#ROBUST#PatchService created")
        val dataDao = RobustDatabase.getDatabase(applicationContext).dataDao()
        repository = PatchRepository(
                dataDao,
                Utils.versionCode(applicationContext)
        )
        allResult = repository.allData

        allResult.observe(PatchLifecycle(), Observer {
            val patchList: MutableList<Patch> = mutableListOf()
            it.forEachIndexed { index, result ->
                decodeData(result, patchList)
            }
            PatchExecutors(applicationContext, patchList, logger).start()
        })
    }

    override fun onHandleWork(intent: Intent) {
        val packageName =
                Utils.packageName(applicationContext)
        val versionName =
                Utils.versionName(applicationContext)
        val buildNumber =
                Utils.versionCode(applicationContext)
        repository.getPatch(
                packageName,
                versionName,
                buildNumber,
                this::onSuccessGetPatch,
                this::onErrorGetPatch
        )
    }

    private fun onErrorGetPatch(t: Throwable) {
        t.printStackTrace()
    }

    private fun onSuccessGetPatch(data: DataResponse) {
        GlobalScope.launch {
            repository.flush()
            data.result?.let {
                it.forEachIndexed { index, result ->
                    result.uid = index
                    repository.insert(result)
                }
                logger.onPatchFetched(applicationContext, result = true, isNet = true)
            }
        }

    }

    private fun decodeData(result: DataResponse.Result, patchList: MutableList<Patch>) {
        val decodedBytes = Decoder.decrypt(result.signature, result.data)

        val file = File.createTempFile(result.versionName, ".zip", applicationContext.cacheDir)
        val bufferedOutputStream = BufferedOutputStream(FileOutputStream(file))
        try {
            bufferedOutputStream.write(decodedBytes)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bufferedOutputStream.close()
                val p = Patch()
                p.version = result.versionName
                p.name = result.description
                p.md5 = result.signature
                p.patchesInfoImplClassFullName = PATCHES_CLASS_FULL_NAME
                p.tempPath = file.absolutePath
                patchList.add(p)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        logger.logMessage(applicationContext, "P1#ROBUST#PatchService destroyed")
    }
}