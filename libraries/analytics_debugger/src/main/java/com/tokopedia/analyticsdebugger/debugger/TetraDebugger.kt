package com.tokopedia.analyticsdebugger.debugger

import android.content.Context

/**
 * @author okasurya on 2019-08-15.
 * Tetra Debugger is remote analytics debugger to automate data layer testing
 * by validating the data on testing server
 */
interface TetraDebugger {

    /**
     * Initialize the test process, by validating the deviceId of the phone to testing server
     */
    fun init()

    /**
     * send each data layer (currently only support gtm) to testing server
     */
    fun send(data: Map<String, Any>)

    fun setUserId(value: String)

    fun cancel()

    companion object {

        fun instance(context: Context) : TetraDebugger {
            return TetraDebuggerScope(context)
        }
    }
}