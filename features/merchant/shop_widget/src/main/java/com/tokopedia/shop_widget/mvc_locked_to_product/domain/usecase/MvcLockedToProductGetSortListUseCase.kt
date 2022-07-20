package com.tokopedia.shop_widget.mvc_locked_to_product.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop_widget.mvc_locked_to_product.domain.model.MvcLockedToProductSortListResponse
import javax.inject.Inject

class MvcLockedToProductGetSortListUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<MvcLockedToProductSortListResponse>(gqlRepository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, GQL_QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(MvcSortList())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(MvcLockedToProductSortListResponse::class.java)
    }

    fun setParams() {
        setRequestParams(
            mapOf(KEY_PARAMS to generateParamsValue())
        )
    }

    private fun generateParamsValue(): String {
        val paramValueList = mutableListOf<String>()
        addParamValue(paramValueList, PARAM_SOURCE_KEY, PARAM_SOURCE_VALUE)
        addParamValue(paramValueList, PARAM_DEVICE_KEY, PARAM_DEVICE_VALUE)
        return paramValueList.joinToString(separator = PARAM_DELIMITER)
    }

    private fun addParamValue(
        paramValueList: MutableList<String>,
        paramSourceKey: String,
        paramSourceValue: String
    ) {
        paramValueList.add("$paramSourceKey=$paramSourceValue")
    }

    companion object {
        private const val KEY_PARAMS = "params"
        private const val QUERY_NAME = "MvcSortList"
        private const val PARAM_SOURCE_KEY = "source"
        private const val PARAM_SOURCE_VALUE = "mvc_page"
        private const val PARAM_DEVICE_KEY = "device"
        private const val PARAM_DEVICE_VALUE = "android"
        private const val PARAM_DELIMITER = "&"
        private const val GQL_QUERY = """
            query MvcSortList(${'$'}params: String!) {
                filter_sort_product(params: ${'$'}params) {
                      data {
                        sort {
                          name
                          key
                          value
                        }
                     }
               }
            }"""
    }
}