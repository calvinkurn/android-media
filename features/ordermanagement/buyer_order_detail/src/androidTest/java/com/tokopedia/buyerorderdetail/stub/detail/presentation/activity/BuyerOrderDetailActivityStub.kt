package com.tokopedia.buyerorderdetail.stub.detail.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailCommonIntentParamKey
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailIntentParamKey
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.activity.BuyerOrderDetailActivity
import com.tokopedia.buyerorderdetail.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.buyerorderdetail.stub.detail.di.component.DaggerBuyerOrderDetailComponentStub
import com.tokopedia.buyerorderdetail.stub.detail.presentation.fragment.BuyerOrderDetailFragmentStub

class BuyerOrderDetailActivityStub : BuyerOrderDetailActivity() {

    override fun getComponent(): BuyerOrderDetailComponent {
        val baseAppComponentStub = BaseAppComponentStubInstance.getBaseAppComponentStub(application)
        return DaggerBuyerOrderDetailComponentStub.builder()
            .baseAppComponentStub(baseAppComponentStub)
            .build()
    }

    override fun getNewFragment(): Fragment? {
        val extras = intent.extras ?: createIntentExtrasFromAppLink()
        return if (extras == null) {
            finish()
            null
        } else {
            BuyerOrderDetailFragmentStub.newInstance(extras)
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(
            context: Context,
            cartString: String,
            orderId: String,
            paymentId: String
        ) = Intent(context, BuyerOrderDetailActivityStub::class.java).apply {
            putExtra(BuyerOrderDetailIntentParamKey.PARAM_CART_STRING, cartString)
            putExtra(BuyerOrderDetailCommonIntentParamKey.ORDER_ID, orderId)
            putExtra(BuyerOrderDetailIntentParamKey.PARAM_PAYMENT_ID, paymentId)
        }
    }
}