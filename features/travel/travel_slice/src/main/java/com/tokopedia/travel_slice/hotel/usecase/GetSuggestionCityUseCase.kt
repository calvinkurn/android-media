package com.tokopedia.travel_slice.hotel.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel_slice.hotel.data.SearchSuggestionParam
import com.tokopedia.travel_slice.hotel.data.SuggestionCity
import com.tokopedia.travel_slice.ui.provider.MainSliceProvider
import com.tokopedia.travel_slice.hotel.data.HotelSliceQueries
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 19/10/20
 */

class GetSuggestionCityUseCase @Inject constructor(private val repository: GraphqlRepository) : UseCase<List<SuggestionCity>>() {

    var cityParam = ""

    override suspend fun executeOnBackground(): List<SuggestionCity> {
        val getSuggestionParams = mapOf(MainSliceProvider.DATA_PARAM to SearchSuggestionParam(cityParam))
        val suggestionGraphqlRequest = GraphqlRequest(HotelSliceQueries.SEARCH_SUGGESTION, SuggestionCity.Response::class.java, getSuggestionParams)
        val response = repository.getReseponse(listOf(suggestionGraphqlRequest)).getSuccessData<SuggestionCity.Response>()
        return response.response.data
    }
}