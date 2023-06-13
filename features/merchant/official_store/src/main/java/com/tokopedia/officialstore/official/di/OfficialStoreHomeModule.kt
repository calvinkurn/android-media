package com.tokopedia.officialstore.official.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.officialstore.official.data.mapper.OfficialProductCardMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides

@Module(includes = [OfficialStoreHomeViewModelModule::class])
class OfficialStoreHomeModule {

    @OfficialStoreHomeScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()


    @OfficialStoreHomeScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @OfficialStoreHomeScope
    @Provides
    fun provideGraphqlCoroutineUseCase(graphqlRepository: GraphqlRepository) =
            com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Any>(graphqlRepository)

    @OfficialStoreHomeScope
    @Provides
    fun provideOfficialHomeMapper(@ApplicationContext context: Context, dispatchers: CoroutineDispatchers) = OfficialProductCardMapper(context, dispatchers)

    @OfficialStoreHomeScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @OfficialStoreHomeScope
    @Provides
    fun provideAddToWishlistV2UseCase(graphqlRepository: GraphqlRepository): AddToWishlistV2UseCase = AddToWishlistV2UseCase(graphqlRepository)

    @OfficialStoreHomeScope
    @Provides
    fun provideDeleteWishlistV2UseCase(graphqlRepository: GraphqlRepository): DeleteWishlistV2UseCase = DeleteWishlistV2UseCase(graphqlRepository)

    @OfficialStoreHomeScope
    @Provides
    fun provideBestSellerMapper(@ApplicationContext context: Context) = BestSellerMapper(context)

    @OfficialStoreHomeScope
    @Provides
    fun provideGetTopAdsHeadlineUseCase(graphqlRepository: GraphqlRepository): GetTopAdsHeadlineUseCase {
        return GetTopAdsHeadlineUseCase(graphqlRepository)
    }

    @OfficialStoreHomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig = FirebaseRemoteConfigImpl(context)

    @OfficialStoreHomeScope
    @Provides
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface, topAdsIrisSession: TopAdsIrisSession): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(), topAdsIrisSession.getSessionId())
    }

}
