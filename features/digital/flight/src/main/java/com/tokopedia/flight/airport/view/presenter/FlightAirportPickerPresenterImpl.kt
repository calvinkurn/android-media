package com.tokopedia.flight.airport.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPopularCityUseCase
import com.tokopedia.flight.airport.domain.interactor.FlightAirportSuggestionUseCase
import com.tokopedia.flight.common.subscriber.AutoCompleteInputListener
import com.tokopedia.flight.common.subscriber.AutoCompleteKeywordListener
import com.tokopedia.flight.common.subscriber.AutoCompleteKeywordSubscriber
import com.tokopedia.usecase.RequestParams
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 10/24/17.
 */

class FlightAirportPickerPresenterImpl
@Inject constructor(val flightAirportPopularCityUseCase: FlightAirportPopularCityUseCase,
                    val flightAirportSuggestionUseCase: FlightAirportSuggestionUseCase)
    : BaseDaggerPresenter<FlightAirportPickerContract.View>(),
        FlightAirportPickerContract.Presenter,
        AutoCompleteKeywordListener {

    private val compositeSubscription: CompositeSubscription
    private var inputListener: AutoCompleteInputListener? = null

    init {
        this.compositeSubscription = CompositeSubscription()
        Observable.create(
                Observable.OnSubscribe<String> { subscriber ->
                    inputListener = AutoCompleteInputListener { query -> subscriber.onNext(query.toString()) }
                })
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(AutoCompleteKeywordSubscriber(this))
    }

    override fun getPopularCityAirport() {
        view.showGetAirportListLoading()
        if (isViewAttached) {
            view.showLoading()
            compositeSubscription.add(flightAirportPopularCityUseCase.createObservable(RequestParams.EMPTY)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<List<Visitable<*>>>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            if (isViewAttached) {
                                view.hideGetAirportListLoading()
                                view.showGetListError(e)
                            }
                        }

                        override fun onNext(visitables: List<Visitable<*>>) {
                            view.hideGetAirportListLoading()
                            view.renderList(visitables)
                        }
                    })
            )
        }
    }

    override fun getSuggestionAirport(text: String) {
        view.showGetAirportListLoading()
        if (TextUtils.isEmpty(text)) {
            getPopularCityAirport()
        } else {
            if (inputListener != null) {
                inputListener!!.onQuerySubmit(text)
            }
        }
    }

    override fun onTextReceive(keyword: String) {
        if (isViewAttached) {
            view.showLoading()
            compositeSubscription.add(flightAirportSuggestionUseCase.createObservable(
                    flightAirportSuggestionUseCase.createRequestParam(keyword))
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<List<Visitable<*>>>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            if (isViewAttached) {
                                view.hideGetAirportListLoading()
                                view.showGetListError(e)
                            }
                        }

                        override fun onNext(visitables: List<Visitable<*>>) {
                            view.hideGetAirportListLoading()
                            view.renderList(visitables)
                        }
                    })
            )
        }
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun detachView() {
        super.detachView()
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe()
    }
}
