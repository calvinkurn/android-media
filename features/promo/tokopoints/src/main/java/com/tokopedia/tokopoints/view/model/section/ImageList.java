
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageList {

    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    @SerializedName("imageURLMobile")
    @Expose
    private String imageURLMobile;
    @SerializedName("redirectURL")
    @Expose
    private String redirectURL;
    @SerializedName("redirectAppLink")
    @Expose
    private String redirectAppLink;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subTitle")
    @Expose
    private String subTitle;
    @SerializedName("inBannerTitle")
    @Expose
    private String inBannerTitle;
    @SerializedName("inBannerSubTitle")
    @Expose
    private String inBannerSubTitle;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURLMobile() {
        return imageURLMobile;
    }

    public void setImageURLMobile(String imageURLMobile) {
        this.imageURLMobile = imageURLMobile;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getRedirectAppLink() {
        return redirectAppLink;
    }

    public void setRedirectAppLink(String redirectAppLink) {
        this.redirectAppLink = redirectAppLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getInBannerTitle() {
        return inBannerTitle;
    }

    public void setInBannerTitle(String inBannerTitle) {
        this.inBannerTitle = inBannerTitle;
    }

    public String getInBannerSubTitle() {
        return inBannerSubTitle;
    }

    public void setInBannerSubTitle(String inBannerSubTitle) {
        this.inBannerSubTitle = inBannerSubTitle;
    }

}
