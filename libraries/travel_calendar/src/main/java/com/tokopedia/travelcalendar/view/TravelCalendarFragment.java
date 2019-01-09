package com.tokopedia.travelcalendar.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.travelcalendar.R;
import com.tokopedia.travelcalendar.view.model.CellDate;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarFragment extends BaseDaggerFragment {

    private CalendarPickerView calendarPickerView;
    private ActionListener actionListener;
    private RecyclerView recyclerViewHoliday;
    private HolidayAdapter holidayAdapter;
    private Calendar currentCalendar = Calendar.getInstance();
    private int month;
    private int year;

    public static Fragment newInstance(Date selectedDate, int month, int year, Calendar maxDate,
                                       Calendar minDate,
                                       ArrayList<HolidayResult> holidayResultList) {
        TravelCalendarFragment fragment = new TravelCalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TravelCalendarActivity.EXTRA_INITAL_DATE, selectedDate);
        bundle.putParcelableArrayList(TravelCalendarActivity.EXTRA_LIST_HOLIDAY, holidayResultList);
        bundle.putInt(TravelCalendarActivity.EXTRA_MONTH, month);
        bundle.putInt(TravelCalendarActivity.EXTRA_YEAR, year);
        bundle.putSerializable(TravelCalendarActivity.EXTRA_MAX_DATE, maxDate);
        bundle.putSerializable(TravelCalendarActivity.EXTRA_MIN_DATE, minDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_calendar, container, false);
        calendarPickerView = view.findViewById(R.id.calendar_picker_view);
        recyclerViewHoliday = view.findViewById(R.id.recycler_view_holiday);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get current month
        month =  getArguments().getInt(TravelCalendarActivity.EXTRA_MONTH);
        year = getArguments().getInt(TravelCalendarActivity.EXTRA_YEAR);

        List<HolidayResult> holidayResultList = getArguments().getParcelableArrayList(TravelCalendarActivity.EXTRA_LIST_HOLIDAY);
        List<HolidayResult> currentHolidayList = new ArrayList<>();
        for (int i = 0; i < holidayResultList.size(); i++) {
            try {
                Date dateHoliday = new SimpleDateFormat("yyyy-MM-dd").parse(holidayResultList.get(i).getAttributes().getDate());
                Date zeroTimeHolidayDate = DateCalendarUtil.getZeroTimeDate(dateHoliday);
                Calendar calendarHoliday = (Calendar) currentCalendar.clone();
                calendarHoliday.setTime(zeroTimeHolidayDate);
                holidayResultList.get(i).getAttributes().setDateHoliday(zeroTimeHolidayDate);

                if (calendarHoliday.get(Calendar.MONTH) == month && calendarHoliday.get(Calendar.YEAR) == year) {
                    currentHolidayList.add(holidayResultList.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        recyclerViewHoliday.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewHoliday.setLayoutManager(linearLayoutManager);

        holidayAdapter = new HolidayAdapter(new ArrayList<HolidayResult>());
        holidayAdapter.addHoliday(currentHolidayList);
        recyclerViewHoliday.setAdapter(holidayAdapter);

        //date selected from outside
        CellDate cellDate = new CellDate();
        cellDate.setDate((Date) getArguments().getSerializable(TravelCalendarActivity.EXTRA_INITAL_DATE));
        cellDate.setSelected(true);
        calendarPickerView.setDateRange(
                cellDate, getArguments().getInt(TravelCalendarActivity.EXTRA_MONTH),
                getArguments().getInt(TravelCalendarActivity.EXTRA_YEAR),
                (Calendar) getArguments().getSerializable(TravelCalendarActivity.EXTRA_MAX_DATE),
                (Calendar) getArguments().getSerializable(TravelCalendarActivity.EXTRA_MIN_DATE),
                currentHolidayList);
        calendarPickerView.setActionListener(new CalendarPickerView.ActionListener() {
            @Override
            public void onDateClicked(CellDate cellDate) {
                actionListener.onClickDate(cellDate);
            }
        });
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        if (context instanceof ActionListener) {
            actionListener = (ActionListener) context;
        }
    }

    public interface ActionListener {
        void onClickDate(CellDate cellDate);
    }
}
