package com.tokopedia.flight.dashboard.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightDatePickerFragment extends BaseDaggerFragment {
    public static final String EXTRA_DATE = "EXTRA_DATE";

    public static FlightDatePickerFragment newInstance(String date) {
        FlightDatePickerFragment fragment = new FlightDatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATE, date);
        fragment.setArguments(bundle);
        return fragment;
    }


    public FlightDatePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_date_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date date = FlightDateUtil.stringToDate(getArguments().getString(EXTRA_DATE));
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.cv_flight_date);
        calendarView.setDate(date.getTime());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
