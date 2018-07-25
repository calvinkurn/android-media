package com.tokopedia.product.common.domain;

import com.tokopedia.product.common.domain.model.DatePickerDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/8/17.
 */

public interface DatePickerRepository {

    Observable<Boolean> clearSetting();

    Observable<DatePickerDomainModel> fetchSetting();

    Observable<Boolean> saveSetting(DatePickerDomainModel datePickerDomainModel);
}
