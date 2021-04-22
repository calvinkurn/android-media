package com.tokopedia.search.result.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.kotlin.model.ImpressHolder;
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory;
import com.tokopedia.search.utils.SearchKotlinExtKt;
import com.tokopedia.utils.text.currency.StringUtils;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;

public class ProductItemDataView extends ImpressHolder implements Parcelable, Visitable<ProductListTypeFactory> {

    private static final String ACTION_FIELD = "/searchproduct - %s";
    private static final String ORGANIC = "organic";
    private static final String ORGANIC_ADS = "organic ads";

    private String productID;
    private String warehouseID;
    private String productName;
    private String imageUrl;
    private String imageUrl300;
    private String imageUrl700;
    private String ratingString;
    private String price;
    private int priceInt;
    private String priceRange;
    private String shopID;
    private String shopName;
    private String shopCity;
    private String shopUrl;
    private boolean isWishlisted;
    private boolean isWishlistButtonEnabled = true;
    private List<BadgeItemDataView> badgesList;
    private int position;
    private String originalPrice;
    private int discountPercentage;
    private int categoryID = 0;
    private String categoryName = "";
    private String categoryBreadcrumb = "";
    private boolean isTopAds;
    private boolean isOrganicAds;
    private String topadsImpressionUrl;
    private String topadsClickUrl;
    private String topadsWishlistUrl;
    private String topadsClickShopUrl;
    private boolean isNew;
    private List<LabelGroupDataView> labelGroupList = new ArrayList<>();
    private List<LabelGroupVariantDataView> labelGroupVariantList = new ArrayList<>();
    private FreeOngkirDataView freeOngkirDataView = new FreeOngkirDataView();
    private String boosterList = "";
    private String sourceEngine = "";
    private int minOrder = 1;
    private boolean isShopOfficialStore = false;
    private boolean isShopPowerMerchant = false;
    private String productUrl = "";
    private String pageTitle;

    public boolean isTopAds() {
        return isTopAds;
    }

    public void setTopAds(boolean topAds) {
        isTopAds = topAds;
    }

    public boolean isOrganicAds() {
        return isOrganicAds;
    }

    public void setOrganicAds(boolean isOrganicAds) {
        this.isOrganicAds = isOrganicAds;
    }

    public boolean isAds() {
        return isTopAds() || isOrganicAds();
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

    public String getTopadsClickShopUrl() {
        return topadsClickShopUrl;
    }

    public void setTopadsClickShopUrl(String topadsClickShopUrl) {
        this.topadsClickShopUrl = topadsClickShopUrl;
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

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShopUrl() {
        return this.shopUrl;
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

    public void setBadgesList(List<BadgeItemDataView> badgesList) {
        this.badgesList = badgesList;
    }

    public List<BadgeItemDataView> getBadgesList() {
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

    public String getRatingString() {
        return ratingString;
    }

    public void setRatingString(String ratingString) {
        this.ratingString = ratingString;
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

    public void setLabelGroupList(List<LabelGroupDataView> productLabelGroupList) {
        this.labelGroupList = productLabelGroupList;
    }

    public List<LabelGroupDataView> getLabelGroupList() {
        return this.labelGroupList;
    }

    public void setLabelGroupVariantList(List<LabelGroupVariantDataView> productLabelGroupVariantList) {
        this.labelGroupVariantList = productLabelGroupVariantList;
    }

    public List<LabelGroupVariantDataView> getLabelGroupVariantList() {
        return this.labelGroupVariantList;
    }

    public void setFreeOngkirDataView(FreeOngkirDataView freeOngkirDataView) {
        this.freeOngkirDataView = freeOngkirDataView;
    }

    public FreeOngkirDataView getFreeOngkirDataView() {
        return freeOngkirDataView;
    }

    public void setBoosterList(String boosterList) {
        this.boosterList = boosterList;
    }

    public String getBoosterList() {
        return this.boosterList;
    }

    public void setSourceEngine(String sourceEngine) {
        this.sourceEngine = sourceEngine;
    }

    public String getSourceEngine() {
        return this.sourceEngine;
    }

    public String getCategoryString() {
        return StringUtils.INSTANCE.isBlank(categoryName) ? categoryBreadcrumb : categoryName;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public int getMinOrder() {
        return this.minOrder;
    }

    public void setShopOfficialStore(boolean isShopOfficialStore) {
        this.isShopOfficialStore = isShopOfficialStore;
    }

    public boolean isShopOfficialStore() {
        return this.isShopOfficialStore;
    }

    public void setShopPowerMerchant(boolean isShopPowerMerchant) {
        this.isShopPowerMerchant = isShopPowerMerchant;
    }

    public boolean isShopPowerMerchant() {
        return this.isShopPowerMerchant;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductUrl() {
        return this.productUrl;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public ProductItemDataView() {
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Object getProductAsObjectDataLayer(String userId, String filterSortParams, String dimension90) {
        return DataLayer.mapOf(
                "name", getProductName(),
                "id", getProductID(),
                "price", getPriceInt(),
                "brand", "none / other",
                "category", getCategoryBreadcrumb(),
                "variant", "none / other",
                "list", getActionFieldString(),
                "position", Integer.toString(getPosition()),
                "userId", userId,
                "shopId", getShopID(),
                "dimension61", TextUtils.isEmpty(filterSortParams) ? "none / other" : filterSortParams,
                "dimension83", setFreeOngkirDataLayer(),
                "dimension87", "search result",
                "dimension88", "search - product",
                "dimension90", dimension90,
                "dimension96", getBoosterList(),
                "dimension99", System.currentTimeMillis(),
                "dimension100", getSourceEngine()
        );
    }

    private String setFreeOngkirDataLayer() {
        boolean isFreeOngkirActive = isFreeOngkirActive();

        if (isFreeOngkirActive && hasLabelGroupFulfillment()) {
            return "bebas ongkir extra";
        }
        else if (isFreeOngkirActive && !hasLabelGroupFulfillment()) {
            return "bebas ongkir";
        }
        else {
            return "none / other";
        }
    }

    private boolean isFreeOngkirActive() {
        return freeOngkirDataView != null && freeOngkirDataView.isActive();
    }

    public boolean hasLabelGroupFulfillment() {
        return CollectionsKt.any(labelGroupList, labelGroupDataView -> labelGroupDataView.getPosition().equals(SearchConstant.ProductCardLabel.LABEL_FULFILLMENT));
    }

    private String getActionFieldString() {
        String organicStatus = isOrganicAds() ? ORGANIC_ADS : ORGANIC;

        return String.format(ACTION_FIELD, organicStatus);
    }

    public Object getProductAsATCObjectDataLayer(String cartId) {
        return DataLayer.mapOf(
                "name", productName,
                "id", productID,
                "price", String.valueOf(SearchKotlinExtKt.safeCastRupiahToInt(getPrice())),
                "brand", "none / other",
                "category", getCategoryBreadcrumb(),
                "variant", "none / other",
                "quantity", getMinOrder(),
                "shop_id", getShopID(),
                "shop_type", getShopType(),
                "shop_name", getShopName(),
                "category_id", getCategoryID(),
                "dimension82", cartId
        );
    }

    public Object getProductAsShopPageObjectDataLayer() {
        return DataLayer.mapOf(
                "id", shopID,
                "name", String.format(ACTION_FIELD, isAds() ? ORGANIC_ADS : ORGANIC),
                "creative", shopName,
                "creative_url", shopUrl,
                "position", Integer.toString(getPosition()),
                "category", getCategoryBreadcrumb(),
                "promo_id", "none / other",
                "promo_code", "none / other"
        );
    }

    private String getShopType() {
        if (isShopOfficialStore) return "official_store";
        else if (isShopPowerMerchant) return "gold_merchant";
        else return "reguler";
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
        dest.writeParcelable(this.freeOngkirDataView, flags);
    }

    protected ProductItemDataView(Parcel in) {
        this.productID = in.readString();
        this.warehouseID = in.readString();
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.imageUrl700 = in.readString();
        this.ratingString = in.readString();
        this.price = in.readString();
        this.priceRange = in.readString();
        this.shopID = in.readString();
        this.shopName = in.readString();
        this.shopCity = in.readString();
        this.isWishlisted = in.readByte() != 0;
        this.isWishlistButtonEnabled = in.readByte() != 0;
        this.badgesList = in.createTypedArrayList(BadgeItemDataView.CREATOR);
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
        this.labelGroupList = in.createTypedArrayList(LabelGroupDataView.CREATOR);
        this.freeOngkirDataView = in.readParcelable(FreeOngkirDataView.class.getClassLoader());
    }

    public static final Creator<ProductItemDataView> CREATOR = new Creator<ProductItemDataView>() {
        @Override
        public ProductItemDataView createFromParcel(Parcel source) {
            return new ProductItemDataView(source);
        }

        @Override
        public ProductItemDataView[] newArray(int size) {
            return new ProductItemDataView[size];
        }
    };
}
