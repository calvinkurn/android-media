package com.tokopedia.datepicker.range.data.source.cache;

import android.support.annotation.NonNull;

import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.datepicker.range.data.source.cache.model.DatePickerCacheModel;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 2/27/17.
 */
public class DatePickerCache {
    private static final String DATE_PICKER_SETTING = "DATE_PICKER_SETTING";

    @Inject
    public DatePickerCache() {
    }

    public Observable<DatePickerCacheModel> getDatePickerSetting() {
        return getDatePickerCache().map(datePickerCacheModel -> {
            if (datePickerCacheModel == null) {
                throw new RuntimeException("Cache is empty");
            } else {
                return datePickerCacheModel;
            }
        });
    }

    @NonNull
    private Observable<DatePickerCacheModel> getDatePickerCache() {
        return Observable.just(true)
                .map(aBoolean ->
                        PersistentCacheManager.instance.get(DATE_PICKER_SETTING, DatePickerCacheModel.class)
                );
    }

    public Observable<Boolean> storeDatePickerSetting(final DatePickerCacheModel datePickerCacheModel) {
        return Observable.just(datePickerCacheModel).map(new Func1<DatePickerCacheModel, Boolean>() {
            @Override
            public Boolean call(DatePickerCacheModel datePickerCacheModel) {
                PersistentCacheManager.instance.put(DATE_PICKER_SETTING, datePickerCacheModel);
                return true;
            }
        });
    }

    public Observable<Boolean> clearCache() {
        PersistentCacheManager.instance.delete(DATE_PICKER_SETTING);
        return Observable.just(true);
    }
}