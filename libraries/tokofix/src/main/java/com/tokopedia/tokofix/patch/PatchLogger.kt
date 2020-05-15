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
        Timber.w("P1#ROBUST#onPatchListFetched result: $result isNet: $isNet")
        for (patch in patches) {
            Timber.w("P1#ROBUST#onPatchListFetched patch: " + patch.name)
        }
    }

    override fun onPatchFetched(result: Boolean, isNet: Boolean, patch: Patch) {
        Timber.w("P1#ROBUST#onPatchFetched isNet: $isNet patch: "+patch.name+" result: $result")
    }

    override fun onPatchApplied(result: Boolean, patch: Patch) {
        Timber.w("P1#ROBUST#onPatchApplied patch name: " + patch.name+ " result: $result")
    }

    override fun logNotify(log: String, where: String) {
        Timber.w("P1#ROBUST#logNotify log: $log where: $where")
    }

    override fun exceptionNotify(throwable: Throwable, where: String) {
        Timber.e(throwable, "P1#ROBUST#exceptionNotify where: $where")
    }

    companion object {
        private val TAG = PatchLogger::class.java.simpleName
    }

    private fun toast(text: String?) {
        Handler(Looper.getMainLooper()).post(Runnable { Toast.makeText(context, text, Toast.LENGTH_LONG).show() })
    }
}