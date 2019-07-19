package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.kotlin.model.ImpressHolder;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductItemViewModel extends ImpressHolder implements Parcelable, Visitable<ProductListTypeFactory> {

    private static final String ACTION_FIELD = "/searchproduct - p$1 - product";
    public static String imageClick = "/imagesearch - p%s";

    private String productID;
    private String productName;
    private String imageUrl;
    private String imageUrl700;
    private int rating;
    private int countReview;
    private int countCourier;
    private String price;
    private String priceRange;
    private String shopID;
    private String shopName;
    private String shopCity;
    private boolean isGoldMerchant;
    private boolean isWishlisted;
    private boolean isWishlistButtonEnabled = true;
    private List<BadgeItemViewModel> badgesList;
    private List<LabelItemViewModel> labelList;
    private int position;
    private String originalPrice;
    private int discountPercentage;
    private String topLabel;
    private String bottomLabel;
    private String productWishlistUrl;
    private int categoryID;
    private String categoryName;
    private String categoryBreadcrumb;
    private boolean isTopAds;
    private String topadsImpressionUrl;
    private String topadsClickUrl;
    private String topadsWishlistUrl;
    private boolean isNew;
    private List<LabelGroupViewModel> labelGroupList = new ArrayList<>();
    private boolean isShopPowerBadge;
    private boolean isShopOfficialStore;

    public boolean isTopAds() {
        return isTopAds;
    }

    public void setTopAds(boolean topAds) {
        isTopAds = topAds;
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

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopCity() {
        return shopCity;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean isGoldMerchant) {
        this.isGoldMerchant = isGoldMerchant;
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

    public void setLabelList(List<LabelItemViewModel> labelList) {
        this.labelList = labelList;
    }

    public List<LabelItemViewModel> getLabelList() {
        return labelList;
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

    public int getCountReview() {
        return countReview;
    }

    public void setCountReview(int countReview) {
        this.countReview = countReview;
    }

    public int getCountCourier() {
        return countCourier;
    }

    public void setCountCourier(int countCourier) {
        this.countCourier = countCourier;
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

    public String getTopLabel() {
        return topLabel;
    }

    public void setTopLabel(String topLabel) {
        this.topLabel = topLabel;
    }

    public String getBottomLabel() {
        return bottomLabel;
    }

    public void setBottomLabel(String bottomLabel) {
        this.bottomLabel = bottomLabel;
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

    public void setProductWishlistUrl(String productWishlistUrl) {
        this.productWishlistUrl = productWishlistUrl;
    }

    public String getProductWishlistUrl() {
        return productWishlistUrl;
    }

    public void setIsShopPowerBadge(boolean isShopPowerBadge) {
        this.isShopPowerBadge = isShopPowerBadge;
    }

    public boolean isShopPowerBadge() {
        return isShopPowerBadge;
    }

    public void setIsShopOfficialStore(boolean isShopOfficialStore) {
        this.isShopOfficialStore = isShopOfficialStore;
    }

    public boolean isShopOfficialStore() {
        return isShopOfficialStore;
    }

    public ProductItemViewModel() {
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Object getProductAsObjectDataLayer(String userId, String filterSortParams) {
        return DataLayer.mapOf(
                "name", getProductName(),
                "id", getProductID(),
                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(getPrice())),
                "brand", "none / other",
                "category", getCategoryBreadcrumb(),
                "variant", "none / other",
                "list", getActionFieldString(getPageNumber()),
                "position", Integer.toString(getPosition()),
                "userId", userId,
                "shopId", getShopID(),
                "dimension61", TextUtils.isEmpty(filterSortParams) ? "none / other" : filterSortParams
        );
    }

    private String getActionFieldString(int pageNumber) {
        return ACTION_FIELD.replace("$1", Integer.toString(pageNumber));
    }

    public Object getProductAsObjectDataLayerForImageSearch(String userId) {
        return DataLayer.mapOf(
                "name", getProductName(),
                "id", getProductID(),
                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(getPrice())),
                "category", "",
                "list", String.format(imageClick, getPosition()),
                "position", Integer.toString(getPosition()),
                "userId", userId
        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productID);
        dest.writeString(this.productName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageUrl700);
        dest.writeInt(this.rating);
        dest.writeInt(this.countReview);
        dest.writeInt(this.countCourier);
        dest.writeString(this.price);
        dest.writeString(this.priceRange);
        dest.writeString(this.shopID);
        dest.writeString(this.shopName);
        dest.writeString(this.shopCity);
        dest.writeByte(this.isGoldMerchant ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWishlisted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWishlistButtonEnabled ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.badgesList);
        dest.writeTypedList(this.labelList);
        dest.writeInt(this.position);
        dest.writeString(this.originalPrice);
        dest.writeInt(this.discountPercentage);
        dest.writeString(this.topLabel);
        dest.writeString(this.bottomLabel);
        dest.writeString(this.productWishlistUrl);
        dest.writeInt(this.categoryID);
        dest.writeString(this.categoryName);
        dest.writeString(this.categoryBreadcrumb);
        dest.writeByte(this.isTopAds ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isNew ? (byte) 1 : (byte) 0);
        dest.writeString(this.topadsImpressionUrl);
        dest.writeString(this.topadsClickUrl);
        dest.writeString(this.topadsWishlistUrl);
        dest.writeTypedList(this.labelGroupList);
        dest.writeByte(this.isShopPowerBadge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShopOfficialStore ? (byte) 1 : (byte) 0);
    }

    protected ProductItemViewModel(Parcel in) {
        this.productID = in.readString();
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.imageUrl700 = in.readString();
        this.rating = in.readInt();
        this.countReview = in.readInt();
        this.countCourier = in.readInt();
        this.price = in.readString();
        this.priceRange = in.readString();
        this.shopID = in.readString();
        this.shopName = in.readString();
        this.shopCity = in.readString();
        this.isGoldMerchant = in.readByte() != 0;
        this.isWishlisted = in.readByte() != 0;
        this.isWishlistButtonEnabled = in.readByte() != 0;
        this.badgesList = in.createTypedArrayList(BadgeItemViewModel.CREATOR);
        this.labelList = in.createTypedArrayList(LabelItemViewModel.CREATOR);
        this.position = in.readInt();
        this.originalPrice = in.readString();
        this.discountPercentage = in.readInt();
        this.topLabel = in.readString();
        this.bottomLabel = in.readString();
        this.productWishlistUrl = in.readString();
        this.categoryID = in.readInt();
        this.categoryName = in.readString();
        this.categoryBreadcrumb = in.readString();
        this.isTopAds = in.readByte() != 0;
        this.isNew = in.readByte() != 0;
        this.topadsImpressionUrl = in.readString();
        this.topadsClickUrl = in.readString();
        this.topadsWishlistUrl = in.readString();
        this.labelGroupList = in.createTypedArrayList(LabelGroupViewModel.CREATOR);
        this.isShopPowerBadge = in.readByte() != 0;
        this.isShopOfficialStore = in.readByte() != 0;
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
