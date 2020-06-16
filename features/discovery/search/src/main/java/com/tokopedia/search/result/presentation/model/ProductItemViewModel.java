package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.kotlin.model.ImpressHolder;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ProductItemViewModel extends ImpressHolder implements Parcelable, Visitable<ProductListTypeFactory> {

    private static final String ACTION_FIELD = "/searchproduct - p$1 - product";

    private String productID;
    private String warehouseID;
    private String productName;
    private String imageUrl;
    private String imageUrl300;
    private String imageUrl700;
    private int rating;
    private String ratingString;
    private int countReview;
    private String price;
    private int priceInt;
    private String priceRange;
    private String shopID;
    private String shopName;
    private String shopCity;
    private boolean isWishlisted;
    private boolean isWishlistButtonEnabled = true;
    private List<BadgeItemViewModel> badgesList;
    private int position;
    private String originalPrice;
    private int discountPercentage;
    private int categoryID;
    private String categoryName;
    private String categoryBreadcrumb;
    private boolean isTopAds;
    private boolean isOrganicAds;
    private String topadsImpressionUrl;
    private String topadsClickUrl;
    private String topadsWishlistUrl;
    private boolean isNew;
    private List<LabelGroupViewModel> labelGroupList = new ArrayList<>();
    private FreeOngkirViewModel freeOngkirViewModel = new FreeOngkirViewModel();
    private String boosterList = "";

    public boolean isTopAds() {
        return isTopAds;
    }

    public void setTopAds(boolean topAds) {
        isTopAds = topAds;
    }

    public boolean isOrganicAds() {
        return isOrganicAds;
    }

    public void setIsOrganicAds(boolean isOrganicAds) {
        this.isOrganicAds = isOrganicAds;
    }

    public String getTopadsImpressionUrl() {
        return topadsImpressionUrl;
    }

    public void setTopadsImpressionUrl(String topadsImpressionUrl) {
        this.topadsImpressionUrl = topadsImpressionUrl;
    }

    public String getTopadsClickUrl() {
        return topadsClickUrl;
    }

    public void setTopadsClickUrl(String topadsClickUrl) {
        this.topadsClickUrl = topadsClickUrl;
    }

    public String getTopadsWishlistUrl() {
        return topadsWishlistUrl;
    }

    public void setTopadsWishlistUrl(String topadsWishlistUrl) {
        this.topadsWishlistUrl = topadsWishlistUrl;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductID() {
        return productID;
    }

    public String getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl300(String imageUrl300) {
        this.imageUrl300 = imageUrl300;
    }

    public String getImageUrl300() {
        return imageUrl300;
    }

    public void setImageUrl700(String imageUrl700) {
        this.imageUrl700 = imageUrl700;
    }

    public String getImageUrl700() {
        return imageUrl700;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setPriceInt(int priceInt) {
        this.priceInt = priceInt;
    }

    public int getPriceInt() {
        return priceInt;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopCity() {
        return shopCity;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }

    public boolean isWishlistButtonEnabled() {
        return isWishlistButtonEnabled;
    }

    public void setWishlistButtonEnabled(boolean wishlistButtonEnabled) {
        isWishlistButtonEnabled = wishlistButtonEnabled;
    }

    public void setBadgesList(List<BadgeItemViewModel> badgesList) {
        this.badgesList = badgesList;
    }

    public List<BadgeItemViewModel> getBadgesList() {
        return badgesList;
    }

    public int getPageNumber() {
        return (position - 1) / Integer.parseInt(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS) + 1;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRatingString() {
        return ratingString;
    }

    public void setRatingString(String ratingString) {
        this.ratingString = ratingString;
    }

    public int getCountReview() {
        return countReview;
    }

    public void setCountReview(int countReview) {
        this.countReview = countReview;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryBreadcrumb() {
        return categoryBreadcrumb;
    }

    public void setCategoryBreadcrumb(String categoryBreadcrumb) {
        this.categoryBreadcrumb = categoryBreadcrumb;
    }

    public void setLabelGroupList(List<LabelGroupViewModel> productLabelGroupList) {
        this.labelGroupList = productLabelGroupList;
    }

    public List<LabelGroupViewModel> getLabelGroupList() {
        return this.labelGroupList;
    }

    public void setFreeOngkirViewModel(FreeOngkirViewModel freeOngkirViewModel) {
        this.freeOngkirViewModel = freeOngkirViewModel;
    }

    public FreeOngkirViewModel getFreeOngkirViewModel() {
        return freeOngkirViewModel;
    }

    public void setBoosterList(String boosterList) {
        this.boosterList = boosterList;
    }

    public String getBoosterList() {
        return this.boosterList;
    }

    public ProductItemViewModel() {
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Object getProductAsObjectDataLayer(String userId, String filterSortParams, String searchRef) {
        return DataLayer.mapOf(
                "name", getProductName(),
                "id", getProductID(),
                "price", getPriceInt(),
                "brand", "none / other",
                "category", getCategoryBreadcrumb(),
                "variant", "none / other",
                "list", getActionFieldString(getPageNumber()),
                "position", Integer.toString(getPosition()),
                "userId", userId,
                "shopId", getShopID(),
                "dimension61", TextUtils.isEmpty(filterSortParams) ? "none / other" : filterSortParams,
                "dimension83", isFreeOngkirActive() ? "bebas ongkir" : "none / other",
                "dimension87", "search result",
                "dimension88", "search - product",
                "dimension90", searchRef,
                "dimension96", getBoosterList(),
                "dimension99", System.currentTimeMillis()
        );
    }

    private boolean isFreeOngkirActive() {
        return freeOngkirViewModel != null && freeOngkirViewModel.isActive();
    }

    private String getActionFieldString(int pageNumber) {
        return ACTION_FIELD.replace("$1", Integer.toString(pageNumber));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productID);
        dest.writeString(this.warehouseID);
        dest.writeString(this.productName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageUrl700);
        dest.writeString(this.ratingString);
        dest.writeInt(this.rating);
        dest.writeInt(this.countReview);
        dest.writeString(this.price);
        dest.writeString(this.priceRange);
        dest.writeString(this.shopID);
        dest.writeString(this.shopName);
        dest.writeString(this.shopCity);
        dest.writeByte(this.isWishlisted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWishlistButtonEnabled ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.badgesList);
        dest.writeInt(this.position);
        dest.writeString(this.originalPrice);
        dest.writeInt(this.discountPercentage);
        dest.writeInt(this.categoryID);
        dest.writeString(this.categoryName);
        dest.writeString(this.categoryBreadcrumb);
        dest.writeByte(this.isTopAds ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
        dest.writeString(this.topadsImpressionUrl);
        dest.writeString(this.topadsClickUrl);
        dest.writeString(this.topadsWishlistUrl);
        dest.writeTypedList(this.labelGroupList);
        dest.writeParcelable(this.freeOngkirViewModel, flags);
    }

    protected ProductItemViewModel(Parcel in) {
        this.productID = in.readString();
        this.warehouseID = in.readString();
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.imageUrl700 = in.readString();
        this.ratingString = in.readString();
        this.rating = in.readInt();
        this.countReview = in.readInt();
        this.price = in.readString();
        this.priceRange = in.readString();
        this.shopID = in.readString();
        this.shopName = in.readString();
        this.shopCity = in.readString();
        this.isWishlisted = in.readByte() != 0;
        this.isWishlistButtonEnabled = in.readByte() != 0;
        this.badgesList = in.createTypedArrayList(BadgeItemViewModel.CREATOR);
        this.position = in.readInt();
        this.originalPrice = in.readString();
        this.discountPercentage = in.readInt();
        this.categoryID = in.readInt();
        this.categoryName = in.readString();
        this.categoryBreadcrumb = in.readString();
        this.isTopAds = in.readByte() != 0;
        this.isNew = in.readByte() != 0;
        this.topadsImpressionUrl = in.readString();
        this.topadsClickUrl = in.readString();
        this.topadsWishlistUrl = in.readString();
        this.labelGroupList = in.createTypedArrayList(LabelGroupViewModel.CREATOR);
        this.freeOngkirViewModel = in.readParcelable(FreeOngkirViewModel.class.getClassLoader());
    }

    public static final Creator<ProductItemViewModel> CREATOR = new Creator<ProductItemViewModel>() {
        @Override
        public ProductItemViewModel createFromParcel(Parcel source) {
            return new ProductItemViewModel(source);
        }

        @Override
        public ProductItemViewModel[] newArray(int size) {
            return new ProductItemViewModel[size];
        }
    };
}
