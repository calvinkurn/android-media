package com.tokopedia.home_recom.repository

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home_recom.*
import com.tokopedia.home_recom.model.entity.SingleProductRecommendationEntity
import com.tokopedia.recommendation_widget_common.PARAM_PAGE_NAME
import com.tokopedia.recommendation_widget_common.PARAM_PAGE_NUMBER
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Lukas on 29/08/19
 */
class SingleProductRecommendationRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSession: UserSessionInterface,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        @Named("singleProductRecommendation") private val recommendationProductQuery: String
) : Repository<GraphqlResponse>(){

    companion object{
        private const val DEFAULT_PAGE_SIZE = 10
    }

    override suspend fun load(page: Int, ref: String, productIds: String): GraphqlResponse {
        return withContext(Dispatchers.IO) {
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val params = mapOf(
                    PARAM_X_DEVICE to DEFAULT_X_DEVICE,
                    PARAM_X_SOURCE to DEFAULT_X_SOURCE,
                    PARAM_REF to ref,
                    PARAM_PRODUCT_IDS to productIds,
                    PARAM_USER_ID to if(userSession.isLoggedIn) userSession.userId.toInt() else 0,
                    PARAM_PAGE_NUMBER to page,
                    PARAM_PAGE_NAME to "recom_page"
            )

            val gqlRecommendationRequest = GraphqlRequest(
                    recommendationProductQuery,
                    SingleProductRecommendationEntity::class.java,
                    params
            )

            graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        }
    }

    suspend fun addWishlistTopAds(wishlistUrl: String): WishlistModel? = withContext(Dispatchers.IO){
        val params = RequestParams.create()
        params.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, wishlistUrl)
        topAdsWishlishedUseCase.getExecuteObservable(params).toBlocking().first()
    }

    override fun getDefaultPageSize(): Int = DEFAULT_PAGE_SIZE

}