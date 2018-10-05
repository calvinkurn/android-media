package com.tokopedia.flight.searchV2.presentation.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

public class FlightSearchReturnActivity extends FlightSearchActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getCallingIntent(FlightSearchActivity flightSearchActivity,
                                          FlightSearchPassDataViewModel passDataViewModel,
                                          String selectedFlightID) {
        return null;
    }
}
