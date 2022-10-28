package com.tokopedia.gifting.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gifting.presentation.bottomsheet.GiftingBottomSheet
import dagger.Component

@GiftingScope
@Component(modules = [GiftingModule::class, GiftingViewModelModule::class], dependencies = [BaseAppComponent::class])
interface GiftingComponent {
    fun inject(giftingBottomSheet: GiftingBottomSheet)
}