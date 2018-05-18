package com.tokopedia.travelcalendar.data;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.travelcalendar.data.entity.HolidayEntity;
import com.tokopedia.travelcalendar.data.entity.HolidayResultEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarLocalDataStore implements TravelCalendarDataStore {

    public static final String KEY_CALENDAR_HOLIDAY = "calendar_holiday";

    private CacheManager cacheManager;

    public TravelCalendarLocalDataStore(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Observable<List<HolidayResultEntity>> getHolidayResults() {
        return Observable.just(true).map(new Func1<Boolean, HolidayEntity>() {
            @Override
            public HolidayEntity call(Boolean aBoolean) {
                if (getCache() != null) {
                    return (CacheUtil.convertStringToModel(getCache(), new TypeToken<HolidayEntity>() {
                    }.getType()));
                } else
                    throw new RuntimeException("Cache has expired");
            }
        }).map(new Func1<HolidayEntity, List<HolidayResultEntity>>() {
            @Override
            public List<HolidayResultEntity> call(HolidayEntity holidayEntity) {
                return holidayEntity.getHolidayResultEntities();
            }
        });
    }

    private String getCache() {
        return cacheManager.get(KEY_CALENDAR_HOLIDAY);
    }
}
