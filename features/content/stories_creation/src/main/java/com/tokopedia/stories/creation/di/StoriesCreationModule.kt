package com.tokopedia.stories.creation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
@Module
object StoriesCreationModule {

    @Provides
    @StoriesCreationScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @StoriesCreationScope
    fun provideRemoteConfig(context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
