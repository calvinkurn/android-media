package com.tokopedia.travelcalendar.view.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.view.DateCalendarUtil;
import com.tokopedia.travelcalendar.view.adapter.GridCalendarAdapter;
import com.tokopedia.travelcalendar.view.model.CellDate;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nabillasabbaha on 07/05/18.
 */

public class CalendarPickerView extends LinearLayout {

    /**
     * set 1 if sunday is the first day of gridview
     * set 2 if sunday is the last day of gridview
     */
    private static final int FIRST_DAY_INITIAL = 2;

    private static final int DAYS_COUNT = 42;

    private RecyclerView calendarGrid;
    private Calendar currentDate = Calendar.getInstance();
    private Calendar calendarDateUser;
    private ActionListener actionListener;
    private GridCalendarAdapter adapter;
    private CellDate cellDateUser;
    private ArrayList<CellDate> cells;
    private List<HolidayResult> holidayResultList;

    public CalendarPickerView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        init(context);
    }

    public CalendarPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        init(context);
        initViewListener();
    }

    public CalendarPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        init(context);
        initViewListener();
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void initViewListener() {

    }

    private void renderView(int month, int year, Calendar maxDateCal, Calendar minDateCal) {
        calendarDateUser = (Calendar) currentDate.clone();
        calendarDateUser.setTime(cellDateUser.getDate());
        renderCalendar(month, year, maxDateCal, minDateCal);
    }

    private void renderCalendar(int month, int year, Calendar maxDateCal, Calendar minDateCal) {
        cells = new ArrayList<>();

        Calendar mCal = currentDate;
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        mCal.set(Calendar.MONTH, month);
        mCal.set(Calendar.YEAR, year);

        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - FIRST_DAY_INITIAL;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);

        while (cells.size() < DAYS_COUNT) {
            CellDate cellDate = null;
            if (DateCalendarUtil.getZeroTimeDate(calendarDateUser.getTime()).compareTo(
                    DateCalendarUtil.getZeroTimeDate(mCal.getTime())) == 0) {
                cellDate = cellDateUser;
            } else {
                cellDate = new CellDate(mCal.getTime(), false);
            }
            cells.add(cellDate);
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        adapter = new GridCalendarAdapter(cells, mCal, maxDateCal, minDateCal, holidayResultList);
        adapter.setActionListener(new GridCalendarAdapter.ActionListener() {
            @Override
            public void onClickDate(CellDate cellDate) {
                for (int i = 0; i < cells.size(); i++) {
                    if (cellDate.getDate() == cells.get(i).getDate()) {
                        cells.get(i).setSelected(true);
                    } else {
                        cells.get(i).setSelected(false);
                    }
                }
                adapter.notifyDataSetChanged();
                cellDateUser = cellDate;
                actionListener.onDateClicked(cellDate);
            }
        });
        calendarGrid.setLayoutManager(new GridLayoutManager(getContext(), 7));
        calendarGrid.setAdapter(adapter);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_calendar_picker, this, true);
        calendarGrid = view.findViewById(R.id.calendar_grid);
    }

    public void setDateRange(CellDate cellDate, int month, int year, Calendar maxDateCal,
                             Calendar minDateCal, List<HolidayResult> holidayResultList) {
        this.cellDateUser = cellDate;
        this.holidayResultList = holidayResultList;
        renderView(month, year, maxDateCal, minDateCal);
    }

    public interface ActionListener {
        void onDateClicked(CellDate dateRange);
    }
}