package com.tokopedia.affiliate.feature.createpost.data.pojo.submitpost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubmitPostRequest {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("authorType")
    @Expose
    private String authorType;
    @SerializedName("authorID")
    @Expose
    private String authorID;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("adID")
    @Expose
    private String adID;
    @SerializedName("productID")
    @Expose
    private String productID;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("media")
    @Expose
    private List<Medium> media = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthorType() {
        return authorType;
    }

    public void setAuthorType(String authorType) {
        this.authorType = authorType;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }

}
