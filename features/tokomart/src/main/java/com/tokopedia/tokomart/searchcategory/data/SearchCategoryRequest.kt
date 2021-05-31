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

private const val DYNAMIC_CHANNEL_QUERY =
        "query getDynamicChannel(\${'$'}type: String!){\n" +
            "    dynamicHomeChannel {\n" +
            "        channels(type: \${'$'}type){\n" +
            "          id\n" +
            "          group_id\n" +
            "          galaxy_attribution\n" +
            "          persona\n" +
            "          brand_id\n" +
            "          category_persona\n" +
            "          name\n" +
            "          layout\n" +
            "          type\n" +
            "          pageName\n" +
            "          showPromoBadge\n" +
            "          categoryID\n" +
            "          perso_type\n" +
            "          campaignCode\n" +
            "          has_close_button\n" +
            "          isAutoRefreshAfterExpired\n" +
            "          token\n" +
            "          widgetParam\n" +
            "          contextualInfo\n" +
            "          header {\n" +
            "            id\n" +
            "            name\n" +
            "            subtitle\n" +
            "            url\n" +
            "            applink\n" +
            "            serverTime\n" +
            "            expiredTime\n" +
            "            backColor\n" +
            "            backImage\n" +
            "            textColor\n" +
            "          }\n" +
            "           grids {\n" +
            "             campaignCode\n" +
            "             id\n" +
            "             back_color\n" +
            "             name\n" +
            "             url\n" +
            "             applink\n" +
            "             price\n" +
            "             slashedPrice\n" +
            "             discount\n" +
            "             imageUrl\n" +
            "             label\n" +
            "             soldPercentage\n" +
            "             attribution\n" +
            "             productClickUrl\n" +
            "             impression\n" +
            "             cashback\n" +
            "             isTopads\n" +
            "             freeOngkir {\n" +
            "                isActive\n" +
            "                imageUrl\n" +
            "              }\n" +
            "              productViewCountFormatted\n" +
            "              isOutOfStock\n" +
            "              warehouseID\n" +
            "              minOrder\n" +
            "              recommendationType\n" +
            "              shop{\n" +
            "                shopID\n" +
            "                city\n" +
            "               }\n" +
            "              labelGroup {\n" +
            "                title\n" +
            "                position\n" +
            "                type\n" +
            "                url\n" +
            "              }\n" +
            "              has_buy_button\n" +
            "              rating\n" +
            "              ratingAverage\n" +
            "              count_review\n" +
            "              benefit {\n" +
            "                 type\n" +
            "                 value\n" +
            "              }\n" +
            "              textColor\n" +
            "              badges {\n" +
            "                 title\n" +
            "                 image_url\n" +
            "              }\n" +
            "          }\n" +
            "          banner {\n" +
            "            id\n" +
            "            title\n" +
            "            description\n" +
            "            url\n" +
            "            back_color\n" +
            "            cta {\n" +
            "              type\n" +
            "              mode\n" +
            "              text\n" +
            "              coupon_code\n" +
            "            }\n" +
            "            applink\n" +
            "            text_color\n" +
            "            image_url\n" +
            "            attribution\n" +
            "            gradient_color\n" +
            "\n" +
            "          }\n" +
            "        }\n" +
            "    }\n" +
            "}"