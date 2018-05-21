
package com.tokopedia.digital_deals.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;

public class DealsCategoryItemDomain {

    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("seo_url")
    @Expose
    private String seoUrl;
    @SerializedName("image_web")
    @Expose
    private String imageWeb;
    @SerializedName("thumbnail_web")
    @Expose
    private String thumbnailWeb;
    @SerializedName("long_rich_desc")
    @Expose
    private String longRichDesc;
    @SerializedName("mrp")
    @Expose
    private Integer mrp;
    @SerializedName("sales_price")
    @Expose
    private Integer salesPrice;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("sold_quantity")
    @Expose
    private Integer soldQuantity;
    @SerializedName("sell_rate")
    @Expose
    private Integer sellRate;
    @SerializedName("thumbs_up")
    @Expose
    private Integer thumbsUp;
    @SerializedName("thumbs_down")
    @Expose
    private Integer thumbsDown;
    @SerializedName("is_featured")
    @Expose
    private Integer isFeatured;
    @SerializedName("min_start_date")
    @Expose
    private Integer minStartDate;
    @SerializedName("max_end_date")
    @Expose
    private Integer maxEndDate;
    @SerializedName("sale_start_date")
    @Expose
    private Integer saleStartDate;
    @SerializedName("sale_end_date")
    @Expose
    private Integer saleEndDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("date_range")
    @Expose
    private Boolean dateRange;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("schedules")
    @Expose
    private Object schedules;
    @SerializedName("forms")
    @Expose
    private Object forms;
    @SerializedName("media")
    @Expose
    private Object media;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_searchable")
    @Expose
    private Integer isSearchable;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("provider_product_id")
    @Expose
    private String providerProductId;
    @SerializedName("provider_product_name")
    @Expose
    private String providerProductName;
    @SerializedName("sale_start_time")
    @Expose
    private String saleStartTime;
    @SerializedName("sale_end_time")
    @Expose
    private String saleEndTime;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("saving_percentage")
    @Expose
    private String savingPercentage;
    @SerializedName("display_tags")
    @Expose
    private String displayTags;
    @SerializedName("brand")
    @Expose
    private BrandDomain brand;


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeoUrl() {
        return seoUrl;
    }

    public void setSeoUrl(String seoUrl) {
        this.seoUrl = seoUrl;
    }

    public String getImageWeb() {
        return imageWeb;
    }

    public void setImageWeb(String imageWeb) {
        this.imageWeb = imageWeb;
    }

    public String getThumbnailWeb() {
        return thumbnailWeb;
    }

    public void setThumbnailWeb(String thumbnailWeb) {
        this.thumbnailWeb = thumbnailWeb;
    }

    public String getLongRichDesc() {
        return longRichDesc;
    }

    public void setLongRichDesc(String longRichDesc) {
        this.longRichDesc = longRichDesc;
    }

    public Integer getMrp() {
        return mrp;
    }

    public void setMrp(Integer mrp) {
        this.mrp = mrp;
    }

    public Integer getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Integer salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Integer soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Integer getSellRate() {
        return sellRate;
    }

    public void setSellRate(Integer sellRate) {
        this.sellRate = sellRate;
    }

    public Integer getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(Integer thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public Integer getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(Integer thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public Integer getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Integer isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getMinStartDate() {
        return minStartDate;
    }

    public void setMinStartDate(Integer minStartDate) {
        this.minStartDate = minStartDate;
    }

    public Integer getMaxEndDate() {
        return maxEndDate;
    }

    public void setMaxEndDate(Integer maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public Integer getSaleStartDate() {
        return saleStartDate;
    }

    public void setSaleStartDate(Integer saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public Integer getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(Integer saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDateRange() {
        return dateRange;
    }

    public void setDateRange(Boolean dateRange) {
        this.dateRange = dateRange;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsSearchable() {
        return isSearchable;
    }

    public void setIsSearchable(Integer isSearchable) {
        this.isSearchable = isSearchable;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getProviderProductId() {
        return providerProductId;
    }

    public void setProviderProductId(String providerProductId) {
        this.providerProductId = providerProductId;
    }

    public String getProviderProductName() {
        return providerProductName;
    }

    public void setProviderProductName(String providerProductName) {
        this.providerProductName = providerProductName;
    }

    public String getSaleStartTime() {
        return saleStartTime;
    }

    public void setSaleStartTime(String saleStartTime) {
        this.saleStartTime = saleStartTime;
    }

    public String getSaleEndTime() {
        return saleEndTime;
    }

    public void setSaleEndTime(String saleEndTime) {
        this.saleEndTime = saleEndTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getSchedules() {
        return schedules;
    }

    public void setSchedules(Object schedules) {
        this.schedules = schedules;
    }

    public Object getForms() {
        return forms;
    }

    public void setForms(Object forms) {
        this.forms = forms;
    }

    public Object getMedia() {
        return media;
    }

    public void setMedia(Object media) {
        this.media = media;
    }

    public String getSavingPercentage() {
        return savingPercentage;
    }

    public void setSavingPercentage(String savingPercentage) {
        this.savingPercentage = savingPercentage;
    }


    public String getDisplayTags() {
        return displayTags;
    }

    public void setDisplayTags(String displayTags) {
        this.displayTags = displayTags;
    }


    public BrandDomain getBrand() {
        return brand;
    }

    public void setBrand(BrandDomain brand) {
        this.brand = brand;
    }


}