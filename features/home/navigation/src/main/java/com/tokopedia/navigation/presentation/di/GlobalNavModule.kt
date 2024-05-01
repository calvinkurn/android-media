package com.tokopedia.navigation.presentation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.navigation.data.mapper.NotificationRequestMapper
import com.tokopedia.navigation.domain.GetBottomNavNotificationUseCase
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase
import com.tokopedia.navigation.presentation.presenter.MainParentPresenter
import com.tokopedia.navigation_common.util.LottieCacheManager
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

/**
 * Created by Lukas on 2019-07-31
 */

@Module(includes = [RecommendationModule::class])
class GlobalNavModule {
    @Provides
    fun provideMainParentPresenter(
        getNotificationUseCase: GetBottomNavNotificationUseCase,
        userSession: UserSessionInterface
    ): MainParentPresenter {
        return MainParentPresenter(getNotificationUseCase, userSession)
    }

    @Provides
    fun provideGetBottomNavNotificationUseCase(
        getDrawerNotificationUseCase: GetDrawerNotificationUseCase
    ): GetBottomNavNotificationUseCase {
        return GetBottomNavNotificationUseCase(getDrawerNotificationUseCase)
    }

    @Provides
    fun provideGetDrawerNotificationUseCase(
        @ApplicationContext context: Context,
        graphqlUseCase: GraphqlUseCase
    ): GetDrawerNotificationUseCase {
        return GetDrawerNotificationUseCase(context, graphqlUseCase, NotificationRequestMapper())
    }

    @Provides
    fun provideGraphqlRepository(): GraphqlRepository =
        GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideAddToWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase =
        AddToWishlistV2UseCase(graphqlRepository)

    @Provides
    fun provideDeleteWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase =
        DeleteWishlistV2UseCase(graphqlRepository)

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    fun provideLottieCacheManager(context: Context): LottieCacheManager {
        return LottieCacheManager(context)
    }

    @Provides
    fun provideWeakReferenceContext(context: Context): WeakReference<Context> {
        return WeakReference(context)
    }
}
