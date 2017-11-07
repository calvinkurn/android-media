package com.tokopedia.tkpd.datepicker;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by normansyahputa on 8/22/17.
 */

public class DatePickerUtil {

    public static final int MAX_DAY_FROM_CURRENT_DATE = -1;
    private static final int MAX_DATE_RANGE = 60;
    private static final String MIN_DATE = "25 07 2015";

    public static Intent getDatePickerIntent(Activity activity, long startDate, long endDate,
                                             List<PeriodRangeModel> periodRangeModels,
                                             int datePickerSelection, int datePickerType) {
        Intent intent = new Intent(activity, DatePickerTokoCashActivity.class);
        Calendar maxCalendar = getMaxDateCalendar();

        DateFormat dateFormat = new SimpleDateFormat(DatePickerConstant.DATE_FORMAT, DatePickerConstant.LOCALE);
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

        intent.putExtra(DatePickerConstant.EXTRA_START_DATE, startDate);
        intent.putExtra(DatePickerConstant.EXTRA_END_DATE, endDate);

        intent.putExtra(DatePickerConstant.EXTRA_MIN_START_DATE, minCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_END_DATE, maxCalendar.getTimeInMillis());
        intent.putExtra(DatePickerConstant.EXTRA_MAX_DATE_RANGE, MAX_DATE_RANGE);

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, new ArrayList<>(periodRangeModels));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, datePickerSelection);
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, datePickerType);

        return intent;
    }

    private static Calendar getMaxDateCalendar() {
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.DATE, MAX_DAY_FROM_CURRENT_DATE);
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);
        return maxCalendar;
    }
}