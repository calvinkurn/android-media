package com.tokopedia.travelcalendar.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.view.model.CellDate;

import java.util.Date;

public class TravelCalendarActivity extends BaseSimpleActivity implements TravelCalendarFragment.ActionListener {

    public static final String DATE_SELECTED = "date_selected";
    public static final String EXTRA_INITAL_DATE = "initial_date";
    public static final String EXTRA_MONTH = "month";
    public static final String EXTRA_YEAR = "year";
    public static final String EXTRA_MIN_DATE = "min_date";
    public static final String EXTRA_MAX_DATE = "max_date";
    public static final String EXTRA_SHOW_HOLIDAY = "show_holiday";
    public static final int DEPARTURE_TYPE = 1;
    public static final int RETURN_TYPE = 2;
    public static final int DEFAULT_TYPE = 0;
    public static final String EXTRA_SCHEDULE_TYPE = "schedule_type";

    public static Intent newInstance(Context context, Date initialDate, Date minDate, Date maxDate,
                                     int scheduleType, boolean showHoliday) {
        Intent intent = new Intent(context, TravelCalendarActivity.class);
        intent.putExtra(EXTRA_INITAL_DATE, initialDate);
        intent.putExtra(EXTRA_MIN_DATE, minDate);
        intent.putExtra(EXTRA_MAX_DATE, maxDate);
        intent.putExtra(EXTRA_SCHEDULE_TYPE, scheduleType);
        intent.putExtra(EXTRA_SHOW_HOLIDAY, showHoliday);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitleToolbar();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_travel_calendar;
    }

    private void updateTitleToolbar() {
        int scheduleType = getIntent().getIntExtra(EXTRA_SCHEDULE_TYPE, 0);
        if (scheduleType == DEPARTURE_TYPE) {
            updateTitle(getResources().getString(R.string.travel_calendar_label_choose_departure_trip_date));
        } else if (scheduleType == RETURN_TYPE) {
            updateTitle(getResources().getString(R.string.travel_calendar_label_choose_return_trip_date));
        } else {
            updateTitle(getResources().getString(R.string.travel_calendar_label_choose_date));
        }
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        return TravelCalendarFragment.Companion.newInstance(
                (Date) getIntent().getSerializableExtra(EXTRA_INITAL_DATE),
                (Date) getIntent().getSerializableExtra(EXTRA_MAX_DATE),
                (Date) getIntent().getSerializableExtra(EXTRA_MIN_DATE),
                getIntent().getBooleanExtra(EXTRA_SHOW_HOLIDAY, false));
    }

    @Override
    public void onClickDate(CellDate cellDate) {
        Intent intentDate = new Intent();
        intentDate.putExtra(DATE_SELECTED, cellDate.getDate());
        setResult(RESULT_OK, intentDate);
        finish();
    }
}