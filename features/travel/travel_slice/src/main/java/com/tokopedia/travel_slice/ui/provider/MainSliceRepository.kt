package com.tokopedia.travel_slice.ui.provider

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.travel_slice.data.HotelData
import com.tokopedia.travel_slice.data.HotelOrderListModel
import com.tokopedia.travel_slice.data.SuggestionCity
import com.tokopedia.travel_slice.usecase.GetPropertiesUseCase
import com.tokopedia.travel_slice.usecase.GetSuggestionCityUseCase
import com.tokopedia.travel_slice.utils.TravelSliceQueries.HOTEL_ORDER_LIST_QUERY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 25/11/20
 */

class HotelSliceRepository @Inject constructor(
        private val getSuggestionCityUseCase: GetSuggestionCityUseCase,
        private val getPropertiesUseCase: GetPropertiesUseCase,
        private val graphqlRepository: GraphqlRepository) {

    suspend fun getHotelData(city: String, checkIn: String, checkOut: String): List<HotelData> {
        return withContext(Dispatchers.IO, block = {
            val cityIdResponse = getHotelSuggestion(city)
            if (cityIdResponse != null) {
                getPropertiesUseCase.createParam(checkIn, checkOut, cityIdResponse)
                return@withContext getPropertiesUseCase.executeOnBackground().propertyList
            } else {
                throw Throwable()
            }
        })
    }

    private suspend fun getHotelSuggestion(keyword: String): SuggestionCity? {
        getSuggestionCityUseCase.cityParam = keyword
        return getSuggestionCityUseCase.executeOnBackground().firstOrNull()
    }

    suspend fun getHotelOrderData(isLoggedIn: Boolean): List<HotelOrderListModel> {
        if (isLoggedIn) {
            val dataParams = mapOf("orderCategory" to "HOTELS", "Page" to 1, "PerPage" to 3)
            return withContext(Dispatchers.IO, block = {
                val graphqlRequest = GraphqlRequest(HOTEL_ORDER_LIST_QUERY, HotelOrderListModel.Response::class.java, dataParams)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }).getSuccessData<HotelOrderListModel.Response>().response
        } else {
            throw Throwable("unauthorized user")
        }
    }
}