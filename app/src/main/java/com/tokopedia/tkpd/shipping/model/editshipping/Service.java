package com.tokopedia.tkpd.shipping.model.editshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kris on 2/24/2016.
 */
public class Service implements Parcelable{
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("active")
    @Expose
    public String active;

    protected Service(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        active = in.readString();
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public void setActive(String active){
        this.active = active;
    }

    public boolean getActive(){
        return this.active.equals("1");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(active);
    }
}
