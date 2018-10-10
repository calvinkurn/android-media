
package com.tokopedia.groupchat.chatroom.domain.pojo.imageannouncement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo;

public class AdminImagePojo extends BaseGroupChatPojo{

    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;
    @SerializedName("alt_text")
    @Expose
    private String altText;

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

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

}
