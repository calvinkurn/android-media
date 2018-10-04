package com.tokopedia.flight.searchV2.data.repository;

import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.searchV2.data.db.FlightJourneyTable;
import com.tokopedia.flight.searchV2.data.db.FlightSearchSingleDataDbSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Rizky on 03/10/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class FlightTest {

    private FlightSearchRepository flightSearchRepository;

    @Mock
    FlightSearchSingleDataDbSource flightSearchSingleDataDbSource;

    @Mock
    FlightSearchDataCloudSource flightSearchDataCloudSource;

    @Before
    public void setup() {
        flightSearchRepository = new FlightSearchRepository(null, flightSearchDataCloudSource, null,
                flightSearchSingleDataDbSource, null, null);
    }

    @Test
    public void testSearchSingle() {
        FlightDataResponse<List<FlightSearchData>> flightDataResponse = new FlightDataResponse<>();
        List<FlightSearchData> flightSearchData = new ArrayList<>();
        flightSearchData.add(new FlightSearchData());
        flightDataResponse.setData(flightSearchData);

        List<FlightJourneyTable> flightJourneyTables = new ArrayList<>();
        flightJourneyTables.add(new FlightJourneyTable());

        when(flightSearchSingleDataDbSource.getSearchSingle())
                .thenReturn(Observable.just(flightJourneyTables));

        when(flightSearchDataCloudSource.getData(Mockito.any()))
                .thenReturn(Observable.just(flightDataResponse));

        flightSearchRepository.getSearchSingle(Mockito.any());
    }

}
