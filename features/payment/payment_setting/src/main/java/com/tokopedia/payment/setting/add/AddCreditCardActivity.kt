package com.tokopedia.payment.setting.add

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class AddCreditCardActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return AddCreditCardFragment.createInstance()
    }

    companion object {
        fun createIntent(context: Context) : Intent{
            return Intent(context, AddCreditCardActivity::class.java)
        }
    }
}