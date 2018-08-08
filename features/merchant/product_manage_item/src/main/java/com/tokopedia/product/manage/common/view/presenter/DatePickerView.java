package com.tokopedia.product.edit.common.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.product.edit.common.model.DatePickerViewModel;

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