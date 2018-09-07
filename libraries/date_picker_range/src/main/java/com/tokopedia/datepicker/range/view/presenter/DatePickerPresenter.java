package com.tokopedia.datepicker.range.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.datepicker.range.model.DatePickerViewModel;

/**
 * @author sebastianuskh on 4/13/17.
 */

public abstract class DatePickerPresenter<T extends DatePickerView> extends BaseDaggerPresenter<T> {

    public abstract void fetchDatePickerSetting();

    public abstract void saveDateSetting(DatePickerViewModel datePickerViewModel);

    public abstract void clearDatePickerSetting();
}
