package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LabelItemViewModel implements Parcelable {
    private String color;
    private String title;

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.color);
        dest.writeString(this.title);
    }

    public LabelItemViewModel() {
    }

    protected LabelItemViewModel(Parcel in) {
        this.color = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<LabelItemViewModel> CREATOR = new Parcelable.Creator<LabelItemViewModel>() {
        @Override
        public LabelItemViewModel createFromParcel(Parcel source) {
            return new LabelItemViewModel(source);
        }

        @Override
        public LabelItemViewModel[] newArray(int size) {
            return new LabelItemViewModel[size];
        }
    };
}