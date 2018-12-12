package com.tokopedia.payment.setting

import android.content.Context
import android.content.Intent
import com.tokopedia.payment.setting.list.view.activity.SettingListPaymentActivity

class PaymentSettingInternalRouter {
    companion object {
        @JvmStatic
        fun getSettingListPaymentActivityIntent(context: Context) : Intent{
            return Intent(context, SettingListPaymentActivity::class.java);
        }
    }
}