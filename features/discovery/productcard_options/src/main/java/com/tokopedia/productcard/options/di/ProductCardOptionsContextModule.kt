package com.tokopedia.productcard.options.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
internal class ProductCardOptionsContextModule(private val context: Context) {

    @ProductCardOptionsScope
    @Provides
    fun provideContext() = context
}