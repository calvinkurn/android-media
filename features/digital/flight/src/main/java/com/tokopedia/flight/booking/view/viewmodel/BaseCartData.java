package com.tokopedia.flight.booking.view.viewmodel;

import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice;

import java.util.List;

/**
 * @author by alvarisi on 11/29/17.
 */

public class BaseCartData implements Cloneable {
    private String id;
    private int adult;
    private int child;
    private int infant;
    private int total;
    private List<FlightBookingAmenityViewModel> amenities;
    private List<NewFarePrice> newFarePrices;
    private int refreshTime;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FlightBookingAmenityViewModel> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<FlightBookingAmenityViewModel> amenities) {
        this.amenities = amenities;
    }

    public List<NewFarePrice> getNewFarePrices() {
        return newFarePrices;
    }

    public void setNewFarePrices(List<NewFarePrice> newFarePrices) {
        this.newFarePrices = newFarePrices;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
