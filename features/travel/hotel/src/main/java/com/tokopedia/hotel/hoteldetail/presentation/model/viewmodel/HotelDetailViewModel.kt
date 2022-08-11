package com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.travel.ticker.TravelTickerHotelPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.hoteldetail.data.entity.*
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelReviewParam
import com.tokopedia.hotel.hoteldetail.usecase.GetHotelNearbyLandMark
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
                                               private val hotelNearbyLandmark: GetHotelNearbyLandMark,
                                               private val travelTickerUseCase: TravelTickerCoroutineUseCase)
    : BaseViewModel(dispatcher.io) {

    val hotelInfoResult = MutableLiveData<Result<PropertyDetailData>>()
    val hotelReviewResult = MutableLiveData<Result<HotelReview.ReviewData>>()
    val roomListResult = MutableLiveData<Result<MutableList<HotelRoom>>>()
    val hotelNearbyLandmarks = MutableLiveData<Result<HotelNearbyLandmark>>()

    private val mutableTickerData = MutableLiveData<Result<TravelTickerModel>>()
    val tickerData: LiveData<Result<TravelTickerModel>>
        get() = mutableTickerData

    fun fetchTickerData() {
        launch(dispatcher.main) {
            val tickerData = travelTickerUseCase.execute(TravelTickerInstanceId.HOTEL, TravelTickerHotelPage.SEARCH_DETAIL)
            mutableTickerData.postValue(tickerData)
        }
    }

    fun getHotelDetailData(hotelInfoQuery: GqlQueryInterface, roomListQuery: GqlQueryInterface, hotelReviewQuery: GqlQueryInterface, hotelNearbyLandmarksQuery: GqlQueryInterface,
                           propertyId: Long, searchParam: HotelHomepageModel, source: String) {
        launch {
            getHotelInfo(hotelInfoQuery, propertyId, source)
            getHotelReview(hotelReviewQuery, propertyId.toString())
            getRoomList(roomListQuery, searchParam)
            getNearbyLandmarks(hotelNearbyLandmarksQuery, propertyId)
        }
    }

    fun getHotelDetailDataWithoutRoom(hotelInfoQuery: GqlQueryInterface, hotelReviewQuery: GqlQueryInterface, hotelNearbyLandmarksQuery: GqlQueryInterface, propertyId: Long, source: String) {
        launch {
            getHotelInfo(hotelInfoQuery, propertyId, source)
            getHotelReview(hotelReviewQuery, propertyId.toString())
            getNearbyLandmarks(hotelNearbyLandmarksQuery, propertyId)
        }
    }

    fun getRoomWithoutHotelData(roomListQuery: GqlQueryInterface, searchParam: HotelHomepageModel) {
        launch {
            getRoomList(roomListQuery, searchParam)
        }
    }

    private suspend fun getHotelInfo(rawQuery: GqlQueryInterface, propertyId: Long, source: String) {

        val requestDetailParams = PropertyDataParam(propertyId, source)
        val detailParams = mapOf(PARAM_HOTEL_INFO_PROPERTY to requestDetailParams)

        try {
            val hotelInfoData = async {
                val response = withContext(dispatcher.main) {
                    val detailRequest = GraphqlRequest(rawQuery, TYPE_HOTEL_INFO, detailParams)
                    graphqlRepository.response(listOf(detailRequest))
                            .getSuccessData<PropertyDetailData.Response>()
                }
                response
            }

            hotelInfoResult.postValue(Success(hotelInfoData.await().propertyDetailData))
        } catch (t: Throwable) {
            hotelInfoResult.postValue(Fail(t))
        }
    }

    private suspend fun getHotelReview(rawQuery: GqlQueryInterface, propertyId: String) {

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
                    graphqlRepository.response(listOf(reviewRequest))
                            .getSuccessData<HotelReview.Response>()
                }
                response
            }

            hotelReviewResult.postValue(Success(hotelReviewData.await().propertyReview))
        } catch (t: Throwable) {
            hotelReviewResult.postValue(Fail(t))
        }
    }

    private suspend fun getRoomList(rawQuery: GqlQueryInterface, searchParam: HotelHomepageModel) {
        try{
            val hotelRoomListData = async {
                val data = withContext(dispatcher.io){
                    useCase.execute(rawQuery, HotelRoomListPageModel(
                        propertyId = searchParam.locId,
                        checkIn = searchParam.checkInDate,
                        checkOut = searchParam.checkOutDate,
                        adult = searchParam.adultCount,
                        child = 0,
                        room = searchParam.roomCount))                }
                data
            }
            roomListResult.postValue(hotelRoomListData.await())
        } catch (t: Throwable) {
            roomListResult.postValue(Fail(t))
        }
    }

    private suspend fun getNearbyLandmarks(rawQuery: GqlQueryInterface, propertyId: Long){
        val filterNearby = HotelNearbyLandmarkParam.FilterNearbyLandmark(propertyId)
        val nearbyLandmarkParam = HotelNearbyLandmarkParam(template = HotelNearbyLandmarkTemplate.HOTEL_PDP.value, filter = filterNearby)

        try{
            val hotelNearbyData = async {
                val data = withContext(dispatcher.io){
                    hotelNearbyLandmark.execute(rawQuery, nearbyLandmarkParam)
                }
                data
            }
            hotelNearbyLandmarks.postValue(hotelNearbyData.await())
        } catch (t: Throwable) {
            hotelNearbyLandmarks.postValue(Fail(t))
        }
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