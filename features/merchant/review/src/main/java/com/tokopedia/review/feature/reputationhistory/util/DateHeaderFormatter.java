package com.tokopedia.review.feature.reputationhistory.util;

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by normansyahputa on 3/29/17.
 */

public class DateHeaderFormatter {
    public static final String YYYY_M_MDD = "yyyyMMdd";
    public static final String DD_MM = "dd MM";
    private static final Locale locale = new Locale("in", "ID");
    String[] monthNames;
    DateFormat dateFormat = new SimpleDateFormat(YYYY_M_MDD, locale);

    public DateHeaderFormatter(String[] monthNames) {
        this.monthNames = monthNames;
    }

    public String getStartDateFormat(long sDate) {
        validateMonths();

        String dateFormatForInput = DateFormatUtils.getFormattedDate(sDate, DD_MM);
        return GoldMerchantDateUtils.getDateRaw(dateFormatForInput, monthNames);
    }

    protected void validateMonths() {
        if (monthNames == null || monthNames.length == 0)
            throw new IllegalStateException("need to supply valid month name !!");
    }

    public String getEndDateFormat(long eDate) {
        validateMonths();

        return GoldMerchantDateUtils.getDateWithYear(DateFormatUtils.getFormattedDate(eDate, YYYY_M_MDD), monthNames);
    }
}
