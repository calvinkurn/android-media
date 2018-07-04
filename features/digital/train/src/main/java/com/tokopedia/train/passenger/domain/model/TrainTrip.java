package com.tokopedia.train.passenger.domain.model;

import java.util.List;

public class TrainTrip {
    private int errCode;
    private String org;
    private String des;
    private String departureTimestamp;
    private String arrivalTimestamp;
    private String trainNo;
    private String bookCode;
    private String numCode;
    private String normalSales;
    private String displayNormalSales;
    private int extraFee;
    private String displayExtraFee;
    private int bookBalance;
    private String displayBookBalance;
    private int discount;
    private String displayDiscount;
    private int adultPrice;
    private String displayAdultPrice;
    private int infantPrice;
    private String displayInfantPrice;
    private int totalPriceAdult;
    private String displayTotalPriceAdult;
    private int totalPriceInfant;
    private String displayTotalPriceInfant;
    private int totalPrice;
    private String displayTotalPrice;
    private List<TrainPaxPassenger> paxPassengers;

    public int getErrCode() {
        return errCode;
    }

    public String getOrg() {
        return org;
    }

    public String getDes() {
        return des;
    }

    public String getDepartureTimestamp() {
        return departureTimestamp;
    }

    public String getArrivalTimestamp() {
        return arrivalTimestamp;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public String getBookCode() {
        return bookCode;
    }

    public String getNumCode() {
        return numCode;
    }

    public List<TrainPaxPassenger> getPaxPassengers() {
        return paxPassengers;
    }

    public String getNormalSales() {
        return normalSales;
    }

    public String getDisplayNormalSales() {
        return displayNormalSales;
    }

    public int getExtraFee() {
        return extraFee;
    }

    public String getDisplayExtraFee() {
        return displayExtraFee;
    }

    public int getBookBalance() {
        return bookBalance;
    }

    public String getDisplayBookBalance() {
        return displayBookBalance;
    }

    public int getDiscount() {
        return discount;
    }

    public String getDisplayDiscount() {
        return displayDiscount;
    }

    public int getAdultPrice() {
        return adultPrice;
    }

    public String getDisplayAdultPrice() {
        return displayAdultPrice;
    }

    public int getInfantPrice() {
        return infantPrice;
    }

    public String getDisplayInfantPrice() {
        return displayInfantPrice;
    }

    public int getTotalPriceAdult() {
        return totalPriceAdult;
    }

    public String getDisplayTotalPriceAdult() {
        return displayTotalPriceAdult;
    }

    public int getTotalPriceInfant() {
        return totalPriceInfant;
    }

    public String getDisplayTotalPriceInfant() {
        return displayTotalPriceInfant;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getDisplayTotalPrice() {
        return displayTotalPrice;
    }

    public void setPaxPassengers(List<TrainPaxPassenger> paxPassengers) {
        this.paxPassengers = paxPassengers;
    }
}
