package com.tokopedia.homenav.di
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.common_wallet.balance.data.CacheUtil
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
internal class BaseNavModule {

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSessionInterface = UserSession(context)

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig = RemoteConfigInstance.getInstance().abTestPlatform

    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context): TrackingQueue = TrackingQueue(context)

    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, CacheUtil.KEY_POPUP_INTRO_OVO_CACHE)
    }
}