package com.tokopedia.core.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Angga.Prasetiyo on 07/09/2015.
 */
public class DateFormatUtils {
    private static final String TAG = DateFormatUtils.class.getSimpleName();

    @SuppressLint("SimpleDateFormat")
    public static String getStringDateAfter(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, count);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("dd/MM/yyyy").format(newDate);
    }
}
