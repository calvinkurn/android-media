package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.usecase.coroutines.UseCase

class GetShopFeaturedProductUseCase(private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<List<ShopFeaturedProduct>>() {

    private val query = """
            query getShopFeaturedProduct(${'$'}shopId: Int!,${'$'}userID: Int!){
              shop_featured_product(shopID:${'$'}shopId, userID:${'$'}userID){
                data{
                  parent_id
                  product_id
                  name
                  uri
                  image_uri
                  price
                  preorder
                  returnable
                  wholesale
                  cashback
                  isWishlist
                  is_rated
                  original_price
                  percentage_amount
                  cashback_detail{
                    cashback_status
                    cashback_percent
                    is_cashback_expired
                    cashback_value
                  }
                  free_ongkir {
                    is_active
                    img_url
                  }
                  label_groups {
                    position
                    type
                    title
                    url
                  }
                  total_review
                  rating
                }
              }
            }
        """.trimIndent()

    var params = mapOf<String, Int>()
    var isFromCacheFirst: Boolean = true

    override suspend fun executeOnBackground(): List<ShopFeaturedProduct> {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlRequest = GraphqlRequest(query, ShopFeaturedProduct.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopFeaturedProduct.Response::class.java)

        if (error == null || error.isEmpty()){
            return gqlResponse.getData<ShopFeaturedProduct.Response>(ShopFeaturedProduct.Response::class.java)
                    .shopFeaturedProductList.data
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache(){
        gqlUseCase.clearCache()
    }

    companion object{
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_USER_ID = "userID"

        @JvmStatic
        fun createParams(shopId: Int, userId: Int): Map<String, Int> = mapOf(PARAM_SHOP_ID to shopId,PARAM_USER_ID to userId)
    }
}