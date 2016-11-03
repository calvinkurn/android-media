package com.tokopedia.tkpd.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * ProductPreorder
 * Created by Angga.Prasetiyo on 05/07/2016.
 */
public class ProductPreorder implements Parcelable {
    @SerializedName("preorder_status")
    @Expose
    private Integer preorderStatus;
    @SerializedName("preorder_process_time_type")
    @Expose
    private Integer preorderProcessTimeType;
    @SerializedName("preorder_process_time_type_string")
    @Expose
    private String preorderProcessTimeTypeString;
    @SerializedName("preorder_process_time")
    @Expose
    private String preorderProcessTime;

    public Integer getPreorderStatus() {
        return preorderStatus;
    }

    public void setPreorderStatus(Integer preorderStatus) {
        this.preorderStatus = preorderStatus;
    }

    public Integer getPreorderProcessTimeType() {
        return preorderProcessTimeType;
    }

    public void setPreorderProcessTimeType(Integer preorderProcessTimeType) {
        this.preorderProcessTimeType = preorderProcessTimeType;
    }

    public String getPreorderProcessTimeTypeString() {
        return preorderProcessTimeTypeString;
    }

    public void setPreorderProcessTimeTypeString(String preorderProcessTimeTypeString) {
        this.preorderProcessTimeTypeString = preorderProcessTimeTypeString;
    }

    public String getPreorderProcessTime() {
        return preorderProcessTime;
    }

    public void setPreorderProcessTime(String preorderProcessTime) {
        this.preorderProcessTime = preorderProcessTime;
    }

    protected ProductPreorder(Parcel in) {
        preorderStatus = in.readByte() == 0x00 ? null : in.readInt();
        preorderProcessTimeType = in.readByte() == 0x00 ? null : in.readInt();
        preorderProcessTimeTypeString = in.readString();
        preorderProcessTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (preorderStatus == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(preorderStatus);
        }
        if (preorderProcessTimeType == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(preorderProcessTimeType);
        }
        dest.writeString(preorderProcessTimeTypeString);
        dest.writeString(preorderProcessTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProductPreorder> CREATOR = new Parcelable.Creator<ProductPreorder>() {
        @Override
        public ProductPreorder createFromParcel(Parcel in) {
            return new ProductPreorder(in);
        }

        @Override
        public ProductPreorder[] newArray(int size) {
            return new ProductPreorder[size];
        }
    };
}
