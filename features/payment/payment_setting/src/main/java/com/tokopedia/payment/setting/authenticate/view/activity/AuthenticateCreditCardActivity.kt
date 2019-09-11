package com.tokopedia.payment.setting.authenticate.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.payment.setting.authenticate.view.fragment.AuthenticateCreditCardFragment

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