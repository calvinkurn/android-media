package com.tokopedia.buyerorderdetail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailModule
import com.tokopedia.buyerorderdetail.presentation.fragment.PartialOrderFulfillmentFragment

class PartialOrderFulfillmentActivity : BaseSimpleActivity(),
    HasComponent<BuyerOrderDetailComponent> {

    override fun getNewFragment(): Fragment? {
        return PartialOrderFulfillmentFragment.newInstance()
    }

    override fun getComponent(): BuyerOrderDetailComponent {
        DaggerBuyerOrderDetailComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .buyerOrderDetailModule(BuyerOrderDetailModule())
            .build()
    }


}
