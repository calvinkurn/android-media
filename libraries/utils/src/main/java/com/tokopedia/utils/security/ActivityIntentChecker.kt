package com.tokopedia.utils.security

import android.app.Activity
import android.content.Intent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

private const val SCHEME_INTERNAL = "tokopedia-android-internal"
private const val ERROR_MESSAGE = "Security exception: unsafe intent launch"

private const val KEY_ERROR_MESSAGE = "errorMessage"
private const val KEY_INTENT = "intent"

private const val TAG = "ACTIVITY_INTENT_CHECKER"

fun Activity.checkActivity(block: (Intent) -> Unit) {
    val packageName = callingActivity?.packageName ?: packageName
    evaluate(intent, packageName, block)
}

fun Intent.checkIntent(block: (Intent) -> Unit) {
    val packageName = component?.packageName.orEmpty()
    evaluate(this, packageName, block)
}

private fun evaluate(intent: Intent, packageName: String?, block: (Intent) -> Unit) {
    if((packageName == GlobalConfig.PACKAGE_CONSUMER_APP || packageName == GlobalConfig.PACKAGE_SELLER_APP) &&
        (intent.scheme == null || intent.scheme == SCHEME_INTERNAL)) {
        block.invoke(intent)
    } else {
        ServerLogger.log(Priority.P1, TAG, mapOf(
            KEY_ERROR_MESSAGE to ERROR_MESSAGE,
            KEY_INTENT to "$intent"
        ))
    }
}
