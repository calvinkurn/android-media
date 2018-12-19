package com.tokopedia.flight.search.presentation.model.resultstatistics;

import com.tokopedia.flight.search.presentation.model.FlightAirlineViewModel;

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
    private List<RefundableStat> refundableTypeStatList;
    private boolean isHaveSpecialPrice;

    public FlightSearchStatisticModel(int minPrice, int maxPrice, int minDuration, int maxDuration,
                                      List<TransitStat> transitTypeStatList, List<AirlineStat> airlineStatList,
                                      List<DepartureStat> departureTimeStatList, List<RefundableStat> refundableTypeStatList,
                                      boolean isHaveSpecialPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.transitTypeStatList = transitTypeStatList;
        this.airlineStatList = airlineStatList;
        this.departureTimeStatList = departureTimeStatList;
        this.refundableTypeStatList = refundableTypeStatList;
        this.isHaveSpecialPrice = isHaveSpecialPrice;
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

    public FlightAirlineViewModel getAirline(String airlineID) {
        List<AirlineStat> airlineStatList = getAirlineStatList();
        if (airlineStatList!= null) {
            for (int i = 0, sizei = airlineStatList.size(); i < sizei; i++) {
                FlightAirlineViewModel flightAirlineDB = airlineStatList.get(i).getAirlineDB();
                if (airlineID.equals(flightAirlineDB.getId())) {
                    return flightAirlineDB;
                }
            }
        }
        return new FlightAirlineViewModel(airlineID, "", "", "");
    }

    public List<DepartureStat> getDepartureTimeStatList() {
        return departureTimeStatList;
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
}
