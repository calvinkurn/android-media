package com.tokopedia.tkpd.beranda.domain.model.category;

import com.google.gson.annotations.SerializedName;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class CategoryLayoutRowModel {
    private static final String MARKETPLACE = "Marketplace";
    private static final String DIGITAL = "Digital";

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("Weight")
    private int Weight;
    @SerializedName("type")
    private String type;
    @SerializedName("additional_info")
    private String additionalInfo;
    @SerializedName("category_id")
    private String categoryId;
    @SerializedName("applinks")
    private String applinks;
    private String redirectValue; // DepId or redirect url if category type is Digital

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int Weight) {
        this.Weight = Weight;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getRedirectValue() {
        return redirectValue;
    }

    public void setRedirectValue(String redirectValue) {
        this.redirectValue = redirectValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        if(isCategoryItemMarketPlace()){
            setRedirectValue(getCategoryId());
        } else if(isCategoryItemDigital()){
            setRedirectValue(getUrl());
        } else {
            setRedirectValue(getUrl());
        }
    }

    private boolean isCategoryItemMarketPlace() {
        return MARKETPLACE.equalsIgnoreCase(type);
    }

    private boolean isCategoryItemDigital() {
        return DIGITAL.equalsIgnoreCase(type);
    }
}
