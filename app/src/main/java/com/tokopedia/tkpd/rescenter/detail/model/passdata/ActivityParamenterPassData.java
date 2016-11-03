package com.tokopedia.tkpd.rescenter.detail.model.passdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hangnadi on 2/9/16.
 */
public class ActivityParamenterPassData implements Parcelable {

    private static final String TAG = ActivityParamenterPassData.class.getSimpleName();

    private String resCenterId;

    public ActivityParamenterPassData() {
    }

    protected ActivityParamenterPassData(Parcel in) {
        resCenterId = in.readString();
    }

    public static final Creator<ActivityParamenterPassData> CREATOR = new Creator<ActivityParamenterPassData>() {
        @Override
        public ActivityParamenterPassData createFromParcel(Parcel in) {
            return new ActivityParamenterPassData(in);
        }

        @Override
        public ActivityParamenterPassData[] newArray(int size) {
            return new ActivityParamenterPassData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(resCenterId);
    }

    public String getResCenterId() {
        return resCenterId;
    }

    public void setResCenterId(String resCenterId) {
        this.resCenterId = resCenterId;
    }

    public static class Builder {
        private String resCenterId = "";

        public Builder() {
        }

        public static Builder aResCenterPass() {
            return new Builder();
        }

        public Builder setResCenterId(String resCenterId) {
            this.resCenterId = resCenterId;
            return this;
        }

        public Builder but() {
            return aResCenterPass().setResCenterId(resCenterId);
        }

        public ActivityParamenterPassData build() {
            ActivityParamenterPassData activityParamenterPassData = new ActivityParamenterPassData();
            activityParamenterPassData.setResCenterId(resCenterId);
            return activityParamenterPassData;
        }
    }
}
