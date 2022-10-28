package com.tokochat.tokochat_config_common.di.module

import android.content.Context
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import dagger.Module
import dagger.Provides

@Module
class TokoChatConfigContextModule(val context: Context) {

    @Provides
    @TokoChatQualifier
    fun provideContext(): Context = context
}
