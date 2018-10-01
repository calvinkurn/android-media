package com.tokopedia.flight.searchV2.presentation.model;

/**
 * Created by Rizky on 26/09/18.
 */
public class FlightSearchMetaViewModel {

    private String departureAirport;
    private String arrivalAirport;
    private String date;
    private boolean needRefresh;
    private int refreshTime;
    private int maxRetry;
    private int retryNo;
    private long last_pulled;

    public FlightSearchMetaViewModel() {
    }

    public FlightSearchMetaViewModel(String departureAirport, String arrivalAirport, String date, boolean needRefresh, int refreshTime, int maxRetry, int retryNo, long last_pulled) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.date = date;
        this.needRefresh = needRefresh;
        this.refreshTime = refreshTime;
        this.maxRetry = maxRetry;
        this.retryNo = retryNo;
        this.last_pulled = last_pulled;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDate() {
        return date;
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public int getRetryNo() {
        return retryNo;
    }

    public long getLast_pulled() {
        return last_pulled;
    }

}
