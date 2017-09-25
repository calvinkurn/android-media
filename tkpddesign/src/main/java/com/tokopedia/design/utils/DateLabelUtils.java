package com.tokopedia.design.utils;

import android.content.Context;

import com.tokopedia.design.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by nathan on 9/7/17.
 */

public class DateLabelUtils {

    private static final String RANGE_DATE_FORMAT = "dd MMM yyyy";
    private static final String RANGE_DATE_FORMAT_WITHOUT_YEAR = "dd MMM";

    public static String getRangeDateFormatted(Context context, long startDate, long endDate) {
        if (startDate <= 0 || endDate <= 0) {
            return "";
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endDate);
        String startDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(startDate);
        String endDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT, Locale.ENGLISH).format(endDate);
        if (startDateFormatted.equalsIgnoreCase(endDateFormatted)) {
            return endDateFormatted;
        }
        if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
            startDateFormatted = new SimpleDateFormat(RANGE_DATE_FORMAT_WITHOUT_YEAR, Locale.ENGLISH).format(startDate);
        }
        return context.getString(R.string.top_ads_range_date_text, startDateFormatted, endDateFormatted);
    }
}
