package com.tokopedia.tkpd.datepicker;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.seller.goldmerchant.statistic.utils.GMStatisticDateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by normansyahputa on 8/22/17.
 */

public class DatePickerUtil extends GMStatisticDateUtils {
    public static Intent getDatePickerIntent(Activity activity, DatePickerViewModel datePickerViewModel, List<PeriodRangeModel> periodRangeModels) {
        Intent intent = new Intent(activity, DatePickerActivity.class);
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

        intent.putExtra(DatePickerConstant.EXTRA_DATE_PERIOD_LIST, new ArrayList<>(periodRangeModels));
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, datePickerViewModel.getDatePickerSelection());
        intent.putExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, datePickerViewModel.getDatePickerType());

        intent.putExtra(DatePickerConstant.EXTRA_PAGE_TITLE, activity.getString(com.tokopedia.seller.R.string.set_date));
        return intent;
    }
}
