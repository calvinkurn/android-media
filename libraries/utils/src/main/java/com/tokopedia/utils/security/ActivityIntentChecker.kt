package com.tokopedia.utils.security

import android.app.Activity
import android.content.Intent
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.config.GlobalConfig

@Throws(SecurityException::class)
fun Activity.checkActivity(block: (Intent) -> Unit) {
    val packageName = callingActivity?.packageName ?: packageName
    evaluate(intent, packageName, block)
}

private fun evaluate(intent: Intent, packageName: String?, block: (Intent) -> Unit) {
    if(packageName == GlobalConfig.PACKAGE_CONSUMER_APP &&
        (intent.scheme == null || intent.scheme == DeeplinkConstant.SCHEME_INTERNAL)) {
        block.invoke(intent)
    } else {
        throw SecurityException("Unsafe Intent Launch: $intent")
    }
}
