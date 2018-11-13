package com.tokopedia.shop.page.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent

@Deprecated(level = DeprecationLevel.WARNING,
        message = "This is dummy page for no op and should be removed in the future. " +
                "Access this activity should via ShopPageInternalRouter to support no op. " +
                "Do not include this class directly"
)
class ShopPageActivity : Activity() {
    companion object {

        @JvmStatic
        fun createIntent(context: Context, shopId: String) = Intent()
    }
}