package com.tokopedia.gm.statistic.view.activity;

import android.os.Bundle;

import com.tokopedia.seller.common.datepicker.view.activity.DatePickerActivity;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.fragment.DatePickerCustomFragment;
import com.tokopedia.seller.common.datepicker.view.fragment.DatePickerPeriodFragment;
import com.tokopedia.gm.statistic.view.fragment.GMStatisticDatePickerCustomFragment;
import com.tokopedia.gm.statistic.view.fragment.GMStatisticDatePickerPeriodFragment;

/**
 * Created by nathan on 7/11/17.
 */

public class GMStatisticDatePickerActivity extends DatePickerActivity {

    private boolean compareDate;

    protected DatePickerPeriodFragment getDatePickerPeriodFragment() {
        DatePickerPeriodFragment datePickerPeriodFragment = GMStatisticDatePickerPeriodFragment.newInstance(selectionPeriod, periodRangeModelList, compareDate);
        return datePickerPeriodFragment;
    }

    protected DatePickerCustomFragment getDatePickerCustomFragment() {
        DatePickerCustomFragment datePickerCustomFragment = GMStatisticDatePickerCustomFragment.newInstance(startDate, endDate, minStartDate, maxStartDate, maxDateRange, compareDate);
        return datePickerCustomFragment;
    }

    @Override
    protected void fetchIntent(Bundle extras) {
        super.fetchIntent(extras);
        if (extras != null) {
            compareDate = extras.getBoolean(DatePickerConstant.EXTRA_COMPARE_DATE);
        }
    }
}
