package com.tokopedia.tokofood.feature.search.initialstate.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.common.domain.param.TokoFoodLocationParamMapper
import com.tokopedia.tokofood.feature.search.initialstate.domain.mapper.TokoFoodInitStateSearchMapper
import com.tokopedia.tokofood.feature.search.initialstate.domain.model.TokoFoodInitSearchStateResponse
import com.tokopedia.tokofood.feature.search.initialstate.domain.query.INITIAL_SEARCH_STATE_QUERY
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.InitialStateWrapperUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("InitialSearchStateQuery", INITIAL_SEARCH_STATE_QUERY)
class GetInitSearchStateUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<TokoFoodInitSearchStateResponse>,
    private val mapper: TokoFoodInitStateSearchMapper
) {
    init {
        useCase.setGraphqlQuery(InitialSearchStateQuery())
        useCase.setTypeClass(TokoFoodInitSearchStateResponse::class.java)
    }

    suspend fun execute(localCacheModel: LocalCacheModel): InitialStateWrapperUiModel {
        useCase.setRequestParams(createRequestParams(localCacheModel))
        return mapper.mapToInitialStateWrapperUiModel(useCase.executeOnBackground().tokofoodInitSearchState)
    }

    private fun createRequestParams(localCacheModel: LocalCacheModel): Map<String, Any> {
        return RequestParams.create().apply {
            putString(LOCATION_KEY, TokoFoodLocationParamMapper.mapLocation(localCacheModel))
        }.parameters
    }

    companion object {
        private const val LOCATION_KEY = "location"
    }
}