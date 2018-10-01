
package com.tokopedia.affiliate.feature.createpost.data.pojo.submitform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medium {

    @SerializedName("mediaURL")
    @Expose
    private String mediaURL;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("order")
    @Expose
    private int order;

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
