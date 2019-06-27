package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductItem implements Parcelable {

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
    @SerializedName("is_featured")
    @Expose
    private int isFeatured;
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
    @SerializedName("date_range")
    @Expose
    private Boolean dateRange;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("likes")
    @Expose
    private int likes;
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
    private int id;
    @SerializedName("is_searchable")
    @Expose
    private int isSearchable;
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
    @SerializedName("sale_start_time")
    @Expose
    private String saleStartTime;
    @SerializedName("sale_end_time")
    @Expose
    private String saleEndTime;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("saving_percentage")
    @Expose
    private String savingPercentage;
    @SerializedName("display_tags")
    @Expose
    private String displayTags;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("desktop_url")
    @Expose
    private String desktopUrl;
    @SerializedName("catalog")
    @Expose
    private Catalog catalog;
    @SerializedName("is_liked")
    private boolean isLiked;
    private boolean isTrack = false;



    public ProductItem() {
    }

    protected ProductItem(Parcel in) {
        displayName = in.readString();
        url = in.readString();
        seoUrl = in.readString();
        imageWeb = in.readString();
        thumbnailWeb = in.readString();
        longRichDesc = in.readString();
        mrp = in.readInt();
        salesPrice = in.readInt();
        quantity = in.readInt();
        soldQuantity = in.readInt();
        sellRate = in.readInt();
        thumbsUp = in.readInt();
        thumbsDown = in.readInt();
        isFeatured = in.readInt();
        minStartDate = in.readInt();
        maxEndDate = in.readInt();
        saleStartDate = in.readInt();
        saleEndDate = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        byte tmpDateRange = in.readByte();
        dateRange = tmpDateRange == 0 ? null : tmpDateRange == 1;
        cityName = in.readString();
        rating = in.readInt();
        likes = in.readInt();
        id = in.readInt();
        isSearchable = in.readInt();
        categoryId = in.readInt();
        providerId = in.readInt();
        providerProductId = in.readString();
        providerProductName = in.readString();
        saleStartTime = in.readString();
        saleEndTime = in.readString();
        status = in.readInt();
        savingPercentage = in.readString();
        displayTags = in.readString();
        brand = in.readParcelable(Brand.class.getClassLoader());
        catalog = in.readParcelable(Catalog.class.getClassLoader());
        isLiked = in.readByte() != 0;
        this.isTrack = in.readByte() != 0;
        desktopUrl = in.readString();

    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };

    public String getDesktopUrl() {
        return desktopUrl;
    }

    public void setDesktopUrl(String desktopUrl) {
        this.desktopUrl = desktopUrl;
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

    public int getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(int isFeatured) {
        this.isFeatured = isFeatured;
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getDisplayTags() {
        return displayTags;
    }

    public void setDisplayTags(String displayTags) {
        this.displayTags = displayTags;
    }


    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTrack() {
        return isTrack;
    }

    public void setTrack(boolean track) {
        isTrack = track;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(displayName);
        dest.writeString(url);
        dest.writeString(seoUrl);
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
        dest.writeInt(isFeatured);
        dest.writeInt(minStartDate);
        dest.writeInt(maxEndDate);
        dest.writeInt(saleStartDate);
        dest.writeInt(saleEndDate);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeByte((byte) (dateRange == null ? 0 : dateRange ? 1 : 2));
        dest.writeString(cityName);
        dest.writeInt(rating);
        dest.writeInt(likes);
        dest.writeInt(id);
        dest.writeInt(isSearchable);
        dest.writeInt(categoryId);
        dest.writeInt(providerId);
        dest.writeString(providerProductId);
        dest.writeString(providerProductName);
        dest.writeString(saleStartTime);
        dest.writeString(saleEndTime);
        dest.writeInt(status);
        dest.writeString(savingPercentage);
        dest.writeString(displayTags);
        dest.writeParcelable(brand, flags);
        dest.writeParcelable(catalog, flags);
        dest.writeByte((byte) (isLiked ? 1 : 0));
        dest.writeByte(this.isTrack ? (byte) 1 : (byte) 0);
        dest.writeString(desktopUrl);

    }

    public int describeContents() {
        return 0;
    }

}