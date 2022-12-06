package com.tokopedia.tokochat.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.data.repository.TokoChatImageRepository
import com.tokopedia.tokochat.data.repository.api.TokoChatDownloadImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatImageApi
import com.tokopedia.tokochat_common.util.TokoChatCacheManager
import com.tokopedia.tokochat_common.util.TokoChatCacheManagerImpl
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object TokoChatModule {

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideTokoChatImageRepository(
        tokoChatImageApi: TokoChatImageApi,
        tokoChatDownloadImageApi: TokoChatDownloadImageApi
    ): TokoChatImageRepository {
        return TokoChatImageRepository(tokoChatImageApi, tokoChatDownloadImageApi)
    }

    @ActivityScope
    @Provides
    internal fun provideTopchatCacheManager(@ApplicationContext context: Context): TokoChatCacheManager {
        val topchatCachePref = context.getSharedPreferences(TokoChatValueUtil.TOKOCHAT_CACHE, Context.MODE_PRIVATE)
        return TokoChatCacheManagerImpl(topchatCachePref)
    }
}
