package com.tokopedia.people.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class UserProfileModule(
    private val activityContext: Context,
) {

    @Provides
    @UserProfileScope
    fun provideActivityContext() = activityContext

    @Provides
    @UserProfileScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @UserProfileScope
    fun provideGraphqlRepositoryCase(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @UserProfileScope
    fun provideTrackingQueue(@ApplicationContext context: Context): TrackingQueue {
        return TrackingQueue(context)
    }

    @Provides
    @UserProfileScope
    fun provideRemoteConfig(@ApplicationContext appContext: Context): RemoteConfig = FirebaseRemoteConfigImpl(appContext)

    @Provides
    @UserProfileScope
    fun provideUpdateChannelUseCase(graphqlRepository: GraphqlRepository) = UpdateChannelUseCase(graphqlRepository)
}
