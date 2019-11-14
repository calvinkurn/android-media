package com.tokopedia.purchase_platform.features.cart.domain.model.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 02/03/18.
 */

public class CartTickerErrorData implements Parcelable {

    private String errorInfo;
    private String actionInfo;
    private int errorCount;

    private CartTickerErrorData(Builder builder) {
        errorInfo = builder.errorInfo;
        actionInfo = builder.actionInfo;
        errorCount = builder.errorCount;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public void setActionInfo(String actionInfo) {
        this.actionInfo = actionInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public String getActionInfo() {
        return actionInfo;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.errorInfo);
        dest.writeString(this.actionInfo);
        dest.writeInt(this.errorCount);
    }

    public CartTickerErrorData() {
    }

    protected CartTickerErrorData(Parcel in) {
        this.errorInfo = in.readString();
        this.actionInfo = in.readString();
        this.errorCount = in.readInt();
    }

    public static final Creator<CartTickerErrorData> CREATOR = new Creator<CartTickerErrorData>() {
        @Override
        public CartTickerErrorData createFromParcel(Parcel source) {
            return new CartTickerErrorData(source);
        }

        @Override
        public CartTickerErrorData[] newArray(int size) {
            return new CartTickerErrorData[size];
        }
    };

    public static final class Builder {
        private String errorInfo;
        private String actionInfo;
        private int errorCount;

        public Builder() {
        }

        public Builder errorInfo(String val) {
            errorInfo = val;
            return this;
        }

        public Builder actionInfo(String val) {
            actionInfo = val;
            return this;
        }

        public Builder errorCount(int val) {
            errorCount = val;
            return this;
        }

        public CartTickerErrorData build() {
            return new CartTickerErrorData(this);
        }
    }
}
