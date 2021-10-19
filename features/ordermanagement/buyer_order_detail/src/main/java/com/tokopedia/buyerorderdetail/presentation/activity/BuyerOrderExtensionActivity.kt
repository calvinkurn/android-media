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

class BuyerOrderExtensionActivity: BaseSimpleActivity(), HasComponent<BuyerOrderDetailComponent> {

    override fun getNewFragment(): Fragment {
        val intentExtras = intent.extras?.apply {
            val orderId = intent?.data?.getQueryParameter(ApplinkConstInternalOrder.PARAM_ORDER_ID).orEmpty()
            putString(ApplinkConstInternalOrder.PARAM_ORDER_ID, orderId)
        } ?: Bundle()
        return BuyerOrderExtensionFragment.newInstance(intentExtras)
    }

    override fun getComponent(): BuyerOrderDetailComponent {
        return DaggerBuyerOrderDetailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .buyerOrderDetailModule(BuyerOrderDetailModule())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_order_extension_base)
    }

    override fun onBackPressed() {
        setResultFinish()
    }

    fun setResultFinish() {
        val isOrderExtended: Boolean? = null
        val toasterType: Int? = null
        val intent = Intent()

        intent.apply {
            putExtra(
                ApplinkConstInternalOrder.OrderExtensionKey.TOASTER_MESSAGE,
                ""
            )
            putExtra(
                ApplinkConstInternalOrder.OrderExtensionKey.IS_ORDER_EXTENDED,
                isOrderExtended
            )
            putExtra(
                ApplinkConstInternalOrder.OrderExtensionKey.TOASTER_TYPE, toasterType
            )
        }

        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }
}