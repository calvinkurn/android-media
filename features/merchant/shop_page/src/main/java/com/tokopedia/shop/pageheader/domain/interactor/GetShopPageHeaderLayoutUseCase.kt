package com.tokopedia.shop.pageheader.domain.interactor

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.usecase.RequestParams
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
            return gqlResponse.getData<ShopPageHeaderLayoutResponse>(ShopPageHeaderLayoutResponse::class.java)
//            return getMockData()!!
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    //for testing purpose
//    fun getMockData(): ShopPageHeaderLayoutResponse? {
//        val gson = GsonBuilder().serializeNulls().create()
//        val output = gson.fromJson(strMockResponse, ShopPageHeaderLayoutResponse::class.java)
//        return output
//    }

//    val strMockResponse = """
//        {
//          "ShopPageGetHeaderLayout": {
//            "widgets": [
//              {
//                "widgetID": 1,
//                "name": "shop_basic_info",
//                "type": "shop_basic_info",
//                "component": [
//                  {
//                    "name": "shop_logo",
//                    "type": "image_only",
//                    "data": {
//                      "__typename": "ImageOnly",
//                      "image": "https://images.tokopedia.net/img/seller_no_logo_1.png",
//                      "imageLink": "",
//                      "isBottomSheet": false
//                    }
//                  },
//                  {
//                    "name": "shop_name",
//                    "type": "badge_text_value",
//                    "data": {
//                      "__typename": "BadgeTextValue",
//                      "ctaText": "",
//                      "ctaLink": "tokopedia://3418893/info",
//                      "text": [
//                        {
//                          "icon": "",
//                          "textLink": "",
//                          "textHtml": "",
//                          "isBottomSheet": false
//                        },
//                        {
//                          "icon": "",
//                          "textLink": "",
//                          "textHtml": "<span style=\"color:#31353B;\">Terakhir Online <strong>Jan 1970</strong></span> • <span style=\"color:#31353B;\"></span>",
//                          "isBottomSheet": false
//                        }
//                      ]
//                    }
//                  }
//                ]
//              },
//              {
//                "widgetID": 2,
//                "name": "shop_performance",
//                "type": "shop_performance",
//                "component": [
//                  {
//                    "name": "shop_chat_reply_speed",
//                    "type": "badge_text_value",
//                    "data": {
//                      "__typename": "BadgeTextValue",
//                      "ctaText": "",
//                      "ctaLink": "",
//                      "text": [
//                        {
//                          "icon": "",
//                          "textLink": "",
//                          "textHtml": "<b>N/A</b>",
//                          "isBottomSheet": false
//                        },
//                        {
//                          "icon": "",
//                          "textLink": "",
//                          "textHtml": "Performa chat",
//                          "isBottomSheet": false
//                        }
//                      ]
//                    }
//                  },
//                  {
//                    "name": "shop_total_follower",
//                    "type": "badge_text_value",
//                    "data": {
//                      "__typename": "BadgeTextValue",
//                      "ctaText": "",
//                      "ctaLink": "",
//                      "text": [
//                        {
//                          "icon": "",
//                          "textLink": "",
//                          "textHtml": "<b>N/A</b>",
//                          "isBottomSheet": false
//                        },
//                        {
//                          "icon": "",
//                          "textLink": "",
//                          "textHtml": "Follower",
//                          "isBottomSheet": false
//                        }
//                      ]
//                    }
//                  }
//                ]
//              },
//              {
//                "widgetID": 3,
//                "name": "action_button",
//                "type": "action_button",
//                "component": [
//                {
//                                      "name": "catatan_toko",
//                                      "type": "button",
//                                      "data": {
//                                        "__typename": "ButtonComponent",
//                                        "icon": "",
//                                        "buttonType": "",
//                                        "link": "",
//                                        "isBottomSheet": false,
//                                        "label": "☰"
//                                      }
//                                    },
//                  {
//                    "name": "chat",
//                    "type": "button",
//                    "data": {
//                      "__typename": "ButtonComponent",
//                      "icon": "",
//                      "buttonType": "",
//                      "link": "",
//                      "isBottomSheet": false,
//                      "label": "Chat"
//                    }
//                  },
//                  {
//                    "name": "follow",
//                    "type": "button",
//                    "data": {
//                      "__typename": "ButtonComponent",
//                      "icon": "",
//                      "buttonType": "",
//                      "link": "",
//                      "isBottomSheet": false,
//                      "label": ""
//                    }
//                  }
//                ]
//              }
//            ]
//          }
//        }
//    """.trimIndent()

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