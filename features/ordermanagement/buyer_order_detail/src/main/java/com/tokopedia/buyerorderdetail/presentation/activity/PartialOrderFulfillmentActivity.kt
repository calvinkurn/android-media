package com.tokopedia.buyerorderdetail.presentation.activity

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailModule
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.fragment.PartialOrderFulfillmentFragment
import com.tokopedia.tokochat.config.util.TokoChatConnection

class PartialOrderFulfillmentActivity :
    BaseSimpleActivity(),
    HasComponent<BuyerOrderDetailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustOrientation()
    }

    override fun getNewFragment(): Fragment? {
        val intentExtras = intent.extras?.apply {
            val orderId = intent
                ?.data
                ?.getQueryParameter(DeeplinkMapperOrder.Pof.INTENT_PARAM_ORDER_ID)
                .orEmpty()
            putString(DeeplinkMapperOrder.Pof.INTENT_PARAM_ORDER_ID, orderId)
        } ?: Bundle()
        return PartialOrderFulfillmentFragment.newInstance(intentExtras)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_partial_order_fulfillment_base
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view_pof
    }

    override fun getComponent(): BuyerOrderDetailComponent {
        return DaggerBuyerOrderDetailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .tokoChatConfigComponent(TokoChatConnection.getComponent(this))
            .buyerOrderDetailModule(BuyerOrderDetailModule())
            .build()
    }

    override fun onBackPressed() {
        setResultFinish(Activity.RESULT_CANCELED)
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    fun setResultFinish(resultCode: Int) {
        setResult(resultCode)
        finish()
    }
}
