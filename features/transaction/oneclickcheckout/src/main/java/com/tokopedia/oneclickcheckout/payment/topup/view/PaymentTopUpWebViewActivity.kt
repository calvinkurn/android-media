package com.tokopedia.oneclickcheckout.payment.topup.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.model.OrderPaymentOvoCustomerData
import com.tokopedia.oneclickcheckout.payment.di.DaggerPaymentComponent
import com.tokopedia.oneclickcheckout.payment.di.PaymentComponent
import com.tokopedia.oneclickcheckout.payment.di.PaymentModule

class PaymentTopUpWebViewActivity : BaseSimpleActivity(), HasComponent<PaymentComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titleResource = intent.getStringExtra(PaymentTopUpWebViewFragment.EXTRA_TITLE) ?: getString(R.string.title_one_click_checkout_top_up)
        updateTitle(titleResource)
    }

    override fun getNewFragment(): Fragment? {
        val url = intent.getStringExtra(PaymentTopUpWebViewFragment.EXTRA_URL)
        val redirectUrl = intent.getStringExtra(PaymentTopUpWebViewFragment.EXTRA_REDIRECT_URL)
        val isHideDigital = intent.getIntExtra(PaymentTopUpWebViewFragment.EXTRA_IS_HIDE_DIGITAL, -1)
        val customerData = intent.getParcelableExtra<OrderPaymentOvoCustomerData>(PaymentTopUpWebViewFragment.EXTRA_CUSTOMER_DATA)
        if (redirectUrl == null || (url == null && (isHideDigital == -1 || customerData == null))) {
            finish()
            return null
        }
        return PaymentTopUpWebViewFragment.createInstance(url ?: "", redirectUrl, isHideDigital, customerData)
    }

    override fun getComponent(): PaymentComponent {
        return DaggerPaymentComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .paymentModule(PaymentModule())
                .build()
    }

    companion object {

        fun createIntent(context: Context,
                         title: String,
                         url: String? = null,
                         redirectUrl: String? = null,
                         isHideDigital: Int? = null,
                         customerData: OrderPaymentOvoCustomerData? = null): Intent {
            return Intent(context, PaymentTopUpWebViewActivity::class.java)
                    .putExtra(PaymentTopUpWebViewFragment.EXTRA_TITLE, title)
                    .putExtra(PaymentTopUpWebViewFragment.EXTRA_URL, url)
                    .putExtra(PaymentTopUpWebViewFragment.EXTRA_REDIRECT_URL, redirectUrl)
                    .putExtra(PaymentTopUpWebViewFragment.EXTRA_IS_HIDE_DIGITAL, isHideDigital)
                    .putExtra(PaymentTopUpWebViewFragment.EXTRA_CUSTOMER_DATA, customerData)
        }
    }
}