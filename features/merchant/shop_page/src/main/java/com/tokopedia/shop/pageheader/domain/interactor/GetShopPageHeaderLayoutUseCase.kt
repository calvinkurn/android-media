package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import javax.inject.Inject

class GetShopPageHeaderLayoutUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopPageHeaderLayoutResponse>(graphqlRepository) {

    var params = mapOf<String, Any>()
    var isFromCloud: Boolean = false

    override suspend fun executeOnBackground(): ShopPageHeaderLayoutResponse {
        val request = GraphqlRequest(QUERY, ShopPageHeaderLayoutResponse::class.java, params)
        val cacheStrategy = GraphqlCacheStrategy.Builder(
                if (isFromCloud)
                    CacheType.ALWAYS_CLOUD
                else
                    CacheType.CLOUD_THEN_CACHE
        ).build()
        val gqlResponse = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        val error = gqlResponse.getError(ShopPageHeaderLayoutResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(ShopPageHeaderLayoutResponse::class.java)
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        const val QUERY = """
            query getShopPageGetHeaderLayout(${'$'}shopId: String!){
                ShopPageGetHeaderLayout(shopID:${'$'}shopId){
                  widgets {
                  widgetID
                  name
                  type
                  component {
                    name
                    type
                    data {
                      __typename
                      ... on ImageOnly{
                        image
                        imageLink
                        isBottomSheet
                      }
                      ... on BadgeTextValue {
                        ctaText
                        ctaLink
                        ctaIcon
                        text {
                          icon
                          textLink
                          textHtml
                          isBottomSheet
                        }
                      }
                      ... on ButtonComponent {
                        icon
                        buttonType
                        link
                        isBottomSheet
                        label
                      }
                    }
                  }
                }
            }
          }
        """
        @JvmStatic
        fun createParams(
                shopId: String
        ) = mapOf(PARAM_SHOP_ID to shopId)
    }
}