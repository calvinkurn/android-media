package com.tokopedia.tokofood.purchase.promopage.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokofood.purchase.promopage.presentation.TokoFoodPromoFragment
import dagger.Component

@ActivityScope
@Component(modules = [TokoFoodPromoViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TokoFoodPromoComponent {
    fun inject(fragment: TokoFoodPromoFragment)
}