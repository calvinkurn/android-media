package com.tokopedia.core.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Angga.Prasetiyo on 07/09/2015.
 */
public class DateFormatUtils {
    private static final String TAG = DateFormatUtils.class.getSimpleName();
    public static final String FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String FORMAT_DD_MMMM_YYYY = "dd MMMM yyyy";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    @SuppressLint("SimpleDateFormat")
    public static String getStringDateAfter(int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, count);
        Date newDate = calendar.getTime();
        return new SimpleDateFormat("dd/MM/yyyy").format(newDate);
    }

    public static String formatDate(String currentFormat, String newFormat, String dateString){

        try{
            DateFormat fromFormat = new SimpleDateFormat(currentFormat);
            fromFormat.setLenient(false);
            DateFormat toFormat = new SimpleDateFormat(newFormat);
            toFormat.setLenient(false);
            Date date = fromFormat.parse(dateString);
            return toFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return dateString;
        }

    }
}
