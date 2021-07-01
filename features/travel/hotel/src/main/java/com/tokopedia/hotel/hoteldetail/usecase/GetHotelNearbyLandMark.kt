package com.tokopedia.hotel.hoteldetail.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.hotel.hoteldetail.data.entity.HotelNearbyLandmark
import com.tokopedia.hotel.hoteldetail.data.entity.HotelNearbyLandmarkParam
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by astidhiyaa on 01/07/2021
 */
class GetHotelNearbyLandMark @Inject constructor(val graphqlRepository: GraphqlRepository):
        GraphqlUseCase<HotelNearbyLandmark.Response>(graphqlRepository) {
    suspend fun execute(rawQuery: String, param: HotelNearbyLandmarkParam): Result<HotelNearbyLandmark> {
        return try {
            this.setTypeClass(HotelNearbyLandmark.Response::class.java)
            this.setGraphqlQuery(rawQuery)
            this.setRequestParams(mapOf(HotelDetailViewModel.PARAM_HOTEL_INFO_PROPERTY to param))

            val data = this.executeOnBackground()
            Success(data.response)
        } catch (t: Throwable) {
            Fail(t)
        }
    }
}