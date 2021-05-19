package com.tokopedia.tokomart.searchcategory.data

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.utils.UrlParamUtils.generateUrlParamString
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokomart.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.tokomart.searchcategory.domain.model.CategoryFilterModel
import com.tokopedia.tokomart.searchcategory.domain.model.QuickFilterModel
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_QUERY_PARAMS
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
        mapOf(SearchConstant.GQL.KEY_PARAMS to createCategoryFilterParams(params))
)

private fun createCategoryFilterParams(params: Map<String?, Any>): String {
    val paramMap = params.toMutableMap().also {
//            it[SearchApiConst.SOURCE] = CATEGORY_TOKONOW // Temporary, source should be category tokonow
        it[SearchApiConst.SOURCE] = SearchApiConst.DEFAULT_VALUE_SOURCE_QUICK_FILTER
    }

    return generateUrlParamString(paramMap)
}

internal fun createQuickFilterRequest(params: Map<String?, Any>) = GraphqlRequest(
        QUICK_FILTER_QUERY,
        QuickFilterModel::class.java,
        mapOf(SearchConstant.GQL.KEY_PARAMS to createQuickFilterParams(params))
)

private fun createQuickFilterParams(params: Map<String?, Any>): String {
    val paramMap = params.toMutableMap().also {
//            it[SearchApiConst.SOURCE] = QUICK_FILTER_TOKONOW // Temporary, source should be quick filter tokonow
        it[SearchApiConst.SOURCE] = SearchApiConst.DEFAULT_VALUE_SOURCE_QUICK_FILTER
    }

    return generateUrlParamString(paramMap)
}

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
