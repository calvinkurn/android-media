package com.tokopedia.core.var;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * @author Kulomady on 12/2/16.
 */
@Parcel
public class Badge extends RecyclerViewItem {

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

}
