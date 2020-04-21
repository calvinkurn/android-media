package com.tokopedia.tokofix

import android.app.Application
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.tokopedia.tokofix.domain.PatchRepository
import com.tokopedia.tokofix.domain.data.DataResponse
import com.tokopedia.tokofix.patch.PatchLogger


/**
 * Author errysuprayogi on 03,February,2020
 */
class TokoFix private constructor(private val app: Application, val version: String) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onForegroud() {
        PatchService.startService(app, version)
    }

    companion object {
        private val TAG = TokoFix::class.java.simpleName
        @JvmStatic
        fun init(application: Application, version: String) {
            TokoFix(application, version)
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}
