package com.tokopedia.officialstore.official.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_BANNERS
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_BENEFITS
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_DYNAMIC_CHANNEL
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_FEATURED_SHOPS
import com.tokopedia.officialstore.GQLQueryConstant.QUERY_OFFICIAL_STORE_PRODUCT_RECOMMENDATION
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

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
    @Named(QUERY_OFFICIAL_STORE_BANNERS)
    fun provideQueryofficialStoreBanners(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_banner)
    }

    @OfficialStoreHomeScope
    @Provides
    @Named(QUERY_OFFICIAL_STORE_BENEFITS)
    fun provideQueryofficialStoreBenefitShop(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_benefit)
    }

    @OfficialStoreHomeScope
    @Provides
    @Named(QUERY_OFFICIAL_STORE_FEATURED_SHOPS)
    fun provideQueryofficialStoreFeaturedShop(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_featured)
    }

    @OfficialStoreHomeScope
    @Provides
    @Named(QUERY_OFFICIAL_STORE_DYNAMIC_CHANNEL)
    fun provideQueryOfficialStoreDynamicChannel(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_dynamic_channel)
    }

    @OfficialStoreHomeScope
    @Provides
    @Named(QUERY_OFFICIAL_STORE_PRODUCT_RECOMMENDATION)
    fun provideQueryOfficialStoreProductRecommendation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_official_store_product_recommendation)
    }


    @OfficialStoreHomeScope
    @Provides
    fun provideOfficialHomeMapper(@ApplicationContext context: Context, dispatchers: CoroutineDispatchers) = OfficialHomeMapper(context, dispatchers)

    @OfficialStoreHomeScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @OfficialStoreHomeScope
    @Provides
    fun provideAddWishlistUseCase(@ApplicationContext context: Context): AddToWishlistV2UseCase = AddToWishlistV2UseCase(provideGraphqlRepository())

    @OfficialStoreHomeScope
    @Provides
    fun provideRemoveWishlistUseCase(@ApplicationContext context: Context): DeleteWishlistV2UseCase = DeleteWishlistV2UseCase(provideGraphqlRepository())

    @OfficialStoreHomeScope
    @Provides
    fun provideBestSellerMapper(@ApplicationContext context: Context) = BestSellerMapper(context)

    @OfficialStoreHomeScope
    @Provides
    fun provideGetTopAdsHeadlineUseCase(graphqlRepository: GraphqlRepository): GetTopAdsHeadlineUseCase {
        return GetTopAdsHeadlineUseCase(graphqlRepository)
    }
}