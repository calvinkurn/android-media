package com.tokopedia.datepicker.range.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.datepicker.range.model.DatePickerViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public interface DatePickerView extends CustomerView {

    void onSuccessLoadDatePicker(DatePickerViewModel datePickerViewModel);

    void onErrorLoadDatePicker(Throwable throwable);

    void onSuccessSaveDatePicker();

    void onErrorSaveDatePicker(Throwable throwable);

    void onSuccessClearDatePicker();

    void onErrorClearDatePicker(Throwable throwable);
}