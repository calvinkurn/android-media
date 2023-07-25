package com.tokopedia.iris

import android.os.Bundle

/**
 * @author okasurya on 10/9/18.
 */
interface Iris {

    /**
     * Simplify initialize iris
     */
    fun initialize()

    /**
     * save event to persistence storage,
     * the events will be sent to server periodically
     *
     */
    fun saveEvent(map: Map<String, Any>)

    fun trackPerformance(irisPerformanceData: IrisPerformanceData)

    fun saveEvent(bundle: Bundle)

    /**
     * direct send event to server
     */
    @Deprecated(
        message = "function should not be called directly",
        replaceWith = ReplaceWith(expression = "saveEvent(map)")
    )
    fun sendEvent(map: Map<String, Any>)

    fun setAlarm(isTurnOn: Boolean, force: Boolean)

    /**
     * get Iris Session ID to provide DA & DE
     */
    fun getSessionId(): String
}