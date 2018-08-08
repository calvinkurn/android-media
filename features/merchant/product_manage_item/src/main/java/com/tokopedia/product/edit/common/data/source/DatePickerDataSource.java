package com.tokopedia.product.edit.common.data.source;


import com.tokopedia.product.edit.common.data.mapper.DatePickerMapper;
import com.tokopedia.product.edit.common.data.source.cache.DatePickerCache;
import com.tokopedia.product.edit.common.data.source.cache.model.DatePickerCacheModel;
import com.tokopedia.product.edit.common.domain.model.DatePickerDomainModel;
import com.tokopedia.product.edit.common.data.mapper.DatePickerMapper;
import com.tokopedia.product.edit.common.data.source.cache.DatePickerCache;
import com.tokopedia.product.edit.common.data.source.cache.model.DatePickerCacheModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class DatePickerDataSource {

    private final DatePickerCache datePickerCache;

    @Inject
    public DatePickerDataSource(DatePickerCache datePickerCache) {
        this.datePickerCache = datePickerCache;
    }

    public Observable<DatePickerDomainModel> getData() {
        return datePickerCache.getDatePickerSetting().map(new DatePickerMapper());
    }

    public Observable<Boolean> saveData(DatePickerDomainModel datePickerDomainModel) {
        return datePickerCache.storeDatePickerSetting(new DatePickerCacheModel(datePickerDomainModel));
    }

    public Observable<Boolean> clearData() {
        return datePickerCache.clearCache();
    }
}
