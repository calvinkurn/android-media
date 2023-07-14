package com.tokopedia.people.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.utils.UserProfileSharedPref
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
@Module
class UserProfileTestModule(
    private val activityContext: Context,
    private val mockUserSession: UserSessionInterface,
    private val mockRepo: UserProfileRepository,
    private val mockContentCoachMarkManager: ContentCoachMarkManager,
    private val mockUserProfileSharedPref: UserProfileSharedPref,
    private val mockRouter: Router,
) {

    @Provides
    @UserProfileScope
    fun provideActivityContext() = activityContext

    @Provides
    @UserProfileScope
    fun provideUserSession() = mockUserSession

    @Provides
    @UserProfileScope
    fun provideUserProfileRepository() = mockRepo

    @Provides
    @UserProfileScope
    fun provideContentCoachMarkManager() = mockContentCoachMarkManager

    @Provides
    @UserProfileScope
    fun provideUserProfileSharedPref() = mockUserProfileSharedPref

    @Provides
    @UserProfileScope
    fun provideRouter() = mockRouter

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
