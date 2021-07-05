package com.tokopedia.travel_slice.hotel.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel_slice.hotel.data.HotelList
import com.tokopedia.travel_slice.hotel.data.HotelParam
import com.tokopedia.travel_slice.hotel.data.SuggestionCity
import com.tokopedia.travel_slice.hotel.data.HotelSliceQueries
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * @author by jessica on 19/10/20
 */

class GetPropertiesUseCase @Inject constructor(private val repository: GraphqlRepository) : UseCase<HotelList>() {

    private var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): HotelList {
        val graphqlRequest = GraphqlRequest(HotelSliceQueries.GET_HOTEL_PROPERTY_QUERY, HotelList.Response::class.java, params)
        val response = repository.getReseponse(listOf(graphqlRequest)).getSuccessData<HotelList.Response>()
        return response.propertySearch
    }

    fun createParam(checkIn: String, checkOut: String, city: SuggestionCity) {
        params = mapOf(DATA_PARAM to HotelParam(checkIn = checkIn, checkOut = checkOut,
                location = HotelParam.ParamLocation(searchType = city.searchType, searchId = city.searchId)))
    }

    companion object {
        const val DATA_PARAM = "data"
    }
}

