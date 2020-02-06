package com.tokopedia.flight.search.presentation.model;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.search.data.api.single.response.Route;
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory;
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum;

import java.util.List;

/**
 * Created by User on 10/30/2017.
 */

public class FlightJourneyViewModel implements Visitable<FlightSearchAdapterTypeFactory> {

    public static final int ONE_HOURS_DAY = 2400;

    private String term;
    private String id;
    private String departureAirport;
    private String departureAirportName; // merge result
    private String departureAirportCity; // merge result
    private String departureTime; //ini waktu berangkat 2018-01-01T14:45:00Z
    private int departureTimeInt; //1450
    private String arrivalAirport;
    private String arrivalAirportName; // merge result
    private String arrivalAirportCity; // merge result
    private String arrivalTime;
    private int arrivalTimeInt; //1450
    private int totalTransit;
    private int addDayArrival;
    private String duration; // 1 jam 50 menit
    private int durationMinute;
    private String total; // "Rp 693.000"
    private int totalNumeric; // Fare 693000
    private String comboPrice; // "Rp 500.000"
    private int comboPriceNumeric; // Fare 500000
    private boolean isBestPairing;
    private String beforeTotal; // original price
    private boolean showSpecialPriceTag;
    private RefundableEnum isRefundable;
    private boolean isReturning;
    private FlightFareViewModel fare;
    private List<Route> routeList;
    private List<FlightAirlineViewModel> airlineDataList; // merge result
    private String comboId;
    private String specialTagText;

    public FlightJourneyViewModel(String term, String id, String departureAirport,
                                  String departureAirportName, String departureAirportCity, String departureTime,
                                  int departureTimeInt, String arrivalAirport, String arrivalTime,
                                  String arrivalAirportName, String arrivalAirportCity, int arrivalTimeInt,
                                  int totalTransit, int addDayArrival, String duration, int durationMinute,
                                  String total, int totalNumeric, String comboPrice, int comboPriceNumeric,
                                  boolean isBestPairing, String beforeTotal, boolean showSpecialPriceTag,
                                  RefundableEnum isRefundable, boolean isReturning, FlightFareViewModel fare,
                                  List<Route> routeList, List<FlightAirlineViewModel> airlineDataList,
                                  String comboId, String specialTagText) {
        this.term = term;
        this.id = id;
        this.departureAirport = departureAirport;
        this.departureAirportName = departureAirportName;
        this.departureAirportCity = departureAirportCity;
        this.departureTime = departureTime;
        this.departureTimeInt = departureTimeInt;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
        this.arrivalAirportName = arrivalAirportName;
        this.arrivalAirportCity = arrivalAirportCity;
        this.arrivalTimeInt = arrivalTimeInt;
        this.totalTransit = totalTransit;
        this.addDayArrival = addDayArrival;
        this.duration = duration;
        this.durationMinute = durationMinute;
        this.total = total;
        this.totalNumeric = totalNumeric;
        this.comboPrice = comboPrice;
        this.comboPriceNumeric = comboPriceNumeric;
        this.isBestPairing = isBestPairing;
        this.beforeTotal = beforeTotal;
        this.showSpecialPriceTag = showSpecialPriceTag;
        this.isRefundable = isRefundable;
        this.isReturning = isReturning;
        this.fare = fare;
        this.routeList = routeList;
        this.airlineDataList = airlineDataList;
        this.comboId = comboId;
        this.specialTagText = specialTagText;
    }

    public String getTerm() {
        return term;
    }

    public String getId() {
        return id;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getDepartureTimeInt() {
        return departureTimeInt;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
    }

    public int getArrivalTimeInt() {
        return arrivalTimeInt;
    }

    public int getTotalTransit() {
        return totalTransit;
    }

    public int getAddDayArrival() {
        return addDayArrival;
    }

    public String getDuration() {
        return duration;
    }

    public int getDurationMinute() {
        return durationMinute;
    }

    public String getTotal() {
        return total;
    }

    public int getTotalNumeric() {
        return totalNumeric;
    }

    public String getComboPrice() {
        return comboPrice;
    }

    public int getComboPriceNumeric() {
        return comboPriceNumeric;
    }

    public boolean isBestPairing() {
        return isBestPairing;
    }

    public String getBeforeTotal() {
        return beforeTotal;
    }

    public boolean isShowSpecialPriceTag() { return showSpecialPriceTag; }

    public RefundableEnum isRefundable() {
        return isRefundable;
    }

    public boolean isReturning() {
        return isReturning;
    }

    public FlightFareViewModel getFare() {
        return fare;
    }

    public List<Route> getRouteList() {
        return routeList;
    }

    public List<FlightAirlineViewModel> getAirlineDataList() {
        return airlineDataList;
    }

    public String getComboId() {
        return comboId;
    }

    public String getSpecialTagText() { return specialTagText; }

    @Override
    public int type(FlightSearchAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
