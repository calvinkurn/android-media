package com.tokopedia.travelcalendar.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.di.TravelCalendarComponent;
import com.tokopedia.travelcalendar.view.model.CellDate;
import com.tokopedia.travelcalendar.view.model.HolidayResult;
import com.tokopedia.travelcalendar.view.presenter.TravelCalendarPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class TravelCalendarActivity extends BaseSimpleActivity implements TravelCalendarFragment.ActionListener,
        HasComponent<TravelCalendarComponent>, TravelCalendarContract.View {

    public static final String DATE_SELECTED = "date_selected";
    public static final String EXTRA_INITAL_DATE = "initial_date";
    public static final String EXTRA_LIST_HOLIDAY = "list_holiday";
    public static final String EXTRA_MONTH = "month";
    public static final String EXTRA_YEAR = "year";
    public static final String EXTRA_MIN_DATE = "mindate";
    public static final String EXTRA_MAX_DATE = "maxdate";
    public static final int DEPARTURE_TYPE = 1;
    public static final int RETURN_TYPE = 2;
    public static final int DEFAULT_TYPE = 0;
    public static final String EXTRA_SCHEDULE_TYPE = "schedule_type";


    private int month;
    private int year;
    private int monthDeviation;
    private ViewPager viewPager;
    private CalendarPagerAdapter adapter;
    private PagerTabStrip pagerTabStrip;
    private TravelCalendarComponent travelCalendarComponent;
    private Calendar currentCalendar = Calendar.getInstance();
    private ProgressBar progressBar;

    @Inject
    TravelCalendarPresenter presenter;

    public static Intent newInstance(Context context, Date initialDate, Date minDate, Date maxDate,
                                     int scheduleType) {
        Intent intent = new Intent(context, TravelCalendarActivity.class);
        intent.putExtra(EXTRA_INITAL_DATE, initialDate);
        intent.putExtra(EXTRA_MIN_DATE, minDate);
        intent.putExtra(EXTRA_MAX_DATE, maxDate);
        intent.putExtra(EXTRA_SCHEDULE_TYPE, scheduleType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        updateTitleToolbar();
        bindView();
        presenter.getMonthsCalendarList((Date) getIntent().getSerializableExtra(EXTRA_MIN_DATE),
                (Date) getIntent().getSerializableExtra(EXTRA_MAX_DATE));
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

    private void bindView() {
        viewPager = findViewById(R.id.pager);
        pagerTabStrip = findViewById(R.id.tab_strip);
        progressBar = findViewById(R.id.loading_progress_bar);

        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.tkpd_main_green));
        pagerTabStrip.setTextColor(getResources().getColor(R.color.tkpd_main_green));
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setGravity(Gravity.CENTER_VERTICAL);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_travel_activity;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onClickDate(CellDate cellDate) {
        Intent intentDate = new Intent();
        intentDate.putExtra(DATE_SELECTED, cellDate.getDate());
        setResult(RESULT_OK, intentDate);
        finish();
    }

    @Override
    public TravelCalendarComponent getComponent() {
        travelCalendarComponent = TravelCalendarComponentInstance.getComponent(getApplication());
        return travelCalendarComponent;
    }

    private void initInjector() {
        getComponent();
        travelCalendarComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    public void renderCalendarMonthList(int monthMinDate, int yearMinDate, int monthDeviation) {
        this.month = monthMinDate;
        this.year = yearMinDate;
        this.monthDeviation = monthDeviation;
        presenter.getHolidayEvents();
    }

    @Override
    public void renderAllHolidayEvent(List<HolidayResult> holidayResultList) {
        Date dateFromIntent = (Date) getIntent().getSerializableExtra(EXTRA_INITAL_DATE);
        List<Fragment> fragments = new ArrayList<>();
        List<String> titleMonths = new ArrayList<>();
        Calendar calendarDateUser = (Calendar) currentCalendar.clone();

        Calendar calendarMaxDate = (Calendar) Calendar.getInstance();
        calendarMaxDate.setTime((Date) getIntent().getSerializableExtra(EXTRA_MAX_DATE));

        Calendar calendarMinDate = (Calendar) Calendar.getInstance();
        calendarMinDate.setTime((Date) getIntent().getSerializableExtra(EXTRA_MIN_DATE));

        Calendar loopCalendar = (Calendar) currentCalendar.clone();
        loopCalendar.set(Calendar.DATE, 1);
        for (int i = 0; i < monthDeviation; i++) {
            fragments.add(TravelCalendarFragment.newInstance(dateFromIntent, month, year, calendarMaxDate, calendarMinDate,
                    (ArrayList) holidayResultList));

            loopCalendar.set(Calendar.MONTH, month);
            loopCalendar.set(Calendar.YEAR, year);
            String monthName = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(loopCalendar.getTime());
            titleMonths.add(monthName);

            if (month > 10) {
                month = 0;
                year++;
            } else {
                month++;
            }
        }

        adapter = new CalendarPagerAdapter(getSupportFragmentManager(), fragments, titleMonths);
        viewPager.setAdapter(adapter);

        //set pager to specific date user pick
        calendarDateUser.setTime(dateFromIntent);
        String monthName = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(calendarDateUser.getTime());
        for (int i = 0; i < titleMonths.size(); i++) {
            if (monthName.equalsIgnoreCase(titleMonths.get(i))) {
                viewPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void renderErrorMessage(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        String errorMessage = ErrorHandler.getErrorMessage(this, throwable);
        NetworkErrorHelper.createSnackbarWithAction(this, errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        presenter.getHolidayEvents();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }
}
