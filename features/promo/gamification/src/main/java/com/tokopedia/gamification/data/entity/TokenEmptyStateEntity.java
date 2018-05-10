package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 4/9/18.
 */

public class TokenEmptyStateEntity {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("buttonText")
    @Expose
    private String buttonText;

    @SerializedName("buttonApplink")
    @Expose
    private String buttonApplink;

    @SerializedName("buttonURL")
    @Expose
    private String buttonURL;

    @SerializedName("backgroundImgUrl")
    @Expose
    private String backgroundImgUrl;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("version")
    @Expose
    private Integer version;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setButtonApplink(String buttonApplink) {
        this.buttonApplink = buttonApplink;
    }

    public void setButtonURL(String buttonURL) {
        this.buttonURL = buttonURL;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTitle() {
        return this.title;
    }

    public String getButtonText() {
        return this.buttonText;
    }

    public String getButtonApplink() {
        return this.buttonApplink;
    }

    public String getButtonURL() {
        return this.buttonURL;
    }

    public String getBackgroundImgUrl() {
        return this.backgroundImgUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Integer getVersion() {
        return this.version;
    }
}
