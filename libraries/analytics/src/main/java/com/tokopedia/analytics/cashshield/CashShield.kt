package com.tokopedia.analytics.cashshield

import android.content.Context

/**
 * @author okasurya on 2019-07-11.
 */
class CashShield(val context: Context?) {
    val cashShieldScope = CashShieldScope(context)

    fun send() {
        cashShieldScope.send()
    }

    fun cancel() {
        cashShieldScope.cancel()
    }
}