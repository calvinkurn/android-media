package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 18/12/18.
 */
public class OverlayMessageAssetPojo {

    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("image_link")
    private String imageLink;
    @SerializedName("btn_title")
    private String btnTitle;
    @SerializedName("btn_link")
    private String btnLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getBtnTitle() {
        return btnTitle;
    }

    public void setBtnTitle(String btnTitle) {
        this.btnTitle = btnTitle;
    }

    public String getBtnLink() {
        return btnLink;
    }

    public void setBtnLink(String btnLink) {
        this.btnLink = btnLink;
    }
}
