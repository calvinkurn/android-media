package com.tokopedia.events.domain.model.scanticket;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanProductDetail implements Parcelable {


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
    @SerializedName("seo_url")
    @Expose
    private String seoUrl;
    @SerializedName("image_web")
    @Expose
    private String imageWeb;
    @SerializedName("thumbnail_web")
    @Expose
    private String thumbnailWeb;
    @SerializedName("tnc")
    @Expose
    private String tnc;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("long_rich_desc")
    @Expose
    private String longRichDesc;
    @SerializedName("search_tags")
    @Expose
    private String searchTags;
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
    @SerializedName("rating")
    @Expose
    private int rating;
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

    protected ScanProductDetail(Parcel in) {
        id = in.readInt();
        brandId = in.readInt();
        categoryId = in.readInt();
        providerId = in.readInt();
        providerProductId = in.readString();
        providerProductName = in.readString();
        displayName = in.readString();
        url = in.readString();
        seoUrl = in.readString();
        imageWeb = in.readString();
        thumbnailWeb = in.readString();
        tnc = in.readString();
        shortDesc = in.readString();
        longRichDesc = in.readString();
        searchTags = in.readString();
        quantity = in.readInt();
        soldQuantity = in.readInt();
        sellRate = in.readInt();
        thumbsUp = in.readInt();
        thumbsDown = in.readInt();
        rating = in.readInt();
        minStartDate = in.readInt();
        maxEndDate = in.readInt();
        saleStartDate = in.readInt();
        saleEndDate = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<ScanProductDetail> CREATOR = new Creator<ScanProductDetail>() {
        @Override
        public ScanProductDetail createFromParcel(Parcel in) {
            return new ScanProductDetail(in);
        }

        @Override
        public ScanProductDetail[] newArray(int size) {
            return new ScanProductDetail[size];
        }
    };

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

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongRichDesc() {
        return longRichDesc;
    }

    public void setLongRichDesc(String longRichDesc) {
        this.longRichDesc = longRichDesc;
    }

    public String getSearchTags() {
        return searchTags;
    }

    public void setSearchTags(String searchTags) {
        this.searchTags = searchTags;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(brandId);
        parcel.writeInt(categoryId);
        parcel.writeInt(providerId);
        parcel.writeString(providerProductId);
        parcel.writeString(providerProductName);
        parcel.writeString(displayName);
        parcel.writeString(url);
        parcel.writeString(seoUrl);
        parcel.writeString(imageWeb);
        parcel.writeString(thumbnailWeb);
        parcel.writeString(tnc);
        parcel.writeString(shortDesc);
        parcel.writeString(longRichDesc);
        parcel.writeString(searchTags);
        parcel.writeInt(quantity);
        parcel.writeInt(soldQuantity);
        parcel.writeInt(sellRate);
        parcel.writeInt(thumbsUp);
        parcel.writeInt(thumbsDown);
        parcel.writeInt(rating);
        parcel.writeInt(minStartDate);
        parcel.writeInt(maxEndDate);
        parcel.writeInt(saleStartDate);
        parcel.writeInt(saleEndDate);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
    }
}
