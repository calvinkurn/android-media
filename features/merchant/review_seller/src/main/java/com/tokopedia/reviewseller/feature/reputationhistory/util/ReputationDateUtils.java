package com.tokopedia.reviewseller.feature.reputationhistory.util;

import com.db.williamchart.util.GoldMerchantDateUtils;

import java.util.Locale;

/**
 * @author normansyahputa on 3/17/17.
 */

public class ReputationDateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final Locale locale = new Locale("in", "ID");
    private static final String TAG = "ReputationDateUtils";

    public static String getDateFormat(long date, int previousDateCount) {
        return GoldMerchantDateUtils.getPreviousDate(date, previousDateCount, DATE_FORMAT);
    }
}
