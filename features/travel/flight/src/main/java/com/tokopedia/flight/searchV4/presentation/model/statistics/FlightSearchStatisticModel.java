package com.tokopedia.flight.searchV4.presentation.model.statistics;

import com.tokopedia.flight.searchV4.presentation.model.FlightAirlineModel;

import java.util.List;

/**
 * Created by User on 10/30/2017.
 */

public class FlightSearchStatisticModel {
    private int minPrice;
    private int maxPrice;
    private int minDuration;
    private int maxDuration;
    private List<TransitStat> transitTypeStatList;
    private List<AirlineStat> airlineStatList;
    private List<DepartureStat> departureTimeStatList;
    private List<DepartureStat> arrivalTimeStatList;
    private List<RefundableStat> refundableTypeStatList;
    private boolean isHaveSpecialPrice;
    private boolean isHaveBaggage;
    private boolean isHaveInFlightMeal;
    private boolean isHasFreeRapidTest;
    private boolean isSeatDistancing;

    public FlightSearchStatisticModel(int minPrice, int maxPrice, int minDuration, int maxDuration,
                                      List<TransitStat> transitTypeStatList, List<AirlineStat> airlineStatList,
                                      List<DepartureStat> departureTimeStatList, List<DepartureStat> arrivalTimeStatList,
                                      List<RefundableStat> refundableTypeStatList, boolean isHaveSpecialPrice,
                                      boolean isHaveBaggage, boolean isHaveInFlightMeal, boolean isHasFreeRapidTest,
                                      boolean isSeatDistancing) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.transitTypeStatList = transitTypeStatList;
        this.airlineStatList = airlineStatList;
        this.departureTimeStatList = departureTimeStatList;
        this.arrivalTimeStatList = arrivalTimeStatList;
        this.refundableTypeStatList = refundableTypeStatList;
        this.isHaveSpecialPrice = isHaveSpecialPrice;
        this.isHaveBaggage = isHaveBaggage;
        this.isHaveInFlightMeal = isHaveInFlightMeal;
        this.isHasFreeRapidTest = isHasFreeRapidTest;
        this.isSeatDistancing = isSeatDistancing;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public int getMinDuration() {
        return minDuration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public List<AirlineStat> getAirlineStatList() {
        return airlineStatList;
    }

    public FlightAirlineModel getAirline(String airlineID) {
        List<AirlineStat> airlineStatList = getAirlineStatList();
        if (airlineStatList != null) {
            for (int i = 0, sizei = airlineStatList.size(); i < sizei; i++) {
                FlightAirlineModel flightAirlineDB = airlineStatList.get(i).getAirlineDB();
                if (airlineID.equals(flightAirlineDB.getId())) {
                    return flightAirlineDB;
                }
            }
        }
        return new FlightAirlineModel(airlineID, "", "", "");
    }

    public List<DepartureStat> getDepartureTimeStatList() {
        return departureTimeStatList;
    }

    public List<DepartureStat> getArrivalTimeStatList() {
        return arrivalTimeStatList;
    }

    public List<TransitStat> getTransitTypeStatList() {
        return transitTypeStatList;
    }

    public List<RefundableStat> getRefundableTypeStatList() {
        return refundableTypeStatList;
    }

    public boolean isHaveSpecialPrice() {
        return isHaveSpecialPrice;
    }

    public boolean isHaveBaggage() {
        return isHaveBaggage;
    }

    public boolean isHaveInFlightMeal() {
        return isHaveInFlightMeal;
    }

    public boolean isHasFreeRapidTest() {
        return isHasFreeRapidTest;
    }

    public boolean isSeatDistancing() {
        return isSeatDistancing;
    }
}
