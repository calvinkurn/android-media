
package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewImageUrl {

    @SerializedName("attachment_id")
    @Expose
    private int attachmentId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("uri_thumbnail")
    @Expose
    private String uriThumbnail;
    @SerializedName("uri_large")
    @Expose
    private String uriLarge;

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUriThumbnail() {
        return uriThumbnail;
    }

    public void setUriThumbnail(String uriThumbnail) {
        this.uriThumbnail = uriThumbnail;
    }

    public String getUriLarge() {
        return uriLarge;
    }

    public void setUriLarge(String uriLarge) {
        this.uriLarge = uriLarge;
    }

}
