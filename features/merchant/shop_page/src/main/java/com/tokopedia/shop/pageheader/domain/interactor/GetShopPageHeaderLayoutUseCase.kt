package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
        val gqlResponse = graphqlRepository.response(listOf(request), cacheStrategy)
        val error = gqlResponse.getError(ShopPageHeaderLayoutResponse::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData(ShopPageHeaderLayoutResponse::class.java)
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_DISTRICT_ID = "districtID"
        private const val PARAM_CITY_ID = "cityID"
        const val QUERY = """
            query getShopPageGetHeaderLayout(${'$'}shopId: String!, ${'$'}districtID: Int, ${'$'}cityID: Int){
                ShopPageGetHeaderLayout(shopID:${'$'}shopId, districtID:${'$'}districtID, cityID:${'$'}cityID){
                  isOverrideTheme
                  generalComponentConfigList {
                     name
                     type
                     data {
                      ... on TemplateComp { 
                         patternColorType         
                         bgColors
                         bgObjects {
                           objectType
                           url
                         }
                         colorSchemaList {
                           name
                           colorSchemaType
                           value          
                         }
                      }
                      ... on ThemeComp {
                         fontFace
                         confettiURL
                      }
                     }
                  }
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
                          ... on ImageText {
                            images {
                              style
                              data {
                                image
                                imageLink
                                isBottomSheet
                              }
                            }
                            textComponent {
                              style
                              data {
                                icon
                                textLink
                                textHtml
                                isBottomSheet
                              }
                            }
                          }
                        }
                      }
                  }
            }
          }
        """

        @JvmStatic
        fun createParams(
            shopId: String,
            districtId: String,
            cityId: String
        ) = mapOf(
            PARAM_SHOP_ID to shopId,
            PARAM_DISTRICT_ID to districtId.toIntOrZero(),
            PARAM_CITY_ID to cityId.toIntOrZero()
        )
    }
}
