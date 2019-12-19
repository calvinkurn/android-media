package com.tokopedia.datepicker.range.model;

import androidx.annotation.Nullable;

import com.tokopedia.datepicker.range.domain.model.DatePickerDomainModel;

/**
 * Created by nathan on 7/13/17.
 */

public class DatePickerViewModel extends DatePickerDomainModel {

    public DatePickerViewModel() {
        super();
    }

    public DatePickerViewModel(DatePickerDomainModel datePickerDomainModel) {
        super(datePickerDomainModel);
    }

    public boolean equal(@Nullable DatePickerViewModel previousDatePickerViewModel) {
        if (previousDatePickerViewModel == null) {
            return false;
        }
        if (this.getStartDate() != previousDatePickerViewModel.getStartDate()) {
            return false;
        }
        if (this.getEndDate() != previousDatePickerViewModel.getEndDate()) {
            return false;
        }
        if (this.getDatePickerType() != previousDatePickerViewModel.getDatePickerType()) {
            return false;
        }
        if (this.getDatePickerSelection() != previousDatePickerViewModel.getDatePickerSelection()) {
            return false;
        }
        if (this.isCompareDate() != previousDatePickerViewModel.isCompareDate()) {
            return false;
        }
        return true;
    }

}
