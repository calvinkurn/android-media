package com.tokopedia.travelcalendar.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.QuickSingleFilterView;
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterView;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.di.TravelCalendarComponent;
import com.tokopedia.travelcalendar.view.model.CellDate;
import com.tokopedia.travelcalendar.view.model.HolidayResult;
import com.tokopedia.travelcalendar.view.presenter.TravelCalendarPresenter;
import com.tokopedia.travelcalendar.view.widget.CalendarPickerView;
import com.tokopedia.travelcalendar.view.widget.CustomQuickFilterMonthView;
import com.tokopedia.travelcalendar.view.widget.HolidayWidgetView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarFragment extends BaseDaggerFragment implements TravelCalendarContract.View {

    private CalendarPickerView calendarPickerView;
    private ActionListener actionListener;
    private Calendar currentCalendar = Calendar.getInstance();
    private int month;
    private int year;
    private Date selectedDate;
    private Date maxDate;
    private Date minDate;
    private TabLayout tabLayout;

    private List<Integer> yearTabList;
    private CustomViewQuickFilterView monthQuickFilter;
    private List<QuickFilterItem> quickFilterItemList;
    private Map<CustomViewQuickFilterItem, Integer> mapDate;
    private HolidayWidgetView holidayWidgetView;
    private boolean showHoliday;
    private ProgressBar progressBar;
    private RelativeLayout layoutCalendar;

    @Inject
    TravelCalendarPresenter presenter;

    public static Fragment newInstance(Date selectedDate, Date maxDate,
                                       Date minDate, boolean showHoliday) {
        TravelCalendarFragment fragment = new TravelCalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TravelCalendarActivity.EXTRA_INITAL_DATE, selectedDate);
        bundle.putSerializable(TravelCalendarActivity.EXTRA_MAX_DATE, maxDate);
        bundle.putSerializable(TravelCalendarActivity.EXTRA_MIN_DATE, minDate);
        bundle.putSerializable(TravelCalendarActivity.EXTRA_SHOW_HOLIDAY, showHoliday);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {
        GraphqlClient.init(getActivity());
        TravelCalendarComponent travelCalendarComponent = TravelCalendarComponentInstance
                .getComponent(getActivity().getApplication());
        travelCalendarComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_calendar, container, false);
        calendarPickerView = view.findViewById(R.id.calendar_picker_view);
        tabLayout = view.findViewById(R.id.tab_layout);
        monthQuickFilter = view.findViewById(R.id.quick_filter_months);
        holidayWidgetView = view.findViewById(R.id.holiday_widget_view);
        layoutCalendar = view.findViewById(R.id.layout_calendar);
        progressBar = view.findViewById(R.id.loading_progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDataFromBundle();
        presenter.getMonthsCalendarList(minDate, maxDate);
        setActionListener();
    }

    private void getDataFromBundle() {
        quickFilterItemList = new ArrayList<>();
        //get current month
        month = getArguments().getInt(TravelCalendarActivity.EXTRA_MONTH);
        year = getArguments().getInt(TravelCalendarActivity.EXTRA_YEAR);
        selectedDate = (Date) getArguments().getSerializable(TravelCalendarActivity.EXTRA_INITAL_DATE);
        maxDate = (Date) getArguments().getSerializable(TravelCalendarActivity.EXTRA_MAX_DATE);
        minDate = (Date) getArguments().getSerializable(TravelCalendarActivity.EXTRA_MIN_DATE);
        showHoliday = getArguments().getBoolean(TravelCalendarActivity.EXTRA_SHOW_HOLIDAY, false);
    }

    private void setDataTabCalendar(int month, int year, int monthDeviation) {
        yearTabList = new ArrayList<>();
        mapDate = new HashMap<>();
        Calendar loopCalendar = (Calendar) currentCalendar.clone();
        loopCalendar.set(Calendar.DATE, 1);
        yearTabList.add(year);

        for (int i = 0; i < monthDeviation; i++) {
            loopCalendar.set(Calendar.MONTH, month);
            loopCalendar.set(Calendar.YEAR, year);
            String monthName = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(loopCalendar.getTime());
            mapDate.put(convertQuickFilterItems(month, monthName), year);

            if (month > 10) {
                month = 0;
                year++;
                yearTabList.add(year);
            } else {
                month++;
            }
        }
    }

    private void renderInitalSelectedDateCalendar() {
        Calendar calendarNow = (Calendar) currentCalendar.clone();
        calendarNow.setTime(selectedDate);

        boolean selected;
        String yearDateNow = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(calendarNow.getTime());
        String monthDateNow = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(calendarNow.getTime());
        this.year = Integer.valueOf(yearDateNow);
        for (int i = 0; i < yearTabList.size(); i++) {
            selected = yearDateNow.equals(String.valueOf(yearTabList.get(i)));
            tabLayout.addTab(tabLayout.newTab().setText(String.valueOf(yearTabList.get(i))), selected);
        }

        CustomViewQuickFilterItem quickFilterItemInitial = null;
        quickFilterItemList.clear();
        for (Map.Entry map : mapDate.entrySet()) {
            CustomViewQuickFilterItem quickFilterItem = (CustomViewQuickFilterItem) map.getKey();
            CustomQuickFilterMonthView monthView = new CustomQuickFilterMonthView(getActivity());
            monthView.setTextMonth(quickFilterItem.getName());
            quickFilterItem.setDefaultView(monthView);
            quickFilterItem.setSelectedView(monthView);

            if (quickFilterItem.getName().equals(monthDateNow)) {
                quickFilterItem.setSelected(true);
                quickFilterItemInitial = quickFilterItem;
            }
            if (map.getValue().equals(this.year)) {
                quickFilterItemList.add(quickFilterItem);
            }
        }
        monthQuickFilter.renderFilter(quickFilterItemList);
        monthQuickFilter.setDefaultItem(quickFilterItemInitial);
        this.month = Integer.parseInt(quickFilterItemInitial.getType());
        presenter.getDataHolidayCalendar(showHoliday);
    }

    private CustomViewQuickFilterItem convertQuickFilterItems(int month, String monthName) {
        CustomViewQuickFilterItem quickFilterItem = new CustomViewQuickFilterItem();
        quickFilterItem.setName(monthName);
        quickFilterItem.setType(String.valueOf(month));
        quickFilterItem.setSelected(false);
        quickFilterItem.setColorBorder(R.color.tkpd_main_green);
        return quickFilterItem;
    }

    private void setActionListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                year = Integer.parseInt(tab.getText().toString());

                quickFilterItemList.clear();
                for (Map.Entry map : mapDate.entrySet()) {
                    CustomViewQuickFilterItem quickFilterItem = (CustomViewQuickFilterItem) map.getKey();
                    quickFilterItem.setSelected(false);
                    CustomQuickFilterMonthView monthView = new CustomQuickFilterMonthView(getActivity());
                    monthView.setTextMonth(quickFilterItem.getName());
                    quickFilterItem.setDefaultView(monthView);
                    quickFilterItem.setSelectedView(monthView);
                    if (map.getValue().equals(year)) {
                        quickFilterItemList.add(quickFilterItem);
                    }
                }
                monthQuickFilter.renderFilter(quickFilterItemList);
                quickFilterItemList.get(0).setSelected(true);
                monthQuickFilter.setDefaultItem(quickFilterItemList.get(0));
                month = Integer.parseInt(quickFilterItemList.get(0).getType());
                presenter.getDataHolidayCalendar(showHoliday);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        monthQuickFilter.setListener(new QuickSingleFilterView.ActionListener() {
            @Override
            public void selectFilter(String typeFilter) {
                month = Integer.parseInt(typeFilter);
                presenter.getDataHolidayCalendar(showHoliday);
            }
        });

        calendarPickerView.setActionListener(new CalendarPickerView.ActionListener() {
            @Override
            public void onDateClicked(CellDate cellDate) {
                actionListener.onClickDate(cellDate);
            }
        });
    }

    @Override
    public void renderCalendarMonthList(int monthMinDate, int yearMinDate, int monthDeviation) {
        setDataTabCalendar(monthMinDate, yearMinDate, monthDeviation);
        renderInitalSelectedDateCalendar();
    }

    @Override
    public void renderAllHolidayEvent(List<HolidayResult> holidayYearList) {
        //date selected from outside
        CellDate cellDate = new CellDate(selectedDate, true);
        Calendar calendarMaxDate = (Calendar) Calendar.getInstance();
        calendarMaxDate.setTime(maxDate);
        Calendar calendarMinDate = (Calendar) Calendar.getInstance();
        calendarMinDate.setTime(minDate);

        if (holidayYearList.isEmpty()) {
            holidayWidgetView.setVisibility(View.GONE);
        } else {
            holidayWidgetView.setVisibility(View.VISIBLE);
            holidayWidgetView.setHolidayData(holidayYearList, month, year);
        }

        calendarPickerView.setDateRange(cellDate, month, year, calendarMaxDate, calendarMinDate,
                holidayWidgetView.getCurrentHolidayList());
    }

    @Override
    public void renderErrorMessage(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        String errorMessage = ErrorHandler.getErrorMessage(getActivity(), throwable);
        NetworkErrorHelper.createSnackbarWithAction(getActivity(), errorMessage,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        showHoliday = false;
                        renderInitalSelectedDateCalendar();
                    }
                }).showRetrySnackbar();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        layoutCalendar.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        layoutCalendar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onAttachActivity(Context context) {
        if (context instanceof ActionListener) {
            actionListener = (ActionListener) context;
        }
    }

    @Override
    public void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    public interface ActionListener {
        void onClickDate(CellDate cellDate);
    }
}
