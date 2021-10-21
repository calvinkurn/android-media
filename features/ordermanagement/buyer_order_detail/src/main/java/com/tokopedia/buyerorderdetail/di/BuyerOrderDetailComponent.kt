package com.tokopedia.buyerorderdetail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import com.tokopedia.recommendation_widget_common.di.RecommendationCoroutineModule
import dagger.Component

@BuyerOrderDetailScope
@Component(modules = [BuyerOrderDetailModule::class, BuyerOrderDetailViewModelModule::class, RecommendationCoroutineModule::class], dependencies = [BaseAppComponent::class])
interface BuyerOrderDetailComponent {
    fun inject(buyerOrderDetailFragment: BuyerOrderDetailFragment)
}