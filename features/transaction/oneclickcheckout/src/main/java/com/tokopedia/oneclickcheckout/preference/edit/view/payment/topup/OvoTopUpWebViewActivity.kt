package com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentOvoCustomerData
import com.tokopedia.oneclickcheckout.preference.edit.di.DaggerPreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditModule

class OvoTopUpWebViewActivity: BaseSimpleActivity(), HasComponent<PreferenceEditComponent> {

    override fun getNewFragment(): Fragment? {
        val redirectUrl = intent.getStringExtra(OvoTopUpWebViewFragment.EXTRA_REDIRECT_URL)
        val isHideDigital = intent.getIntExtra(OvoTopUpWebViewFragment.EXTRA_IS_HIDE_DIGITAL, -1)
        val customerData = intent.getParcelableExtra<OrderPaymentOvoCustomerData>(OvoTopUpWebViewFragment.EXTRA_CUSTOMER_DATA)
        if (redirectUrl == null || isHideDigital == -1 || customerData == null) {
            finish()
            return null
        }
        return OvoTopUpWebViewFragment.createInstance(redirectUrl, isHideDigital, customerData)
    }

    override fun getComponent(): PreferenceEditComponent {
        return DaggerPreferenceEditComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .preferenceEditModule(PreferenceEditModule(this))
                .build()
    }

    companion object {

        fun createIntent(context: Context, redirectUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData): Intent {
            return Intent(context, OvoTopUpWebViewActivity::class.java)
                    .putExtra(OvoTopUpWebViewFragment.EXTRA_REDIRECT_URL, redirectUrl)
                    .putExtra(OvoTopUpWebViewFragment.EXTRA_IS_HIDE_DIGITAL, isHideDigital)
                    .putExtra(OvoTopUpWebViewFragment.EXTRA_CUSTOMER_DATA, customerData)
        }
    }
}