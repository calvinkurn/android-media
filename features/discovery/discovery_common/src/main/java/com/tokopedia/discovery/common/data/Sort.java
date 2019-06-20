package com.tokopedia.discovery.common.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final class Sort implements Parcelable {

    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("key")
    @Expose
    String key;
    @SerializedName("value")
    @Expose
    String value;
    @SerializedName("input_type")
    @Expose
    String inputType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.key);
        dest.writeString(this.value);
        dest.writeString(this.inputType);
    }

    public Sort() {
    }

    protected Sort(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
        this.value = in.readString();
        this.inputType = in.readString();
    }

    public static final Creator<Sort> CREATOR = new Creator<Sort>() {
        @Override
        public Sort createFromParcel(Parcel source) {
            return new Sort(source);
        }

        @Override
        public Sort[] newArray(int size) {
            return new Sort[size];
        }
    };
}
