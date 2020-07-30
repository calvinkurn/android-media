package com.tokopedia.thankyou_native.recommendation.di.module

import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.recommendation.di.scope.RecommendationScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named


@Module
class DispatcherModule {

    @Provides
    @CoroutineMainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @CoroutineBackgroundDispatcher
    fun provideBackgroundDispatcher(): CoroutineDispatcher = Dispatchers.IO

}