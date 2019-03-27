
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryTokopointsList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("iconImageURL")
    @Expose
    private String iconImageURL;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("appLink")
    @Expose
    private String appLink;
    @SerializedName("isNewCategory")
    @Expose
    private Boolean isNewCategory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIconImageURL() {
        return iconImageURL;
    }

    public void setIconImageURL(String iconImageURL) {
        this.iconImageURL = iconImageURL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public Boolean getIsNewCategory() {
        return isNewCategory;
    }

    public void setIsNewCategory(Boolean isNewCategory) {
        this.isNewCategory = isNewCategory;
    }

}
