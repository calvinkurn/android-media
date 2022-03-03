package com.tokopedia.broadcaster.revamp.util.log

import android.util.Log

/**
 * Created by meyta.taliti on 03/03/22.
 */
class DefaultBroadcasterLogger : BroadcasterLogger {

    override fun v(msg: String) {
        Log.v(BroadcasterLogger.TAG, msg)
    }

    override fun d(msg: String) {
        Log.d(BroadcasterLogger.TAG, msg)
    }

    override fun w(msg: String) {
        Log.w(BroadcasterLogger.TAG, msg)
    }

    override fun i(msg: String) {
        Log.i(BroadcasterLogger.TAG, msg)
    }

    override fun e(msg: String) {
        Log.e(BroadcasterLogger.TAG, msg)
    }
}