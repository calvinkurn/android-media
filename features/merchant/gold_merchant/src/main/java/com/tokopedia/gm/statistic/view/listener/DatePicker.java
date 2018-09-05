package com.tokopedia.gm.statistic.view.listener;

import com.tokopedia.datepicker.range.model.DatePickerViewModel;

/**
 * Created by nathan on 7/18/17.
 */

public interface DatePicker extends BaseDatePicker {

    void loadDataByDate(DatePickerViewModel datePickerViewModel);

}
