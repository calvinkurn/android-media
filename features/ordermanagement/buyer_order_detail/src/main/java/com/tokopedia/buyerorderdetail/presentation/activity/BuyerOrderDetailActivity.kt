package com.tokopedia.buyerorderdetail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.di.DaggerBuyerOrderDetailComponent
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment

class BuyerOrderDetailActivity: BaseSimpleActivity(), HasComponent<BuyerOrderDetailComponent> {
    override fun getNewFragment(): Fragment? {
        return BuyerOrderDetailFragment()
    }

    override fun getComponent(): BuyerOrderDetailComponent {
        return DaggerBuyerOrderDetailComponent.create()
    }
}