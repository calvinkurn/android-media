package com.tokopedia.expresscheckout.view.variant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class CheckoutVariantActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Activity?): Intent {
            return Intent(context, CheckoutVariantActivity::class.java)
        }

    }

    override fun getNewFragment(): Fragment {
        return CheckoutVariantFragment.createInstance()
    }

}
