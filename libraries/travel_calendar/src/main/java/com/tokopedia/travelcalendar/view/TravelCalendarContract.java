package com.tokopedia.travelcalendar.view;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.util.Date;
import java.util.List;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public interface TravelCalendarContract {

    interface View extends CustomerView {

        void renderCalendarMonthList(int monthMinDate, int yearMinDate, int monthDeviation);

        void renderAllHolidayEvent(List<HolidayResult> holidayResultList);

        void renderErrorMessage(String message);

        void showLoading();

        void hideLoading();

        Context getContext();

    }

    interface Presenter extends CustomerPresenter<View> {

        void getHolidayEvents();

        void getMonthsCalendarList(Date minDate, Date maxDate);

        void onDestroyView();
    }
}
