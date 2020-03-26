package com.tokopedia.tokofix

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.meituan.robust.PatchExecutor
import com.tokopedia.tokofix.domain.PatchRepository
import com.tokopedia.tokofix.domain.data.DataResponse
import com.tokopedia.tokofix.patch.PatchLogger
import com.tokopedia.tokofix.patch.PatchManipulatedImp
import timber.log.Timber

/**
 * Author errysuprayogi on 23,March,2020
 */
class PatchService : Service() {

    private val repository = PatchRepository()
    private lateinit var logger: PatchLogger

    override fun onCreate() {
        super.onCreate()
        logger = PatchLogger(this)
        Timber.w("P1#ROBUST#PatchService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            repository.getPatch(intent.getStringExtra("version"), this::onSuccessGetPatch)
        } else {
            PatchExecutor(this, PatchManipulatedImp(logger), logger).start()
        }
        return START_STICKY
    }

    private fun onSuccessGetPatch(data: DataResponse){
        repository.donwloadPatch(this, data.data.downloadUrl, logger)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Timber.w("P1#ROBUST#PatchService destroyed")
    }
}