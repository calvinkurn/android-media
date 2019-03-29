package com.tokopedia.flight.search.data.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.tokopedia.flight.search.presentation.model.FlightAirlineViewModel;
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum;
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum;
import com.tokopedia.flight.search.presentation.model.FlightAirlineViewModel;

import java.util.List;

/**
 * Created by Rizky on 01/10/18.
 */
@Entity
public class FlightJourneyTable {
    @PrimaryKey
    @NonNull
    private String id;
    private String term;
    private String departureAirport;
    private String departureAirportName;
    private String departureAirportCity;
    private String arrivalAirport;
    private String arrivalAirportName;
    private String arrivalAirportCity;
    @Ignore
    private List<FlightAirlineViewModel> flightAirlineDBS;
    private String departureTime;
    private int departureTimeInt;
    private String arrivalTime;
    private int arrivalTimeInt;
    private int totalTransit;
    private int totalStop;
    private int addDayArrival;
    private String duration;
    private int durationMinute;
    private String adult;
    private String adultCombo;
    private int adultNumeric;
    private int adultNumericCombo;
    private String child;
    private String childCombo;
    private int childNumeric;
    private int childNumericCombo;
    private String infant;
    private String infantCombo;
    private int infantNumeric;
    private int infantNumericCombo;
    private String total;
    private String totalCombo;
    private int totalNumeric;
    private int totalNumericCombo;
    private boolean isBestPairing;
    private String beforeTotal;
    private boolean showSpecialPriceTag;
    private String sortPrice;
    private int sortPriceNumeric;
    private boolean isReturn;
    private boolean isSpecialPrice;
    private RefundableEnum isRefundable;
    private String comboId;

    public FlightJourneyTable() {
    }

    public FlightJourneyTable(@NonNull String id, String term, String departureAirport,
                              String departureAirportName, String departureAirportCity,
                              String arrivalAirport, String arrivalAirportName, String arrivalAirportCity,
                              List<FlightAirlineViewModel> flightAirlineDBS, String departureTime, int departureTimeInt,
                              String arrivalTime, int arrivalTimeInt, int totalTransit, int totalStop,
                              int addDayArrival, String duration, int durationMinute, String adult,
                              String adultCombo, int adultNumeric, int adultNumericCombo, String child,
                              String childCombo, int childNumeric, int childNumericCombo, String infant,
                              String infantCombo, int infantNumeric, int infantNumericCombo, String total,
                              String totalCombo, int totalNumeric, int totalNumericCombo,
                              boolean isBestPairing, String beforeTotal, boolean showSpecialPriceTag,
                              String sortPrice, int sortPriceNumeric, boolean isReturn, RefundableEnum isRefundable,
                              boolean isSpecialPrice, String comboId) {
        this.id = id;
        this.term = term;
        this.departureAirport = departureAirport;
        this.departureAirportName = departureAirportName;
        this.departureAirportCity = departureAirportCity;
        this.arrivalAirport = arrivalAirport;
        this.arrivalAirportName = arrivalAirportName;
        this.arrivalAirportCity = arrivalAirportCity;
        this.flightAirlineDBS = flightAirlineDBS;
        this.departureTime = departureTime;
        this.departureTimeInt = departureTimeInt;
        this.arrivalTime = arrivalTime;
        this.arrivalTimeInt = arrivalTimeInt;
        this.totalTransit = totalTransit;
        this.totalStop = totalStop;
        this.addDayArrival = addDayArrival;
        this.duration = duration;
        this.durationMinute = durationMinute;
        this.adult = adult;
        this.adultCombo = adultCombo;
        this.adultNumeric = adultNumeric;
        this.adultNumericCombo = adultNumericCombo;
        this.child = child;
        this.childCombo = childCombo;
        this.childNumeric = childNumeric;
        this.childNumericCombo = childNumericCombo;
        this.infant = infant;
        this.infantCombo = infantCombo;
        this.infantNumeric = infantNumeric;
        this.infantNumericCombo = infantNumericCombo;
        this.total = total;
        this.totalCombo = totalCombo;
        this.totalNumeric = totalNumeric;
        this.totalNumericCombo = totalNumericCombo;
        this.isBestPairing = isBestPairing;
        this.beforeTotal = beforeTotal;
        this.showSpecialPriceTag = showSpecialPriceTag;
        this.sortPrice = sortPrice;
        this.sortPriceNumeric = sortPriceNumeric;
        this.isReturn = isReturn;
        this.isRefundable = isRefundable;
        this.isSpecialPrice = isSpecialPrice;
        this.comboId = comboId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getDepartureAirportCity() {
        return departureAirportCity;
    }

    public void setDepartureAirportCity(String departureAirportCity) {
        this.departureAirportCity = departureAirportCity;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public void setArrivalAirportName(String arrivalAirportName) {
        this.arrivalAirportName = arrivalAirportName;
    }

    public String getArrivalAirportCity() {
        return arrivalAirportCity;
    }

    public void setArrivalAirportCity(String arrivalAirportCity) {
        this.arrivalAirportCity = arrivalAirportCity;
    }

    public List<FlightAirlineViewModel> getFlightAirlineDBS() {
        return flightAirlineDBS;
    }

    public void setFlightAirlineDBS(List<FlightAirlineViewModel> flightAirlineDBS) {
        this.flightAirlineDBS = flightAirlineDBS;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getDepartureTimeInt() {
        return departureTimeInt;
    }

    public void setDepartureTimeInt(int departureTimeInt) {
        this.departureTimeInt = departureTimeInt;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getArrivalTimeInt() {
        return arrivalTimeInt;
    }

    public void setArrivalTimeInt(int arrivalTimeInt) {
        this.arrivalTimeInt = arrivalTimeInt;
    }

    public int getTotalTransit() {
        return totalTransit;
    }

    public void setTotalTransit(int totalTransit) {
        this.totalTransit = totalTransit;
    }

    public int getTotalStop() {
        return totalStop;
    }

    public void setTotalStop(int totalStop) {
        this.totalStop = totalStop;
    }

    public int getAddDayArrival() {
        return addDayArrival;
    }

    public void setAddDayArrival(int addDayArrival) {
        this.addDayArrival = addDayArrival;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getDurationMinute() {
        return durationMinute;
    }

    public void setDurationMinute(int durationMinute) {
        this.durationMinute = durationMinute;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getAdultCombo() {
        return adultCombo;
    }

    public void setAdultCombo(String adultCombo) {
        this.adultCombo = adultCombo;
    }

    public int getAdultNumeric() {
        return adultNumeric;
    }

    public void setAdultNumeric(int adultNumeric) {
        this.adultNumeric = adultNumeric;
    }

    public int getAdultNumericCombo() {
        return adultNumericCombo;
    }

    public void setAdultNumericCombo(int adultNumericCombo) {
        this.adultNumericCombo = adultNumericCombo;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getChildCombo() {
        return childCombo;
    }

    public void setChildCombo(String childCombo) {
        this.childCombo = childCombo;
    }

    public int getChildNumeric() {
        return childNumeric;
    }

    public void setChildNumeric(int childNumeric) {
        this.childNumeric = childNumeric;
    }

    public int getChildNumericCombo() {
        return childNumericCombo;
    }

    public void setChildNumericCombo(int childNumericCombo) {
        this.childNumericCombo = childNumericCombo;
    }

    public String getInfant() {
        return infant;
    }

    public void setInfant(String infant) {
        this.infant = infant;
    }

    public String getInfantCombo() {
        return infantCombo;
    }

    public void setInfantCombo(String infantCombo) {
        this.infantCombo = infantCombo;
    }

    public int getInfantNumeric() {
        return infantNumeric;
    }

    public void setInfantNumeric(int infantNumeric) {
        this.infantNumeric = infantNumeric;
    }

    public int getInfantNumericCombo() {
        return infantNumericCombo;
    }

    public void setInfantNumericCombo(int infantNumericCombo) {
        this.infantNumericCombo = infantNumericCombo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalCombo() {
        return totalCombo;
    }

    public void setTotalCombo(String totalCombo) {
        this.totalCombo = totalCombo;
    }

    public int getTotalNumeric() {
        return totalNumeric;
    }

    public void setTotalNumeric(int totalNumeric) {
        this.totalNumeric = totalNumeric;
    }

    public int getTotalNumericCombo() {
        return totalNumericCombo;
    }

    public void setTotalNumericCombo(int totalNumericCombo) {
        this.totalNumericCombo = totalNumericCombo;
    }

    public boolean isBestPairing() {
        return isBestPairing;
    }

    public void setBestPairing(boolean bestPairing) {
        isBestPairing = bestPairing;
    }

    public String getBeforeTotal() {
        return beforeTotal;
    }

    public void setBeforeTotal(String beforeTotal) {
        this.beforeTotal = beforeTotal;
    }

    public boolean isShowSpecialPriceTag() { return showSpecialPriceTag; }

    public void setShowSpecialPriceTag(boolean showSpecialPriceTag) { this.showSpecialPriceTag = showSpecialPriceTag; }

    public String getSortPrice() {
        return sortPrice;
    }

    public void setSortPrice(String sortPrice) {
        this.sortPrice = sortPrice;
    }

    public int getSortPriceNumeric() {
        return sortPriceNumeric;
    }

    public void setSortPriceNumeric(int sortPriceNumeric) {
        this.sortPriceNumeric = sortPriceNumeric;
    }

    public boolean isReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public RefundableEnum getIsRefundable() {
        return isRefundable;
    }

    public void setIsRefundable(RefundableEnum isRefundable) {
        this.isRefundable = isRefundable;
    }

    public boolean isSpecialPrice() {
        return isSpecialPrice;
    }

    public void setSpecialPrice(boolean specialPrice) {
        isSpecialPrice = specialPrice;
    }

    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }
}