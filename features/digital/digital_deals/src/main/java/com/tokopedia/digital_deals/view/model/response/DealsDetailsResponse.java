package com.tokopedia.digital_deals.view.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Catalog;
import com.tokopedia.digital_deals.view.model.Media;
import com.tokopedia.digital_deals.view.model.Outlet;

import java.util.ArrayList;
import java.util.List;

public class DealsDetailsResponse implements Parcelable {

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
    @SerializedName("min_qty")
    private int minQty;
    @SerializedName("max_qty")
    private int maxQty;
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
    private Brand brand;
    @SerializedName("recommendation_url")
    @Expose
    private String recommendationUrl;
    @SerializedName("media")
    @Expose
    private List<Media> mediaUrl;
    @SerializedName("tnc")
    @Expose
    private String tnc;
    @SerializedName("seo_url")
    private String seoUrl;
    @SerializedName("is_liked")
    private boolean isLiked;
    @SerializedName("desktop_url")
    private String desktopUrl;


    public final static Parcelable.Creator<DealsDetailsResponse> CREATOR = new Creator<DealsDetailsResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DealsDetailsResponse createFromParcel(Parcel in) {
            return new DealsDetailsResponse(in);
        }

        public DealsDetailsResponse[] newArray(int size) {
            return (new DealsDetailsResponse[size]);
        }

    };

    protected DealsDetailsResponse(Parcel in) {
        this.id = in.readInt();
        this.brandId = in.readInt();
        this.categoryId = in.readInt();
        this.providerId = in.readInt();
        this.providerProductId = in.readString();
        this.providerProductName = in.readString();
        this.displayName = in.readString();
        this.url = in.readString();
        this.imageWeb = in.readString();
        this.thumbnailWeb = in.readString();
        this.longRichDesc = in.readString();
        this.mrp = in.readInt();
        this.salesPrice = in.readInt();
        this.quantity = in.readInt();
        this.soldQuantity = in.readInt();
        this.sellRate = in.readInt();
        this.thumbsUp = in.readInt();
        this.thumbsDown = in.readInt();
        this.status = in.readInt();
        this.minStartDate = in.readInt();
        this.maxEndDate = in.readInt();
        this.saleStartDate = in.readInt();
        this.saleEndDate = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.minStartTime = in.readString();
        this.maxEndTime = in.readString();
        this.saleStartTime = in.readString();
        this.saleEndTime = in.readString();
        this.dateRange = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.cityName = in.readString();
        this.outlets = new ArrayList<Outlet>();
        in.readList(this.outlets, Outlet.class.getClassLoader());
        this.rating = in.readInt();
        this.likes = in.readInt();
        this.savingPercentage = in.readString();
        this.catalog = ((Catalog) in.readValue((Catalog.class.getClassLoader())));
        this.brand = ((Brand) in.readValue((Brand.class.getClassLoader())));
        this.recommendationUrl = in.readString();
        this.mediaUrl = new ArrayList<>();
        in.readList(this.mediaUrl, Media.class.getClassLoader());
        this.isLiked = in.readValue((Boolean.class.getClassLoader())) != null;
        this.tnc = in.readString();
        this.seoUrl = in.readString();
        this.desktopUrl = in.readString();
    }

    public DealsDetailsResponse() {
    }

    public String getDesktopUrl() {
        return desktopUrl;
    }

    public void setDesktopUrl(String desktopUrl) {
        this.desktopUrl = desktopUrl;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
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

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
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

    public String getSavingPercentage() {
        return savingPercentage;
    }

    public void setSavingPercentage(String savingPercentage) {
        this.savingPercentage = savingPercentage;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }


    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecommendationUrl() {
        return recommendationUrl;
    }

    public void setRecommendationUrl(String recommendationUrl) {
        this.recommendationUrl = recommendationUrl;
    }

    public List<Media> getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(List<Media> mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean liked) {
        isLiked = liked;
    }


    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getSeoUrl() {
        return seoUrl;
    }

    public void setSeoUrl(String seoUrl) {
        this.seoUrl = seoUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(brandId);
        dest.writeInt(categoryId);
        dest.writeInt(providerId);
        dest.writeString(providerProductId);
        dest.writeString(providerProductName);
        dest.writeString(displayName);
        dest.writeString(url);
        dest.writeString(imageWeb);
        dest.writeString(thumbnailWeb);
        dest.writeString(longRichDesc);
        dest.writeInt(mrp);
        dest.writeInt(salesPrice);
        dest.writeInt(quantity);
        dest.writeInt(soldQuantity);
        dest.writeInt(sellRate);
        dest.writeInt(thumbsUp);
        dest.writeInt(thumbsDown);
        dest.writeInt(status);
        dest.writeInt(minStartDate);
        dest.writeInt(maxEndDate);
        dest.writeInt(saleStartDate);
        dest.writeInt(saleEndDate);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(minStartTime);
        dest.writeString(maxEndTime);
        dest.writeString(saleStartTime);
        dest.writeString(saleEndTime);
        dest.writeValue(dateRange);
        dest.writeString(cityName);
        dest.writeList(outlets);
        dest.writeInt(rating);
        dest.writeInt(likes);
        dest.writeString(savingPercentage);
        dest.writeValue(catalog);
        dest.writeValue(brand);
        dest.writeString(recommendationUrl);
        dest.writeList(mediaUrl);
        dest.writeValue(isLiked);
        dest.writeString(tnc);
        dest.writeString(seoUrl);
        dest.writeString(desktopUrl);
    }

    public int describeContents() {
        return 0;
    }

}


