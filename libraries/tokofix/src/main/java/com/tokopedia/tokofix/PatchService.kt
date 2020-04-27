package com.tokopedia.tokofix

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.JobIntentService
import com.meituan.robust.PatchExecutor
import com.tokopedia.tokofix.domain.PatchRepository
import com.tokopedia.tokofix.domain.data.DataResponse
import com.tokopedia.tokofix.patch.PatchLogger
import com.tokopedia.tokofix.patch.PatchManipulatedImp
import timber.log.Timber

/**
 * Author errysuprayogi on 23,March,2020
 */
class PatchService : JobIntentService() {

    private val repository = PatchRepository()
    private lateinit var logger: PatchLogger

    companion object{
        private const val JOB_ID = 12131415
        private const val VERSION = "version"

        fun startService(context: Context, version: String) {
            val work = Intent(context, PatchService::class.java).apply {
                putExtra(VERSION, version)
            }
            enqueueWork(context, PatchService::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        super.onCreate()
        logger = PatchLogger(this)
        Timber.w("P1#ROBUST#PatchService created")
    }

    override fun onHandleWork(intent: Intent) {
        if (intent != null) {
            repository.getPatch(intent.getStringExtra(VERSION), this::onSuccessGetPatch)
        } else {
            PatchExecutor(this, PatchManipulatedImp(logger), logger).start()
        }
    }

    private fun onSuccessGetPatch(data: DataResponse){
        repository.donwloadPatch(this, data.data.downloadUrl, logger)
    }

    override fun onDestroy() {
        Timber.w("P1#ROBUST#PatchService destroyed")
    }
}