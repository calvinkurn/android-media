package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDataParam
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelReviewParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               dispatcher: CoroutineDispatcher,
                                               private val useCase: GetHotelRoomListUseCase)
    : BaseViewModel(dispatcher) {

    val hotelInfoResult = MutableLiveData<Result<PropertyDetailData>>()
    val hotelReviewResult = MutableLiveData<Result<HotelReview.ReviewData>>()
    val roomListResult = MutableLiveData<Result<MutableList<HotelRoom>>>()

    fun getHotelDetailData(hotelInfoQuery: String, roomListQuery: String, hotelReviewQuery: String, propertyId: Int, searchParam: HotelHomepageModel) {
        launch {
            getHotelInfo(hotelInfoQuery, propertyId)
            getHotelReview(hotelReviewQuery, propertyId)
            getRoomList(roomListQuery, searchParam)
        }
    }

    private suspend fun getHotelInfo(rawQuery: String, propertyId: Int) {

        val requestDetailParams = PropertyDataParam(propertyId)
        val detailParams = mapOf(PARAM_HOTEL_INFO_PROPERTY to requestDetailParams)

        try {
            val hotelInfoData = async {
                val response = withContext(Dispatchers.Default) {
                    val detailRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_INFO, detailParams)
                    graphqlRepository.getReseponse(listOf(detailRequest))
                            .getSuccessData<PropertyDetailData.Response>()
                }
                response
            }

            hotelInfoResult.value = Success(hotelInfoData.await().propertyDetailData)
        } catch (t: Throwable) {
            hotelInfoResult.value = Fail(t)
        }
    }

    private suspend fun getHotelReview(rawQuery: String, propertyId: Int) {

        val requestReviewParams = HotelReviewParam(propertyId, DEFAULT_PAGE_REVIEW, DEFAULT_ROW_REVIEW, DEFAULT_SORT_BY_REVIEW, DEFAULT_SORT_ORDER)
        val reviewParams = mapOf(PARAM_HOTEL_INFO_PROPERTY to requestReviewParams)

        try {
            val hotelReviewData = async {
                val response = withContext(Dispatchers.Default) {
                    val reviewRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_REVIEW, reviewParams)
                    graphqlRepository.getReseponse(listOf(reviewRequest))
                            .getSuccessData<HotelReview.Response>()
                }
                response
            }

            hotelReviewResult.value = Success(hotelReviewData.await().propertyReview)
        } catch (t: Throwable) {
            hotelReviewResult.value = Fail(t)
        }
    }

    private suspend fun getRoomList(rawQuery: String, searchParam: HotelHomepageModel) {
        roomListResult.value = useCase.execute(rawQuery, HotelRoomListPageModel(
                propertyId = searchParam.locId,
                checkIn = searchParam.checkInDate,
                checkOut = searchParam.checkOutDate,
                adult = searchParam.adultCount,
                child = 0,
                room = searchParam.roomCount))
    }

    companion object {
        const val PARAM_HOTEL_INFO_PROPERTY = "data"

        const val DEFAULT_PAGE_REVIEW = 0
        const val DEFAULT_ROW_REVIEW = 5
        const val DEFAULT_SORT_BY_REVIEW = "score"
        const val DEFAULT_SORT_ORDER = "desc"

        private val TYPE_HOTEL_INFO = PropertyDetailData.Response::class.java
        private val TYPE_HOTEL_REVIEW = HotelReview.Response::class.java
    }

}