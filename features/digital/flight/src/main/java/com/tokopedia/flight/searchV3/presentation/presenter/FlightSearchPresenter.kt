package com.tokopedia.flight.searchV3.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.common.travel.ticker.TravelTickerFlightPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.flight.R
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.data.model.FlightException
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel
import com.tokopedia.flight.search.domain.usecase.*
import com.tokopedia.flight.search.presentation.model.*
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchContract
import com.tokopedia.flight.searchV3.presentation.fragment.FlightSearchFragment
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
                .subscribe {
                    view.hideHorizontalProgress()
                })
    }

    override fun resetCounterCall() {
        callCounter = 0
    }

    override fun isDoneLoadData(): Boolean = callCounter >= maxCall

    override fun onSeeDetailItemClicked(journeyViewModel: FlightJourneyViewModel, adapterPosition: Int) {
        flightAnalytics.eventSearchDetailClick(journeyViewModel, adapterPosition)
        flightAnalytics.eventProductDetailImpression(journeyViewModel, adapterPosition)
    }

    override fun onSearchItemClicked(journeyViewModel: FlightJourneyViewModel?, adapterPosition: Int, selectedId: String) {
        if (selectedId.isEmpty()) {
            if (adapterPosition == -1) {
                flightAnalytics.eventSearchProductClickFromList(view.getSearchPassData(), journeyViewModel)
            } else {
                flightAnalytics.eventSearchProductClickFromList(view.getSearchPassData(), journeyViewModel, adapterPosition)
            }
            deleteFlightReturnSearch(getDeleteFlightReturnSubscriber(journeyViewModel!!))
        } else {
            flightSearchJourneyByIdUseCase.execute(flightSearchJourneyByIdUseCase.createRequestParams(selectedId),
                    object : Subscriber<FlightJourneyViewModel>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable?) {
                            e?.printStackTrace()
                        }

                        override fun onNext(t: FlightJourneyViewModel?) {
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
                object : Subscriber<FlightJourneyViewModel>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onNext(journeyViewModel: FlightJourneyViewModel?) {
                        view.onSuccessGetDetailFlightDeparture(journeyViewModel!!)
                    }
                })
    }

    override fun fetchCombineData(passDataViewModel: FlightSearchPassDataViewModel) {
        val flightPassengerViewModel = passDataViewModel.flightPassengerViewModel

        val departureAirport = if (passDataViewModel.departureAirport.airportCode != null &&
                passDataViewModel.departureAirport.airportCode != "") {
            passDataViewModel.departureAirport.airportCode
        } else {
            passDataViewModel.departureAirport.cityCode
        }

        val arrivalAirport = if (passDataViewModel.arrivalAirport.airportCode != null &&
                passDataViewModel.departureAirport.airportCode != "") {
            passDataViewModel.arrivalAirport.airportCode
        } else {
            passDataViewModel.arrivalAirport.cityCode
        }

        val routes = arrayListOf(
                FlightRouteModel(departureAirport, arrivalAirport, passDataViewModel.getDate(false)),
                FlightRouteModel(arrivalAirport, departureAirport, passDataViewModel.getDate(true))
        )

        val combineRequestModel = FlightSearchCombinedApiRequestModel(routes, flightPassengerViewModel.adult,
                flightPassengerViewModel.children, flightPassengerViewModel.infant, passDataViewModel.flightClass.id)

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

    override fun fetchSearchData(passDataViewModel: FlightSearchPassDataViewModel, airportCombineModelList: FlightAirportCombineModelList) {
        if (isViewAttached) {
            view.removeToolbarElevation()
        }

        maxCall = airportCombineModelList.data.size

        for (item in airportCombineModelList.data) {
            val needLoadFromCloud = !(item.isHasLoad && !item.isNeedRefresh)

            if (needLoadFromCloud) {
                fetchSearchDataCloud(passDataViewModel, item)
            }
        }
    }

    override fun fetchSearchDataCloud(passDataViewModel: FlightSearchPassDataViewModel, airportCombineModel: FlightAirportCombineModel, delayInSecond: Int) {

        // fetch data with delay
        if (delayInSecond != -1) {
            view.removeToolbarElevation()
            val subscription: Subscription = Observable.timer(delayInSecond.toLong(), TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        fetchSearchDataCloud(passDataViewModel, airportCombineModel)
                    }
            addSubscription(subscription)
            return
        }

        // normal fetch
        val date: String = passDataViewModel.getDate(view.isReturning())
        val flightPassengerViewModel: FlightPassengerViewModel = passDataViewModel.flightPassengerViewModel
        val adult = flightPassengerViewModel.adult
        val child = flightPassengerViewModel.children
        val infant = flightPassengerViewModel.infant
        val classID = passDataViewModel.flightClass.id

        val requestModel = FlightSearchApiRequestModel(
                airportCombineModel.depAirport,
                airportCombineModel.arrAirport,
                date, adult, child, infant, classID,
                airportCombineModel.airlines,
                FlightRequestUtil.getLocalIpAddress())

        flightSearchUseCase.execute(flightSearchUseCase.createRequestParams(
                requestModel,
                view.isReturning(),
                !passDataViewModel.isOneWay,
                view.getFilterModel().journeyId),
                object : Subscriber<FlightSearchMetaViewModel>() {
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
                        }
                    }

                    override fun onNext(flightSearchMetaViewModel: FlightSearchMetaViewModel?) {
                        if (flightSearchMetaViewModel != null) {
                            if (!flightSearchMetaViewModel.isNeedRefresh) {
                                callCounter++
                            }

                            view.onGetSearchMeta(flightSearchMetaViewModel)
                        }
                    }
                }
        )
    }

    override fun fetchSortAndFilter(flightSortOption: Int, flightFilterModel: FlightFilterModel, needRefresh: Boolean) {
        flightSortAndFilterUseCase.execute(
                flightSortAndFilterUseCase.createRequestParams(flightSortOption, flightFilterModel),
                object : Subscriber<List<FlightJourneyViewModel>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        if (isViewAttached) {
                            view.stopTrace()
                        }
                    }

                    override fun onNext(flightJourneyViewModelList: List<FlightJourneyViewModel>?) {
                        if (!needRefresh || flightJourneyViewModelList!!.isNotEmpty()) {
                            view.clearAdapterData()
                        }

                        view.renderSearchList(flightJourneyViewModelList!!, needRefresh)

                        if (view.isDoneLoadData()) {
                            view.addBottomPaddingForSortAndFilterActionButton()
                            view.addToolbarElevation()
                            view.hideHorizontalProgress()
                            view.stopTrace()
                        }
                    }
                }
        )
    }

    override fun fetchTickerData() {
        travelTickerUseCase.execute(travelTickerUseCase.createRequestParams(
                TravelTickerInstanceId.FLIGHT, TravelTickerFlightPage.SEARCH),
                object : Subscriber<TravelTickerViewModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(travelTickerViewModel: TravelTickerViewModel) {
                        if (travelTickerViewModel.message.isNotEmpty()) {
                            view.renderTickerView(travelTickerViewModel)
                        }
                    }
                })
    }

    override fun fireAndForgetReturnFlight(passDataViewModel: FlightSearchPassDataViewModel, airportCombineModel: FlightAirportCombineModel) {
        // normal fetch
        val date: String = passDataViewModel.getDate(true)
        val flightPassengerViewModel: FlightPassengerViewModel = passDataViewModel.flightPassengerViewModel
        val adult = flightPassengerViewModel.adult
        val child = flightPassengerViewModel.children
        val infant = flightPassengerViewModel.infant
        val classID = passDataViewModel.flightClass.id

        val requestModel = FlightSearchApiRequestModel(
                airportCombineModel.arrAirport,
                airportCombineModel.depAirport,
                date, adult, child, infant, classID,
                airportCombineModel.airlines,
                FlightRequestUtil.getLocalIpAddress())

        flightSearchUseCase.execute(flightSearchUseCase.createRequestParams(
                requestModel,
                true,
                !passDataViewModel.isOneWay,
                view.getFilterModel().journeyId),
                object : Subscriber<FlightSearchMetaViewModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }

                    override fun onNext(flightSearchMetaViewModel: FlightSearchMetaViewModel?) {
                        // Do Nothing
                    }
                }
        )
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

    private fun getDeleteFlightReturnSubscriber(journeyViewModel: FlightJourneyViewModel): Subscriber<Boolean> =
            object : Subscriber<Boolean>() {
                override fun onCompleted() {

                }

                override fun onError(e: Throwable?) {
                    if (isViewAttached && e != null) {
                        view.onErrorDeleteFlightCache(e)
                    }
                }

                override fun onNext(t: Boolean?) {
                    val priceViewModel = FlightPriceViewModel()
                    priceViewModel.departurePrice = buildFare(journeyViewModel.fare, !view.getSearchPassData().isOneWay)
                    view.navigateToTheNextPage(journeyViewModel.id, priceViewModel, journeyViewModel.isBestPairing)
                }
            }

    private fun buildFare(journeyFare: FlightFareViewModel, isNeedCombo: Boolean): FlightFareViewModel =
            if (isNeedCombo) {
                FlightFareViewModel(
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
                FlightFareViewModel(
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