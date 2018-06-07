package com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;

import java.util.List;

public class DealsDetailsDomain {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("brand_id")
    @Expose
    private int brandId;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("provider_id")
    @Expose
    private int providerId;
    @SerializedName("provider_product_id")
    @Expose
    private String providerProductId;
    @SerializedName("provider_product_name")
    @Expose
    private String providerProductName;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("url")
    @Expose
    private String url;
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
    private int mrp;
    @SerializedName("sales_price")
    @Expose
    private int salesPrice;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("sold_quantity")
    @Expose
    private int soldQuantity;
    @SerializedName("sell_rate")
    @Expose
    private int sellRate;
    @SerializedName("thumbs_up")
    @Expose
    private int thumbsUp;
    @SerializedName("thumbs_down")
    @Expose
    private int thumbsDown;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("min_start_date")
    @Expose
    private int minStartDate;
    @SerializedName("max_end_date")
    @Expose
    private int maxEndDate;
    @SerializedName("sale_start_date")
    @Expose
    private int saleStartDate;
    @SerializedName("sale_end_date")
    @Expose
    private int saleEndDate;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("min_start_time")
    @Expose
    private String minStartTime;
    @SerializedName("max_end_time")
    @Expose
    private String maxEndTime;
    @SerializedName("sale_start_time")
    @Expose
    private String saleStartTime;
    @SerializedName("sale_end_time")
    @Expose
    private String saleEndTime;
    @SerializedName("date_range")
    @Expose
    private Boolean dateRange;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("schedules")
    @Expose
    private Object schedules;
    @SerializedName("forms")
    @Expose
    private Object forms;
    @SerializedName("outlets")
    @Expose
    private List<Outlet> outlets = null;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("likes")
    @Expose
    private int likes;
    @SerializedName("catalog")
    @Expose
    private Catalog catalog;
    @SerializedName("saving_percentage")
    @Expose
    private String savingPercentage;
    @SerializedName("brand")
    @Expose
    private BrandDomain brand;
    @SerializedName("recommendation_url")
    @Expose
    private String recommendationUrl;
    @SerializedName("media")
    @Expose
    private List<MediaDomain> media;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
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

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public int getSellRate() {
        return sellRate;
    }

    public void setSellRate(int sellRate) {
        this.sellRate = sellRate;
    }

    public int getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(int thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public int getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(int thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMinStartDate() {
        return minStartDate;
    }

    public void setMinStartDate(int minStartDate) {
        this.minStartDate = minStartDate;
    }

    public int getMaxEndDate() {
        return maxEndDate;
    }

    public void setMaxEndDate(int maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public int getSaleStartDate() {
        return saleStartDate;
    }

    public void setSaleStartDate(int saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    public int getSaleEndDate() {
        return saleEndDate;
    }

    public void setSaleEndDate(int saleEndDate) {
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

    public String getMinStartTime() {
        return minStartTime;
    }

    public void setMinStartTime(String minStartTime) {
        this.minStartTime = minStartTime;
    }

    public String getMaxEndTime() {
        return maxEndTime;
    }

    public void setMaxEndTime(String maxEndTime) {
        this.maxEndTime = maxEndTime;
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

    public List<Outlet> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<Outlet> outlets) {
        this.outlets = outlets;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public String getSavingPercentage() {
        return savingPercentage;
    }

    public void setSavingPercentage(String savingPercentage) {
        this.savingPercentage = savingPercentage;
    }

    public BrandDomain getBrand() {
        return brand;
    }

    public void setBrand(BrandDomain brand) {
        this.brand = brand;
    }

    public String getRecommendationUrl() {
        return recommendationUrl;
    }

    public void setRecommendationUrl(String recommendationUrl) {
        this.recommendationUrl = recommendationUrl;
    }

    public List<MediaDomain> getMedia() {
        return media;
    }

    public void setMedia(List<MediaDomain> media) {
        this.media = media;
    }


}


