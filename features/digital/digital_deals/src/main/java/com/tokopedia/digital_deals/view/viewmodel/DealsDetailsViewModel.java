package com.tokopedia.digital_deals.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DealsDetailsViewModel implements Parcelable {

    private int id;
    private int brandId;
    private int categoryId;
    private int providerId;
    private String providerProductId;
    private String providerProductName;
    private String displayName;
    private String url;
    private String imageWeb;
    private String thumbnailWeb;
    private String longRichDesc;
    private String tnc;
    private int mrp;
    private int salesPrice;
    private int quantity;
    private int soldQuantity;
    private int sellRate;
    private int thumbsUp;
    private int thumbsDown;
    private int status;
    private int minStartDate;
    private int maxEndDate;
    private int saleStartDate;
    private int saleEndDate;
    private String createdAt;
    private String updatedAt;
    private String minStartTime;
    private String maxEndTime;
    private String saleStartTime;
    private String saleEndTime;
    private Boolean dateRange;
    private String cityName;
    private List<OutletViewModel> outlets = null;
    private int rating;
    private int likes;
    private String savingPercentage;
    private CatalogViewModel catalog;
    private BrandViewModel brand;
    private List<String> mediaUrl;
    private String recommendationUrl;
    private boolean isLiked;
    private String seoUrl;


    public final static Parcelable.Creator<DealsDetailsViewModel> CREATOR = new Creator<DealsDetailsViewModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DealsDetailsViewModel createFromParcel(Parcel in) {
            return new DealsDetailsViewModel(in);
        }

        public DealsDetailsViewModel[] newArray(int size) {
            return (new DealsDetailsViewModel[size]);
        }

    };

    protected DealsDetailsViewModel(Parcel in) {
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
        this.outlets = new ArrayList<OutletViewModel>();
        in.readList(this.outlets, OutletViewModel.class.getClassLoader());
        this.rating = in.readInt();
        this.likes = in.readInt();
        this.savingPercentage = in.readString();
        this.catalog = ((CatalogViewModel) in.readValue((CatalogViewModel.class.getClassLoader())));
        this.brand = ((BrandViewModel) in.readValue((BrandViewModel.class.getClassLoader())));
        this.recommendationUrl = in.readString();
        this.mediaUrl = new ArrayList<>();
        in.readList(this.mediaUrl, String.class.getClassLoader());
        this.isLiked = in.readValue((Boolean.class.getClassLoader())) != null;
        this.tnc = in.readString();
        this.seoUrl = in.readString();
    }

    public DealsDetailsViewModel() {
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

    public List<OutletViewModel> getOutlets() {
        return outlets;
    }

    public void setOutlets(List<OutletViewModel> outlets) {
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

    public CatalogViewModel getCatalog() {
        return catalog;
    }

    public void setCatalog(CatalogViewModel catalog) {
        this.catalog = catalog;
    }


    public BrandViewModel getBrand() {
        return brand;
    }

    public void setBrand(BrandViewModel brand) {
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

    public List<String> getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(List<String> mediaUrl) {
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
    }

    public int describeContents() {
        return 0;
    }

}


