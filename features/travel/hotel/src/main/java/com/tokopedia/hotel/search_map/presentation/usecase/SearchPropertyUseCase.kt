package com.tokopedia.hotel.search_map.presentation.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.search_map.data.model.PropertySearch
import com.tokopedia.hotel.search_map.data.model.params.SearchParam
import com.tokopedia.hotel.search_map.presentation.viewmodel.HotelSearchMapViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 31/08/20
 */

class SearchPropertyUseCase @Inject constructor(val graphqlRepository: GraphqlRepository) :
        GraphqlUseCase<PropertySearch.Response>(graphqlRepository) {
    suspend fun execute(rawQuery: GqlQueryInterface, searchParam: SearchParam): Result<PropertySearch> {
            val response = graphqlRepository.response(listOf(buildRequest(rawQuery, searchParam)))
            val error = response.getError(PropertySearch.Response::class.java)
        return if (error == null || error.isEmpty()) {
            val dataResult = response.getData<PropertySearch.Response>(PropertySearch.Response::class.java)
            Success(dataResult.response)
        } else {
            val errorMessage = error.mapNotNull { it.message }.joinToString(separator = ", ")
            throw MessageErrorException(errorMessage, error[0].extensions.code.toString())
        }
    }

    private fun buildRequest(rawQuery: GqlQueryInterface, searchParam: SearchParam): GraphqlRequest {
        val params = mapOf(HotelSearchMapViewModel.PARAM_SEARCH_PROPERTY to searchParam)
        return GraphqlRequest(
            rawQuery,
            PropertySearch.Response::class.java,
            params
        )
    }
}
