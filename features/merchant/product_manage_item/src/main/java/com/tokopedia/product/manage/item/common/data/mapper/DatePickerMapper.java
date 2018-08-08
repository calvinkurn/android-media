package com.tokopedia.product.edit.common.data.mapper;

import com.tokopedia.product.edit.common.data.source.cache.model.DatePickerCacheModel;
import com.tokopedia.product.edit.common.domain.model.DatePickerDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class DatePickerMapper implements Func1<DatePickerCacheModel, DatePickerDomainModel> {

    @Override
    public DatePickerDomainModel call(DatePickerCacheModel datePickerCacheModel) {
        return mapDomainModels(datePickerCacheModel);
    }

    public static DatePickerDomainModel mapDomainModels(DatePickerCacheModel datePickerCacheModel) {
        return datePickerCacheModel;
    }

}
