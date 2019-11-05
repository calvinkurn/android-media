package com.tokopedia.flight.search.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.view.CommonUtils
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.search.domain.FlightGetComboKeyUseCase
import com.tokopedia.flight.search.domain.FlightSearchJourneyByIdUseCase
import com.tokopedia.flight.search.presentation.contract.FlightSearchContract
import com.tokopedia.flight.search.presentation.contract.FlightSearchReturnContract
import com.tokopedia.flight.search.presentation.model.FlightFareViewModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
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
                                                      private val flightGetComboKeyUseCase: FlightGetComboKeyUseCase,
                                                      private val flightAnalytics: FlightAnalytics) :
        BaseDaggerPresenter<FlightSearchReturnContract.View>(),
        FlightSearchReturnContract.Presenter {

    private val compositeSubscription = CompositeSubscription()

    override fun onFlightSearchSelected(selectedFlightDeparture: String, returnJourneyViewModel: FlightJourneyViewModel, adapterPosition: Int) {
        if (adapterPosition >= 0) {
            flightAnalytics.eventSearchProductClickFromList((view as FlightSearchContract.View)
                    .getSearchPassData(), returnJourneyViewModel, adapterPosition)
        }

        val priceViewModel = view.getFlightPriceViewModel()

        compositeSubscription.add(Observable.zip(
                flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase
                        .createRequestParams(selectedFlightDeparture)),
                flightGetComboKeyUseCase.createObservable(flightGetComboKeyUseCase
                        .createRequestParams(selectedFlightDeparture, returnJourneyViewModel.id))
        ) { departureJourney, comboKey ->
            priceViewModel.comboKey = comboKey
            if (departureJourney != null &&
                    isValidReturnJourney(departureJourney, returnJourneyViewModel)) {
                priceViewModel.returnPrice = buildFare(returnJourneyViewModel.fare, true)
                true
            } else {
                false
            }
        }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        if (t != null && t) {
                            view.navigateToCart(returnFlightSearchViewModel = returnJourneyViewModel,
                                    flightPriceViewModel = priceViewModel)
                        } else {
                            view.showReturnTimeShouldGreaterThanArrivalDeparture()
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                        if (isViewAttached) {
                            view.showErrorPickJourney()
                        }
                    }

                })
        )
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
                        },
                flightGetComboKeyUseCase.createObservable(flightGetComboKeyUseCase
                        .createRequestParams(selectedFlightDeparture, selectedFlightReturn))
        ) { departureJourney, returnJourney, comboKey ->
            priceViewModel.returnPrice = buildFare(returnJourney.fare, true)
            priceViewModel.comboKey = comboKey

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