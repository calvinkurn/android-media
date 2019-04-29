package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDataParam
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.*
import javax.inject.Inject

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               dispatcher: CoroutineDispatcher,
                                               private val useCase: GetHotelRoomListUseCase)
    : BaseViewModel(dispatcher) {

    val hotelInfoResult = MutableLiveData<Result<PropertyDetailData>>()
    val roomListResult = MutableLiveData<Result<MutableList<HotelRoom>>>()

    fun getHotelDetailData(hotelInfoQuery: String, roomListQuery: String, propertyId: Int, searchParam: HotelHomepageModel) {
        launch {
            getHotelInfo(hotelInfoQuery, propertyId)
            getRoomList(roomListQuery, searchParam)
        }
    }

    suspend fun getHotelInfo(rawQuery: String, propertyId: Int) {

        val requestParams = PropertyDataParam(propertyId)
        val params = mapOf(PARAM_HOTEL_INFO_PROPERTY to requestParams)

        try {
            val hotelInfoData = async {
                val response = withContext(Dispatchers.Default) {
                    val graphqlRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_INFO, params)
                    graphqlRepository.getReseponse(listOf(graphqlRequest))
                            .getSuccessData<PropertyDetailData.Response>()
                }
                response
            }

            hotelInfoResult.value = Success(hotelInfoData.await().propertyDetailData)
        } catch (t: Throwable) {
            hotelInfoResult.value = Fail(t)
        }
    }

    suspend fun getRoomList(rawQuery: String, searchParam: HotelHomepageModel) {
        roomListResult.value = useCase.execute(rawQuery, HotelRoomListPageModel(
                propertyId = searchParam.locId,
                checkIn = searchParam.checkInDate,
                checkOut = searchParam.checkOutDate,
                adult = searchParam.adultCount,
                child = searchParam.childCount,
                room = searchParam.roomCount))
    }

    companion object {
        const val PARAM_HOTEL_INFO_PROPERTY = "data"
        private val TYPE_HOTEL_INFO = PropertyDetailData.Response::class.java
    }

}