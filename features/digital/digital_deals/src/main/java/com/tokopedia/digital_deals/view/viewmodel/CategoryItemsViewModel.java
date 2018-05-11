package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CategoryItemsViewModel implements Parcelable {

    private String displayName;
    private String url;
    private String seoUrl;
    private String imageWeb;
    private String thumbnailWeb;
    private String longRichDesc;
    private Integer mrp;
    private Integer salesPrice;
    private Integer quantity;
    private Integer soldQuantity;
    private Integer sellRate;
    private Integer thumbsUp;
    private Integer thumbsDown;
    private Integer isFeatured;
    private Integer minStartDate;
    private Integer maxEndDate;
    private Integer saleStartDate;
    private Integer saleEndDate;
    private String createdAt;
    private String updatedAt;
    private Boolean dateRange;
    private String cityName;
    private Integer rating;
    private Integer likes;
    private Object schedules;
    private Object forms;
    private Object media;
    private String savingPercentage;
    private BrandViewModel brand;

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
        this.displayName = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
        this.seoUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.imageWeb = ((String) in.readValue((String.class.getClassLoader())));
        this.thumbnailWeb = ((String) in.readValue((String.class.getClassLoader())));
        this.longRichDesc = ((String) in.readValue((String.class.getClassLoader())));
        this.mrp = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.salesPrice = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.quantity = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.soldQuantity = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.sellRate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.thumbsUp = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.thumbsDown = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.isFeatured = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.minStartDate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.maxEndDate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.saleStartDate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.saleEndDate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.dateRange = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.cityName = ((String) in.readValue((String.class.getClassLoader())));
        this.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.likes = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.schedules = ((Object) in.readValue((Object.class.getClassLoader())));
        this.forms = ((Object) in.readValue((Object.class.getClassLoader())));
        this.media = ((Object) in.readValue((Object.class.getClassLoader())));
        this.savingPercentage = ((String) in.readValue((Integer.class.getClassLoader())));
        this.brand = ((BrandViewModel) in.readValue((BrandViewModel.class.getClassLoader())));

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

    public String getSavingPercentage() { return savingPercentage; }

    public void setSavingPercentage(String savingPercentage) { this.savingPercentage = savingPercentage; }

    public BrandViewModel getBrand() {
        return brand;
    }

    public void setBrand(BrandViewModel brand) {
        this.brand = brand;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(displayName);
        dest.writeValue(url);
        dest.writeValue(seoUrl);
        dest.writeValue(imageWeb);
        dest.writeValue(thumbnailWeb);
        dest.writeValue(longRichDesc);
        dest.writeValue(mrp);
        dest.writeValue(salesPrice);
        dest.writeValue(quantity);
        dest.writeValue(soldQuantity);
        dest.writeValue(sellRate);
        dest.writeValue(thumbsUp);
        dest.writeValue(thumbsDown);
        dest.writeValue(isFeatured);
        dest.writeValue(minStartDate);
        dest.writeValue(maxEndDate);
        dest.writeValue(saleStartDate);
        dest.writeValue(saleEndDate);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
        dest.writeValue(dateRange);
        dest.writeValue(cityName);
        dest.writeValue(rating);
        dest.writeValue(likes);
        dest.writeValue(schedules);
        dest.writeValue(forms);
        dest.writeValue(media);
        dest.writeValue(savingPercentage);
        dest.writeValue(brand);
    }

    public int describeContents() {
        return 0;
    }

}