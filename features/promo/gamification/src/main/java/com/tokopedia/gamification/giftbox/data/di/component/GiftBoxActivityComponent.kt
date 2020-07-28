package com.tokopedia.gamification.giftbox.data.di.component

import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.data.di.modules.GiftBoxActivityModule
import com.tokopedia.gamification.giftbox.data.di.scope.GiftBoxActivityScope
import com.tokopedia.gamification.giftbox.presentation.activities.BaseGiftBoxActivity
import dagger.Component

@GiftBoxActivityScope
@Component(modules = [GiftBoxActivityModule::class, ActivityContextModule::class])
interface GiftBoxActivityComponent {
    fun inject(activity: BaseGiftBoxActivity)
}