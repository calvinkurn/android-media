package com.tokopedia.transaction.common.data.order;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class CourierServiceModel implements Parcelable {

    private String serviceId;

    private String serviceName;

    public CourierServiceModel() {
    }

    protected CourierServiceModel(Parcel in) {
        serviceId = in.readString();
        serviceName = in.readString();
    }

    public static final Creator<CourierServiceModel> CREATOR = new Creator<CourierServiceModel>() {
        @Override
        public CourierServiceModel createFromParcel(Parcel in) {
            return new CourierServiceModel(in);
        }

        @Override
        public CourierServiceModel[] newArray(int size) {
            return new CourierServiceModel[size];
        }
    };

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(serviceId);
        parcel.writeString(serviceName);
    }
}
