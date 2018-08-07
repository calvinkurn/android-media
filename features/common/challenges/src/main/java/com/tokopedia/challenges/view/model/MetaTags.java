
package com.tokopedia.challenges.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaTags {

    @SerializedName("og:title")
    @Expose
    private String ogTitle;
    @SerializedName("og:description")
    @Expose
    private String ogDescription;
    @SerializedName("og:image")
    @Expose
    private String ogImage;

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getOgImage() {
        return ogImage;
    }

    public void setOgImage(String ogImage) {
        this.ogImage = ogImage;
    }

}
