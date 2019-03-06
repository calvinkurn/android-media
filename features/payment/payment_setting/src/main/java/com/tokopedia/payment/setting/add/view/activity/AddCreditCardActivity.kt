package com.tokopedia.payment.setting.add.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.payment.setting.add.view.fragment.AddCreditCardFragment

class AddCreditCardActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return AddCreditCardFragment.createInstance()
    }

    companion object {
        fun createIntent(context: Context) : Intent {
            return Intent(context, AddCreditCardActivity::class.java)
        }
    }

    object DeeplinkIntent {
        @DeepLink(ApplinkConst.ADD_CREDIT_CARD)
        @JvmStatic
        fun createApplinkIntent(context: Context, bundle: Bundle): Intent {
            return Intent(context, AddCreditCardActivity::class.java)
        }
    }
}