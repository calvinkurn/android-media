package com.tokopedia.gamification.giftbox.data.di.component

import com.tokopedia.gamification.giftbox.data.di.modules.PltModule
import com.tokopedia.gamification.giftbox.data.di.scope.PltScope
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment
import dagger.Component

@PltScope
@Component(modules = [PltModule::class])
interface PltComponent {
    fun inject(fragment: GiftBoxDailyFragment)
}