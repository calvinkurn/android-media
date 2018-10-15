package com.tokopedia.affiliatecommon.data.pojo.submitpost.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitPostMedium {
    private static final String TYPE_IMAGE = "image";

    @SerializedName("mediaURL")
    @Expose
    private String mediaURL;
    @SerializedName("type")
    @Expose
    private String type = TYPE_IMAGE;
    @SerializedName("order")
    @Expose
    private int order;

    public SubmitPostMedium(String mediaURL, int order) {
        this.mediaURL = mediaURL;
        this.order = order;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
