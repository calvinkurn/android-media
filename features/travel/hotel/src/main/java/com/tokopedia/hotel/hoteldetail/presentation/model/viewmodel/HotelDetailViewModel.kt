package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.ticker.TravelTickerHotelPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               private val dispatcher: CoroutineDispatchers,
                                               private val useCase: GetHotelRoomListUseCase,
                                               private val travelTickerUseCase: TravelTickerCoroutineUseCase)
    : BaseViewModel(dispatcher.io) {

    val hotelInfoResult = MutableLiveData<Result<PropertyDetailData>>()
    val hotelReviewResult = MutableLiveData<Result<HotelReview.ReviewData>>()
    val roomListResult = MutableLiveData<Result<MutableList<HotelRoom>>>()

    private val mutableTickerData = MutableLiveData<Result<TravelTickerModel>>()
    val tickerData: LiveData<Result<TravelTickerModel>>
        get() = mutableTickerData

    fun fetchTickerData() {
        launch(dispatcher.main) {
            val tickerData = travelTickerUseCase.execute(TravelTickerInstanceId.HOTEL, TravelTickerHotelPage.SEARCH_DETAIL)
            mutableTickerData.postValue(tickerData)
        }
    }

    fun getHotelDetailData(hotelInfoQuery: String, roomListQuery: String, hotelReviewQuery: String,
                           propertyId: Long, searchParam: HotelHomepageModel, source: String) {
        launch {
            getHotelInfo(hotelInfoQuery, propertyId, source)
            getHotelReview(hotelReviewQuery, propertyId)
            getRoomList(roomListQuery, searchParam)
        }
    }

    fun getHotelDetailDataWithoutRoom(hotelInfoQuery: String, hotelReviewQuery: String, propertyId: Long, source: String) {
        launch {
            getHotelInfo(hotelInfoQuery, propertyId, source)
            getHotelReview(hotelReviewQuery, propertyId)
        }
    }

    fun getRoomWithoutHotelData(roomListQuery: String, searchParam: HotelHomepageModel) {
        launch {
            getRoomList(roomListQuery, searchParam)
        }
    }

    private suspend fun getHotelInfo(rawQuery: String, propertyId: Long, source: String) {

        val requestDetailParams = PropertyDataParam(propertyId, source)
        val detailParams = mapOf(PARAM_HOTEL_INFO_PROPERTY to requestDetailParams)

        try {
            val hotelInfoData = async {
                val response = withContext(dispatcher.main) {
                    val detailRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_INFO, detailParams)
                    graphqlRepository.getReseponse(listOf(detailRequest))
                            .getSuccessData<PropertyDetailData.Response>()
                }
                response
            }

            hotelInfoResult.postValue(Success(hotelInfoData.await().propertyDetailData))
        } catch (t: Throwable) {
            hotelInfoResult.postValue(Fail(t))
        }
    }

    private suspend fun getHotelReview(rawQuery: String, propertyId: Long) {

        val requestReviewParams = HotelReviewParam(propertyId = propertyId,
                page = DEFAULT_PAGE_REVIEW,
                rows = DEFAULT_ROW_REVIEW,
                sortBy = DEFAULT_SORT_BY_REVIEW,
                sortType = DEFAULT_SORT_ORDER,
                filterByCountry = DEFAULT_REVIEW_BY_COUNTRY)

        val reviewParams = mapOf(PARAM_HOTEL_INFO_PROPERTY to requestReviewParams)

        try {
            val hotelReviewData = async {
                val response = withContext(dispatcher.main) {
                    val reviewRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_REVIEW, reviewParams)
                    graphqlRepository.getReseponse(listOf(reviewRequest))
                            .getSuccessData<HotelReview.Response>()
                }
                response
            }

            hotelReviewResult.postValue(Success(hotelReviewData.await().propertyReview))
        } catch (t: Throwable) {
            hotelReviewResult.postValue(Fail(t))
        }
    }

    private suspend fun getRoomList(rawQuery: String, searchParam: HotelHomepageModel) {
        roomListResult.postValue(useCase.execute(rawQuery, HotelRoomListPageModel(
                propertyId = searchParam.locId,
                checkIn = searchParam.checkInDate,
                checkOut = searchParam.checkOutDate,
                adult = searchParam.adultCount,
                child = 0,
                room = searchParam.roomCount)))
    }

    companion object {
        const val PARAM_HOTEL_INFO_PROPERTY = "data"

        const val DEFAULT_PAGE_REVIEW = 0
        const val DEFAULT_ROW_REVIEW = 5
        const val DEFAULT_SORT_BY_REVIEW = "score"
        const val DEFAULT_SORT_ORDER = "desc"
        const val DEFAULT_REVIEW_BY_COUNTRY = "all"

        private val TYPE_HOTEL_INFO = PropertyDetailData.Response::class.java
        private val TYPE_HOTEL_REVIEW = HotelReview.Response::class.java
    }

}