package com.tokopedia.payment.setting.list

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class SettingListPaymentActivity : BaseSimpleActivity(){

    override fun getNewFragment(): Fragment {
        return SettingListPaymentFragment.createInstance()
    }
}