package com.tokopedia.gamification.giftbox.data.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gamification.giftbox.data.di.modules.GiftBoxActivityModule
import com.tokopedia.gamification.giftbox.data.di.scope.GiftBoxActivityScope
import com.tokopedia.gamification.giftbox.presentation.activities.BaseGiftBoxActivity
import com.tokopedia.gamification.giftbox.presentation.activities.GiftBoxDailyActivity
import com.tokopedia.gamification.giftbox.presentation.activities.GiftBoxTapTapActivity
import dagger.Component

@GiftBoxActivityScope
@Component(modules = [GiftBoxActivityModule::class], dependencies = [BaseAppComponent::class])
interface GiftBoxActivityComponent {
    fun inject(activity: BaseGiftBoxActivity)
}