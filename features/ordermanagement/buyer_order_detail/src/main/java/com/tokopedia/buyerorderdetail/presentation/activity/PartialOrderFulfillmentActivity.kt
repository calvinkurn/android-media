package com.tokopedia.buyerorderdetail.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailModule
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderExtensionFragment
import com.tokopedia.buyerorderdetail.presentation.fragment.PartialOrderFulfillmentFragment

class PartialOrderFulfillmentActivity : BaseSimpleActivity(),
    HasComponent<BuyerOrderDetailComponent> {

    override fun getNewFragment(): Fragment? {
        val intentExtras = intent.extras?.apply {
            val orderId = intent?.data?.getQueryParameter(ApplinkConstInternalOrder.PARAM_ORDER_ID).orEmpty()
            putString(ApplinkConstInternalOrder.PARAM_ORDER_ID, orderId)
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
            .buyerOrderDetailModule(BuyerOrderDetailModule())
            .build()
    }

    override fun onBackPressed() {
        setResultFinish(Activity.RESULT_CANCELED)
    }

    fun setResultFinish(resultCode: Int) {
        setResult(resultCode)
        finish()
    }


}
