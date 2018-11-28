package com.tokopedia.topads.dashboard.data.utils;

import android.content.Context;

import com.tokopedia.datepicker.range.view.constant.DatePickerConstant;
import com.tokopedia.datepicker.range.view.model.PeriodRangeModel;
import com.tokopedia.topads.dashboard.R;

import java.util.ArrayList;
import java.util.Calendar;

public class TopAdsDatePeriodUtil {

    public static ArrayList<PeriodRangeModel> getPeriodRangeList(Context context) {
        ArrayList<PeriodRangeModel> periodRangeList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        periodRangeList.add(new PeriodRangeModel(endCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.label_today)));
        startCalendar.add(Calendar.DATE, -1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), startCalendar.getTimeInMillis(), context.getString(R.string.yesterday)));
        startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.thirty_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DATE, 1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.label_this_month)));
        return periodRangeList;
    }
}
