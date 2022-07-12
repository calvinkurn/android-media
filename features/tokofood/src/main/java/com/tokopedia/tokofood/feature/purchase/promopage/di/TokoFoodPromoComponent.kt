package com.tokopedia.tokofood.feature.purchase.promopage.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.feature.purchase.promopage.presentation.TokoFoodPromoFragment
import dagger.Component

@ActivityScope
@Component(modules = [TokoFoodPromoViewModelModule::class, TokoFoodPromoModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodPromoComponent {
    fun inject(fragment: TokoFoodPromoFragment)
}