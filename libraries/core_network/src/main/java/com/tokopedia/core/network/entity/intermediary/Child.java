package com.tokopedia.core.network.entity.intermediary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Child implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnailImage;
    @SerializedName("hidden")
    @Expose
    private Integer hidden;
    @SerializedName("is_revamp")
    @Expose
    private Boolean isRevamp;
    @SerializedName("is_intermediary")
    @Expose
    private Boolean isIntermediary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Boolean getIsRevamp() {
        return isRevamp;
    }

    public void setIsRevamp(Boolean isRevamp) {
        this.isRevamp = isRevamp;
    }

    public Boolean getIsIntermediary() {
        return isIntermediary;
    }

    public void setIsIntermediary(Boolean isIntermediary) {
        this.isIntermediary = isIntermediary;
    }


    protected Child(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        thumbnailImage = in.readString();
        hidden = in.readByte() == 0x00 ? null : in.readInt();
        byte isRevampVal = in.readByte();
        isRevamp = isRevampVal == 0x02 ? null : isRevampVal != 0x00;
        byte isIntermediaryVal = in.readByte();
        isIntermediary = isIntermediaryVal == 0x02 ? null : isIntermediaryVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(thumbnailImage);
        if (hidden == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(hidden);
        }
        if (isRevamp == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isRevamp ? 0x01 : 0x00));
        }
        if (isIntermediary == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isIntermediary ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Child> CREATOR = new Parcelable.Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };
}