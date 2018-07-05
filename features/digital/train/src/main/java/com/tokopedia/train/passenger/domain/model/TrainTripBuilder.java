package com.tokopedia.train.passenger.domain.model;

import java.util.List;

public class TrainTripBuilder {

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

    public TrainTripBuilder errCode(int errCode) {
        this.errCode = errCode;
        return this;
    }

    public TrainTripBuilder org(String org) {
        this.org = org;
        return this;
    }

    public TrainTripBuilder des(String des) {
        this.des = des;
        return this;
    }

    public TrainTripBuilder departureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
        return this;
    }

    public TrainTripBuilder arrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
        return this;
    }

    public TrainTripBuilder trainNo(String trainNo) {
        this.trainNo = trainNo;
        return this;
    }

    public TrainTripBuilder bookCode(String bookCode) {
        this.bookCode = bookCode;
        return this;
    }

    public TrainTripBuilder numCode(String numCode) {
        this.numCode = numCode;
        return this;
    }

    public TrainTripBuilder normalSales(String normalSales) {
        this.normalSales = normalSales;
        return this;
    }

    public TrainTripBuilder displayNormalSales(String displayNormalSales) {
        this.displayNormalSales = displayNormalSales;
        return this;
    }

    public TrainTripBuilder extraFee(int extraFee) {
        this.extraFee = extraFee;
        return this;
    }

    public TrainTripBuilder displayExtraFee(String displayExtraFee) {
        this.displayExtraFee = displayExtraFee;
        return this;
    }

    public TrainTripBuilder bookBalance(int bookBalance) {
        this.bookBalance = bookBalance;
        return this;
    }

    public TrainTripBuilder displayBookBalance(String displayBookBalance) {
        this.displayBookBalance = displayBookBalance;
        return this;
    }

    public TrainTripBuilder discount(int discount) {
        this.discount = discount;
        return this;
    }

    public TrainTripBuilder displayDiscount(String displayDiscount) {
        this.displayDiscount = displayDiscount;
        return this;
    }

    public TrainTripBuilder adultPrice(int adultPrice) {
        this.adultPrice = adultPrice;
        return this;
    }

    public TrainTripBuilder displayAdultPrice(String displayAdultPrice) {
        this.displayAdultPrice = displayAdultPrice;
        return this;
    }

    public TrainTripBuilder infantPrice(int infantPrice) {
        this.infantPrice = infantPrice;
        return this;
    }

    public TrainTripBuilder displayInfantPrice(String displayInfantPrice) {
        this.displayInfantPrice = displayInfantPrice;
        return this;
    }

    public TrainTripBuilder totalPriceAdult(int totalPriceAdult) {
        this.totalPriceAdult = totalPriceAdult;
        return this;
    }

    public TrainTripBuilder displayTotalPriceAdult(String displayTotalPriceAdult) {
        this.displayTotalPriceAdult = displayTotalPriceAdult;
        return this;
    }

    public TrainTripBuilder totalPriceInfant(int totalPriceInfant) {
        this.totalPriceInfant = totalPriceInfant;
        return this;
    }

    public TrainTripBuilder displayTotalPriceInfant(String displayTotalPriceInfant) {
        this.displayTotalPriceInfant = displayTotalPriceInfant;
        return this;
    }

    public TrainTripBuilder totalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public TrainTripBuilder displayTotalPrice(String displayTotalPrice) {
        this.displayTotalPrice = displayTotalPrice;
        return this;
    }

    public TrainTripBuilder paxPassengers(List<TrainPaxPassenger> paxPassengers) {
        this.paxPassengers = paxPassengers;
        return this;
    }

    public TrainTrip createTrainTrip() {
        return new TrainTrip(errCode, org, des, departureTimestamp, arrivalTimestamp, trainNo, bookCode, numCode, normalSales, displayNormalSales, extraFee, displayExtraFee, bookBalance, displayBookBalance, discount, displayDiscount, adultPrice, displayAdultPrice, infantPrice, displayInfantPrice, totalPriceAdult, displayTotalPriceAdult, totalPriceInfant, displayTotalPriceInfant, totalPrice, displayTotalPrice, paxPassengers);
    }

}