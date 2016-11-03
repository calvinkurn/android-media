package com.tokopedia.tkpd.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Generated;

public class Count implements Parcelable {

    @SerializedName("buyer_resol")
    @Expose
    private Integer buyerResol;
    @SerializedName("seller_resol")
    @Expose
    private Integer sellerResol;

    /**
     * 
     * @return
     *     The buyerResol
     */
    public Integer getBuyerResol() {
        return buyerResol;
    }

    /**
     * 
     * @param buyerResol
     *     The buyer_resol
     */
    public void setBuyerResol(Integer buyerResol) {
        this.buyerResol = buyerResol;
    }

    /**
     * 
     * @return
     *     The sellerResol
     */
    public Integer getSellerResol() {
        return sellerResol;
    }

    /**
     * 
     * @param sellerResol
     *     The seller_resol
     */
    public void setSellerResol(Integer sellerResol) {
        this.sellerResol = sellerResol;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.buyerResol);
        dest.writeValue(this.sellerResol);
    }

    public Count() {
    }

    protected Count(Parcel in) {
        this.buyerResol = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sellerResol = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Count> CREATOR = new Parcelable.Creator<Count>() {
        @Override
        public Count createFromParcel(Parcel source) {
            return new Count(source);
        }

        @Override
        public Count[] newArray(int size) {
            return new Count[size];
        }
    };
}
