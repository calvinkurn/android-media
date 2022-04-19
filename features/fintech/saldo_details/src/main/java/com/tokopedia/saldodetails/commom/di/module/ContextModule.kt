package com.tokopedia.saldodetails.commom.di.module

import android.content.Context
import com.tokopedia.saldodetails.commom.di.scope.SaldoDetailsScope
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