package com.tokopedia.events.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.utils.Utils;
import com.tokopedia.travelcalendar.view.TravelCalendarActivity;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.events.view.contractor.EventFilterContract.EVERYDAY;
import static com.tokopedia.events.view.contractor.EventFilterContract.REQ_OPEN_CALENDAR;
import static com.tokopedia.events.view.contractor.EventFilterContract.TIME_ID;
import static com.tokopedia.travelcalendar.view.TravelCalendarActivity.DATE_SELECTED;
import static okhttp3.internal.Util.UTC;

public class TimeFilterFragment extends Fragment {
    private static final String ARG_TIMERAMGE = "timerange";
    private static final String STARTDATE = "startdate";
    @BindView(R2.id.tv_today)
    TextView tvToday;
    @BindView(R2.id.tv_tomorrow)
    TextView tvTomorrow;
    @BindView(R2.id.tv_next_week)
    TextView tvNextWeek;
    @BindView(R2.id.tv_next_month)
    TextView tvNextMonth;
    @BindView(R2.id.tv_everyday)
    TextView tvEveryday;
    @BindView(R2.id.tv_from_date)
    TextView tvFromDate;
    @BindView(R2.id.tv_simpan)
    TextView tvSimpan;
    Unbinder unbinder;

    @OnClick({R2.id.tv_today,
            R2.id.tv_tomorrow,
            R2.id.tv_next_week,
            R2.id.tv_next_month,
            R2.id.tv_everyday,
            R2.id.tv_from_date
    })
    void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_today) {
            selectButton(tvToday);
            timeRange = TIME_ID[1];
            resetStartDate();
        } else if (id == R.id.tv_tomorrow) {
            selectButton(tvTomorrow);
            timeRange = TIME_ID[0];
            resetStartDate();
        } else if (id == R.id.tv_next_month) {
            selectButton(tvNextMonth);
            timeRange = TIME_ID[2];
            resetStartDate();
        } else if (id == R.id.tv_next_week) {
            selectButton(tvNextWeek);
            timeRange = TIME_ID[3];
            resetStartDate();
        } else if (id == R.id.tv_everyday) {
            selectButton(tvEveryday);
            timeRange = EVERYDAY;
            resetStartDate();
        } else {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DAY_OF_MONTH, 90);
            Intent calendarIntent = TravelCalendarActivity.newInstance(getContext(), new Date(), new Date(), now.getTime(), 0);
            startActivityForResult(calendarIntent, REQ_OPEN_CALENDAR);
        }
    }

    private TextView selectedButton;

    // TODO: Rename and change types of parameters
    private String timeRange;
    private long startDate;

    private OnSelectTimeFilterListener mListener;

    public TimeFilterFragment() {
    }


    public static TimeFilterFragment newInstance(String timerange, long startdate) {
        TimeFilterFragment fragment = new TimeFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TIMERAMGE, timerange);
        args.putLong(STARTDATE, startdate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timeRange = getArguments().getString(ARG_TIMERAMGE);
            startDate = getArguments().getLong(STARTDATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_time_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (timeRange.equals(TIME_ID[0])) {
            selectButton(tvTomorrow);
        } else if (timeRange.equals(TIME_ID[1]))
            selectButton(tvToday);
        else if (timeRange.equals(TIME_ID[2]))
            selectButton(tvNextMonth);
        else if (timeRange.equals(TIME_ID[3]))
            selectButton(tvNextWeek);
        else if (timeRange.equals(EVERYDAY))
            selectButton(tvEveryday);
        else if (timeRange.isEmpty() && startDate > 0) {
            setSelectedDate(startDate);
        }
        return view;
    }


    @OnClick(R2.id.tv_simpan)
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction(timeRange, startDate);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectTimeFilterListener) {
            mListener = (OnSelectTimeFilterListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSelectTimeFilterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void selectButton(TextView v) {
        deselectButton(selectedButton);
        selectedButton = v;
        v.setTextColor(getResources().getColor(R.color.green_nob));
        v.setBackgroundResource(R.drawable.ellipse_white_green_br);
    }

    private void deselectButton(TextView v) {
        if (v != null) {
            v.setTextColor(getResources().getColor(R.color.black_56));
            v.setBackgroundResource(R.drawable.ellipse_white_grey_br);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_OPEN_CALENDAR) {
            if (resultCode == RESULT_OK) {
                Date inDate = (Date) data.getSerializableExtra(DATE_SELECTED);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(UTC);
                calendar.setTime(inDate);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long timeMillis = calendar.getTimeInMillis();
                setSelectedDate(timeMillis);
            }
        }
    }

    private void resetStartDate() {
        startDate = 0;
        tvFromDate.setText(R.string.date_hint);
    }

    private void setSelectedDate(long inTimeMillis) {
        startDate = inTimeMillis;
        timeRange = "";
        deselectButton(selectedButton);
        tvFromDate.setText(Utils.convertLongEpoch(inTimeMillis));
    }

    public interface OnSelectTimeFilterListener {
        void onFragmentInteraction(String timerange, long startdate);
    }
}
