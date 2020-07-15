package com.tokopedia.saldodetails.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule (val context: Context) {

    @SaldoDetailsScope
    @Provides
    fun provideContext(): Context {
        return context
    }

}