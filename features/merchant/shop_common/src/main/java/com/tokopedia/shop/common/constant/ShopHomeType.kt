package com.tokopedia.shop.common.constant

import androidx.annotation.StringDef

/**
 * @author normansyahputa on 4/25/17.
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@StringDef(ShopHomeType.NATIVE, ShopHomeType.WEBVIEW, ShopHomeType.NONE)
annotation class ShopHomeType {
    companion object {
        const val NATIVE = "native"
        const val WEBVIEW = "webview"
        const val NONE = "none"
    }
}