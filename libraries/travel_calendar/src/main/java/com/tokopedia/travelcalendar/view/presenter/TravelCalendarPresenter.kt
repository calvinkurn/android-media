package com.tokopedia.travelcalendar.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.travelcalendar.domain.GetHolidayUseCase
import com.tokopedia.travelcalendar.domain.TravelCalendarProvider
import com.tokopedia.travelcalendar.view.TravelCalendarContract
import com.tokopedia.travelcalendar.view.model.HolidayResult
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.util.*
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 14/05/18.
 */
class TravelCalendarPresenter @Inject constructor(private val getHolidayUseCase: GetHolidayUseCase,
                                                  private val travelCalendarProvider: TravelCalendarProvider)
    : BaseDaggerPresenter<TravelCalendarContract.View>(), TravelCalendarContract.Presenter {

    private val compositeSubscription: CompositeSubscription

    init {
        compositeSubscription = CompositeSubscription()
    }

    override fun getDataHolidayCalendar(showHoliday: Boolean) {
        if (showHoliday) {
            compositeSubscription.add(
                    getHolidayUseCase.createObservable(RequestParams.EMPTY)
                            .subscribeOn(travelCalendarProvider.computation())
                            .unsubscribeOn(travelCalendarProvider.computation())
                            .observeOn(travelCalendarProvider.uiScheduler())
                            .subscribe(object : Subscriber<List<HolidayResult>>() {
                                override fun onCompleted() {

                                }

                                override fun onError(e: Throwable) {
                                    if (isViewAttached) {
                                        view.renderErrorMessage(e)
                                    }
                                }

                                override fun onNext(holidayResults: List<HolidayResult>) {
                                    view.renderAllHolidayEvent(holidayResults)
                                }
                            })
            )
        } else {
            view.renderAllHolidayEvent(ArrayList())
        }
    }

    override fun onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe()
        detachView()
    }
}
