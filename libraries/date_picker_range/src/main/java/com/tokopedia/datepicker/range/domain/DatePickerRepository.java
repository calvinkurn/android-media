package com.tokopedia.datepicker.range.domain;

import com.tokopedia.datepicker.range.domain.model.DatePickerDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface DatePickerRepository {

    Observable<Boolean> clearSetting();

    Observable<DatePickerDomainModel> fetchSetting();

    Observable<Boolean> saveSetting(DatePickerDomainModel datePickerDomainModel);
}
