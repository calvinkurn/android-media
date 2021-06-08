package com.tokopedia.tokomart.searchcategory.data

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.UrlParamUtils.generateUrlParamString
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokomart.searchcategory.domain.model.CategoryFilterModel
import com.tokopedia.tokomart.searchcategory.domain.model.DynamicChannelModel
import com.tokopedia.tokomart.searchcategory.domain.model.QuickFilterModel
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_QUERY_PARAMS
import com.tokopedia.tokomart.searchcategory.utils.TYPE
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

private const val ACE_SEARCH_PRODUCT_QUERY = """
    query aceSearchProductV4(${'$'}params: String!) {
      ace_search_product_v4(params: ${'$'}params){
        header {
          totalData
          totalDataText
        }
        data {
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
            stock
            shop {
              id
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