package com.tokopedia.gamification.giftbox.data.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.data.di.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ActivityContextModule::class])
class GiftBoxDailyModule {

    @Provides
    @Named(GIFT_BOX_DAILY)
    fun provideGiftBoxDailyQuery(context: Context): String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.gamification.R.raw.gf_gift_box_daily)

    @Provides
    @Named(GIFT_BOX_DAILY_REWARD)
    fun provideGiftBoxDailyRewardQuery(context: Context): String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.gamification.R.raw.gf_gift_box_daily_reward)

    @Provides
    @Named(GAMI_REMIND_ME)
    fun provideGamiRemindMe(context: Context): String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.gamification.R.raw.gf_gami_remind_me)

    @Provides
    @Named(GAMI_REMIND_ME_CHECK)
    fun provideGamiRemindMeCheck(context: Context): String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.gamification.R.raw.gf_gami_remind_me_check)

    @Provides
    @Named(AUTO_APPLY)
    fun provideAutoApply(context: Context): String = GraphqlHelper.loadRawString(context.resources,com.tokopedia.gamification.R.raw.gf_gift_auto_apply)

    @Provides
    @Named(GET_COUPON_DETAIL)
    fun provideCouponDetailString(context: Context): String = context.resources.openRawResource(com.tokopedia.gamification.R.raw.gf_query_hachiko_catalog_detail)
            .bufferedReader()
            .readText()
}