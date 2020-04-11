package com.tokopedia.gamification.giftbox.data.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.di.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class GiftBoxTapTapModule {

    @Provides
    @Named(GAMI_TAP_EGG_HOME)
    fun provideGiftTapHomeQuery(@ApplicationContext context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.gf_token_tap_tap)

    @Provides
    @Named(GAMI_TAP_CRACK_EGG)
    fun provideGiftBoxDailyRewardQuery(@ApplicationContext context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.crack_egg_result_mutation)
}