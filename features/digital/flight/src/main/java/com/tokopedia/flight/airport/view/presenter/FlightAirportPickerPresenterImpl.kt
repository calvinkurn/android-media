package com.tokopedia.flight.airport.view.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPopularCityUseCase
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

class FlightAirportPickerPresenterImpl @Inject
constructor(private val flightAirportPopularCityUseCase: FlightAirportPopularCityUseCase)
    : BaseDaggerPresenter<FlightAirportPickerView>(), FlightAirportPickerPresenter, AutoCompleteKeywordListener {

    private val compositeSubscription: CompositeSubscription
    private var inputListener: AutoCompleteInputListener? = null

    init {
        this.compositeSubscription = CompositeSubscription()
        Observable.create(Observable.OnSubscribe<String> { subscriber -> inputListener = AutoCompleteInputListener { query -> subscriber.onNext(query.toString()) } }).debounce(250, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(AutoCompleteKeywordSubscriber(this))
    }

    override fun getAirportList(text: String, isFirstTime: Boolean) {
        view.showGetAirportListLoading()
        if (inputListener != null) {
            inputListener!!.onQuerySubmit(text)
        }
    }

    override fun detachView() {
        super.detachView()
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe()
    }


    override fun onTextReceive(keyword: String) {
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

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }
}
