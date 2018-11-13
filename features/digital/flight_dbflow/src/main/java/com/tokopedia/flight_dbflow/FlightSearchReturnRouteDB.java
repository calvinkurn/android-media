package com.tokopedia.flight_dbflow;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by Rizky on 25/10/18.
 */
@Table(database = TkpdFlightDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
public class FlightSearchReturnRouteDB extends FlightSearchSingleRouteDB {

    public FlightSearchReturnRouteDB(String id, String flightType, String term, String aid,
                                     String departureAirport, String departureTime, int departureTimeInt,
                                     String arrivalAirport, String arrivalTime, int arrivalTimeInt,
                                     int totalTransit, int addDayArrival, String duration, int durationMinute,
                                     String total, int totalNumeric, String beforeTotal, String routes,
                                     String adult, int adultNumeric, String child, int childNumeric,
                                     String infant, int infantNumeric, String airline, int isRefundable) {
        super(id, flightType, term, aid, departureAirport, departureTime, departureTimeInt, arrivalAirport,
                arrivalTime, arrivalTimeInt, totalTransit, addDayArrival, duration, durationMinute,
                total, totalNumeric, beforeTotal, routes, adult, adultNumeric, child, childNumeric,
                infant, infantNumeric, airline, isRefundable);
    }

    public FlightSearchReturnRouteDB() {
        super();
    }

}
