package com.tokopedia.tokopatch.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
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
    private val TAG = "RobustLogger"
    private val SERVER_TAG = "ROBUST"

    private object HOLDER {
        val INSTANCE = PatchLogger()
    }

    override fun onStart() {
        start = System.currentTimeMillis()
    }

    override fun onFinish() {
        end = System.currentTimeMillis()
        var elapsed = end - start
        //ServerLogger.log(Priority.P1, SERVER_TAG, mutableMapOf("patching_time" to elapsed.toString()))
    }

    override fun onPatchListFetched(
        context: Context,
        result: Boolean,
        isNet: Boolean,
        patches: List<Patch>
    ) {
//         for (patch in patches) {
//             ServerLogger.log(Priority.P1, SERVER_TAG,mutableMapOf("fetch_patch_name" to patch.name))
//         }
    }

    override fun onPatchFetched(context: Context, result: Boolean, isNet: Boolean) {
        ServerLogger.log(
            Priority.P1,
            SERVER_TAG,
            mutableMapOf("patch_fetch_is_net" to "${isNet}", "patch_fetch_result" to "${result}")
        )
    }

    override fun onPatchApplied(context: Context, result: Boolean, patch: Patch) {
        if (patch.debug) {
            Log.v(TAG, "Applied patch: " + patch.name)
        } else {
//             ServerLogger.log(
//                 Priority.P1,
//                 SERVER_TAG,
//                 mutableMapOf("patch_applied" to "${patch.name}", "patch_applied_result" to "${result}")
//             )
        }
    }

    override fun showToaster(context: Context, message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun logNotify(context: Context, debug: Boolean, log: String, where: String, patchName: String) {
        if(debug){
            Log.e(TAG, log)
        } else {
//             ServerLogger.log(
//                 Priority.P1,
//                 SERVER_TAG,
//                 mutableMapOf("error_log" to "${log}", "where" to "${where}", "patch_name" to "${patchName}")
//             )
        }
    }

    override fun logMessage(context: Context, debug: Boolean, log: String) {
        if (debug) {
            Log.v(TAG, log)
        }
    }

    override fun exceptionNotify(context: Context, throwable: Throwable, where: String) {
//         ServerLogger.log(Priority.P1, SERVER_TAG, mutableMapOf("exception" to "${throwable.message}"))
    }

    companion object {
        val instance: PatchLogger by lazy { HOLDER.INSTANCE }
    }
}
