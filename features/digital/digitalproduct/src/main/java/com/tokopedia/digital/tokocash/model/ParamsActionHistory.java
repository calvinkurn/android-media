package com.tokopedia.digital.tokocash.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 8/23/17.
 */

public class ParamsActionHistory implements Parcelable {

    private String refundId;

    private String refundType;

    public ParamsActionHistory() {
    }

    protected ParamsActionHistory(Parcel in) {
        refundId = in.readString();
        refundType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(refundId);
        dest.writeString(refundType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParamsActionHistory> CREATOR = new Creator<ParamsActionHistory>() {
        @Override
        public ParamsActionHistory createFromParcel(Parcel in) {
            return new ParamsActionHistory(in);
        }

        @Override
        public ParamsActionHistory[] newArray(int size) {
            return new ParamsActionHistory[size];
        }
    };

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }
}
