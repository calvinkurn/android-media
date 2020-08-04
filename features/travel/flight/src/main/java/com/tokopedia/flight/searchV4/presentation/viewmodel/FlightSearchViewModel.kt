package com.tokopedia.flight.searchV4.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.ticker.TravelTickerFlightPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerCoroutineUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.searchV4.data.FlightSearchThrowable
import com.tokopedia.flight.searchV4.data.cloud.combine.FlightCombineRequestModel
import com.tokopedia.flight.searchV4.data.cloud.combine.FlightCombineRouteRequest
import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchRequestModel
import com.tokopedia.flight.searchV4.domain.*
import com.tokopedia.flight.searchV4.presentation.model.*
import com.tokopedia.flight.searchV4.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV4.presentation.model.statistics.FlightSearchStatisticModel
import com.tokopedia.flight.searchV4.presentation.util.FlightSearchCache
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 09/04/2020
 */
class FlightSearchViewModel @Inject constructor(
        private val flightSearchUseCase: FlightSearchUseCase,
        private val flightSortAndFilterUseCase: FlightSortAndFilterUseCase,
        private val flightSearchDeleteAllDataUseCase: FlightSearchDeleteAllDataUseCase,
        private val flightSearchDeleteReturnDataUseCase: FlightSearchDeleteReturnDataUseCase,
        private val flightSearchJourneyByIdUseCase: FlightSearchJouneyByIdUseCase,
        private val flightSearchCombineUseCase: FlightSearchCombineUseCase,
        private val travelTickerUseCase: TravelTickerCoroutineUseCase,
        private val flightSearchStatisticUseCase: FlightSearchStatisticsUseCase,
        private val flightAnalytics: FlightAnalytics,
        private val flightSearchCache: FlightSearchCache,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    lateinit var flightSearchPassData: FlightSearchPassDataModel
    lateinit var flightAirportCombine: FlightAirportCombineModel
    lateinit var filterModel: FlightFilterModel
    var isCombineDone: Boolean = false
    var selectedSortOption: Int = TravelSortOption.CHEAPEST
    var priceFilterStatistic: Pair<Int, Int> = Pair(0, Int.MAX_VALUE)
    private var searchStatisticModel: FlightSearchStatisticModel? = null
    val isInFilterMode: Boolean
        get() {
            if (::filterModel.isInitialized) {
                return filterModel.isHasFilter
            }
            return false
        }

    private val mutableJourneyList = MutableLiveData<Result<List<FlightJourneyModel>>>()
    val journeyList: LiveData<Result<List<FlightJourneyModel>>>
        get() = mutableJourneyList

    private val mutableSelectedJourney = MutableLiveData<FlightSearchSelectedModel>()
    val selectedJourney: LiveData<FlightSearchSelectedModel>
        get() = mutableSelectedJourney

    private val mutableTickerData = MutableLiveData<Result<TravelTickerModel>>()
    val tickerData: LiveData<Result<TravelTickerModel>>
        get() = mutableTickerData

    val progress = MutableLiveData<Int>()
    private var isSearchViewSent: Boolean = false

    init {
        progress.value = DEFAULT_PROGRESS_VALUE
        isSearchViewSent = false
        fetchTickerData()
    }

    fun initialize(needDeleteData: Boolean, isReturnTrip: Boolean) {
        if (flightSearchPassData.linkUrl.contains(DeeplinkConstant.SCHEME_INTERNAL)) {
            flightSearchPassData.linkUrl.replace(DeeplinkConstant.SCHEME_INTERNAL, DeeplinkConstant.SCHEME_TOKOPEDIA)
        }

        if (needDeleteData) {
            if (isReturnTrip) {
                deleteFlightReturnSearch {}
            } else {
                deleteAllSearchData()
            }
        }

        if (!flightSearchPassData.isOneWay && !isCombineDone) {
            fetchCombineData()
            runFireAndForgetForReturn(isReturnTrip)
        }
    }

    fun generateSearchStatistics() {
        launchCatchError(context = dispatcherProvider.ui(), block = {
            if (::filterModel.isInitialized) {
                searchStatisticModel = flightSearchStatisticUseCase.execute(filterModel)
            }
        }) {
            it.printStackTrace()
        }
    }

    fun changeHasFilterValue() {
        filterModel.setHasFilter(searchStatisticModel)
    }

    fun setProgress(progress: Int) {
        this.progress.postValue(progress)
    }

    fun fetchSearchDataCloud(isReturnTrip: Boolean, delayInSeconds: Long = -1) {
        val date: String = flightSearchPassData.getDate(isReturnTrip)
        val adult = flightSearchPassData.flightPassengerModel.adult
        val child = flightSearchPassData.flightPassengerModel.children
        val infant = flightSearchPassData.flightPassengerModel.infant
        val classId = flightSearchPassData.flightClass.id
        val searchRequestId = flightSearchPassData.searchRequestId

        val requestModel = FlightSearchRequestModel(
                flightAirportCombine.departureAirport,
                flightAirportCombine.arrivalAirport,
                date, adult, child, infant, classId,
                flightAirportCombine.airlines,
                FlightRequestUtil.getLocalIpAddress(),
                searchRequestId)

        launchCatchError(dispatcherProvider.ui(), {
            if (delayInSeconds > -1) {
                delay(TimeUnit.SECONDS.toMillis(delayInSeconds))
            }

            val data = flightSearchUseCase.execute(requestModel,
                    !flightSearchPassData.isOneWay,
                    isReturnTrip,
                    filterModel.journeyId)

            onGetSearchMeta(data, isReturnTrip)
        }) {
            if (it is FlightSearchThrowable) {
                mutableJourneyList.postValue(Fail(it))
            }
            it.printStackTrace()
        }
    }

    private fun fetchTickerData() {
        launch(dispatcherProvider.ui()) {
            val tickerData = travelTickerUseCase.execute(TravelTickerInstanceId.FLIGHT, TravelTickerFlightPage.SEARCH)
            mutableTickerData.postValue(tickerData)
        }
    }

    private fun fetchCombineData() {
        val departureAirport = if (flightSearchPassData.departureAirport.airportCode != null &&
                flightSearchPassData.departureAirport.airportCode.isNotEmpty()) {
            flightSearchPassData.departureAirport.airportCode
        } else {
            flightSearchPassData.departureAirport.cityCode
        }

        val arrivalAirport = if (flightSearchPassData.arrivalAirport.airportCode != null &&
                flightSearchPassData.arrivalAirport.airportCode.isNotEmpty()) {
            flightSearchPassData.arrivalAirport.airportCode
        } else {
            flightSearchPassData.arrivalAirport.cityCode
        }

        val routes = arrayListOf(
                FlightCombineRouteRequest(departureAirport, arrivalAirport, flightSearchPassData.getDate(false)),
                FlightCombineRouteRequest(arrivalAirport, departureAirport, flightSearchPassData.getDate(true)))

        val combineRequestModel = FlightCombineRequestModel(
                routes,
                flightSearchPassData.flightPassengerModel.adult,
                flightSearchPassData.flightPassengerModel.children,
                flightSearchPassData.flightPassengerModel.infant,
                flightSearchPassData.flightClass.id,
                FlightRequestUtil.getLocalIpAddress(),
                flightSearchPassData.searchRequestId)

        launchCatchError(context = dispatcherProvider.ui(), block = {
            isCombineDone = flightSearchCombineUseCase.execute(combineRequestModel)
            fetchSortAndFilter()
        }) {
            it.printStackTrace()
        }
    }

    fun buildAirportCombineModel(departureAirport: FlightAirportModel, arrivalAirport: FlightAirportModel): FlightAirportCombineModel {
        val departureAirportCode = if (departureAirport.airportCode == null || departureAirport.airportCode.isEmpty()) {
            departureAirport.cityCode
        } else {
            departureAirport.airportCode
        }

        val arrivalAirportCode = if (arrivalAirport.airportCode == null || arrivalAirport.airportCode.isEmpty()) {
            arrivalAirport.cityCode
        } else {
            arrivalAirport.airportCode
        }

        return FlightAirportCombineModel(departureAirportCode, arrivalAirportCode)
    }

    fun fetchSortAndFilter() {
        launchCatchError(context = dispatcherProvider.ui(), block = {
            flightSortAndFilterUseCase.execute(selectedSortOption, filterModel).let {
                mutableJourneyList.postValue(Success(it))
            }
        }) {
            if (it is FlightSearchThrowable) {
                mutableJourneyList.postValue(Fail(it))
            }
            it.printStackTrace()
        }
    }

    fun onSearchItemClicked(journeyModel: FlightJourneyModel? = null, adapterPosition: Int = -1, selectedId: String = "") {
        if (selectedId.isEmpty()) {
            if (adapterPosition == -1) {
                flightAnalytics.eventSearchProductClickFromList(flightSearchPassData, journeyModel)
            } else {
                flightAnalytics.eventSearchProductClickFromList(flightSearchPassData, journeyModel, adapterPosition)
            }
            journeyModel?.let {
                deleteFlightReturnSearch { getOnNextDeleteReturnFunction(it) }
            }
        } else {
            launchCatchError(context = dispatcherProvider.ui(), block = {
                flightSearchJourneyByIdUseCase.execute(selectedId).let {
                    flightAnalytics.eventSearchProductClickFromDetail(flightSearchPassData, it)
                    deleteFlightReturnSearch { getOnNextDeleteReturnFunction(it) }
                }
            }) {
                it.printStackTrace()
            }
        }
    }

    fun isDoneLoadData(): Boolean {
        progress.value?.let {
            if (it >= MAX_PROGRESS && !isSearchViewSent) {
                flightAnalytics.eventSearchView(flightSearchPassData, true)
                isSearchViewSent = true
            }
            return it >= MAX_PROGRESS
        }
        return false
    }

    fun isFilterModelInitialized(): Boolean = ::filterModel.isInitialized

    fun isOneWay(): Boolean = flightSearchPassData.isOneWay

    fun recountFilterCounter(): Int {
        var counter = 0

        counter += filterModel.transitTypeList.size
        counter += filterModel.airlineList.size
        counter += filterModel.departureTimeList.size
        counter += filterModel.arrivalTimeList.size
        counter += filterModel.refundableTypeList.size
        counter += filterModel.facilityList.size

        if (filterModel.priceMin > priceFilterStatistic.first ||
                filterModel.priceMax < priceFilterStatistic.second) {
            counter++
        }

        return counter
    }

    fun sendQuickFilterTrack(filterName: String) {
        flightAnalytics.eventQuickFilterClick(filterName)
    }

    fun sendDetailClickTrack(journeyModel: FlightJourneyModel, adapterPosition: Int) {
        flightAnalytics.eventSearchDetailClick(journeyModel, adapterPosition)
        flightAnalytics.eventProductDetailImpression(journeyModel, adapterPosition)
    }

    private fun deleteAllSearchData() {
        launchCatchError(dispatcherProvider.ui(), block = {
            flightSearchDeleteAllDataUseCase.execute()
        }) {
            it.printStackTrace()
        }
    }

    private fun deleteFlightReturnSearch(onNext: () -> Unit) {
        launchCatchError(dispatcherProvider.ui(), block = {
            flightSearchDeleteReturnDataUseCase.execute()
            onNext()
        }) {
            it.printStackTrace()
        }
    }

    private fun getOnNextDeleteReturnFunction(journeyModel: FlightJourneyModel) {
        val priceViewModel = FlightPriceModel()
        priceViewModel.departurePrice = buildFare(journeyModel.fare, !flightSearchPassData.isOneWay)

        mutableSelectedJourney.postValue(FlightSearchSelectedModel(journeyModel, priceViewModel))
    }

    private fun onGetSearchMeta(flightSearchMeta: FlightSearchMetaModel, returnTrip: Boolean) {

        flightSearchCache.setInternationalTransitTag(flightSearchMeta.internationalTag)
        if (flightSearchMeta.backgroundRefreshTime > 0) {
            flightSearchCache.setBackgroundRefreshTime(flightSearchMeta.backgroundRefreshTime)
        }

        if (!returnTrip) {
            flightSearchPassData.searchRequestId = flightSearchMeta.searchRequestId
        }

        for (item in flightSearchMeta.airlines) {
            if (!flightAirportCombine.airlines.contains(item)) {
                flightAirportCombine.airlines.add(item)
            }
        }

        if (flightAirportCombine.isNeedRefresh) {
            if (flightSearchMeta.isNeedRefresh) {
                flightAirportCombine.noOfRetry++

                // already reach max retry limit, end retry
                if (flightAirportCombine.noOfRetry > flightSearchMeta.maxRetry) {
                    flightAirportCombine.isNeedRefresh = false
                } else {
                    // retry
                    fetchSearchDataCloud(returnTrip, flightSearchMeta.refreshTime.toLong())
                }
            } else {
                flightAirportCombine.isNeedRefresh = false
            }
        }

        progress.postValue(
                if (flightAirportCombine.isNeedRefresh) ((flightAirportCombine.noOfRetry.toDouble()) / (flightSearchMeta.maxRetry.toDouble()) * MAX_PROGRESS).toInt()
                else MAX_PROGRESS)

        fetchSortAndFilter()
    }

    private fun runFireAndForgetForReturn(isReturnTrip: Boolean) {
        if (!isReturnTrip) {
            val date: String = flightSearchPassData.getDate(true)
            val adult = flightSearchPassData.flightPassengerModel.adult
            val child = flightSearchPassData.flightPassengerModel.children
            val infant = flightSearchPassData.flightPassengerModel.infant
            val classId = flightSearchPassData.flightClass.id
            val searchRequestId = flightSearchPassData.searchRequestId

            val requestModel = FlightSearchRequestModel(
                    flightAirportCombine.arrivalAirport,
                    flightAirportCombine.departureAirport,
                    date, adult, child, infant, classId,
                    flightAirportCombine.airlines,
                    FlightRequestUtil.getLocalIpAddress(),
                    searchRequestId)

            launchCatchError(dispatcherProvider.ui(), {
                flightSearchUseCase.execute(requestModel,
                        !flightSearchPassData.isOneWay,
                        true,
                        filterModel.journeyId)
            }) {
                it.printStackTrace()
            }
        }
    }

    private fun buildFare(journeyFare: FlightFareModel, isNeedCombo: Boolean): FlightFareModel =
            if (isNeedCombo) {
                FlightFareModel(
                        journeyFare.adult,
                        journeyFare.adultCombo,
                        journeyFare.child,
                        journeyFare.childCombo,
                        journeyFare.infant,
                        journeyFare.infantCombo,
                        journeyFare.adultNumeric,
                        journeyFare.adultNumericCombo,
                        journeyFare.childNumeric,
                        journeyFare.childNumericCombo,
                        journeyFare.infantNumeric,
                        journeyFare.infantNumericCombo
                )
            } else {
                FlightFareModel(
                        journeyFare.adult,
                        "",
                        journeyFare.child,
                        "",
                        journeyFare.infant,
                        "",
                        journeyFare.adultNumeric,
                        0,
                        journeyFare.childNumeric,
                        0,
                        journeyFare.infantNumeric,
                        0
                )
            }

    companion object {
        private const val DEFAULT_PROGRESS_VALUE = 0
        private const val MAX_PROGRESS = 100
    }

}