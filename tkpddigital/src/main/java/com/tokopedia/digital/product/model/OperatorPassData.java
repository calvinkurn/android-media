package com.tokopedia.digital.product.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rizky on 12/12/17.
 */

public class OperatorPassData implements Parcelable {
    private String operatorId;
    private String operatorImage;
    private String operatorName;

    public OperatorPassData(String operatorId, String operatorImage, String operatorName) {
        this.operatorId = operatorId;
        this.operatorImage = operatorImage;
        this.operatorName = operatorName;
    }

    protected OperatorPassData(Parcel in) {
        operatorId = in.readString();
        operatorImage = in.readString();
        operatorName = in.readString();
    }

    public static final Creator<OperatorPassData> CREATOR = new Creator<OperatorPassData>() {
        @Override
        public OperatorPassData createFromParcel(Parcel in) {
            return new OperatorPassData(in);
        }

        @Override
        public OperatorPassData[] newArray(int size) {
            return new OperatorPassData[size];
        }
    };

    public String getOperatorId() {
        return operatorId;
    }

    public String getOperatorImage() {
        return operatorImage;
    }

    public String getOperatorName() {
        return operatorName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(operatorId);
        parcel.writeString(operatorImage);
        parcel.writeString(operatorName);
    }
}
