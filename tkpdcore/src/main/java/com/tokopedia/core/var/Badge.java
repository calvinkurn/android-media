package com.tokopedia.core.var;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Kulomady on 12/2/16.
 */
public class Badge extends RecyclerViewItem implements Parcelable {


    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("img_url")
    @Expose
    String imgUrl;

    //sometimes it's different at ws (sometimes image_url and sometimes imgurl)
    @SerializedName("image_url")
    @Expose
    String imageUrl;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The imgUrl
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * @param imgUrl The img_url
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.title);
        dest.writeString(this.imgUrl);
        dest.writeString(this.imageUrl);
    }

    public Badge() {
    }

    protected Badge(android.os.Parcel in) {
        super(in);
        this.title = in.readString();
        this.imgUrl = in.readString();
        this.imageUrl = in.readString();
    }

    public static final Creator<Badge> CREATOR = new Creator<Badge>() {
        @Override
        public Badge createFromParcel(android.os.Parcel source) {
            return new Badge(source);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };
}
