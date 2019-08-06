package com.tokopedia.search.result.domain.usecase.searchshop

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.domain.usecase.SearchUseCase
import com.tokopedia.search.utils.UrlParamUtils
import java.util.*

class SearchShopLoadMoreUseCase(
        private val graphqlUseCase: GraphqlUseCase<SearchShopModel>
): SearchUseCase<SearchShopModel>() {

    override suspend fun executeOnBackground(): SearchShopModel {
        graphqlUseCase.setRequestParams(createParametersForQuery())

        return graphqlUseCase.executeOnBackground()
    }

    private fun createParametersForQuery(): Map<String, Any> {
        val variables = HashMap<String, Any>()

        variables[SearchConstant.GQL.KEY_PARAMS] = UrlParamUtils.generateUrlParamString(requestParams)

        return variables
    }
}