package com.tokopedia.travelcalendar.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.travelcalendar.domain.GetHolidayUseCase;
import com.tokopedia.travelcalendar.view.TravelCalendarContract;
import com.tokopedia.travelcalendar.view.model.HolidayResult;
import com.tokopedia.usecase.RequestParams;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarPresenter extends BaseDaggerPresenter<TravelCalendarContract.View> implements
        TravelCalendarContract.Presenter {

    private GetHolidayUseCase getHolidayUseCase;

    @Inject
    public TravelCalendarPresenter(GetHolidayUseCase getHolidayUseCase) {
        this.getHolidayUseCase = getHolidayUseCase;
    }

    @Override
    public void getHolidayEvents() {
        getHolidayUseCase.execute(RequestParams.EMPTY, new Subscriber<List<HolidayResult>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().renderErrorMessage(e);
            }

            @Override
            public void onNext(List<HolidayResult> holidayResults) {
                getView().renderAllHolidayEvent(holidayResults);
            }
        });
    }

    @Override
    public void getMonthsCalendarList(Date minDate, Date maxDate) {
        Calendar initCalendar = Calendar.getInstance();
        Calendar calendarMinDate = (Calendar) initCalendar.clone();
        calendarMinDate.setTime(minDate);
        int monthMinDate = calendarMinDate.get(Calendar.MONTH);
        int yearMinDate = calendarMinDate.get(Calendar.YEAR);

        Calendar calendarMaxDate = (Calendar) initCalendar.clone();
        calendarMaxDate.setTime(maxDate);
        int monthMaxDate = calendarMaxDate.get(Calendar.MONTH);
        int yearMaxDate = calendarMaxDate.get(Calendar.YEAR);

        int yearDeviation = (yearMaxDate - yearMinDate) * 12;
        int monthDeviation = (yearDeviation + monthMaxDate) - monthMinDate;

        if (isViewAttached()) {
            getView().renderCalendarMonthList(monthMinDate, yearMinDate, monthDeviation);
        }

    }

    @Override
    public void onDestroyView() {
        if (getHolidayUseCase != null)
            getHolidayUseCase.unsubscribe();
    }
}
