package com.tokopedia.travelcalendar.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.travelcalendar.domain.GetHolidayUseCase;
import com.tokopedia.travelcalendar.domain.TravelCalendarProvider;
import com.tokopedia.travelcalendar.view.TravelCalendarContract;
import com.tokopedia.travelcalendar.view.model.HolidayResult;
import com.tokopedia.usecase.RequestParams;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarPresenter extends BaseDaggerPresenter<TravelCalendarContract.View> implements
        TravelCalendarContract.Presenter {

    private GetHolidayUseCase getHolidayUseCase;
    private CompositeSubscription compositeSubscription;
    private TravelCalendarProvider travelCalendarProvider;

    @Inject
    public TravelCalendarPresenter(GetHolidayUseCase getHolidayUseCase, TravelCalendarProvider travelCalendarProvider) {
        this.getHolidayUseCase = getHolidayUseCase;
        compositeSubscription = new CompositeSubscription();
        this.travelCalendarProvider = travelCalendarProvider;
    }

    @Override
    public void getHolidayEvents() {
        compositeSubscription.add(
                getHolidayUseCase.createObservable(RequestParams.EMPTY)
                        .subscribeOn(travelCalendarProvider.computation())
                        .unsubscribeOn(travelCalendarProvider.computation())
                        .observeOn(travelCalendarProvider.uiScheduler())
                        .subscribe(new Subscriber<List<HolidayResult>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().renderErrorMessage(e);
                            }

                            @Override
                            public void onNext(List<HolidayResult> holidayResults) {
                                getView().hideLoading();
                                getView().renderAllHolidayEvent(holidayResults);
                            }
                        })
        );
    }

    @Override
    public void getMonthsCalendarList(Date minDate, Date maxDate) {
        getView().showLoading();
        Calendar initCalendar = Calendar.getInstance();
        Calendar calendarMinDate = (Calendar) initCalendar.clone();
        calendarMinDate.setTime(minDate);
        int monthMinDate = calendarMinDate.get(Calendar.MONTH);
        int yearMinDate = calendarMinDate.get(Calendar.YEAR);

        Calendar calendarMaxDate = (Calendar) initCalendar.clone();
        calendarMaxDate.setTime(maxDate);
        int monthMaxDate = calendarMaxDate.get(Calendar.MONTH);
        int yearMaxDate = calendarMaxDate.get(Calendar.YEAR);

        int yearDeviation = ((yearMaxDate - yearMinDate) * 12) + 1;
        int monthDeviation = (yearDeviation + monthMaxDate) - monthMinDate;

        if (isViewAttached()) {
            getView().renderCalendarMonthList(monthMinDate, yearMinDate, monthDeviation);
        }

    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
        detachView();
    }
}
