
package com.tokopedia.core.network.entity.home.recentView;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Deprecated
public class Label implements Parcelable {

    @SerializedName("color")
    private String mColor;
    @SerializedName("title")
    private String mTitle;

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mColor);
        dest.writeString(this.mTitle);
    }

    public Label() {
    }

    protected Label(Parcel in) {
        this.mColor = in.readString();
        this.mTitle = in.readString();
    }

    public static final Parcelable.Creator<Label> CREATOR = new Parcelable.Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel source) {
            return new Label(source);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };
}
