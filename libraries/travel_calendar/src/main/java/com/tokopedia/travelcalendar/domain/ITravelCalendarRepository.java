package com.tokopedia.travelcalendar.domain;

import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public interface ITravelCalendarRepository {

    Observable<List<HolidayResult>> getHolidayResults();

    Observable<List<HolidayResult>> getHolidayResultsLocal();
}
