package com.tokopedia.analyticsdebugger.debugger

/**
 * @author okasurya on 5/16/18.
 */
interface AnalyticsLogger {

    val isNotificationEnabled: Boolean

    fun save(name: String, data: Map<String, Any>)

    fun saveError(errorData: String)

    fun wipe()

    fun openActivity()

    fun openErrorActivity()

    fun navigateToValidator()

    fun enableNotification(status: Boolean)
}
