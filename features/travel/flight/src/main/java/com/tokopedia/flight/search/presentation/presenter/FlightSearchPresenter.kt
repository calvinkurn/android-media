package com.tokopedia.flight.search.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.travel.ticker.TravelTickerFlightPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.flight.R
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.data.model.FlightException
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel
import com.tokopedia.flight.search.domain.*
import com.tokopedia.flight.search.presentation.contract.FlightSearchContract
import com.tokopedia.flight.search.presentation.fragment.FlightSearchFragment
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 07/01/19
 */
class FlightSearchPresenter @Inject constructor(private val flightSearchUseCase: FlightSearchV2UseCase,
                                                private val flightSortAndFilterUseCase: FlightSortAndFilterUseCase,
                                                private val flightSearchCombinedUseCase: FlightSearchCombinedUseCase,
                                                private val flightDeleteAllFlightSearchDataUseCase: FlightDeleteAllFlightSearchDataUseCase,
                                                private val flightDeleteFlightSearchReturnDataUseCase: FlightDeleteFlightSearchReturnDataUseCase,
                                                private val flightSearchJourneyByIdUseCase: FlightSearchJourneyByIdUseCase,
                                                private val flightAnalytics: FlightAnalytics,
                                                private val travelTickerUseCase: TravelTickerUseCase) :
        BaseDaggerPresenter<FlightSearchContract.View>(), FlightSearchContract.Presenter {

    private var compositeSubscription = CompositeSubscription()

    private var maxCall: Int = 0
    private var callCounter: Int = 0

    private var lastPosition = 0

    override fun initialize(needDeleteData: Boolean) {

        if (needDeleteData) {
            if (view.isReturning()) {
                deleteFlightReturnSearch(getNoActionSubscriber())
            } else {
                deleteAllSearchData(getNoActionSubscriber())
            }
        }

        if (!view.getSearchPassData().isOneWay &&
                !view.isStatusCombineDone()) {
            fetchCombineData(view.getSearchPassData())

            runFireAndForgetForReturn()
        }
    }

    override fun setDelayHorizontalProgress() {
        addSubscription(Observable.timer(DELAY_HORIZONTAL_PROGRESS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Long>() {
                    override fun onNext(t: Long?) {
                        view.hideHorizontalProgress()
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {}
                }))
    }

    override fun resetCounterCall() {
        callCounter = 0
    }

    override fun isDoneLoadData(): Boolean = callCounter >= maxCall

    override fun onSeeDetailItemClicked(journeyModel: FlightJourneyModel, adapterPosition: Int) {
        flightAnalytics.eventSearchDetailClick(journeyModel, adapterPosition)
        flightAnalytics.eventProductDetailImpression(journeyModel, adapterPosition)
    }

    override fun onSearchItemClicked(journeyModel: FlightJourneyModel?, adapterPosition: Int, selectedId: String) {
        if (selectedId.isEmpty()) {
            if (adapterPosition == -1) {
                flightAnalytics.eventSearchProductClickFromList(view.getSearchPassData(), journeyModel)
            } else {
                flightAnalytics.eventSearchProductClickFromList(view.getSearchPassData(), journeyModel, adapterPosition)
            }
            deleteFlightReturnSearch(getDeleteFlightReturnSubscriber(journeyModel!!))
        } else {
            flightSearchJourneyByIdUseCase.execute(flightSearchJourneyByIdUseCase.createRequestParams(selectedId),
                    object : Subscriber<FlightJourneyModel>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable?) {
                            e?.printStackTrace()
                        }

                        override fun onNext(t: FlightJourneyModel?) {
                            flightAnalytics.eventSearchProductClickFromDetail(view.getSearchPassData(), t)
                            deleteFlightReturnSearch(getDeleteFlightReturnSubscriber(t!!))
                        }

                    })
        }
    }

    override fun onSuccessDateChanged(year: Int, month: Int, dayOfMonth: Int) {
        val flightSearchPassDataViewModel = view.getSearchPassData()
        val calendar: Calendar = FlightDateUtil.getCurrentCalendar()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DATE, dayOfMonth)
        val dateToSet: Date = calendar.time
        val twoYears: Date = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2)

        if (dateToSet.after(twoYears)) {
            view.showDepartureDateMaxTwoYears(R.string.flight_dashboard_departure_max_one_years_from_today_error)
        } else if (!view.isReturning() && dateToSet.before(FlightDateUtil.getCurrentDate())) {
            view.showDepartureDateShouldAtLeastToday(R.string.flight_dashboard_departure_should_atleast_today_error)
        } else if (view.isReturning() && dateToSet.before(FlightDateUtil.stringToDate(flightSearchPassDataViewModel.departureDate))) {
            view.showReturnDateShouldGreatedOrEqual(R.string.flight_dashboard_return_should_greater_equal_error)
        } else {
            val dateString = FlightDateUtil.dateToString(dateToSet, FlightDateUtil.DEFAULT_FORMAT)

            if (view.isReturning()) {
                flightSearchPassDataViewModel.returnDate = dateString
                deleteFlightReturnSearch(getDeleteFlightAfterChangeDateSubscriber())
            } else {
                flightSearchPassDataViewModel.departureDate = dateString
                deleteAllSearchData(getDeleteFlightAfterChangeDateSubscriber())
            }

            view.setSearchPassData(flightSearchPassDataViewModel)
        }
    }

    override fun getDetailDepartureFlight(journeyId: String) {
        flightSearchJourneyByIdUseCase.execute(flightSearchJourneyByIdUseCase.createRequestParams(journeyId),
                object : Subscriber<FlightJourneyModel>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onNext(journeyModel: FlightJourneyModel?) {
                        view.onSuccessGetDetailFlightDeparture(journeyModel!!)
                    }
                })
    }

    override fun fetchCombineData(passDataModel: FlightSearchPassDataModel) {
        val flightPassengerViewModel = passDataModel.flightPassengerViewModel


        val departureAirport = if (passDataModel.departureAirport.airportCode != null &&
                passDataModel.departureAirport.airportCode.isNotEmpty()) {
            passDataModel.departureAirport.airportCode
        } else {
            passDataModel.departureAirport.cityCode
        }

        val arrivalAirport = if (passDataModel.arrivalAirport.airportCode != null &&
                passDataModel.arrivalAirport.airportCode.isNotEmpty()) {
            passDataModel.arrivalAirport.airportCode
        } else {
            passDataModel.arrivalAirport.cityCode
        }

        val routes = arrayListOf(
                FlightRouteModel(departureAirport, arrivalAirport, passDataModel.getDate(false)),
                FlightRouteModel(arrivalAirport, departureAirport, passDataModel.getDate(true))
        )

        val combineRequestModel = FlightSearchCombinedApiRequestModel(routes, flightPassengerViewModel.adult,
                flightPassengerViewModel.children, flightPassengerViewModel.infant, passDataModel.flightClass.id)

        flightSearchCombinedUseCase.execute(flightSearchCombinedUseCase.createRequestParam(combineRequestModel),
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onNext(t: Boolean?) {
                        if (t != null) {
                            view.setCombineStatus(t)
                            view.fetchSortAndFilterData()
                        }
                    }
                })
    }

    override fun fetchSearchData(passDataModel: FlightSearchPassDataModel, airportCombineModelList: FlightAirportCombineModelList) {
        if (isViewAttached) {
            view.removeToolbarElevation()
        }

        maxCall = airportCombineModelList.data.size

        for (item in airportCombineModelList.data) {
            val needLoadFromCloud = !(item.isHasLoad && !item.isNeedRefresh)

            if (needLoadFromCloud) {
                fetchSearchDataCloud(passDataModel, item)
            }
        }
    }

    override fun fetchSearchDataCloud(passDataModel: FlightSearchPassDataModel, airportCombineModel: FlightAirportCombineModel, delayInSecond: Int) {

        // fetch data with delay
        if (delayInSecond != -1) {
            view.removeToolbarElevation()
            val subscription: Subscription = Observable.timer(delayInSecond.toLong(), TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<Long>() {
                        override fun onNext(t: Long?) {
                            fetchSearchDataCloud(passDataModel, airportCombineModel)
                        }

                        override fun onCompleted() {}

                        override fun onError(e: Throwable?) {}

                    })
            addSubscription(subscription)
            return
        }

        // normal fetch
        val date: String = passDataModel.getDate(view.isReturning())
        val flightPassengerModel: FlightPassengerModel = passDataModel.flightPassengerViewModel
        val adult = flightPassengerModel.adult
        val child = flightPassengerModel.children
        val infant = flightPassengerModel.infant
        val classID = passDataModel.flightClass.id
        val searchRequestId = passDataModel.searchRequestId

        val requestModel = FlightSearchApiRequestModel(
                airportCombineModel.depAirport,
                airportCombineModel.arrAirport,
                date, adult, child, infant, classID,
                airportCombineModel.airlines,
                FlightRequestUtil.getLocalIpAddress(),
                searchRequestId)

        flightSearchUseCase.execute(flightSearchUseCase.createRequestParams(
                requestModel,
                view.isReturning(),
                !passDataModel.isOneWay,
                view.getFilterModel().journeyId),
                object : Subscriber<FlightSearchMetaModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e!!.printStackTrace()
                        callCounter++
                        view.addProgress(countProgress())
                        if (isViewAttached && isDoneLoadData()) {
                            if (e is FlightException) {
                                val errors = e.errorList
                                if (errors.contains(FlightError(FlightErrorConstant.FLIGHT_ROUTE_NOT_FOUND))) {
                                    view.showNoRouteFlightEmptyState(e.message!!)
                                    return
                                }
                            }
                            view.showGetSearchListError(e)
                            flightAnalytics.eventSearchView(passDataModel, false)
                        }
                    }

                    override fun onNext(flightSearchMetaModel: FlightSearchMetaModel?) {
                        if (flightSearchMetaModel != null) {
                            if (!flightSearchMetaModel.isNeedRefresh || (airportCombineModel.noOfRetry + 1) > flightSearchMetaModel.maxRetry) {
                                callCounter++
                            }

                            view.onGetSearchMeta(flightSearchMetaModel)
                        }
                    }
                }
        )
    }

    override fun fetchSortAndFilter(flightSortOption: Int, flightFilterModel: FlightFilterModel, needRefresh: Boolean, fromCombo: Boolean) {
        flightSortAndFilterUseCase.execute(
                flightSortAndFilterUseCase.createRequestParams(flightSortOption, flightFilterModel),
                object : Subscriber<List<FlightJourneyModel>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        if (isViewAttached) {
                            view.stopTrace()
                        }
                    }

                    override fun onNext(flightJourneyModelList: List<FlightJourneyModel>) {
                        flightAnalytics.eventSearchView(view.getSearchPassData(), true)
                        if (!needRefresh || flightJourneyModelList.isNotEmpty()) {
                            view.clearAdapterData()
                        }

                        view.renderSearchList(flightJourneyModelList, needRefresh)
                        view.stopTrace()

                        if (view.isDoneLoadData()) {
                            view.addBottomPaddingForSortAndFilterActionButton()
                            view.addToolbarElevation()
                            view.hideHorizontalProgress()
                        }

                        if (lastPosition != flightJourneyModelList.size || fromCombo) onProductViewImpression(flightJourneyModelList)
                        else if (lastPosition == 0 && !needRefresh) onProductViewImpression(flightJourneyModelList)

                        lastPosition = flightJourneyModelList.size
                    }
                }
        )
    }

    override fun fetchTickerData() {
        travelTickerUseCase.execute(travelTickerUseCase.createRequestParams(
                TravelTickerInstanceId.FLIGHT, TravelTickerFlightPage.SEARCH),
                object : Subscriber<TravelTickerModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(travelTickerModel: TravelTickerModel) {
                        if (travelTickerModel.message.isNotEmpty()) {
                            view.renderTickerView(travelTickerModel)
                        }
                    }
                })
    }

    override fun fireAndForgetReturnFlight(passDataModel: FlightSearchPassDataModel, airportCombineModel: FlightAirportCombineModel) {
        // normal fetch
        val date: String = passDataModel.getDate(true)
        val flightPassengerModel: FlightPassengerModel = passDataModel.flightPassengerViewModel
        val adult = flightPassengerModel.adult
        val child = flightPassengerModel.children
        val infant = flightPassengerModel.infant
        val classID = passDataModel.flightClass.id
        val searchRequestId = passDataModel.searchRequestId

        val requestModel = FlightSearchApiRequestModel(
                airportCombineModel.arrAirport,
                airportCombineModel.depAirport,
                date, adult, child, infant, classID,
                airportCombineModel.airlines,
                FlightRequestUtil.getLocalIpAddress(),
                searchRequestId)

        flightSearchUseCase.execute(flightSearchUseCase.createRequestParams(
                requestModel,
                true,
                !passDataModel.isOneWay,
                view.getFilterModel().journeyId),
                object : Subscriber<FlightSearchMetaModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onNext(flightSearchMetaModel: FlightSearchMetaModel?) {
                        // Do Nothing
                    }
                }
        )
    }

    private fun onProductViewImpression(listJourneyModel: List<FlightJourneyModel>) {
        if (listJourneyModel.isEmpty()) flightAnalytics.eventProductViewNotFound(view.getSearchPassData())
        else flightAnalytics.eventProductViewEnchanceEcommerceOld(view.getSearchPassData(), listJourneyModel)
    }

    override fun unsubscribeAll() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe()
        }

        flightSearchJourneyByIdUseCase.unsubscribe()
        flightDeleteFlightSearchReturnDataUseCase.unsubscribe()
        flightSearchCombinedUseCase.unsubscribe()
        flightSortAndFilterUseCase.unsubscribe()
        flightSearchUseCase.unsubscribe()
    }

    override fun recountFilterCounter(): Int {
        var counter = 0

        counter += if (view.getFilterModel().transitTypeList != null) view.getFilterModel().transitTypeList.size else 0
        counter += if (view.getFilterModel().airlineList != null) view.getFilterModel().airlineList.size else 0
        counter += if (view.getFilterModel().departureTimeList != null) view.getFilterModel().departureTimeList.size else 0
        counter += if (view.getFilterModel().arrivalTimeList != null) view.getFilterModel().arrivalTimeList.size else 0
        counter += if (view.getFilterModel().refundableTypeList != null) view.getFilterModel().refundableTypeList.size else 0
        counter += if (view.getFilterModel().facilityList != null) view.getFilterModel().facilityList.size else 0

        if (view.getFilterModel().priceMin > view.getPriceStatisticPair().first ||
                view.getFilterModel().priceMax < view.getPriceStatisticPair().second) {
            counter++
        }

        return counter
    }

    override fun sendQuickFilterTrack(filterName: String) {
        flightAnalytics.eventQuickFilterClick(filterName)
    }

    private fun countProgress(): Int = FlightSearchFragment.MAX_PROGRESS / maxCall

    private fun deleteAllSearchData(subscriber: Subscriber<Boolean>) {
        flightDeleteAllFlightSearchDataUseCase.execute(subscriber)
    }

    private fun deleteFlightReturnSearch(subscriber: Subscriber<Boolean>) {
        flightDeleteFlightSearchReturnDataUseCase.execute(subscriber)
    }

    private fun getDeleteFlightAfterChangeDateSubscriber(): Subscriber<Boolean> =
            object : Subscriber<Boolean>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    if (isViewAttached && e != null) {
                        view.onErrorDeleteFlightCache(e)
                    }
                }

                override fun onNext(t: Boolean?) {
                    view.onSuccessDeleteFlightCache()
                }
            }

    private fun getNoActionSubscriber(): Subscriber<Boolean> =
            object : Subscriber<Boolean>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    e?.printStackTrace()
                }

                override fun onNext(t: Boolean?) {
                    // No Action
                }
            }

    private fun getDeleteFlightReturnSubscriber(journeyModel: FlightJourneyModel): Subscriber<Boolean> =
            object : Subscriber<Boolean>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    if (isViewAttached && e != null) {
                        view.onErrorDeleteFlightCache(e)
                    }
                }

                override fun onNext(t: Boolean?) {
                    val priceViewModel = FlightPriceModel()
                    priceViewModel.departurePrice = buildFare(journeyModel.fare, !view.getSearchPassData().isOneWay)
                    view.navigateToTheNextPage(journeyModel.id, journeyModel.term, priceViewModel, journeyModel.isBestPairing)
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

    private fun runFireAndForgetForReturn() {
        if (!view.isReturning()) {
            for (item in view.getAirportCombineModelList().data) {
                fireAndForgetReturnFlight(view.getSearchPassData(), item)
            }
        }
    }

    private fun addSubscription(subscription: Subscription) {
        if (compositeSubscription.isUnsubscribed) {
            compositeSubscription = CompositeSubscription()
        }
        compositeSubscription.add(subscription)
    }

    companion object {
        val DELAY_HORIZONTAL_PROGRESS: Long = 500
    }
}
