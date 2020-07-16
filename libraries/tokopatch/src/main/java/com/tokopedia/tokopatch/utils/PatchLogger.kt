package com.tokopedia.tokopatch.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.tokopedia.tokopatch.model.Patch
import com.tokopedia.tokopatch.patch.PatchCallBack
import timber.log.Timber

/**
 * Author errysuprayogi on 01,February,2020
 */
class PatchLogger : PatchCallBack {

    override fun onPatchListFetched(
            context: Context,
            result: Boolean,
            isNet: Boolean,
            patches: List<Patch>
    ) {
        for (patch in patches) {
            Timber.w("P1#ROBUST#onPatchListFetched patch: %s", patch.name)
        }
    }

    override fun onPatchFetched(context: Context, result: Boolean, isNet: Boolean) {
        Timber.w("P1#ROBUST#onPatchFetched isNet: $isNet result: $result")
    }

    override fun onPatchApplied(context: Context, result: Boolean, patch: Patch) {
        if (result && Utils.isDebuggable(context)) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Applied patch" + patch.name, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun logNotify(context: Context, log: String, where: String) {
        Timber.w("P1#ROBUST#logNotify log: $log where: $where")
    }

    override fun logMessage(context: Context, log: String) {
        Timber.i("P1#ROBUST#$log")

    }

    override fun exceptionNotify(context: Context, throwable: Throwable, where: String) {
        Timber.e(throwable, "P1#ROBUST#exceptionNotify where: $where")

    }

    companion object {
        const val TAG = "ROBUST_LOGS"
    }
}