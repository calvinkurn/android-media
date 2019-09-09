package com.tokopedia.payment.setting.list.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.payment.setting.list.view.fragment.SettingListPaymentFragment

class SettingListPaymentActivity : BaseSimpleActivity(){

    override fun getNewFragment(): Fragment {
        return SettingListPaymentFragment.createInstance()
    }

    companion object {
        fun createIntent(context: Context) : Intent{
            return Intent(context, SettingListPaymentActivity::class.java)
        }
    }
}