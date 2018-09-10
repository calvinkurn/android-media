package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collection implements Parcelable {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("ThumbnailImage")
    @Expose
    private String thumbnailImage;
    @SerializedName("HashTag")
    @Expose
    private String hashTag;
    @SerializedName("Me")
    @Expose
    private Me me;
    public final static Parcelable.Creator<Collection> CREATOR = new Creator<Collection>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        public Collection[] newArray(int size) {
            return (new Collection[size]);
        }

    };

    protected Collection(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.endDate = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailImage = ((String) in.readValue((String.class.getClassLoader())));
        this.hashTag = ((String) in.readValue((String.class.getClassLoader())));
        this.me = ((Me) in.readValue((Me.class.getClassLoader())));
    }

    public Collection() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public Me getMe() {
        return me;
    }

    public void setMe(Me me) {
        this.me = me;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(type);
        dest.writeValue(endDate);
        dest.writeValue(thumbnailImage);
        dest.writeValue(hashTag);
        dest.writeValue(me);
    }

    public int describeContents() {
        return 0;
    }

}


