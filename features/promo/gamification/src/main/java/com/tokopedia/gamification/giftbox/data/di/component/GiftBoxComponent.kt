package com.tokopedia.gamification.giftbox.data.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.gamification.giftbox.data.di.modules.*
import com.tokopedia.gamification.giftbox.data.di.scope.GiftBoxScope
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxDailyFragment
import com.tokopedia.gamification.giftbox.presentation.fragments.GiftBoxTapTapFragment
import com.tokopedia.gamification.giftbox.presentation.viewholder.CouponListResultVH
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import dagger.Component

@GiftBoxScope
@Component(
    modules = [
        GiftBoxDailyModule::class,
        GiftBoxTapTapModule::class,
        ViewModelModule::class,
        DispatcherModule::class,
        AppModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface GiftBoxComponent {
    fun inject(fragment: GiftBoxDailyFragment)
    fun inject(fragment: GiftBoxTapTapFragment)
    fun inject(viewHolder: CouponListResultVH)
    fun inject(view: GiftBoxDailyView)
}
