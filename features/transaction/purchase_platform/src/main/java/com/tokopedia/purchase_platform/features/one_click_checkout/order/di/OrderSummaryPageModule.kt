package com.tokopedia.purchase_platform.features.one_click_checkout.order.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@OrderSummaryPageScope
@Module
class OrderSummaryPageModule {

    @OrderSummaryPageScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}