package com.tokopedia.travelcalendar.data;

import com.tokopedia.travelcalendar.domain.HolidayMapper;
import com.tokopedia.travelcalendar.domain.ITravelCalendarRepository;
import com.tokopedia.travelcalendar.view.model.HolidayResult;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nabillasabbaha on 14/05/18.
 */
public class TravelCalendarRepository implements ITravelCalendarRepository {

    private TravelCalendarSourceFactory travelCalendarSourceFactory;
    private HolidayMapper holidayMapper;

    @Inject
    public TravelCalendarRepository(TravelCalendarSourceFactory travelCalendarSourceFactory,
                                    HolidayMapper holidayMapper) {
        this.travelCalendarSourceFactory = travelCalendarSourceFactory;
        this.holidayMapper = holidayMapper;
    }

    @Override
    public Observable<List<HolidayResult>> getHolidayResults() {
        return travelCalendarSourceFactory.createTravelCalendarDataStore().getHolidayResults()
                .map(holidayMapper);
    }

    @Override
    public Observable<List<HolidayResult>> getHolidayResultsLocal() {
        return travelCalendarSourceFactory.createLocalTravelCalendarDataStore().getHolidayResults()
                .map(holidayMapper);
    }
}
