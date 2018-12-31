package com.tokopedia.travelcalendar.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.view.DateCalendarUtil;
import com.tokopedia.travelcalendar.view.adapter.HolidayAdapter;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nabillasabbaha on 26/12/18.
 */
public class HolidayWidgetView extends BaseCustomView {

    private RecyclerView recyclerViewHoliday;
    private HolidayAdapter holidayAdapter;
    private List<HolidayResult> currentHolidayList;
    private Calendar currentCalendar = Calendar.getInstance();

    public HolidayWidgetView(@NonNull Context context) {
        super(context);
        init();
    }

    public HolidayWidgetView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HolidayWidgetView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.view_holiday_calendar, this);
        recyclerViewHoliday = view.findViewById(R.id.recycler_view_holiday);

        currentHolidayList = new ArrayList<>();
    }

    public void setHolidayData(List<HolidayResult> holidayYearList, int month, int year) {
        currentHolidayList.clear();
        for (int i = 0; i < holidayYearList.size(); i++) {
            try {
                Date dateHoliday = new SimpleDateFormat("yyyy-MM-dd").parse(holidayYearList.get(i).getAttributes().getDate());
                Date zeroTimeHolidayDate = DateCalendarUtil.getZeroTimeDate(dateHoliday);
                Calendar calendarHoliday = currentCalendar;
                calendarHoliday.setTime(zeroTimeHolidayDate);
                holidayYearList.get(i).getAttributes().setDateHoliday(zeroTimeHolidayDate);

                if (calendarHoliday.get(Calendar.MONTH) == month && calendarHoliday.get(Calendar.YEAR) == year) {
                    currentHolidayList.add(holidayYearList.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        recyclerViewHoliday.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewHoliday.setLayoutManager(linearLayoutManager);

        holidayAdapter = new HolidayAdapter(new ArrayList<HolidayResult>());
        holidayAdapter.addHoliday(currentHolidayList);
        recyclerViewHoliday.setAdapter(holidayAdapter);
    }

    public List<HolidayResult> getCurrentHolidayList() {
        return currentHolidayList;
    }
}
