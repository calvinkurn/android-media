package com.tokopedia.flight;

import android.content.Context;
import android.content.Intent;

public interface FlightModuleRouter {

    long getLongConfig(String flightAirport);

    Intent getLoginIntent();

    void goToFlightActivity(Context context);
}