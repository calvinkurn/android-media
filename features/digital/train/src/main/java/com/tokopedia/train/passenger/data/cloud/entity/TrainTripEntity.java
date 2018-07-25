package com.tokopedia.train.passenger.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainTripEntity {

    @SerializedName("errCode")
    @Expose
    private int errCode;
    @SerializedName("org")
    @Expose
    private String org;
    @SerializedName("des")
    @Expose
    private String des;
    @SerializedName("departureTimestamp")
    @Expose
    private String departureTimestamp;
    @SerializedName("arrivalTimestamp")
    @Expose
    private String arrivalTimestamp;
    @SerializedName("trainNo")
    @Expose
    private String trainNo;
    @SerializedName("bookCode")
    @Expose
    private String bookCode;
    @SerializedName("numCode")
    @Expose
    private String numCode;
    @SerializedName("normalSales")
    @Expose
    private String normalSales;
    @SerializedName("displayNormalSales")
    @Expose
    private String displayNormalSales;
    @SerializedName("extraFee")
    @Expose
    private int extraFee;
    @SerializedName("displayExtraFee")
    @Expose
    private String displayExtraFee;
    @SerializedName("bookBalance")
    @Expose
    private int bookBalance;
    @SerializedName("displayBookBalance")
    @Expose
    private String displayBookBalance;
    @SerializedName("discount")
    @Expose
    private int discount;
    @SerializedName("displayDiscount")
    @Expose
    private String displayDiscount;
    @SerializedName("adultPrice")
    @Expose
    private int adultPrice;
    @SerializedName("displayAdultPrice")
    @Expose
    private String displayAdultPrice;
    @SerializedName("infantPrice")
    @Expose
    private int infantPrice;
    @SerializedName("displayInfantPrice")
    @Expose
    private String displayInfantPrice;
    @SerializedName("totalPriceAdult")
    @Expose
    private int totalPriceAdult;
    @SerializedName("displayTotalPriceAdult")
    @Expose
    private String displayTotalPriceAdult;
    @SerializedName("totalPriceInfant")
    @Expose
    private int totalPriceInfant;
    @SerializedName("displayTotalPriceInfant")
    @Expose
    private String displayTotalPriceInfant;
    @SerializedName("totalPrice")
    @Expose
    private int totalPrice;
    @SerializedName("displayTotalPrice")
    @Expose
    private String displayTotalPrice;
    @SerializedName("paxPassenger")
    @Expose
    private List<TrainPaxPassengerEntity> paxPassengers;
    @SerializedName("trainClass")
    @Expose
    private String trainClass;
    @SerializedName("subclass")
    @Expose
    private String subclass;

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

    public List<TrainPaxPassengerEntity> getPaxPassengers() {
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

    public String getTrainClass() {
        return trainClass;
    }

    public String getSubclass() {
        return subclass;
    }
}
