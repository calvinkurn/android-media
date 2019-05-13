package com.tokopedia.discovery.common.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class DynamicFilterModel implements Parcelable {

    @SerializedName("process_time")
    @Expose
    String processTime;
    @SerializedName("data")
    @Expose
    DataValue data = new DataValue();
    @SerializedName("status")
    @Expose
    String status;

    public DynamicFilterModel() {
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public DataValue getData() {
        return data;
    }

    public void setData(DataValue data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.processTime);
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.status);
    }

    protected DynamicFilterModel(Parcel in) {
        this.processTime = in.readString();
        this.data = in.readParcelable(DataValue.class.getClassLoader());
        this.status = in.readString();
    }

    public static final Creator<DynamicFilterModel> CREATOR = new Creator<DynamicFilterModel>() {
        @Override
        public DynamicFilterModel createFromParcel(Parcel source) {
            return new DynamicFilterModel(source);
        }

        @Override
        public DynamicFilterModel[] newArray(int size) {
            return new DynamicFilterModel[size];
        }
    };
}
