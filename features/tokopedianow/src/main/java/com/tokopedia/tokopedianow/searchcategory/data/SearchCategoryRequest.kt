package com.tokopedia.tokopedianow.searchcategory.data

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.UrlParamUtils.generateUrlParamString
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse
import com.tokopedia.tokopedianow.home.domain.query.GetRepurchaseWidget
import com.tokopedia.tokopedianow.home.domain.usecase.GetRepurchaseWidgetUseCase
import com.tokopedia.tokopedianow.home.domain.usecase.GetRepurchaseWidgetUseCase.Companion.PARAM_CAT_ID
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.CategoryFilterModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.DynamicChannelModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.GetFeedbackFieldModel
import com.tokopedia.tokopedianow.searchcategory.domain.model.QuickFilterModel
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_ID
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.tokopedianow.searchcategory.utils.TYPE
import com.tokopedia.tokopedianow.searchcategory.utils.WAREHOUSE_ID
import com.tokopedia.usecase.RequestParams

internal fun getTokonowQueryParam(requestParams: RequestParams): Map<String?, Any> {
    return requestParams.parameters[TOKONOW_QUERY_PARAMS] as? Map<String?, Any> ?: mapOf()
}

internal fun createAceSearchProductRequest(params: Map<String?, Any>) = GraphqlRequest(
        ACE_SEARCH_PRODUCT_QUERY,
        AceSearchProductModel::class.java,
        mapOf(SearchConstant.GQL.KEY_PARAMS to generateUrlParamString(params))
)

internal fun createCategoryFilterRequest(params: Map<String?, Any>) = GraphqlRequest(
        CATEGORY_FILTER_QUERY,
        CategoryFilterModel::class.java,
        mapOf(SearchConstant.GQL.KEY_PARAMS to generateUrlParamString(params))
)

internal fun createQuickFilterRequest(params: Map<String?, Any>) = GraphqlRequest(
        QUICK_FILTER_QUERY,
        QuickFilterModel::class.java,
        mapOf(SearchConstant.GQL.KEY_PARAMS to generateUrlParamString(params))
)

internal fun createDynamicChannelRequest(type: String) = GraphqlRequest(
        DYNAMIC_CHANNEL_QUERY,
        DynamicChannelModel::class.java,
        mapOf(TYPE to type)
)

internal fun createRepurchaseWidgetRequest(params: Map<String, Any>): GraphqlRequest {
    val warehouseID = params[WAREHOUSE_ID]?.toString() ?: ""
    val queryParam = createRepurchaseQueryParam(params)

    return GraphqlRequest(
        GetRepurchaseWidget.getQuery(),
        GetRepurchaseResponse::class.java,
        mapOf(
            GetRepurchaseWidgetUseCase.PARAM_WAREHOUSE_ID to warehouseID,
            GetRepurchaseWidgetUseCase.PARAM_QUERY_PARAM to queryParam,
        )
    )
}

internal fun getFeedbackFieldToggleData(
    graphqlResponse: GraphqlResponse
) : GetFeedbackFieldModel {
    return graphqlResponse
        .getData<GetFeedbackFieldModel?>(GetFeedbackFieldModel::class.java) ?: GetFeedbackFieldModel()
}

internal fun createFeedbackFieldToggleRequest() : GraphqlRequest = GraphqlRequest(
    FEEDBACK_FIELD_TOGGLE_QUERY,
    GetFeedbackFieldModel::class.java,
    mapOf()
)


private fun createRepurchaseQueryParam(params: Map<String, Any>): String {
    val categoryID = params[CATEGORY_ID]?.toString() ?: ""

    val queryParamList = listOf(
        if (categoryID.isNotEmpty()) "$PARAM_CAT_ID=$categoryID" else ""
    )

    return queryParamList
        .filter(String::isNotEmpty)
        .joinToString(separator = "&")
}

private const val ACE_SEARCH_PRODUCT_QUERY = """
    query aceSearchProductV4(${'$'}params: String!) {
      ace_search_product_v4(params: ${'$'}params){
        header {
          totalData
          totalDataText
          responseCode
          keywordProcess
        }
        data {
          isQuerySafe
          suggestion {
            suggestion
            query
            text
          }
          related {
            relatedKeyword
            position
            otherRelated {
                keyword
                url
                applink
                product {
                    id
                    name
                    price
                    imageUrl
                    url
                    applink
                    priceStr
                    wishlist
                    ratingAverage
                    stock
                    minOrder
                    maxOrder
                    labelGroups {
                        title
                        position
                        type
                        url
                    }
                    shop {
                        id
                    }
                }
            }
          }
          products {
            id
            imageUrl300
            name
            price
            priceInt
            discountPercentage
            originalPrice
            childs
            parentId
            maxOrder
            ratingAverage
            minOrder
            stock
            source_engine
            boosterList
            shop {
              id
              name
            }
            labelGroups {
              url
              title
              type
              position
            }
            labelGroupVariant {
              title
              type
              type_variant
              hex_color
            }
            wishlist
          }
        }
      }
    }
"""

private const val CATEGORY_FILTER_QUERY = """
    query CategoryFilter(${'$'}params: String!) {
      filter_sort_product(params:${'$'}params){
        data {
          filter {
            title
            subTitle
            options {
              name
              key
              icon
              value
              isNew
              inputType
              totalData
              valMax
              valMin
              hexColor
              child {
                  key
                  value
                  name
                  icon
                  inputType
                  totalData
                  child {
                      key
                      value
                      name
                      icon
                      inputType
                      totalData
                  }
              }
            }
          }
        }
      }
    }
"""

private const val QUICK_FILTER_QUERY = """
    query QuickFilter(${'$'}params: String!) {
      filter_sort_product(params:${'$'}params){
        data {
          filter {
            title
            subTitle
            options {
              name
              key
              icon
              value
              isNew
              inputType
              totalData
              valMax
              valMin
              hexColor
              child {
                  key
                  value
                  name
                  icon
                  inputType
                  totalData
                  child {
                      key
                      value
                      name
                      icon
                      inputType
                      totalData
                  }
              }
            }
          }
        }
      }
    }
"""

private const val DYNAMIC_CHANNEL_QUERY = """
    query getDynamicChannel(${'$'}type: String!){
        dynamicHomeChannel {
            channels(type: ${'$'}type){
              id
              group_id
              galaxy_attribution
              persona
              brand_id
              category_persona
              name
              layout
              type
              pageName
              showPromoBadge
              categoryID
              perso_type
              campaignCode
              has_close_button
              isAutoRefreshAfterExpired
              token
              widgetParam
              contextualInfo
              header {
                id
                name
                subtitle
                url
                applink
                serverTime
                expiredTime
                backColor
                backImage
                textColor
              }
               grids {
                 campaignCode
                 id
                 back_color
                 name
                 url
                 applink
                 price
                 slashedPrice
                 discount
                 imageUrl
                 label
                 soldPercentage
                 attribution
                 productClickUrl
                 impression
                 cashback
                 isTopads
                 freeOngkir {
                    isActive
                    imageUrl
                  }
                  productViewCountFormatted
                  isOutOfStock
                  warehouseID
                  minOrder
                  recommendationType
                  shop{
                    shopID
                    city
                   }
                  labelGroup {
                    title
                    position
                    type
                    url
                  }
                  has_buy_button
                  rating
                  ratingAverage
                  count_review
                  benefit {
                     type
                     value
                  }
                  textColor
                  badges {
                     title
                     image_url
                  }
              }
              banner {
                id
                title
                description
                url
                back_color
                cta {
                  type
                  mode
                  text
                  coupon_code
                }
                applink
                text_color
                image_url
                attribution
                gradient_color
    
              }
            }
        }
    }
"""

private const val FEEDBACK_FIELD_TOGGLE_QUERY = """
    {
      TokonowFeedbackFieldToggle(){
        header{
          process_time
          error_code
          messages
          reason
        }
        data{
          isActive
        }
      }
    }
"""
