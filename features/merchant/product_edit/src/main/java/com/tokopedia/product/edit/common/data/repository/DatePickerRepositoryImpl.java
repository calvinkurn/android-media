package com.tokopedia.product.edit.common.data.repository;

import com.tokopedia.product.edit.common.data.source.DatePickerDataSource;
import com.tokopedia.product.edit.common.domain.DatePickerRepository;
import com.tokopedia.product.edit.common.domain.model.DatePickerDomainModel;
import com.tokopedia.product.edit.common.domain.DatePickerRepository;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class DatePickerRepositoryImpl implements DatePickerRepository {

    private final DatePickerDataSource datePickerDataSource;

    public DatePickerRepositoryImpl(DatePickerDataSource datePickerDataSource) {
        this.datePickerDataSource = datePickerDataSource;
    }

    @Override
    public Observable<DatePickerDomainModel> fetchSetting() {
        return datePickerDataSource.getData();
    }

    @Override
    public Observable<Boolean> saveSetting(DatePickerDomainModel datePickerDomainModel) {
        return datePickerDataSource.saveData(datePickerDomainModel);
    }

    @Override
    public Observable<Boolean> clearSetting() {
        return datePickerDataSource.clearData();
    }
}