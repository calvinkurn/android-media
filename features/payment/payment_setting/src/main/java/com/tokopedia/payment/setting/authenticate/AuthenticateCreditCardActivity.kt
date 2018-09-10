package com.tokopedia.payment.setting.authenticate

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class AuthenticateCreditCardActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return AuthenticateCreditCardFragment.createInstance()
    }

    companion object {
        fun createIntent(context: Context) : Intent{
            return Intent(context, AuthenticateCreditCardActivity::class.java)
        }
    }
}