package com.tokopedia.events.domain.model.scanticket;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule implements Parcelable {

    @SerializedName("show_data")
    @Expose
    private String showData;
    @SerializedName("category_label")
    @Expose
    private String categoryLabel;
    @SerializedName("name")
    @Expose
    private String name;

    protected Schedule(Parcel in) {
        showData = in.readString();
        categoryLabel = in.readString();
        name = in.readString();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    public String getShowData() {
        return showData;
    }

    public void setShowData(String showData) {
        this.showData = showData;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(showData);
        parcel.writeString(categoryLabel);
        parcel.writeString(name);
    }
}