package com.tokopedia.logisticorder.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * @author anggaprasetiyo on 31/05/18.
 */
public class DateUtil {

    @Inject
    public DateUtil() {
    }

    public String getFormattedDate(String unformattedTime) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,
                new Locale("in", "ID"));

        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,
                new Locale("in", "ID"));

        try {
            Date date = inputFormat.parse(unformattedTime);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return unformattedTime;
        }
    }

    public String getFormattedTime(String unformattedTime) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "HH:mm";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,
                new Locale("in", "ID"));

        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,
                new Locale("in", "ID"));

        try {
            Date date = inputFormat.parse(unformattedTime);
            return outputFormat.format(date) + " WIB";
        } catch (ParseException e) {
            return unformattedTime;
        }
    }

    public String getFormattedDateTime(String unformattedDateTime, String outputPattern) {
        //"2009-11-10T23:00:00Z"
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,
                new Locale("in", "ID"));

        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,
                new Locale("in", "ID"));

        try {
            Date date = inputFormat.parse(unformattedDateTime);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return unformattedDateTime;
        }
    }

    public String getFormattedDateTime(String unformattedDateTime) {
        return getFormattedDateTime(unformattedDateTime, "dd MMMM yyyy");
    }
}
