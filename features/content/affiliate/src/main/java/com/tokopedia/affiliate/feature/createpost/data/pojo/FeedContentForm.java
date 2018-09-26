
package com.tokopedia.affiliate.feature.createpost.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedContentForm {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("components")
    @Expose
    private List<Component> components = null;
    @SerializedName("authors")
    @Expose
    private List<Object> authors = null;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("media")
    @Expose
    private Media media;
    @SerializedName("guides")
    @Expose
    private List<Guide> guides = null;
    @SerializedName("campaigns")
    @Expose
    private List<Object> campaigns = null;
    @SerializedName("error")
    @Expose
    private String error;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public List<Object> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Object> authors) {
        this.authors = authors;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public List<Guide> getGuides() {
        return guides;
    }

    public void setGuides(List<Guide> guides) {
        this.guides = guides;
    }

    public List<Object> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Object> campaigns) {
        this.campaigns = campaigns;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
