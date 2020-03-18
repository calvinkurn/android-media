package com.tokopedia.gamification.giftbox.data.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gamification.giftbox.data.di.modules.GiftBoxDailyModule
import com.tokopedia.gamification.giftbox.data.di.modules.ViewModelModule
import com.tokopedia.gamification.giftbox.data.di.scope.GiftBoxScope
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment
import dagger.Component

@GiftBoxScope
@Component(modules = [GiftBoxDailyModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface GiftBoxComponent {
    fun inject(fragment: GiftBoxDailyFragment)
}