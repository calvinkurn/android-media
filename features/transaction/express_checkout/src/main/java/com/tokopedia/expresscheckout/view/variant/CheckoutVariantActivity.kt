package com.tokopedia.expresscheckout.view.variant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequest

class CheckoutVariantActivity : BaseSimpleActivity() {

    companion object {
        val EXTRA_ATC_REQUEST = "EXTRA_ATC_REQUEST"

        @JvmStatic
        fun createIntent(context: Activity?, atcRequest: AtcRequest): Intent {
            val intent = Intent(context, CheckoutVariantActivity::class.java)
            intent.putExtra(EXTRA_ATC_REQUEST, atcRequest)

            return intent
        }

    }

    override fun getNewFragment(): Fragment {
        return CheckoutVariantFragment.createInstance(intent.extras[EXTRA_ATC_REQUEST] as AtcRequest)
    }

}
