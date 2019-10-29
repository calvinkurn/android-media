package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Irfan Khoirul on 23/05/18.
 */

public class WholesalePrice implements Parcelable {

    private String qtyMinFmt;
    private String qtyMaxFmt;
    private String prdPrcFmt;
    private int qtyMin;
    private int qtyMax;
    private int prdPrc;

    public WholesalePrice() {
    }

    protected WholesalePrice(Parcel in) {
        qtyMinFmt = in.readString();
        qtyMaxFmt = in.readString();
        prdPrcFmt = in.readString();
        qtyMin = in.readInt();
        qtyMax = in.readInt();
        prdPrc = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(qtyMinFmt);
        dest.writeString(qtyMaxFmt);
        dest.writeString(prdPrcFmt);
        dest.writeInt(qtyMin);
        dest.writeInt(qtyMax);
        dest.writeInt(prdPrc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WholesalePrice> CREATOR = new Creator<WholesalePrice>() {
        @Override
        public WholesalePrice createFromParcel(Parcel in) {
            return new WholesalePrice(in);
        }

        @Override
        public WholesalePrice[] newArray(int size) {
            return new WholesalePrice[size];
        }
    };

    public String getQtyMinFmt() {
        return qtyMinFmt;
    }

    public void setQtyMinFmt(String qtyMinFmt) {
        this.qtyMinFmt = qtyMinFmt;
    }

    public String getQtyMaxFmt() {
        return qtyMaxFmt;
    }

    public void setQtyMaxFmt(String qtyMaxFmt) {
        this.qtyMaxFmt = qtyMaxFmt;
    }

    public String getPrdPrcFmt() {
        return prdPrcFmt;
    }

    public void setPrdPrcFmt(String prdPrcFmt) {
        this.prdPrcFmt = prdPrcFmt;
    }

    public int getQtyMin() {
        return qtyMin;
    }

    public void setQtyMin(int qtyMin) {
        this.qtyMin = qtyMin;
    }

    public int getQtyMax() {
        return qtyMax;
    }

    public void setQtyMax(int qtyMax) {
        this.qtyMax = qtyMax;
    }

    public int getPrdPrc() {
        return prdPrc;
    }

    public void setPrdPrc(int prdPrc) {
        this.prdPrc = prdPrc;
    }
}
