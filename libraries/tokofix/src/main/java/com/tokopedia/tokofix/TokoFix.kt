package com.tokopedia.tokofix

import android.app.Application
import android.util.Log
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
class TokoFix private constructor(private val app: Application) : LifecycleObserver {

    private val repository: PatchRepository = PatchRepository()


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onForegroud() {
        repository.getPatch("1", this::onSuccessGetPatch)
    }

    private fun onSuccessGetPatch(data: DataResponse){
        Log.d(TAG, "onSuccessGetPatch "+data.toString())
        val context = app.applicationContext
        repository.donwloadPatch(context, data.data.downloadUrl, PatchLogger(context))
    }

    companion object {
        private val TAG = TokoFix::class.java.simpleName
        @JvmStatic
        fun init(application: Application) {
            TokoFix(application)
        }
    }

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
}