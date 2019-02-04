package com.tokopedia.expresscheckout.router

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActivity
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam

/**
 * Created by Irfan Khoirul on 04/02/19.
 */

class ExpressCheckoutInternalRouter {

    companion object {
        const val EXTRA_ATC_REQUEST = "EXTRA_ATC_REQUEST"

        @JvmStatic
        fun createIntent(context: Activity?, atcRequestParam: AtcRequestParam): Intent {
            val intent = Intent(context, CheckoutVariantActivity::class.java)
            intent.putExtra(EXTRA_ATC_REQUEST, atcRequestParam)

            return intent
        }
    }

}