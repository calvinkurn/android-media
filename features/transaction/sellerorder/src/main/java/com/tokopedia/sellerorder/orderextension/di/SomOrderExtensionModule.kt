package com.tokopedia.sellerorder.orderextension.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class SomOrderExtensionModule(private val context: Context) {
    @Provides
    fun provideContext(): Context = context
}