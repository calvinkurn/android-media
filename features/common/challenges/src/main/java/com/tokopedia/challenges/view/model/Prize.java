
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prize implements Parcelable{

    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Prize")
    @Expose
    private String prize;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Index")
    @Expose
    private int index;

    protected Prize(Parcel in) {
        type = in.readString();
        title = in.readString();
        prize = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            index = 0;
        } else {
            index = in.readInt();
        }
    }

    public static final Creator<Prize> CREATOR = new Creator<Prize>() {
        @Override
        public Prize createFromParcel(Parcel in) {
            return new Prize(in);
        }

        @Override
        public Prize[] newArray(int size) {
            return new Prize[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(title);
        parcel.writeString(prize);
        parcel.writeString(description);
        if (index == 0) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(index);
        }
    }
}
