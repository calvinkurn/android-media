package com.tokopedia.checkout.domain.datamodel.cartlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author anggaprasetiyo on 18/01/18.
 */

public class CartItemData implements Parcelable {

    private OriginData originData;
    private UpdatedData updatedData;
    private MessageErrorData errorData;
    private boolean singleChild;
    private boolean parentHasErrorOrWarning;
    private boolean isError;
    private boolean isWarning;
    private String warningMessageTitle;
    private String warningMessageDescription;
    private String errorMessageTitle;
    private String errorMessageDescription;

    public boolean isSingleChild() {
        return singleChild;
    }

    public void setSingleChild(boolean singleChild) {
        this.singleChild = singleChild;
    }

    public boolean isError() {
        return isError;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getWarningMessageTitle() {
        return warningMessageTitle;
    }

    public void setWarningMessageTitle(String warningMessageTitle) {
        this.warningMessageTitle = warningMessageTitle;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessageTitle() {
        return errorMessageTitle;
    }

    public void setErrorMessageTitle(String errorMessageTitle) {
        this.errorMessageTitle = errorMessageTitle;
    }

    public String getWarningMessageDescription() {
        return warningMessageDescription;
    }

    public void setWarningMessageDescription(String warningMessageDescription) {
        this.warningMessageDescription = warningMessageDescription;
    }

    public String getErrorMessageDescription() {
        return errorMessageDescription;
    }

    public void setErrorMessageDescription(String errorMessageDescription) {
        this.errorMessageDescription = errorMessageDescription;
    }

    public OriginData getOriginData() {
        return originData;
    }

    public void setOriginData(OriginData originData) {
        this.originData = originData;
    }

    public UpdatedData getUpdatedData() {
        return updatedData;
    }

    public void setUpdatedData(UpdatedData updatedData) {
        this.updatedData = updatedData;
    }

    public MessageErrorData getErrorData() {
        return errorData;
    }

    public void setErrorData(MessageErrorData errorData) {
        this.errorData = errorData;
    }

    public boolean isParentHasErrorOrWarning() {
        return parentHasErrorOrWarning;
    }

    public void setParentHasErrorOrWarning(boolean parentHasError) {
        this.parentHasErrorOrWarning = parentHasError;
    }

    public static class OriginData implements Parcelable {
        public static final int CURRENCY_IDR = 1;
        public static final int CURRENCY_USD = 2;

        public static final int WEIGHT_KILOS = 1;
        public static final int WEIGHT_GRAM = 2;

        private int cartId;
        private String parentId;
        private String productId;
        private String productName;
        private int minimalQtyOrder;
        private int invenageValue;
        private double pricePlan;
        private int pricePlanInt;
        private int priceCurrency;
        private String priceFormatted;
        private String wholesalePriceFormatted;
        private String productImage;
        private String productVarianRemark;
        private double weightPlan;
        private int weightUnit;
        private String weightFormatted;
        private boolean isPreOrder;
        private boolean isCod;
        private boolean isFreeReturn;
        private boolean isCashBack;
        private boolean isFavorite;
        private String productCashBack;
        private String cashBackInfo;
        private String freeReturnLogo;
        private String category;
        private String categoryForAnalytics;
        private String categoryId;
        private List<WholesalePrice> wholesalePrice;
        private String trackerAttribution;
        private String trackerListName;
        private String originalRemark;
        private String shopName;
        private String shopId;
        private String shopType;
        private boolean officialStore;
        private boolean goldMerchant;
        private boolean wishlisted;
        private int originalQty;
        private String goldMerchantLogoUrl;
        private String officialStoreLogoUrl;
        private String preOrderInfo;

        public String getTrackerAttribution() {
            return trackerAttribution;
        }

        public void setTrackerAttribution(String trackerAttribution) {
            this.trackerAttribution = trackerAttribution;
        }

        public int getPricePlanInt() {
            return pricePlanInt;
        }

        public void setPricePlanInt(int pricePlanInt) {
            this.pricePlanInt = pricePlanInt;
        }

        public String getTrackerListName() {
            return trackerListName;
        }

        public void setTrackerListName(String trackerListName) {
            this.trackerListName = trackerListName;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategoryForAnalytics() {
            return categoryForAnalytics;
        }

        public void setCategoryForAnalytics(String categoryForAnalytics) {
            this.categoryForAnalytics = categoryForAnalytics;
        }

        public int getCartId() {
            return cartId;
        }

        public void setCartId(int cartId) {
            this.cartId = cartId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public boolean isPreOrder() {
            return isPreOrder;
        }

        public void setPreOrder(boolean preOrder) {
            isPreOrder = preOrder;
        }

        public boolean isFreeReturn() {
            return isFreeReturn;
        }

        public void setFreeReturn(boolean freeReturn) {
            isFreeReturn = freeReturn;
        }

        public boolean isCashBack() {
            return isCashBack;
        }

        public void setCashBack(boolean cashBack) {
            isCashBack = cashBack;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }

        public String getCashBackInfo() {
            return cashBackInfo;
        }

        public void setCashBackInfo(String cashBackInfo) {
            this.cashBackInfo = cashBackInfo;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getMinimalQtyOrder() {
            return minimalQtyOrder;
        }

        public void setMinimalQtyOrder(int minimalQtyOrder) {
            this.minimalQtyOrder = minimalQtyOrder;
        }

        public String getFreeReturnLogo() {
            return freeReturnLogo;
        }

        public void setFreeReturnLogo(String freeReturnLogo) {
            this.freeReturnLogo = freeReturnLogo;
        }

        public double getPricePlan() {
            return pricePlan;
        }

        public void setPricePlan(double pricePlan) {
            this.pricePlan = pricePlan;
        }

        public int getPriceCurrency() {
            return priceCurrency;
        }

        public void setPriceCurrency(int priceCurrency) {
            this.priceCurrency = priceCurrency;
        }

        public String getPriceFormatted() {
            return priceFormatted;
        }

        public void setPriceFormatted(String priceFormatted) {
            this.priceFormatted = priceFormatted;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductVarianRemark() {
            return productVarianRemark;
        }

        public void setProductVarianRemark(String productVarianRemark) {
            this.productVarianRemark = productVarianRemark;
        }

        public double getWeightPlan() {
            return weightPlan;
        }

        public void setWeightPlan(double weightPlan) {
            this.weightPlan = weightPlan;
        }

        public int getWeightUnit() {
            return weightUnit;
        }

        public void setWeightUnit(int weightUnit) {
            this.weightUnit = weightUnit;
        }

        public String getWeightFormatted() {
            return weightFormatted;
        }

        public void setWeightFormatted(String weightFormatted) {
            this.weightFormatted = weightFormatted;
        }

        public String getPreOrderInfo() {
            return preOrderInfo;
        }

        public void setPreOrderInfo(String preOrderInfo) {
            this.preOrderInfo = preOrderInfo;
        }

        public int getInvenageValue() {
            return invenageValue;
        }

        public void setInvenageValue(int invenageValue) {
            this.invenageValue = invenageValue;
        }

        public List<WholesalePrice> getWholesalePrice() {
            return wholesalePrice;
        }

        public void setWholesalePrice(List<WholesalePrice> wholesalePrice) {
            this.wholesalePrice = wholesalePrice;
        }

        public String getWholesalePriceFormatted() {
            return wholesalePriceFormatted;
        }

        public void setWholesalePriceFormatted(String wholesalePriceFormatted) {
            this.wholesalePriceFormatted = wholesalePriceFormatted;
        }

        public String getProductCashBack() {
            return productCashBack;
        }

        public void setProductCashBack(String productCashBack) {
            this.productCashBack = productCashBack;
        }

        public String getOriginalRemark() {
            return originalRemark;
        }

        public void setOriginalRemark(String originalRemark) {
            this.originalRemark = originalRemark;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public boolean isOfficialStore() {
            return officialStore;
        }

        public void setOfficialStore(boolean officialStore) {
            this.officialStore = officialStore;
        }

        public boolean isGoldMerchant() {
            return goldMerchant;
        }

        public void setGoldMerchant(boolean goldMerchant) {
            this.goldMerchant = goldMerchant;
        }

        public String getGoldMerchantLogoUrl() {
            return goldMerchantLogoUrl;
        }

        public void setGoldMerchantLogoUrl(String goldMerchantLogoUrl) {
            this.goldMerchantLogoUrl = goldMerchantLogoUrl;
        }

        public String getOfficialStoreLogoUrl() {
            return officialStoreLogoUrl;
        }

        public void setOfficialStoreLogoUrl(String officialStoreLogoUrl) {
            this.officialStoreLogoUrl = officialStoreLogoUrl;
        }

        public boolean isWishlisted() {
            return wishlisted;
        }

        public void setWishlisted(boolean wishlisted) {
            this.wishlisted = wishlisted;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

        public int getOriginalQty() {
            return originalQty;
        }

        public void setOriginalQty(int originalQty) {
            this.originalQty = originalQty;
        }

        public boolean isCod() {
            return isCod;
        }

        public void setCod(boolean cod) {
            isCod = cod;
        }

        public OriginData() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.cartId);
            dest.writeString(this.parentId);
            dest.writeString(this.productId);
            dest.writeString(this.productName);
            dest.writeInt(this.minimalQtyOrder);
            dest.writeInt(this.invenageValue);
            dest.writeDouble(this.pricePlan);
            dest.writeInt(this.pricePlanInt);
            dest.writeInt(this.priceCurrency);
            dest.writeString(this.priceFormatted);
            dest.writeString(this.wholesalePriceFormatted);
            dest.writeString(this.productImage);
            dest.writeString(this.productVarianRemark);
            dest.writeDouble(this.weightPlan);
            dest.writeInt(this.weightUnit);
            dest.writeString(this.weightFormatted);
            dest.writeByte(this.isPreOrder ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFreeReturn ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isCashBack ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
            dest.writeByte(this.officialStore ? (byte) 1 : (byte) 0);
            dest.writeByte(this.goldMerchant ? (byte) 1 : (byte) 0);
            dest.writeString(this.cashBackInfo);
            dest.writeString(this.freeReturnLogo);
            dest.writeString(this.category);
            dest.writeString(this.categoryForAnalytics);
            dest.writeString(this.categoryId);
            dest.writeTypedList(this.wholesalePrice);
            dest.writeString(this.trackerAttribution);
            dest.writeString(this.trackerListName);
            dest.writeString(this.productCashBack);
            dest.writeString(this.originalRemark);
            dest.writeByte(this.wishlisted ? (byte) 1 : (byte) 0);
            dest.writeString(this.shopName);
            dest.writeString(this.shopId);
            dest.writeString(this.shopType);
            dest.writeInt(this.originalQty);
            dest.writeString(this.goldMerchantLogoUrl);
            dest.writeString(this.officialStoreLogoUrl);
            dest.writeString(this.preOrderInfo);
        }

        protected OriginData(Parcel in) {
            this.cartId = in.readInt();
            this.parentId = in.readString();
            this.productId = in.readString();
            this.productName = in.readString();
            this.minimalQtyOrder = in.readInt();
            this.invenageValue = in.readInt();
            this.pricePlan = in.readDouble();
            this.pricePlanInt = in.readInt();
            this.priceCurrency = in.readInt();
            this.priceFormatted = in.readString();
            this.wholesalePriceFormatted = in.readString();
            this.productImage = in.readString();
            this.productVarianRemark = in.readString();
            this.weightPlan = in.readDouble();
            this.weightUnit = in.readInt();
            this.weightFormatted = in.readString();
            this.isPreOrder = in.readByte() != 0;
            this.isFreeReturn = in.readByte() != 0;
            this.isCashBack = in.readByte() != 0;
            this.isFavorite = in.readByte() != 0;
            this.officialStore = in.readByte() != 0;
            this.goldMerchant = in.readByte() != 0;
            this.cashBackInfo = in.readString();
            this.freeReturnLogo = in.readString();
            this.category = in.readString();
            this.categoryForAnalytics = in.readString();
            this.categoryId = in.readString();
            this.wholesalePrice = in.createTypedArrayList(WholesalePrice.CREATOR);
            this.trackerAttribution = in.readString();
            this.trackerListName = in.readString();
            this.productCashBack = in.readString();
            this.originalRemark = in.readString();
            this.wishlisted = in.readByte() != 0;
            this.shopName = in.readString();
            this.shopId = in.readString();
            this.shopType = in.readString();
            this.originalQty = in.readInt();
            this.goldMerchantLogoUrl = in.readString();
            this.officialStoreLogoUrl = in.readString();
            this.preOrderInfo = in.readString();
        }

        public static final Creator<OriginData> CREATOR = new Creator<OriginData>() {
            @Override
            public OriginData createFromParcel(Parcel source) {
                return new OriginData(source);
            }

            @Override
            public OriginData[] newArray(int size) {
                return new OriginData[size];
            }
        };
    }

    public static class UpdatedData implements Parcelable {
        private int quantity;
        private String remark;
        private int maxQuantity;
        private int maxCharRemark;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public UpdatedData() {
        }

        public int getMaxQuantity() {
            return maxQuantity;
        }

        public void setMaxQuantity(int maxQuantity) {
            this.maxQuantity = maxQuantity;
        }

        public int getMaxCharRemark() {
            return maxCharRemark;
        }

        public void setMaxCharRemark(int maxCharRemark) {
            this.maxCharRemark = maxCharRemark;
        }

        public void resetQuantity() {
            quantity = 0;
        }

        public void decreaseQuantity() {
            if (quantity > 0)
                this.quantity--;
        }

        public void increaseQuantity() {
            this.quantity++;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.quantity);
            dest.writeString(this.remark);
            dest.writeInt(this.maxQuantity);
            dest.writeInt(this.maxCharRemark);
        }

        protected UpdatedData(Parcel in) {
            this.quantity = in.readInt();
            this.remark = in.readString();
            this.maxQuantity = in.readInt();
            this.maxCharRemark = in.readInt();
        }

        public static final Creator<UpdatedData> CREATOR = new Creator<UpdatedData>() {
            @Override
            public UpdatedData createFromParcel(Parcel source) {
                return new UpdatedData(source);
            }

            @Override
            public UpdatedData[] newArray(int size) {
                return new UpdatedData[size];
            }
        };
    }

    public static class MessageErrorData implements Parcelable {
        private String errorCheckoutPriceLimit;
        private String errorFieldBetween;
        private String errorFieldMaxChar;
        private String errorFieldRequired;
        private String errorProductAvailableStock;
        private String errorProductAvailableStockDetail;
        private String errorProductMaxQuantity;
        private String errorProductMinQuantity;

        public String getErrorCheckoutPriceLimit() {
            return errorCheckoutPriceLimit;
        }

        public void setErrorCheckoutPriceLimit(String errorCheckoutPriceLimit) {
            this.errorCheckoutPriceLimit = errorCheckoutPriceLimit;
        }

        public String getErrorFieldBetween() {
            return errorFieldBetween;
        }

        public void setErrorFieldBetween(String errorFieldBetween) {
            this.errorFieldBetween = errorFieldBetween;
        }

        public String getErrorFieldMaxChar() {
            return errorFieldMaxChar;
        }

        public void setErrorFieldMaxChar(String errorFieldMaxChar) {
            this.errorFieldMaxChar = errorFieldMaxChar;
        }

        public String getErrorFieldRequired() {
            return errorFieldRequired;
        }

        public void setErrorFieldRequired(String errorFieldRequired) {
            this.errorFieldRequired = errorFieldRequired;
        }

        public String getErrorProductAvailableStock() {
            return errorProductAvailableStock;
        }

        public void setErrorProductAvailableStock(String errorProductAvailableStock) {
            this.errorProductAvailableStock = errorProductAvailableStock;
        }

        public String getErrorProductAvailableStockDetail() {
            return errorProductAvailableStockDetail;
        }

        public void setErrorProductAvailableStockDetail(String errorProductAvailableStockDetail) {
            this.errorProductAvailableStockDetail = errorProductAvailableStockDetail;
        }

        public String getErrorProductMaxQuantity() {
            return errorProductMaxQuantity;
        }

        public void setErrorProductMaxQuantity(String errorProductMaxQuantity) {
            this.errorProductMaxQuantity = errorProductMaxQuantity;
        }

        public String getErrorProductMinQuantity() {
            return errorProductMinQuantity;
        }

        public void setErrorProductMinQuantity(String errorProductMinQuantity) {
            this.errorProductMinQuantity = errorProductMinQuantity;
        }

        public MessageErrorData() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.errorCheckoutPriceLimit);
            dest.writeString(this.errorFieldBetween);
            dest.writeString(this.errorFieldMaxChar);
            dest.writeString(this.errorFieldRequired);
            dest.writeString(this.errorProductAvailableStock);
            dest.writeString(this.errorProductAvailableStockDetail);
            dest.writeString(this.errorProductMaxQuantity);
            dest.writeString(this.errorProductMinQuantity);
        }

        protected MessageErrorData(Parcel in) {
            this.errorCheckoutPriceLimit = in.readString();
            this.errorFieldBetween = in.readString();
            this.errorFieldMaxChar = in.readString();
            this.errorFieldRequired = in.readString();
            this.errorProductAvailableStock = in.readString();
            this.errorProductAvailableStockDetail = in.readString();
            this.errorProductMaxQuantity = in.readString();
            this.errorProductMinQuantity = in.readString();
        }

        public static final Creator<MessageErrorData> CREATOR = new Creator<MessageErrorData>() {
            @Override
            public MessageErrorData createFromParcel(Parcel source) {
                return new MessageErrorData(source);
            }

            @Override
            public MessageErrorData[] newArray(int size) {
                return new MessageErrorData[size];
            }
        };
    }

    public CartItemData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.originData, flags);
        dest.writeParcelable(this.updatedData, flags);
        dest.writeParcelable(this.errorData, flags);
        dest.writeByte(this.isError ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWarning ? (byte) 1 : (byte) 0);
        dest.writeByte(this.singleChild ? (byte) 1 : (byte) 0);
        dest.writeByte(this.parentHasErrorOrWarning ? (byte) 1 : (byte) 0);
        dest.writeString(this.warningMessageTitle);
        dest.writeString(this.errorMessageTitle);
    }

    protected CartItemData(Parcel in) {
        this.originData = in.readParcelable(OriginData.class.getClassLoader());
        this.updatedData = in.readParcelable(UpdatedData.class.getClassLoader());
        this.errorData = in.readParcelable(MessageErrorData.class.getClassLoader());
        this.isError = in.readByte() != 0;
        this.isWarning = in.readByte() != 0;
        this.singleChild = in.readByte() != 0;
        this.parentHasErrorOrWarning = in.readByte() != 0;
        this.warningMessageTitle = in.readString();
        this.errorMessageTitle = in.readString();
    }

    public static final Creator<CartItemData> CREATOR = new Creator<CartItemData>() {
        @Override
        public CartItemData createFromParcel(Parcel source) {
            return new CartItemData(source);
        }

        @Override
        public CartItemData[] newArray(int size) {
            return new CartItemData[size];
        }
    };
}
