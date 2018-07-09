package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by StevenFredian on 03/05/18.
 */

public class PinnedMessagePojo {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("title")
    @Expose
    private String title;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
