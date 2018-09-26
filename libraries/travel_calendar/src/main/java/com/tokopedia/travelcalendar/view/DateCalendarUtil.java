package com.tokopedia.travelcalendar.view;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nabillasabbaha on 15/05/18.
 */
public class DateCalendarUtil {

    public static Date getZeroTimeDate(Date fecha) {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(fecha);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }
}
