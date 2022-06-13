package com.tokopedia.analyticsdebugger.websocket.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.analyticsdebugger.websocket.data.local.database.WebSocketLogDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */

@Module
class WebSocketLoggingModule {

    @Provides
    @WebSocketLoggingScope
    fun provideDatabase(@ApplicationContext context: Context) = WebSocketLogDatabase.getInstance(context)

    @Provides
    @WebSocketLoggingScope
    @Named("WebSocketLogGsonPrettyPrinting")
    fun provideGson(): Gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
}