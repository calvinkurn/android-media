package com.tokopedia.challenges.view.model.challengesubmission;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Awards implements Parcelable{
    @SerializedName("Type")
    @Expose
    private String type;

    protected Awards(Parcel in) {
        type = in.readString();
    }

    public static final Creator<Awards> CREATOR = new Creator<Awards>() {
        @Override
        public Awards createFromParcel(Parcel in) {
            return new Awards(in);
        }

        @Override
        public Awards[] newArray(int size) {
            return new Awards[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
    }
}
