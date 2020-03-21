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
        Timber.w("P2#ROBUST#onPatchListFetched result: $result")
        Timber.w( "P2#ROBUST#onPatchListFetched isNet: $isNet")
        for (patch in patches) {
            Timber.w("P2#ROBUST#onPatchListFetched patch: " + patch.name)
        }
    }

    override fun onPatchFetched(result: Boolean, isNet: Boolean, patch: Patch) {
        Timber.w("P2#ROBUST#onPatchFetched result: $result")
        Timber.w("P2#ROBUST#onPatchFetched isNet: $isNet")
        Timber.w("P2#ROBUST#onPatchFetched patch: " + patch.name)
    }

    override fun onPatchApplied(result: Boolean, patch: Patch) {
        Timber.w("P2#ROBUST#onPatchApplied result: $result")
        Timber.w("P2#ROBUST#onPatchApplied patch: " + patch.name)
    }

    override fun logNotify(log: String, where: String) {
        Timber.w("P2#ROBUST#logNotify log: $log")
        Timber.w("P2#ROBUST#logNotify where: $where")
    }

    override fun exceptionNotify(throwable: Throwable, where: String) {
        Timber.e(throwable,"P2#ROBUST#exceptionNotify where: $where")
    }

    companion object {
        private val TAG = PatchLogger::class.java.simpleName
    }

    private fun toast(text: String?) {
        Handler(Looper.getMainLooper()).post(Runnable { Toast.makeText(context, text, Toast.LENGTH_LONG).show() })
    }
}