package com.tokopedia.train.passenger.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TrainTrip implements Parcelable {

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

    public TrainTrip(int errCode, String org, String des, String departureTimestamp, String arrivalTimestamp, String trainNo, String bookCode, String numCode, String normalSales, String displayNormalSales, int extraFee, String displayExtraFee, int bookBalance, String displayBookBalance, int discount, String displayDiscount, int adultPrice, String displayAdultPrice, int infantPrice, String displayInfantPrice, int totalPriceAdult, String displayTotalPriceAdult, int totalPriceInfant, String displayTotalPriceInfant, int totalPrice, String displayTotalPrice, List<TrainPaxPassenger> paxPassengers) {
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
        paxPassengers = in.createTypedArrayList(TrainPaxPassenger.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
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
        dest.writeTypedList(paxPassengers);
    }

}
