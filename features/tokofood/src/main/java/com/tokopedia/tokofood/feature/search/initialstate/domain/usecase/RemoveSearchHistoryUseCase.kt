package com.tokopedia.tokofood.feature.search.initialstate.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.tokofood.feature.search.initialstate.domain.mapper.TokoFoodInitStateSearchMapper
import com.tokopedia.tokofood.feature.search.initialstate.domain.model.RemoveSearchHistoryResponse
import com.tokopedia.tokofood.feature.search.initialstate.domain.query.REMOVE_SEARCH_HISTORY_QUERY
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RemoveSearchHistoryUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("RemoveSearchHistoryQuery", REMOVE_SEARCH_HISTORY_QUERY)
class RemoveSearchHistoryUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<RemoveSearchHistoryResponse>,
    private val mapper: TokoFoodInitStateSearchMapper
) {

    init {
        useCase.setGraphqlQuery(RemoveSearchHistoryQuery())
        useCase.setTypeClass(RemoveSearchHistoryResponse::class.java)
    }

    suspend fun execute(key: String): RemoveSearchHistoryUiModel {
        useCase.setRequestParams(createRequestParams(key))
        return mapper.mapToRemoveSearchUiModel(useCase.executeOnBackground().tokofoodRemoveSearchHistory)
    }

    private fun createRequestParams(key: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(KEY_PARAM, key)
        }.parameters
    }

    companion object {
        private const val KEY_PARAM = "key"
    }
}