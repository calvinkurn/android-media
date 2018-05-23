package com.tokopedia.digital.widget.view.model.status;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class Status implements Parcelable {

    private String type;
    private boolean isMaintenance;
    private int minimunAndroidBuild;

    public Status() {
    }

    protected Status(Parcel in) {
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    public boolean isMaintenance() {
        return isMaintenance;
    }

    public String getType() {
        return type;
    }

    public int getMinimunAndroidBuild() {
        return minimunAndroidBuild;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMaintenance(boolean maintenance) {
        isMaintenance = maintenance;
    }

    public void setMinimunAndroidBuild(int minimunAndroidBuild) {
        this.minimunAndroidBuild = minimunAndroidBuild;
    }
}
