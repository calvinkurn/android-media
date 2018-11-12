
package com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Guide {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("header")
    @Expose
    private String header = "";
    @SerializedName("more_text")
    @Expose
    private String moreText = "";
    @SerializedName("image_url")
    @Expose
    private String imageUrl = "";
    @SerializedName("image_description")
    @Expose
    private String imageDescription = "";
    @SerializedName("cta")
    @Expose
    private String cta = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMoreText() {
        return moreText;
    }

    public void setMoreText(String moreText) {
        this.moreText = moreText;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

}
