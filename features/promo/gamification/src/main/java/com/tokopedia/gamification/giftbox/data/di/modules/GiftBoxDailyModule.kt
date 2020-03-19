package com.tokopedia.gamification.giftbox.data.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY
import com.tokopedia.gamification.giftbox.data.di.GIFT_BOX_DAILY_REWARD
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.di.MAIN
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class GiftBoxDailyModule {

    @Provides
    @Named(MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Named(IO)
    fun provideWorkerDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named(GIFT_BOX_DAILY)
    fun provideGiftBoxDailyQuery(@ApplicationContext context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.gf_gift_box_daily)

    @Provides
    @Named(GIFT_BOX_DAILY_REWARD)
    fun provideGiftBoxDailyRewardQuery(@ApplicationContext context: Context): String = GraphqlHelper.loadRawString(context.resources, R.raw.gf_gift_box_daily_reward)


}