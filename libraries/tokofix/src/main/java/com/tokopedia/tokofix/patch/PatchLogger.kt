package com.tokopedia.tokofix.patch

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.meituan.robust.Patch
import com.meituan.robust.RobustCallBack
import timber.log.Timber

/**
 * Author errysuprayogi on 01,February,2020
 */
class PatchLogger(val context: Context) : RobustCallBack {
    override fun onPatchListFetched(result: Boolean, isNet: Boolean, patches: List<Patch>) {
        Timber.d(TAG, "onPatchListFetched result: $result")
        Timber.d(TAG, "onPatchListFetched isNet: $isNet")
        for (patch in patches) {
            Timber.d(TAG, "onPatchListFetched patch: " + patch.name)
        }
    }

    override fun onPatchFetched(result: Boolean, isNet: Boolean, patch: Patch) {
        Timber.d(TAG, "onPatchFetched result: $result")
        Timber.d(TAG, "onPatchFetched isNet: $isNet")
        Timber.d(TAG, "onPatchFetched patch: " + patch.name)
    }

    override fun onPatchApplied(result: Boolean, patch: Patch) {
        Timber.d(TAG, "onPatchApplied result: $result")
        Timber.d(TAG, "onPatchApplied patch: " + patch.name)
    }

    override fun logNotify(log: String, where: String) {
        Timber.d(TAG, "logNotify log: $log")
        Timber.d(TAG, "logNotify where: $where")
    }

    override fun exceptionNotify(throwable: Throwable, where: String) {
        Log.e(TAG, "exceptionNotify where: $where", throwable)
    }

    companion object {
        private val TAG = PatchLogger::class.java.simpleName
    }

    private fun toast(text: String?) {
        Handler(Looper.getMainLooper()).post(Runnable { Toast.makeText(context, text, Toast.LENGTH_LONG).show() })
    }
}