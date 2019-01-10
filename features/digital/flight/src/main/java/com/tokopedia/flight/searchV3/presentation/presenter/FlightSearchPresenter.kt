package com.tokopedia.flight.searchV3.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.search.domain.usecase.*
import com.tokopedia.flight.search.presentation.model.FlightAirportCombineModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchContract
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
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
                                                private val flightAnalytics: FlightAnalytics) :
        BaseDaggerPresenter<FlightSearchContract.View>(), FlightSearchContract.Presenter {

    private var compositeSubscription = CompositeSubscription()

    private var maxCall: Int = 0
    private var callCounter: Int = 0

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

    override fun onSearchItemClicked(journeyViewModel: FlightJourneyViewModel?, adapterPosition: Int?, selectedId: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccessDateChanged(year: Int, month: Int, dayOfMonth: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDetailDepartureFlight(journeyId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchCombineData(passDataViewModel: FlightSearchPassDataViewModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchSearchData(passDataViewModel: FlightSearchPassDataViewModel, airportCombineModel: FlightAirportCombineModel, delayInSecond: Int?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchSortAndFilter(flightSortOption: Int, flightFilterModel: FlightFilterModel, needRefresh: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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