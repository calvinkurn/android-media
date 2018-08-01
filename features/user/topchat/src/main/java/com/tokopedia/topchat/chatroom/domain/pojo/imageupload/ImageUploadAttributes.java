package com.tokopedia.topchat.chatroom.domain.pojo.imageupload;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageUploadAttributes {

    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_url_thumbnail")
    @Expose
    private String thumbnail;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
