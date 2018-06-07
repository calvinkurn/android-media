package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryItemsViewModel implements Parcelable {

    private int id;
    private String displayName;
    private String url;
    private String seoUrl;
    private String imageWeb;
    private String thumbnailWeb;
    private String longRichDesc;
    private int mrp;
    private int salesPrice;
    private int quantity;
    private int soldQuantity;
    private int sellRate;
    private int thumbsUp;
    private int thumbsDown;
    private int isFeatured;
    private int minStartDate;
    private int maxEndDate;
    private int saleStartDate;
    private int saleEndDate;
    private String createdAt;
    private String updatedAt;
    private Boolean dateRange;
    private String cityName;
    private int rating;
    private int likes;
    private Object schedules;
    private Object forms;
    private Object media;
    private String savingPercentage;
    private String displayTags;
    private boolean isLiked;
    private BrandViewModel brand;
    private CatalogViewModel catalog;

    public final static Parcelable.Creator<CategoryItemsViewModel> CREATOR = new Creator<CategoryItemsViewModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoryItemsViewModel createFromParcel(Parcel in) {
            return new CategoryItemsViewModel(in);
        }

        public CategoryItemsViewModel[] newArray(int size) {
            return (new CategoryItemsViewModel[size]);
        }

    };


    protected CategoryItemsViewModel(Parcel in) {
        this.id=in.readInt();
        this.displayName = in.readString();
        this.url = in.readString();
        this.seoUrl = in.readString();
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
        this.isFeatured = in.readInt();
        this.minStartDate = in.readInt();
        this.maxEndDate = in.readInt();
        this.saleStartDate = in.readInt();
        this.saleEndDate = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.dateRange = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.cityName = in.readString();
        this.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.likes = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.schedules = ((Object) in.readValue((Object.class.getClassLoader())));
        this.forms = ((Object) in.readValue((Object.class.getClassLoader())));
        this.media = ((Object) in.readValue((Object.class.getClassLoader())));
        this.savingPercentage = in.readString();
        this.brand = ((BrandViewModel) in.readValue((BrandViewModel.class.getClassLoader())));
        this.displayTags = in.readString();
        this.catalog = ((CatalogViewModel) in.readValue((CatalogViewModel.class.getClassLoader())));
        this.isLiked = in.readValue((Boolean.class.getClassLoader())) != null;

    }

    public CategoryItemsViewModel() {
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

    public BrandViewModel getBrand() {
        return brand;
    }

    public void setBrand(BrandViewModel brand) {
        this.brand = brand;
    }

    public String getDisplayTags() {
        return displayTags;
    }

    public void setDisplayTags(String displayTags) {
        this.displayTags = displayTags;
    }


    public CatalogViewModel getCatalog() {
        return catalog;
    }

    public void setCatalog(CatalogViewModel catalog) {
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


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
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
        dest.writeValue(dateRange);
        dest.writeString(cityName);
        dest.writeValue(rating);
        dest.writeValue(likes);
        dest.writeValue(schedules);
        dest.writeValue(forms);
        dest.writeValue(media);
        dest.writeString(savingPercentage);
        dest.writeValue(brand);
        dest.writeString(displayTags);
        dest.writeValue(catalog);
        dest.writeValue(isLiked);
    }

    public int describeContents() {
        return 0;
    }

}