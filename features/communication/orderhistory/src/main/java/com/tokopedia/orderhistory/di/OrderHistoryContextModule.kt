package com.tokopedia.orderhistory.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class OrderHistoryContextModule(val context: Context) {

    @Provides
    @OrderHistoryScope
    @OrderHistoryContext
    fun provideContext(): Context = context

}