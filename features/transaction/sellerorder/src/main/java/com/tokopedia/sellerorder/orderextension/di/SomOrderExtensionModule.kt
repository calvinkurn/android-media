package com.tokopedia.sellerorder.orderextension.di

import android.app.Activity
import android.content.Context
import com.tokopedia.sellerorder.detail.di.SomDetailScope
import dagger.Module
import dagger.Provides

@Module
class SomOrderExtensionModule(private val activity: Activity) {
    @SomDetailScope
    @Provides
    fun provideActivityContext(): Context = activity
}