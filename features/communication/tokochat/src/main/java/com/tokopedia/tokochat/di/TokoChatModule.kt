package com.tokopedia.tokochat.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokochat.data.repository.TokoChatImageRepository
import com.tokopedia.tokochat.data.repository.api.TokoChatDownloadImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatImageApi
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object TokoChatModule {

    @TokoChatScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @TokoChatScope
    @Provides
    fun provideTokoChatImageRepository(
        tokoChatImageApi: TokoChatImageApi,
        tokoChatDownloadImageApi: TokoChatDownloadImageApi
    ): TokoChatImageRepository {
        return TokoChatImageRepository(tokoChatImageApi, tokoChatDownloadImageApi)
    }
}
