package com.tokopedia.oneclickcheckout.payment.creditcard

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCardAdditionalData
import com.tokopedia.oneclickcheckout.payment.creditcard.CreditCardPickerFragment.Companion.EXTRA_ADDITIONAL_DATA

class CreditCardPickerActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val additionalData = intent.getParcelableExtra<OrderPaymentCreditCardAdditionalData>(EXTRA_ADDITIONAL_DATA)
        if (additionalData == null) {
            finish()
            return null
        }
        return CreditCardPickerFragment.createInstance(additionalData)
    }

    companion object {

        fun createIntent(context: Context, additionalData: OrderPaymentCreditCardAdditionalData): Intent {
            return Intent(context, CreditCardPickerActivity::class.java).putExtra(EXTRA_ADDITIONAL_DATA, additionalData)
        }
    }
}