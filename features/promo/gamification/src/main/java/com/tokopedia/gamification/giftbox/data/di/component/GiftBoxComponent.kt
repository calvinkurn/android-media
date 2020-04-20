package com.tokopedia.gamification.giftbox.data.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gamification.giftbox.data.di.modules.DispatcherModule
import com.tokopedia.gamification.giftbox.data.di.modules.GiftBoxDailyModule
import com.tokopedia.gamification.giftbox.data.di.modules.GiftBoxTapTapModule
import com.tokopedia.gamification.giftbox.data.di.modules.ViewModelModule
import com.tokopedia.gamification.giftbox.data.di.scope.GiftBoxScope
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxTapTapFragment
import dagger.Component

@GiftBoxScope
@Component(modules = [GiftBoxDailyModule::class, GiftBoxTapTapModule::class, ViewModelModule::class, DispatcherModule::class], dependencies = [BaseAppComponent::class])
interface GiftBoxComponent {
    fun inject(fragment: GiftBoxDailyFragment)
    fun inject(fragment: GiftBoxTapTapFragment)
}