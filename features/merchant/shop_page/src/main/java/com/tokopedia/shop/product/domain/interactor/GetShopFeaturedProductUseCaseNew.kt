package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.product.data.GQLQueryConstant
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetShopFeaturedProductUseCaseNew @Inject constructor(
        @Named(GQLQueryConstant.SHOP_FEATURED_PRODUCT) private val gqlQuery: String,
        private val gqlUseCase: GraphqlUseCase<ShopFeaturedProduct.Response>
) : UseCase<List<ShopFeaturedProduct>>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_USER_ID = "userID"
        @JvmStatic
        fun createParams(shopId: Int, userId: Int): RequestParams = RequestParams.create().apply {
            putInt(PARAM_SHOP_ID, shopId)
            putInt(PARAM_USER_ID, userId)
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): List<ShopFeaturedProduct> {
        gqlUseCase.setGraphqlQuery(gqlQuery)
        gqlUseCase.setTypeClass(ShopFeaturedProduct.Response::class.java)
        gqlUseCase.setRequestParams(params.parameters)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        return gqlUseCase.executeOnBackground().shopFeaturedProductList.data
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }


}