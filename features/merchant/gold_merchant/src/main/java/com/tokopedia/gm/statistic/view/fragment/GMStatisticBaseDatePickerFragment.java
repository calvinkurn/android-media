package com.tokopedia.gm.statistic.view.fragment;

import android.content.Intent;
import android.view.View;

import com.tokopedia.seller.base.view.fragment.BaseDatePickerFragment;
import com.tokopedia.seller.common.datepicker.view.constant.DatePickerConstant;
import com.tokopedia.seller.common.datepicker.view.model.DatePickerViewModel;
import com.tokopedia.seller.common.datepicker.view.model.PeriodRangeModel;
import com.tokopedia.gm.statistic.utils.GMStatisticDateUtils;

/**
 * Created by nathan on 7/14/17.
 */

public abstract class GMStatisticBaseDatePickerFragment extends BaseDatePickerFragment {

    private boolean compareDate;

    protected boolean isCompareDate() {
        return compareDate;
    }

    @Override
    public void loadDataByDate(DatePickerViewModel datePickerViewModel) {
        compareDate = datePickerViewModel.isCompareDate();
    }

    @Override
    public Intent getDatePickerIntent(DatePickerViewModel datePickerViewModel) {
        return GMStatisticDateUtils.getDatePickerIntent(getActivity(), datePickerViewModel, isAllowToCompareDate() );
    }

    @Override
    public DatePickerViewModel getDefaultDateViewModel() {
        return GMStatisticDateUtils.getDefaultDatePickerViewModel();
    }

    @Override
    protected void setDateLabelView(DatePickerViewModel datePickerViewModel) {
        super.setDateLabelView(datePickerViewModel);
        if (isAllowToCompareDate() && datePickerViewModel.isCompareDate()) {
            PeriodRangeModel periodRangeModel = GMStatisticDateUtils.getComparedDate(
                    datePickerViewModel.getStartDate(),
                    datePickerViewModel.getEndDate());
            dateLabelView.setComparedDate(periodRangeModel.getStartDate(), periodRangeModel.getEndDate());
            dateLabelView.setComparedDateVisibility(View.VISIBLE);
        } else {
            dateLabelView.setComparedDateVisibility(View.GONE);
        }
    }

    @Override
    public DatePickerViewModel revisitExtraIntent(Intent intent, DatePickerViewModel datePickerViewModel,
                                                  DatePickerViewModel prevDatePickerViewModel) {
        boolean previousCompareDate = prevDatePickerViewModel != null && prevDatePickerViewModel.isCompareDate();
        boolean isComparedDate = intent.getBooleanExtra(DatePickerConstant.EXTRA_COMPARE_DATE, previousCompareDate);
        datePickerViewModel.setCompareDate(isComparedDate);
        return datePickerViewModel;
    }

    public abstract boolean isAllowToCompareDate();
}