package com.tokopedia.buyerorderdetail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import dagger.Component

@BuyerOrderDetailScope
@Component(modules = [BuyerOrderDetailModule::class], dependencies = [BaseAppComponent::class])
interface BuyerOrderDetailComponent {
    fun inject(buyerOrderDetailFragment: BuyerOrderDetailFragment)
}