package com.tokopedia.gamification.giftbox.data.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.data.di.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ActivityContextModule::class])
class GiftBoxTapTapModule {

    @Provides
    @Named(GAMI_TAP_EGG_HOME)
    fun provideGiftTapHomeQuery(context: Context): String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.gamification.R.raw.gf_token_tap_tap)

    @Provides
    @Named(GAMI_TAP_CRACK_EGG)
    fun provideGiftBoxDailyRewardQuery(context: Context): String = GraphqlHelper.loadRawString(context.resources, com.tokopedia.gamification.R.raw.crack_egg_result_mutation)
}