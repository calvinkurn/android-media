package com.tokopedia.datepicker.range.data.repository;

import com.tokopedia.datepicker.range.data.source.DatePickerDataSource;
import com.tokopedia.datepicker.range.domain.DatePickerRepository;
import com.tokopedia.datepicker.range.domain.model.DatePickerDomainModel;
import com.tokopedia.datepicker.range.domain.DatePickerRepository;

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