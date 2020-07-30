package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ValuesItem implements Parcelable {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("priority")
    private int priority;

    protected ValuesItem(Parcel in) {
        name = in.readString();
        id = in.readInt();
        priority = in.readInt();
    }

    public static final Creator<ValuesItem> CREATOR = new Creator<ValuesItem>() {
        @Override
        public ValuesItem createFromParcel(Parcel in) {
            return new ValuesItem(in);
        }

        @Override
        public ValuesItem[] newArray(int size) {
            return new ValuesItem[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return
                "ValuesItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",priority = '" + priority + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        dest.writeInt(priority);
    }
}