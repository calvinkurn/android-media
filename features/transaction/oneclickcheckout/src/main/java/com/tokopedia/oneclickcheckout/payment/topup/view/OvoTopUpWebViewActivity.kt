package com.tokopedia.oneclickcheckout.payment.topup.view

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentOvoCustomerData
import com.tokopedia.oneclickcheckout.payment.di.DaggerPaymentComponent
import com.tokopedia.oneclickcheckout.payment.di.PaymentComponent
import com.tokopedia.oneclickcheckout.payment.di.PaymentModule

class OvoTopUpWebViewActivity: BaseSimpleActivity(), HasComponent<PaymentComponent> {

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

    override fun getComponent(): PaymentComponent {
        return DaggerPaymentComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .paymentModule(PaymentModule())
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