package com.tokopedia.tokomember_seller_dashboard.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class TokomemberActivityContextModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

}