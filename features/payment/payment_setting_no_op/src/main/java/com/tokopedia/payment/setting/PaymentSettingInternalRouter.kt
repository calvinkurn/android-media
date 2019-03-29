package com.tokopedia.payment.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class PaymentSettingInternalRouter {
    companion object {
        @JvmStatic
        fun getSettingListPaymentActivityIntent(context: Context) : Intent{
            Toast.makeText(context, "Setting List Payment", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://www.getSettingListPaymentActivity.com")
            return intent
        }
    }
}