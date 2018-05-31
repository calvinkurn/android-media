package com.tokopedia.digital_deals.view.viewmodel;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class DealsDetailsViewModel implements Parcelable {

    private Integer id;
    private Integer brandId;
    private Integer categoryId;
    private Integer providerId;
    private String providerProductId;
    private String providerProductName;
    private String displayName;
    private String url;
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
    private Integer status;
    private Integer minStartDate;
    private Integer maxEndDate;
    private Integer saleStartDate;
    private Integer saleEndDate;
    private String createdAt;
    private String updatedAt;
    private String minStartTime;
    private String maxEndTime;
    private String saleStartTime;
    private String saleEndTime;
    private Boolean dateRange;
    private String cityName;
    private List<OutletViewModel> outlets = null;
    private Integer rating;
    private Integer likes;
    private String savingPercentage;
    private CatalogViewModel catalog;
    private BrandViewModel brand;
    private String recommendationUrl;


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
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.brandId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.categoryId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.providerId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.providerProductId = ((String) in.readValue((String.class.getClassLoader())));
        this.providerProductName = ((String) in.readValue((String.class.getClassLoader())));
        this.displayName = ((String) in.readValue((String.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
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
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.minStartDate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.maxEndDate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.saleStartDate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.saleEndDate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.minStartTime = ((String) in.readValue((String.class.getClassLoader())));
        this.maxEndTime = ((String) in.readValue((String.class.getClassLoader())));
        this.saleStartTime = ((String) in.readValue((String.class.getClassLoader())));
        this.saleEndTime = ((String) in.readValue((String.class.getClassLoader())));
        this.dateRange = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.cityName = ((String) in.readValue((String.class.getClassLoader())));
        this.outlets = new ArrayList<OutletViewModel>();
        in.readList(this.outlets, OutletViewModel.class.getClassLoader());
        this.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.likes = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.savingPercentage = ((String) in.readValue((Integer.class.getClassLoader())));
        this.catalog = ((CatalogViewModel) in.readValue((CatalogViewModel.class.getClassLoader())));
        this.brand = ((BrandViewModel) in.readValue((BrandViewModel.class.getClassLoader())));
        this.recommendationUrl= ((String) in.readValue((Integer.class.getClassLoader())));

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecommendationUrl() {
        return recommendationUrl;
    }

    public void setRecommendationUrl(String recommendationUrl) {
        this.recommendationUrl = recommendationUrl;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(brandId);
        dest.writeValue(categoryId);
        dest.writeValue(providerId);
        dest.writeValue(providerProductId);
        dest.writeValue(providerProductName);
        dest.writeValue(displayName);
        dest.writeValue(url);
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
        dest.writeValue(status);
        dest.writeValue(minStartDate);
        dest.writeValue(maxEndDate);
        dest.writeValue(saleStartDate);
        dest.writeValue(saleEndDate);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
        dest.writeValue(minStartTime);
        dest.writeValue(maxEndTime);
        dest.writeValue(saleStartTime);
        dest.writeValue(saleEndTime);
        dest.writeValue(dateRange);
        dest.writeValue(cityName);
        dest.writeList(outlets);
        dest.writeValue(rating);
        dest.writeValue(likes);
        dest.writeValue(savingPercentage);
        dest.writeValue(catalog);
        dest.writeValue(brand);
        dest.writeValue(recommendationUrl);
    }

    public int describeContents() {
        return 0;
    }

}


