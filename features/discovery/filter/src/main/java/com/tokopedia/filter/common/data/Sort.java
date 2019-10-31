package com.tokopedia.filter.common.data;

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
    @SerializedName(value="input_type", alternate={"inputType"})
    @Expose
    String inputType;
    @SerializedName("applyFilter")
    @Expose
    String applyFilter;

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

    public String getApplyFilter() {
        return applyFilter;
    }

    public void setApplyFilter(String applyFilter) {
        this.applyFilter = applyFilter;
    }

    @Override
    public String toString() {
        return getName();
    }

    public Sort() {
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
        dest.writeString(this.applyFilter);
    }

    protected Sort(Parcel in) {
        this.name = in.readString();
        this.key = in.readString();
        this.value = in.readString();
        this.inputType = in.readString();
        this.applyFilter = in.readString();
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
