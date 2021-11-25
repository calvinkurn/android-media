package com.tokopedia.analyticsdebugger.sse.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.analyticsdebugger.sse.data.local.database.SSELogDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */

@Module
class SSELoggingModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): SSELogDatabase = SSELogDatabase.getInstance(context)

    @Provides
    @Named("SSELogGsonPrettyPrinting")
    fun provideGson(): Gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
}