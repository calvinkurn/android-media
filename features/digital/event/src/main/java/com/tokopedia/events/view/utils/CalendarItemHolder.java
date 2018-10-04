package com.tokopedia.events.view.utils;

import android.widget.TextView;

import com.tokopedia.events.R2;

import butterknife.BindView;

/**
 * Created by pranaymohapatra on 27/12/17.
 */

public class CalendarItemHolder {
    @BindView(R2.id.tv_month)
    TextView tvMonth;
    @BindView(R2.id.tv_date)
    TextView tvDate;
    @BindView(R2.id.tv_day)
    TextView tvDay;

    public void setTvMonth(String month) {
        tvMonth.setText(month);
    }

    public void setTvDate(String date) {
        tvDate.setText(date);
    }

    public void setTvDay(String day) {
        tvDay.setText(day);
    }
}
