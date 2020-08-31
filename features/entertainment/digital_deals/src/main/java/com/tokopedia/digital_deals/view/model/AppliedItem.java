package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class AppliedItem implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("priority")
    private int priority;

    protected AppliedItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        priority = in.readInt();
    }

    public static final Creator<AppliedItem> CREATOR = new Creator<AppliedItem>() {
        @Override
        public AppliedItem createFromParcel(Parcel in) {
            return new AppliedItem(in);
        }

        @Override
        public AppliedItem[] newArray(int size) {
            return new AppliedItem[size];
        }
    };

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    @Override
    public String toString() {
        return
                "AppliedItem{" +
                        "id = '" + id + '\'' +
                        "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(priority);

    }
}