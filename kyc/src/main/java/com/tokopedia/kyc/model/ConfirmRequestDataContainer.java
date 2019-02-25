package com.tokopedia.kyc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ConfirmRequestDataContainer implements Parcelable {
    private int kycReqId;

    public ConfirmRequestDataContainer(){}
    protected ConfirmRequestDataContainer(Parcel in) {
        kycReqId = in.readInt();
    }

    public static final Creator<ConfirmRequestDataContainer> CREATOR = new Creator<ConfirmRequestDataContainer>() {
        @Override
        public ConfirmRequestDataContainer createFromParcel(Parcel in) {
            return new ConfirmRequestDataContainer(in);
        }

        @Override
        public ConfirmRequestDataContainer[] newArray(int size) {
            return new ConfirmRequestDataContainer[size];
        }
    };

    public int getKycReqId() {
        return kycReqId;
    }

    public void setKycReqId(int kycReqId) {
        this.kycReqId = kycReqId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(kycReqId);
    }
}
