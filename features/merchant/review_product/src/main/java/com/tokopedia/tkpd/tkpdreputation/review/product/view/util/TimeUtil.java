package com.tokopedia.tkpd.tkpdreputation.review.product.view.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static String generateTimeYearly(String postTime) {
        Locale localeID = new Locale("en", "ID");
        try {
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd MMM", localeID);
            SimpleDateFormat sdfYear = new SimpleDateFormat("dd MMM yyyy", localeID);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm", localeID);
            Date postDate = sdf.parse(postTime);
            Date currentTime = new Date();

            Calendar calPostDate = Calendar.getInstance();
            calPostDate.setTime(postDate);

            Calendar calCurrentTime = Calendar.getInstance();
            calCurrentTime.setTime(currentTime);

            if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR))
                return sdfDay.format(postDate);
            else {
                return sdfYear.format(postDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return postTime;
        }
    }
}
