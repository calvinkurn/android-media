package com.tokopedia.oneclickcheckout.payment.list.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.oneclickcheckout.payment.analytics.PaymentListingAnalytics
import com.tokopedia.oneclickcheckout.payment.di.DaggerPaymentComponent
import com.tokopedia.oneclickcheckout.payment.di.PaymentComponent
import com.tokopedia.oneclickcheckout.payment.di.PaymentModule
import javax.inject.Inject

open class PaymentListingActivity : BaseSimpleActivity(), HasComponent<PaymentComponent> {

    @Inject
    lateinit var paymentListingAnalytics: PaymentListingAnalytics

    override fun getComponent(): PaymentComponent {
        return DaggerPaymentComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .paymentModule(PaymentModule())
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
            intent.getStringExtra(EXTRA_PROMO_PARAM) ?: ""
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
        internal const val EXTRA_ADDRESS_ID = "address_id"
        internal const val EXTRA_PAYMENT_PROFILE = "payment_profile"
        internal const val EXTRA_PAYMENT_MERCHANT = "payment_merchant"
        internal const val EXTRA_PAYMENT_AMOUNT = "payment_amount"
        internal const val EXTRA_PAYMENT_BID = "payment_bid"
        internal const val EXTRA_ORDER_METADATA = "order_metadata"
        internal const val EXTRA_PROMO_PARAM = "promo_param"

        internal const val EXTRA_RESULT_GATEWAY = "RESULT_GATEWAY"
        internal const val EXTRA_RESULT_METADATA = "RESULT_METADATA"
    }
}
