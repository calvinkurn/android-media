package com.tokopedia.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 05/03/19.
 */

public class TradeInInfo implements Parcelable {

    private boolean isValidTradeIn;
    private int newDevicePrice;
    private String newDevicePriceFmt;
    private int oldDevicePrice;
    private String oldDevicePriceFmt;

    public TradeInInfo() {
    }

    protected TradeInInfo(Parcel in) {
        isValidTradeIn = in.readByte() != 0;
        newDevicePrice = in.readInt();
        newDevicePriceFmt = in.readString();
        oldDevicePrice = in.readInt();
        oldDevicePriceFmt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isValidTradeIn ? 1 : 0));
        dest.writeInt(newDevicePrice);
        dest.writeString(newDevicePriceFmt);
        dest.writeInt(oldDevicePrice);
        dest.writeString(oldDevicePriceFmt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TradeInInfo> CREATOR = new Creator<TradeInInfo>() {
        @Override
        public TradeInInfo createFromParcel(Parcel in) {
            return new TradeInInfo(in);
        }

        @Override
        public TradeInInfo[] newArray(int size) {
            return new TradeInInfo[size];
        }
    };

    public boolean isValidTradeIn() {
        return isValidTradeIn;
    }

    public void setValidTradeIn(boolean validTradeIn) {
        isValidTradeIn = validTradeIn;
    }

    public int getNewDevicePrice() {
        return newDevicePrice;
    }

    public void setNewDevicePrice(int newDevicePrice) {
        this.newDevicePrice = newDevicePrice;
    }

    public String getNewDevicePriceFmt() {
        return newDevicePriceFmt;
    }

    public void setNewDevicePriceFmt(String newDevicePriceFmt) {
        this.newDevicePriceFmt = newDevicePriceFmt;
    }

    public int getOldDevicePrice() {
        return oldDevicePrice;
    }

    public void setOldDevicePrice(int oldDevicePrice) {
        this.oldDevicePrice = oldDevicePrice;
    }

    public String getOldDevicePriceFmt() {
        return oldDevicePriceFmt;
    }

    public void setOldDevicePriceFmt(String oldDevicePriceFmt) {
        this.oldDevicePriceFmt = oldDevicePriceFmt;
    }
}
