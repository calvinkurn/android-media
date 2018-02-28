package com.tokopedia.gm.statistic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.gm.R;
import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.gm.statistic.constant.GMStatConstant;
import com.tokopedia.gm.statistic.view.activity.GMStatisticDatePickerActivity;
import com.tokopedia.seller.common.williamchart.util.GoldMerchantDateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by nathan on 7/19/17.
 */

public class GMStatisticDateUtils {

    private static final int MAX_DATE_RANGE = 60;
    private static final String MIN_DATE = "25 07 2015";

    public static Intent getDatePickerIntent(Activity activity, DatePickerViewModel datePickerViewModel) {
        return getDatePickerIntent(activity, datePickerViewModel, false);
    }

    public static Intent getDatePickerIntent(Activity activity, DatePickerViewModel datePickerViewModel, boolean compared) {
        Intent intent;
        if (compared) {
            intent = new Intent(activity, GMStatisticDatePickerActivity.class);
        } else {
            intent = new Intent(activity, DatePickerActivity.class);
        }
        Calendar maxCalendar = getMaxDateCalendar();

        DateFormat dateFormat = new SimpleDateFormat(DatePickerConstant.DATE_FORMAT, Locale.ENGLISH);
        Date minDate = new Date();
        try {
            minDate = dateFormat.parse(MIN_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.setTime(minDate);
        minCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minCalendar.set(Calendar.MINUTE, 0);
        minCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.MILLISECOND, 0);

        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, datePickerViewModel.getStartDate());
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, datePickerViewModel.getEndDate());

        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, MAX_DATE_RANGE);

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, getPeriodRangeList(activity));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, datePickerViewModel.getDatePickerSelection());
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, datePickerViewModel.getDatePickerType());

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, activity.getString(R.string.set_date));
        if (compared) {
            intent.putExtra(DatePickerConstant.EXTRA_COMPARE_DATE, datePickerViewModel.isCompareDate());
        }
        return intent;
    }

    private static Calendar getMaxDateCalendar() {
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.DATE, GMStatConstant.MAX_DAY_FROM_CURRENT_DATE);
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);
        return maxCalendar;
    }

    private static ArrayList<PeriodRangeModel> getPeriodRangeList(Context context) {
        ArrayList<PeriodRangeModel> periodRangeList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DATE, -1);
        startCalendar.add(Calendar.DATE, -1);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), startCalendar.getTimeInMillis(), context.getString(R.string.yesterday)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_MONTH);
        periodRangeList.add(new PeriodRangeModel(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis(), context.getString(R.string.thirty_days_ago)));
        return periodRangeList;
    }

    public static DatePickerViewModel getDefaultDatePickerViewModel() {
        DatePickerViewModel datePickerViewModel = new DatePickerViewModel();
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.add(Calendar.DATE, -1);
        datePickerViewModel.setEndDate(currentCalendar.getTimeInMillis());
        currentCalendar.add(Calendar.DATE, -DatePickerConstant.DIFF_ONE_WEEK);
        datePickerViewModel.setStartDate(currentCalendar.getTimeInMillis());
        datePickerViewModel.setDatePickerType(DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
        datePickerViewModel.setDatePickerSelection(1);
        return datePickerViewModel;
    }

    public static PeriodRangeModel getComparedDate(long startdate, long endDate) {
        int diffDate = (int) GoldMerchantDateUtils.generateDateRanges(startdate, endDate).size();
        long comparedStartDate = 0;
        long comparedEndDate = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startdate);
        calendar.add(Calendar.DATE, -1);
        comparedEndDate = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -diffDate + 1);
        comparedStartDate = calendar.getTimeInMillis();
        PeriodRangeModel periodRangeModel = new PeriodRangeModel(comparedStartDate, comparedEndDate);
        return periodRangeModel;
    }
}
