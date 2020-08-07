package com.tokopedia.oneclickcheckout.preference.edit.view.payment.creditcard

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentCreditCardAdditionalData
import com.tokopedia.oneclickcheckout.preference.edit.di.DaggerPreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditModule
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.creditcard.CreditCardPickerFragment.Companion.EXTRA_ADDITIONAL_DATA

class CreditCardPickerActivity: BaseSimpleActivity(), HasComponent<PreferenceEditComponent> {

    override fun getNewFragment(): Fragment? {
        val additionalData = intent.getParcelableExtra<OrderPaymentCreditCardAdditionalData>(EXTRA_ADDITIONAL_DATA)
        if (additionalData == null) {
            finish()
            return null
        }
        return CreditCardPickerFragment.createInstance(additionalData)
    }

    override fun getComponent(): PreferenceEditComponent {
        return DaggerPreferenceEditComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .preferenceEditModule(PreferenceEditModule(this))
                .build()
    }

    companion object {

        fun createIntent(context: Context, additionalData: OrderPaymentCreditCardAdditionalData): Intent {
            return Intent(context, CreditCardPickerActivity::class.java).putExtra(EXTRA_ADDITIONAL_DATA, additionalData)
        }
    }
}