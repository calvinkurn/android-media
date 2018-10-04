package com.tokopedia.train.passenger.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TrainTrip implements Parcelable{

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
    private String subclass;
    private String trainClass;
    private List<TrainPaxPassenger> paxPassengers;
    private int numOfAdultPassenger;
    private int numOfInfantPassenger;

    public TrainTrip() {
    }

    public TrainTrip(int errCode, String org, String des, String departureTimestamp, String arrivalTimestamp,
                     String trainNo, String bookCode, String numCode, String normalSales, String displayNormalSales,
                     int extraFee, String displayExtraFee, int bookBalance, String displayBookBalance,
                     int discount, String displayDiscount, int adultPrice, String displayAdultPrice,
                     int infantPrice, String displayInfantPrice, int totalPriceAdult, String displayTotalPriceAdult,
                     int totalPriceInfant, String displayTotalPriceInfant, int totalPrice, String displayTotalPrice,
                     List<TrainPaxPassenger> paxPassengers, int numOfAdultPassenger, int numOfInfantPassenger) {
        this.errCode = errCode;
        this.org = org;
        this.des = des;
        this.departureTimestamp = departureTimestamp;
        this.arrivalTimestamp = arrivalTimestamp;
        this.trainNo = trainNo;
        this.bookCode = bookCode;
        this.numCode = numCode;
        this.normalSales = normalSales;
        this.displayNormalSales = displayNormalSales;
        this.extraFee = extraFee;
        this.displayExtraFee = displayExtraFee;
        this.bookBalance = bookBalance;
        this.displayBookBalance = displayBookBalance;
        this.discount = discount;
        this.displayDiscount = displayDiscount;
        this.adultPrice = adultPrice;
        this.displayAdultPrice = displayAdultPrice;
        this.infantPrice = infantPrice;
        this.displayInfantPrice = displayInfantPrice;
        this.totalPriceAdult = totalPriceAdult;
        this.displayTotalPriceAdult = displayTotalPriceAdult;
        this.totalPriceInfant = totalPriceInfant;
        this.displayTotalPriceInfant = displayTotalPriceInfant;
        this.totalPrice = totalPrice;
        this.displayTotalPrice = displayTotalPrice;
        this.paxPassengers = paxPassengers;
        this.numOfAdultPassenger = numOfAdultPassenger;
        this.numOfInfantPassenger = numOfInfantPassenger;
    }


    protected TrainTrip(Parcel in) {
        errCode = in.readInt();
        org = in.readString();
        des = in.readString();
        departureTimestamp = in.readString();
        arrivalTimestamp = in.readString();
        trainNo = in.readString();
        bookCode = in.readString();
        numCode = in.readString();
        normalSales = in.readString();
        displayNormalSales = in.readString();
        extraFee = in.readInt();
        displayExtraFee = in.readString();
        bookBalance = in.readInt();
        displayBookBalance = in.readString();
        discount = in.readInt();
        displayDiscount = in.readString();
        adultPrice = in.readInt();
        displayAdultPrice = in.readString();
        infantPrice = in.readInt();
        displayInfantPrice = in.readString();
        totalPriceAdult = in.readInt();
        displayTotalPriceAdult = in.readString();
        totalPriceInfant = in.readInt();
        displayTotalPriceInfant = in.readString();
        totalPrice = in.readInt();
        displayTotalPrice = in.readString();
        subclass = in.readString();
        trainClass = in.readString();
        paxPassengers = in.createTypedArrayList(TrainPaxPassenger.CREATOR);
        numOfAdultPassenger = in.readInt();
        numOfInfantPassenger = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(errCode);
        dest.writeString(org);
        dest.writeString(des);
        dest.writeString(departureTimestamp);
        dest.writeString(arrivalTimestamp);
        dest.writeString(trainNo);
        dest.writeString(bookCode);
        dest.writeString(numCode);
        dest.writeString(normalSales);
        dest.writeString(displayNormalSales);
        dest.writeInt(extraFee);
        dest.writeString(displayExtraFee);
        dest.writeInt(bookBalance);
        dest.writeString(displayBookBalance);
        dest.writeInt(discount);
        dest.writeString(displayDiscount);
        dest.writeInt(adultPrice);
        dest.writeString(displayAdultPrice);
        dest.writeInt(infantPrice);
        dest.writeString(displayInfantPrice);
        dest.writeInt(totalPriceAdult);
        dest.writeString(displayTotalPriceAdult);
        dest.writeInt(totalPriceInfant);
        dest.writeString(displayTotalPriceInfant);
        dest.writeInt(totalPrice);
        dest.writeString(displayTotalPrice);
        dest.writeString(subclass);
        dest.writeString(trainClass);
        dest.writeTypedList(paxPassengers);
        dest.writeInt(numOfAdultPassenger);
        dest.writeInt(numOfInfantPassenger);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrainTrip> CREATOR = new Creator<TrainTrip>() {
        @Override
        public TrainTrip createFromParcel(Parcel in) {
            return new TrainTrip(in);
        }

        @Override
        public TrainTrip[] newArray(int size) {
            return new TrainTrip[size];
        }
    };

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

    public String getTrainClass() {
        return trainClass;
    }

    public String getSubclass() {
        return subclass;
    }

    public int getNumOfAdultPassenger() {
        return numOfAdultPassenger;
    }

    public int getNumOfInfantPassenger() {
        return numOfInfantPassenger;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setDepartureTimestamp(String departureTimestamp) {
        this.departureTimestamp = departureTimestamp;
    }

    public void setArrivalTimestamp(String arrivalTimestamp) {
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public void setNormalSales(String normalSales) {
        this.normalSales = normalSales;
    }

    public void setDisplayNormalSales(String displayNormalSales) {
        this.displayNormalSales = displayNormalSales;
    }

    public void setExtraFee(int extraFee) {
        this.extraFee = extraFee;
    }

    public void setDisplayExtraFee(String displayExtraFee) {
        this.displayExtraFee = displayExtraFee;
    }

    public void setBookBalance(int bookBalance) {
        this.bookBalance = bookBalance;
    }

    public void setDisplayBookBalance(String displayBookBalance) {
        this.displayBookBalance = displayBookBalance;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setDisplayDiscount(String displayDiscount) {
        this.displayDiscount = displayDiscount;
    }

    public void setAdultPrice(int adultPrice) {
        this.adultPrice = adultPrice;
    }

    public void setDisplayAdultPrice(String displayAdultPrice) {
        this.displayAdultPrice = displayAdultPrice;
    }

    public void setInfantPrice(int infantPrice) {
        this.infantPrice = infantPrice;
    }

    public void setDisplayInfantPrice(String displayInfantPrice) {
        this.displayInfantPrice = displayInfantPrice;
    }

    public void setTotalPriceAdult(int totalPriceAdult) {
        this.totalPriceAdult = totalPriceAdult;
    }

    public void setDisplayTotalPriceAdult(String displayTotalPriceAdult) {
        this.displayTotalPriceAdult = displayTotalPriceAdult;
    }

    public void setTotalPriceInfant(int totalPriceInfant) {
        this.totalPriceInfant = totalPriceInfant;
    }

    public void setDisplayTotalPriceInfant(String displayTotalPriceInfant) {
        this.displayTotalPriceInfant = displayTotalPriceInfant;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDisplayTotalPrice(String displayTotalPrice) {
        this.displayTotalPrice = displayTotalPrice;
    }

    public void setPaxPassengers(List<TrainPaxPassenger> paxPassengers) {
        this.paxPassengers = paxPassengers;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass;
    }

    public void setTrainClass(String trainClass) {
        this.trainClass = trainClass;
    }

    public void setNumOfAdultPassenger(int numOfAdultPassenger) {
        this.numOfAdultPassenger = numOfAdultPassenger;
    }

    public void setNumOfInfantPassenger(int numOfInfantPassenger) {
        this.numOfInfantPassenger = numOfInfantPassenger;
    }

}
