package com.tokopedia.analyticsdebugger.sse.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.analyticsdebugger.sse.data.local.database.SSELogDatabase
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */

@Module
class SSELoggingModule {

    @Provides
    @SSELoggingScope
    fun provideDatabase(@ApplicationContext context: Context): SSELogDatabase = SSELogDatabase.getInstance(context)
}