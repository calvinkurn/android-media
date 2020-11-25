package com.tokopedia.checkout.domain.model.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 05/03/19.
 */

public class TradeInInfoData implements Parcelable {

    private boolean isValidTradeIn;
    private int newDevicePrice;
    private String newDevicePriceFmt;
    private int oldDevicePrice;
    private String oldDevicePriceFmt;
    private boolean dropOffEnable;
    private String deviceModel;
    private String diagnosticId;

    public TradeInInfoData() {
    }

    protected TradeInInfoData(Parcel in) {
        isValidTradeIn = in.readByte() != 0;
        newDevicePrice = in.readInt();
        newDevicePriceFmt = in.readString();
        oldDevicePrice = in.readInt();
        oldDevicePriceFmt = in.readString();
        dropOffEnable = in.readByte() != 0;
        deviceModel = in.readString();
        diagnosticId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isValidTradeIn ? 1 : 0));
        dest.writeInt(newDevicePrice);
        dest.writeString(newDevicePriceFmt);
        dest.writeInt(oldDevicePrice);
        dest.writeString(oldDevicePriceFmt);
        dest.writeByte((byte) (dropOffEnable ? 1 : 0));
        dest.writeString(deviceModel);
        dest.writeString(diagnosticId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TradeInInfoData> CREATOR = new Creator<TradeInInfoData>() {
        @Override
        public TradeInInfoData createFromParcel(Parcel in) {
            return new TradeInInfoData(in);
        }

        @Override
        public TradeInInfoData[] newArray(int size) {
            return new TradeInInfoData[size];
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

    public boolean isDropOffEnable() {
        return dropOffEnable;
    }

    public void setDropOffEnable(boolean dropOffEnable) {
        this.dropOffEnable = dropOffEnable;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDiagnosticId() {
        return diagnosticId;
    }

    public void setDiagnosticId(String diagnosticId) {
        this.diagnosticId = diagnosticId;
    }
}
