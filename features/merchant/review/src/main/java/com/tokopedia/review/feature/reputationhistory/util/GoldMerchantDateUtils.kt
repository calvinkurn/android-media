package com.tokopedia.review.feature.reputationhistory.util;

import java.util.ArrayList;
import java.util.List;

public class GoldMerchantDateUtils {

    public static String getDateWithYear(String date, String[] monthNames) {
        List<String> dateRaw = getDateRaw(date);
        String year = (String) dateRaw.get(2);
        String month = (String) dateRaw.get(1);
        month = monthNames[Integer.parseInt(month) - 1];
        String day = String.valueOf(Integer.valueOf((String) dateRaw.get(0)));
        return day + " " + month + " " + year;
    }

    public static String getDateRaw(String label, String[] monthNames) {
        String[] split = label.split(" ");
        return split[0] + " " + monthNames[Integer.parseInt(split[1]) - 1];
    }

    private static List<String> getDateRaw(String s) {
        List<String> result = new ArrayList();
        String year = s.substring(0, 4);
        String month = s.substring(4, 6);
        String day = s.substring(6);
        result.add(day);
        result.add(month);
        result.add(year);
        return result;
    }
}

