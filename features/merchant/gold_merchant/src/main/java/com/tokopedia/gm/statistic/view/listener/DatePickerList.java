package com.tokopedia.gm.statistic.view.listener;

import com.tokopedia.datepicker.range.model.DatePickerViewModel;

/**
 * Created by nathan on 7/18/17.
 */

public interface DatePickerList extends BaseDatePicker{

    void loadDataByDateAndPage(DatePickerViewModel datePickerViewModel, int page);
}
