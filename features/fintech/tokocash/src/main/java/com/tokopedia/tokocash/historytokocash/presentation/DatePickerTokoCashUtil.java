package com.tokopedia.tokocash.historytokocash.presentation;

import android.content.Context;

import com.tokopedia.core.router.digitalmodule.sellermodule.PeriodRangeModelCore;
import com.tokopedia.tokocash.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nabillasabbaha on 12/5/17.
 */

public class DatePickerTokoCashUtil {

    public static List<PeriodRangeModelCore> getPeriodRangeModel(Context context) {
        List<PeriodRangeModelCore> periodRangeModelCoreList = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DATE, 0);
        startCalendar.add(Calendar.DATE, 0);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), context.getString(R.string.range_date_today)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -6);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), context.getString(R.string.range_date_seven_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.DATE, -29);
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), context.getString(R.string.range_date_thirty_days_ago)));
        startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), context.getString(R.string.range_date_this_month)));
        startCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(endCalendar.getTimeInMillis());
        startCalendar.add(Calendar.MONTH, -1);
        startCalendar.set(Calendar.DATE, 1);
        endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.MONTH, -1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        periodRangeModelCoreList.add(convert(startCalendar.getTimeInMillis(),
                endCalendar.getTimeInMillis(), context.getString(R.string.range_date_last_month)));
        return periodRangeModelCoreList;
    }

    private static PeriodRangeModelCore convert(long startDate, long endDate, String label) {
        return new PeriodRangeModelCore(startDate, endDate, label);
    }
}
