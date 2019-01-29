
package com.tokopedia.home.beranda.domain.gql.feed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("applink")
    @Expose
    private String applink;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("is_topads")
    @Expose
    private Boolean isTopads;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("badges")
    @Expose
    private List<Object> badges = null;
    @SerializedName("labels")
    @Expose
    private List<Object> labels = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsTopads() {
        return isTopads;
    }

    public void setIsTopads(Boolean isTopads) {
        this.isTopads = isTopads;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<Object> getBadges() {
        return badges;
    }

    public void setBadges(List<Object> badges) {
        this.badges = badges;
    }

    public List<Object> getLabels() {
        return labels;
    }

    public void setLabels(List<Object> labels) {
        this.labels = labels;
    }

}
