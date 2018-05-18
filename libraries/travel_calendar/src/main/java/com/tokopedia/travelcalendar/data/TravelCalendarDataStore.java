package com.tokopedia.travelcalendar.data;

import com.tokopedia.travelcalendar.data.entity.HolidayResultEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public interface TravelCalendarDataStore {

    Observable<List<HolidayResultEntity>> getHolidayResults();
}
