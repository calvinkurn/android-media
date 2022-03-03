package com.tokopedia.broadcaster.revamp.util.log

/**
 * Created by meyta.taliti on 03/03/22.
 */
interface BroadcasterLogger {

    fun v(msg: String)

    fun d(msg: String)

    fun w(msg: String)

    fun i(msg: String)

    fun e(msg: String)

    companion object {
        const val TAG = "com.tkpd.broadcaster"
    }
}