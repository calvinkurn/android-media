package com.tokopedia.accountprofile.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.accountprofile.common.analytics.TrackingPinUtil
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
open class ProfileCompletionSettingModule {

    @Provides
    open fun provideGraphQlRepository(): GraphqlRepository =
        GraphqlInteractor.getInstance().graphqlRepository

    @ProfileCompletionSettingScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    open fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)

    @Provides
    fun provideTrackingPinUtil() = TrackingPinUtil()

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
