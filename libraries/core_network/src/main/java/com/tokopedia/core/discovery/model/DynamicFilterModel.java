package com.tokopedia.core.discovery.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class DynamicFilterModel implements Parcelable {

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

    /**
     * @return The processTime
     */
    public String getProcessTime() {
        return processTime;
    }

    /**
     * @param processTime The process_time
     */
    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    /**
     * @return The data
     */
    public DataValue getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(DataValue data) {
        this.data = data;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * use this for listener
     */
    public static final class DynamicFilterContainer implements ObjContainer<DynamicFilterModel>, Parcelable {

        DynamicFilterModel dynamicFilterModel;

        public DynamicFilterContainer(DynamicFilterModel dynamicFilterModel) {
            this.dynamicFilterModel = dynamicFilterModel;
        }

        @Override
        public DynamicFilterModel body() {
            return dynamicFilterModel;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.dynamicFilterModel, flags);
        }

        protected DynamicFilterContainer(Parcel in) {
            this.dynamicFilterModel = in.readParcelable(DynamicFilterModel.class.getClassLoader());
        }

        public static final Parcelable.Creator<DynamicFilterContainer> CREATOR = new Parcelable.Creator<DynamicFilterContainer>() {
            @Override
            public DynamicFilterContainer createFromParcel(Parcel source) {
                return new DynamicFilterContainer(source);
            }

            @Override
            public DynamicFilterContainer[] newArray(int size) {
                return new DynamicFilterContainer[size];
            }
        };
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
