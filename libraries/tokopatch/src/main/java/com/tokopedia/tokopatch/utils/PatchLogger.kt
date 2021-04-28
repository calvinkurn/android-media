package com.tokopedia.tokopatch.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.tokopatch.model.Patch
import com.tokopedia.tokopatch.patch.PatchCallBack
import timber.log.Timber

/**
 * Author errysuprayogi on 01,February,2020
 */
class PatchLogger : PatchCallBack {

    private var start: Long = 0L
    private var end: Long = 0L

    private object HOLDER {
        val INSTANCE = PatchLogger()
    }

    override fun onStart() {
        start = System.currentTimeMillis()
    }

    override fun onFinish() {
        end = System.currentTimeMillis()
        var elapsed = end - start
        Timber.i("P1#ROBUST#patch process time took: %s ms", elapsed.toString())
    }

    override fun onPatchListFetched(
            context: Context,
            result: Boolean,
            isNet: Boolean,
            patches: List<Patch>
    ) {
        for (patch in patches) {
            ServerLogger.log(Priority.P1, "ROBUST", mapOf("type" to "onPatchListFetched patch: ${patch.name}"))
        }
    }

    override fun onPatchFetched(context: Context, result: Boolean, isNet: Boolean) {
        ServerLogger.log(Priority.P1, "ROBUST", mapOf("type" to "onPatchFetched isNet: $isNet result: $result"))
    }

    override fun onPatchApplied(context: Context, result: Boolean, patch: Patch) {
        if (result && Utils.isDebuggable(context)) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Applied patch" + patch.name, Toast.LENGTH_LONG).show()
            }
        } else {
            ServerLogger.log(Priority.P1, "ROBUST", mapOf("type" to "onPatchApplied patch name: ${patch.name} result: $result"))
        }
    }

    override fun logNotify(context: Context, log: String, where: String) {
        ServerLogger.log(Priority.P1, "ROBUST", mapOf("type" to "logNotify log: $log where: $where"))
    }

    override fun logMessage(context: Context, log: String) {
        Timber.i("ROBUST#$log")
    }

    override fun exceptionNotify(context: Context, throwable: Throwable, where: String) {
        ServerLogger.log(Priority.P1, "ROBUST", mapOf("type" to "exceptionNotify where: $where"))
    }

    companion object {
        const val TAG = "ROBUST_LOGS"
        val instance: PatchLogger by lazy { HOLDER.INSTANCE }
    }
}