package com.tokopedia.core.myproduct.model;

import com.tokopedia.core.util.GeneralUtils;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 23/12/2015.
 */
@Parcel
public class WholeSaleAdapterModel {
    double quantityOne;
    double quantityTwo;
    double wholeSalePrice;
    long bDid;

    boolean isNoErrorQOne = true;
    boolean isNoErrorQTwo = true;
    boolean isNoErrorPrice = true;

    /**
     * this is for parcelable
     */
    public WholeSaleAdapterModel(){}

    public WholeSaleAdapterModel(String quantityOne, String quantityTwo, String wholeSalePrice,String currency){

        this.quantityOne = Double.parseDouble(quantityOne);
        this.quantityTwo = Double.parseDouble(quantityTwo);
        this.wholeSalePrice = GeneralUtils.parsePriceToDouble(wholeSalePrice,currency);
    }

    public WholeSaleAdapterModel(double quantityOne, double quantityTwo, double wholeSalePrice) {
        this.quantityOne = quantityOne;
        this.quantityTwo = quantityTwo;
        this.wholeSalePrice = wholeSalePrice;
    }

    public double getQuantityOne() {
        return quantityOne;
    }

    public void setQuantityOne(double quantityOne) {
        this.quantityOne = quantityOne;
    }

    public double getQuantityTwo() {
        return quantityTwo;
    }

    public void setQuantityTwo(double quantityTwo) {
        this.quantityTwo = quantityTwo;
    }

    public double getWholeSalePrice() {
        return wholeSalePrice;
    }

    public void setWholeSalePrice(double wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    public long getbDid() {
        return bDid;
    }

    public void setbDid(long bDid) {
        this.bDid = bDid;
    }

    public boolean isNoErrorQOne() {
        return isNoErrorQOne;
    }

    public void setNoErrorQOne(boolean noErrorQOne) {
        isNoErrorQOne = noErrorQOne;
    }

    public boolean isNoErrorQTwo() {
        return isNoErrorQTwo;
    }

    public void setNoErrorQTwo(boolean noErrorQTwo) {
        isNoErrorQTwo = noErrorQTwo;
    }

    public boolean isNoErrorPrice() {
        return isNoErrorPrice;
    }

    public void setNoErrorPrice(boolean noErrorPrice) {
        isNoErrorPrice = noErrorPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WholeSaleAdapterModel that = (WholeSaleAdapterModel) o;

        if (Double.compare(that.quantityOne, quantityOne) != 0) return false;
        if (Double.compare(that.quantityTwo, quantityTwo) != 0) return false;
        return Double.compare(that.wholeSalePrice, wholeSalePrice) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(quantityOne);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(quantityTwo);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(wholeSalePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "WholeSaleAdapterModel{" +
                "quantityOne=" + quantityOne +
                ", quantityTwo=" + quantityTwo +
                ", wholeSalePrice=" + wholeSalePrice +
                ", bDid=" + bDid +
                '}';
    }
}
