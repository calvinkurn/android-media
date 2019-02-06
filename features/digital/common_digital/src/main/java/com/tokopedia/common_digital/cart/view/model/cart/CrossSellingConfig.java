package com.tokopedia.common_digital.cart.view.model.cart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrossSellingConfig implements Parcelable {
    private boolean skipAble;
    private boolean isChecked;
    private String headerTitle;
    private String bodyTitle;
    private String bodyContentBefore;
    private String bodyContentAfter;
    private String checkoutButtonText;

    public CrossSellingConfig() {
    }

    protected CrossSellingConfig(Parcel in) {
        skipAble = in.readByte() != 0;
        isChecked = in.readByte() != 0;
        headerTitle = in.readString();
        bodyTitle = in.readString();
        bodyContentBefore = in.readString();
        bodyContentAfter = in.readString();
        checkoutButtonText = in.readString();
    }

    public static final Creator<CrossSellingConfig> CREATOR = new Creator<CrossSellingConfig>() {
        @Override
        public CrossSellingConfig createFromParcel(Parcel in) {
            return new CrossSellingConfig(in);
        }

        @Override
        public CrossSellingConfig[] newArray(int size) {
            return new CrossSellingConfig[size];
        }
    };

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getBodyTitle() {
        return bodyTitle;
    }

    public String getBodyContentBefore() {
        return bodyContentBefore;
    }

    public String getBodyContentAfter() {
        return bodyContentAfter;
    }

    public String getCheckoutButtonText() {
        return checkoutButtonText;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public void setBodyTitle(String bodyTitle) {
        this.bodyTitle = bodyTitle;
    }

    public void setBodyContentBefore(String bodyContentBefore) {
        this.bodyContentBefore = bodyContentBefore;
    }

    public void setBodyContentAfter(String bodyContentAfter) {
        this.bodyContentAfter = bodyContentAfter;
    }

    public void setCheckoutButtonText(String checkoutButtonText) {
        this.checkoutButtonText = checkoutButtonText;
    }

    public boolean isSkipAble() {
        return skipAble;
    }

    public void setSkipAble(boolean skipAble) {
        this.skipAble = skipAble;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (skipAble ? 1 : 0));
        parcel.writeByte((byte) (isChecked ? 1 : 0));
        parcel.writeString(headerTitle);
        parcel.writeString(bodyTitle);
        parcel.writeString(bodyContentBefore);
        parcel.writeString(bodyContentAfter);
        parcel.writeString(checkoutButtonText);
    }
}
