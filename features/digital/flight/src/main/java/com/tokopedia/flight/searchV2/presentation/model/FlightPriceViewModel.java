package com.tokopedia.flight.searchV2.presentation.model;

/**
 * @author by furqan on 15/10/18.
 */

public class FlightPriceViewModel {
    private FlightFareViewModel departurePrice;
    private FlightFareViewModel returnPrice;
    private boolean isBestPrice;

    public FlightPriceViewModel() {
    }

    public FlightPriceViewModel(FlightFareViewModel departurePrice, FlightFareViewModel returnPrice, boolean isBestPrice) {
        this.departurePrice = departurePrice;
        this.returnPrice = returnPrice;
        this.isBestPrice = isBestPrice;
    }

    public void setDeparturePrice(FlightFareViewModel departurePrice) {
        this.departurePrice = departurePrice;
    }

    public void setReturnPrice(FlightFareViewModel returnPrice) {
        this.returnPrice = returnPrice;
    }

    public void setBestPrice(boolean bestPrice) {
        isBestPrice = bestPrice;
    }

    public FlightFareViewModel getDeparturePrice() {
        return departurePrice;
    }

    public FlightFareViewModel getReturnPrice() {
        return returnPrice;
    }

    public boolean isBestPrice() {
        return isBestPrice;
    }
}
