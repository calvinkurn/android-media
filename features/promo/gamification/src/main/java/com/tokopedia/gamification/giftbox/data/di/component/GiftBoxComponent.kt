package com.tokopedia.gamification.giftbox.data.di.component

import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.data.di.modules.*
import com.tokopedia.gamification.giftbox.data.di.scope.GiftBoxScope
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxTapTapFragment
import dagger.Component

@GiftBoxScope
@Component(modules = [GiftBoxDailyModule::class, GiftBoxTapTapModule::class, ViewModelModule::class, DispatcherModule::class, ActivityContextModule::class])
interface GiftBoxComponent {
    fun inject(fragment: GiftBoxDailyFragment)
    fun inject(fragment: GiftBoxTapTapFragment)
}