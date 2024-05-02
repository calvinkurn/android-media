package com.tokopedia.checkoutpayment.list.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.checkoutpayment.list.di.CheckoutPaymentComponent
import com.tokopedia.checkoutpayment.list.di.CheckoutPaymentModule
import com.tokopedia.checkoutpayment.list.di.DaggerCheckoutPaymentComponent
import javax.inject.Inject

open class PaymentListingActivity : BaseSimpleActivity(), HasComponent<CheckoutPaymentComponent> {

    @Inject
    lateinit var paymentListingAnalytics: PaymentListingAnalytics

    override fun getComponent(): CheckoutPaymentComponent {
        return DaggerCheckoutPaymentComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .checkoutPaymentModule(CheckoutPaymentModule())
            .build()
    }

    override fun getNewFragment(): Fragment {
        return PaymentListingFragment.newInstance(
            intent.getDoubleExtra(EXTRA_PAYMENT_AMOUNT, 0.0),
            intent.getStringExtra(EXTRA_ADDRESS_ID) ?: "",
            intent.getStringExtra(EXTRA_PAYMENT_PROFILE) ?: "",
            intent.getStringExtra(EXTRA_PAYMENT_MERCHANT) ?: "",
            intent.getStringExtra(EXTRA_PAYMENT_BID) ?: "",
            intent.getStringExtra(EXTRA_ORDER_METADATA) ?: "",
            intent.getStringExtra(EXTRA_PROMO_PARAM) ?: "",
            intent.getStringExtra(EXTRA_CALLBACK_URL) ?: "",
            intent.getStringExtra(EXTRA_PAYMENT_REQUEST) ?: ""
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onBackPressed() {
        paymentListingAnalytics.eventClickBackArrowInPilihPembayaran()
        super.onBackPressed()
    }

    companion object {
        const val EXTRA_ADDRESS_ID = "address_id"
        const val EXTRA_PAYMENT_PROFILE = "payment_profile"
        const val EXTRA_PAYMENT_MERCHANT = "payment_merchant"
        const val EXTRA_PAYMENT_AMOUNT = "payment_amount"
        const val EXTRA_PAYMENT_BID = "payment_bid"
        const val EXTRA_ORDER_METADATA = "order_metadata"
        const val EXTRA_PROMO_PARAM = "promo_param"
        const val EXTRA_CALLBACK_URL = "callback_url"
        const val EXTRA_PAYMENT_REQUEST = "payment_request"

        const val EXTRA_RESULT_GATEWAY = "RESULT_GATEWAY"
        const val EXTRA_RESULT_METADATA = "RESULT_METADATA"
    }
}
