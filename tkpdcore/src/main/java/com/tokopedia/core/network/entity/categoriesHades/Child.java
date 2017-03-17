
package com.tokopedia.core.network.entity.categoriesHades;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("is_revamp")
    @Expose
    private Boolean isRevamp;
    @SerializedName("hidden")
    @Expose
    private Integer hidden;
    @SerializedName("View")
    @Expose
    private Integer view;
    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnailImage;

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

    public Boolean getRevamp() {
        return isRevamp;
    }

    public void setRevamp(Boolean revamp) {
        isRevamp = revamp;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }


    protected Child(Parcel in) {
        id = in.readString();
        name = in.readString();
        url = in.readString();
        byte isRevampVal = in.readByte();
        isRevamp = isRevampVal == 0x02 ? null : isRevampVal != 0x00;
        hidden = in.readByte() == 0x00 ? null : in.readInt();
        view = in.readByte() == 0x00 ? null : in.readInt();
        thumbnailImage = in.readString();
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
        if (isRevamp == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isRevamp ? 0x01 : 0x00));
        }
        if (hidden == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(hidden);
        }
        if (view == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(view);
        }
        dest.writeString(thumbnailImage);
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