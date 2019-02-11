package com.tokopedia.flight.searchV3.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.search.domain.usecase.FlightSearchJourneyByIdUseCase
import com.tokopedia.flight.search.presentation.model.FlightFareViewModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchContract
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchReturnContract
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 14/01/19
 */
class FlightSearchReturnPresenter @Inject constructor(private val flightSearchJourneyByIdUseCase: FlightSearchJourneyByIdUseCase,
                                                      private val flightAnalytics: FlightAnalytics) :
        BaseDaggerPresenter<FlightSearchReturnContract.View>(),
        FlightSearchReturnContract.Presenter {

    private val compositeSubscription = CompositeSubscription()

    override fun onFlightSearchSelected(selectedFlightDeparture: String, returnJourneyViewModel: FlightJourneyViewModel, adapterPosition: Int) {
        if (adapterPosition >= 0) {
            flightAnalytics.eventSearchProductClickFromList((view as FlightSearchContract.View)
                    .getSearchPassData(), returnJourneyViewModel, adapterPosition)
        }

        flightSearchJourneyByIdUseCase.execute(flightSearchJourneyByIdUseCase.createRequestParams(selectedFlightDeparture),
                object : Subscriber<FlightJourneyViewModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        if (isViewAttached) {
                            view.showErrorPickJourney()
                        }
                    }

                    override fun onNext(departureJourneyModel: FlightJourneyViewModel?) {
                        if (departureJourneyModel != null &&
                                isValidReturnJourney(departureJourneyModel, returnJourneyViewModel)) {
                            val priceViewModel = view.getFlightPriceViewModel()
                            priceViewModel.returnPrice = buildFare(returnJourneyViewModel.fare, true)
                            priceViewModel.comboKey = returnJourneyViewModel.comboId

                            view.navigateToCart(returnFlightSearchViewModel = returnJourneyViewModel,
                                    flightPriceViewModel = priceViewModel)
                        } else if (departureJourneyModel != null) {
                            view.showReturnTimeShouldGreaterThanArrivalDeparture()
                        }
                    }
                })
    }

    override fun onFlightSearchSelected(selectedFlightDeparture: String, selectedFlightReturn: String) {
        val priceViewModel = view.getFlightPriceViewModel()

        compositeSubscription.add(Observable.zip(
                flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase
                        .createRequestParams(selectedFlightDeparture)),
                flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase
                        .createRequestParams(selectedFlightReturn))
                        .doOnNext {
                            flightAnalytics.eventSearchProductClickFromDetail(
                                    (view as FlightSearchContract.View).getSearchPassData(), it)
                        }
        ) { departureJourney, returnJourney ->
            priceViewModel.returnPrice = buildFare(returnJourney.fare, true)
            priceViewModel.comboKey = returnJourney.comboId

            isValidReturnJourney(departureJourney, returnJourney)
        }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Boolean>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        if (isViewAttached) {
                            view.showErrorPickJourney()
                        }
                    }

                    override fun onNext(t: Boolean?) {
                        if (t != null && t) {
                            view.navigateToCart(selectedFlightReturn = selectedFlightReturn,
                                    flightPriceViewModel = priceViewModel)
                        } else {
                            view.showReturnTimeShouldGreaterThanArrivalDeparture()
                        }
                    }
                })
        )

    }

    override fun onDestroy() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe()
        }

        flightSearchJourneyByIdUseCase.unsubscribe()
    }

    private fun isValidReturnJourney(departureViewModel: FlightJourneyViewModel, returnViewModel: FlightJourneyViewModel): Boolean {
        if (departureViewModel.routeList != null && returnViewModel.routeList != null) {
            if (departureViewModel.routeList.size > 0 && returnViewModel.routeList.size > 0) {
                val lastDepartureRoute = departureViewModel.routeList[departureViewModel.routeList.size - 1]
                val firstReturnRoute = returnViewModel.routeList[0]
                val departureArrivalTime = FlightDateUtil.stringToDate(
                        FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, lastDepartureRoute.arrivalTimestamp)
                val returnDepartureTime = FlightDateUtil.stringToDate(
                        FlightDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, firstReturnRoute.arrivalTimestamp)
                val different = returnDepartureTime.time - departureArrivalTime.time
                return if (different >= 0) {
                    val hours: Long = different / ONE_HOUR
                    CommonUtils.dumper("diff : $hours")
                    hours >= MIN_DIFF_HOURS
                } else {
                    false
                }
            }
        }
        return true
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

    companion object {
        private val ONE_HOUR: Long = TimeUnit.HOURS.toMillis(1)
        private val MIN_DIFF_HOURS = 6
    }
}