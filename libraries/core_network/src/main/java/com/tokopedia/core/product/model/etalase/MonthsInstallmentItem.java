package com.tokopedia.core.product.model.etalase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alifa on 5/17/17.
 */

@Deprecated
public class MonthsInstallmentItem implements Parcelable {

    private String value;
    private String info;
    private String imageUrl;

    public MonthsInstallmentItem(String value, String info, String imageUrl) {
        this.value = value;
        this.info = info;
        this.imageUrl = imageUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    protected MonthsInstallmentItem(Parcel in) {
        value = in.readString();
        info = in.readString();
        imageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeString(info);
        dest.writeString(imageUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MonthsInstallmentItem> CREATOR
            = new Parcelable.Creator<MonthsInstallmentItem>() {

        @Override
        public MonthsInstallmentItem createFromParcel(Parcel in) {
            return new MonthsInstallmentItem(in);
        }

        @Override
        public MonthsInstallmentItem[] newArray(int size) {
            return new MonthsInstallmentItem[size];
        }
    };
}
