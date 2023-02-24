package com.tokopedia.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static final String TIME_FORMAT = "ddMMyyHHmm";

    public static String getCurrentTime() {
        Date currentSystemTime = Calendar.getInstance().getTime();
        return new SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(currentSystemTime);
    }
}
