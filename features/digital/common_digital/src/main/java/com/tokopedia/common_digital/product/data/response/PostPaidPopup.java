package com.tokopedia.common_digital.product.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostPaidPopup {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("actions")
    @Expose
    private ActionPostPaidPopup action;


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ActionPostPaidPopup getAction() {
        return action;
    }
}
