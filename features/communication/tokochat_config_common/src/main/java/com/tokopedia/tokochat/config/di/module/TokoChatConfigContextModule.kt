package com.tokopedia.tokochat.config.di.module

import android.content.Context
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import dagger.Module
import dagger.Provides

@Module
class TokoChatConfigContextModule(val context: Context) {

    @Provides
    @TokoChatQualifier
    fun provideContext(): Context = context
}
