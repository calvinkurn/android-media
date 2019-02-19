package com.tokopedia.expresscheckout.router

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam

/**
 * Created by Irfan Khoirul on 04/02/19.
 */

class ExpressCheckoutInternalRouter {

    companion object {

        @JvmStatic
        fun createIntent(context: Activity?, atcRequestParam: AtcRequestParam): Intent? {
            Toast.makeText(context, "Express Checkout No Op", Toast.LENGTH_SHORT).show()
            return null
        }

    }

}