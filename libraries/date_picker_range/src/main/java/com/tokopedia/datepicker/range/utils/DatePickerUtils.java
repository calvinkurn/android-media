package com.tokopedia.datepicker.range.utils;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.datepicker.range.R;
import com.tokopedia.datepicker.range.model.DatePickerViewModel;
import com.tokopedia.datepicker.range.view.constant.DatePickerConstant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathaniel on 2/3/2017.
 */

public class DatePickerUtils {

    public static String getReadableDate(Context context, long date) {
        String[] monthNamesAbrev = context.getResources().getStringArray(R.array.lib_date_picker_month_entries);
        DateFormat dateFormat = new SimpleDateFormat(DatePickerConstant.DATE_FORMAT, DatePickerConstant.LOCALE);
        String[] split = dateFormat.format(date).split(" ");
        return getDateWithYear(Integer.parseInt(DatePickerUtils.reverseDate(split)), monthNamesAbrev);
    }

    public static String getDateWithYear(int date, String[] monthNames) {
        List<String> dateRaw = getDateRaw(date);
        String year = dateRaw.get(2);
        String month = dateRaw.get(1);
        month = monthNames[Integer.parseInt(month) - 1];

        String day = String.valueOf(Integer.valueOf(dateRaw.get(0)));

        return day + " " + month + " " + year;
    }

    private static List<String> getDateRaw(int date) {
        List<String> result = new ArrayList<>();
        String s = Integer.toString(date);
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        result.add(day);
        result.add(month);
        result.add(year);
        return result;
    }

    public static String reverseDate(String[] split) {
        String reverse = "";
        for (int i = split.length - 1; i >= 0; i--) {
            reverse += split[i];
        }
        return reverse;
    }

    public static DatePickerViewModel convertDatePickerFromIntent(Intent intent) {
        long starDate = intent.getLongExtra(DatePickerConstant.EXTRA_START_DATE, -1);
        long endDate = intent.getLongExtra(DatePickerConstant.EXTRA_END_DATE, -1);
        int lastSelection = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_PERIOD, 1);
        int selectionType = intent.getIntExtra(DatePickerConstant.EXTRA_SELECTION_TYPE, DatePickerConstant.SELECTION_TYPE_PERIOD_DATE);
        DatePickerViewModel datePickerViewModel = new DatePickerViewModel();
        datePickerViewModel.setStartDate(starDate);
        datePickerViewModel.setEndDate(endDate);
        datePickerViewModel.setDatePickerSelection(lastSelection);
        datePickerViewModel.setDatePickerType(selectionType);
        return datePickerViewModel;
    }
}