package com.tokopedia.payment.setting.add.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
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
}