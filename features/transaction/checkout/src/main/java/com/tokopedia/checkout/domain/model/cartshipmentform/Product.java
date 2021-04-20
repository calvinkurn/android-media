package com.tokopedia.checkout.domain.model.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData;
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class Product implements Parcelable {

    private boolean isError;
    private String errorMessage;
    private String errorMessageDescription;

    private long cartId;
    private long productId;
    private String productName;
    private String productPriceFmt;
    private long productPrice;
    private long productOriginalPrice;
    private long productWholesalePrice;
    private String productWholesalePriceFmt;
    private String productWeightFmt;
    private int productWeight;
    private int productCondition;
    private String productUrl;
    private String freeReturnLogo;
    private boolean productReturnable;
    private boolean productIsFreeReturns;
    private boolean productIsPreorder;
    private int preOrderDurationDay;
    private String productCashback;
    private int productMinOrder;
    private int productInvenageValue;
    private int productSwitchInvenage;
    private int productPriceCurrency;
    private String productImageSrc200Square;
    private String productNotes;
    private int productQuantity;
    private int productMenuId;
    private boolean productFinsurance;
    private boolean productFcancelPartial;
    private List<ProductShipment> productShipment = new ArrayList<>();
    private List<ProductShipmentMapping> productShipmentMapping = new ArrayList<>();
    private int productCatId;
    private int productCatalogId;
    private PurchaseProtectionPlanData purchaseProtectionPlanData;
    private String productPreOrderInfo;
    private TradeInInfoData tradeInInfoData;
    private boolean freeShipping;
    private boolean freeShippingExtra;
    private boolean showTicker;
    private String tickerMessage;
    private String variant;
    private String productAlertMessage;
    private List<String> productInformation;

    private AnalyticsProductCheckoutData analyticsProductCheckoutData;

    public AnalyticsProductCheckoutData getAnalyticsProductCheckoutData() {
        return analyticsProductCheckoutData;
    }

    public String getProductPreOrderInfo() {
        return productPreOrderInfo;
    }

    public void setProductPreOrderInfo(String productPreOrderInfo) {
        this.productPreOrderInfo = productPreOrderInfo;
    }

    public void setAnalyticsProductCheckoutData(AnalyticsProductCheckoutData analyticsProductCheckoutData) {
        this.analyticsProductCheckoutData = analyticsProductCheckoutData;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPriceFmt(String productPriceFmt) {
        this.productPriceFmt = productPriceFmt;
    }

    public String getFreeReturnLogo() {
        return freeReturnLogo;
    }

    public void setFreeReturnLogo(String freeReturnLogo) {
        this.freeReturnLogo = freeReturnLogo;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductOriginalPrice(long productOriginalPrice) {
        this.productOriginalPrice = productOriginalPrice;
    }

    public void setProductWholesalePrice(long productWholesalePrice) {
        this.productWholesalePrice = productWholesalePrice;
    }

    public void setProductWholesalePriceFmt(String productWholesalePriceFmt) {
        this.productWholesalePriceFmt = productWholesalePriceFmt;
    }

    public void setProductWeightFmt(String productWeightFmt) {
        this.productWeightFmt = productWeightFmt;
    }

    public void setProductWeight(int productWeight) {
        this.productWeight = productWeight;
    }

    public void setProductCondition(int productCondition) {
        this.productCondition = productCondition;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setProductReturnable(boolean productReturnable) {
        this.productReturnable = productReturnable;
    }

    public void setProductIsFreeReturns(boolean productIsFreeReturns) {
        this.productIsFreeReturns = productIsFreeReturns;
    }

    public void setProductIsPreorder(boolean productIsPreorder) {
        this.productIsPreorder = productIsPreorder;
    }

    public void setProductCashback(String productCashback) {
        this.productCashback = productCashback;
    }

    public void setProductMinOrder(int productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public void setProductInvenageValue(int productInvenageValue) {
        this.productInvenageValue = productInvenageValue;
    }

    public void setProductSwitchInvenage(int productSwitchInvenage) {
        this.productSwitchInvenage = productSwitchInvenage;
    }

    public void setProductPriceCurrency(int productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public void setProductImageSrc200Square(String productImageSrc200Square) {
        this.productImageSrc200Square = productImageSrc200Square;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public void setProductMenuId(int productMenuId) {
        this.productMenuId = productMenuId;
    }

    public void setProductFinsurance(boolean productFinsurance) {
        this.productFinsurance = productFinsurance;
    }

    public void setProductFcancelPartial(boolean productFcancelPartial) {
        this.productFcancelPartial = productFcancelPartial;
    }

    public void setProductShipment(List<ProductShipment> productShipment) {
        this.productShipment = productShipment;
    }

    public void setProductShipmentMapping(List<ProductShipmentMapping> productShipmentMapping) {
        this.productShipmentMapping = productShipmentMapping;
    }

    public void setProductCatId(int productCatId) {
        this.productCatId = productCatId;
    }

    public void setProductCatalogId(int productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public void setPurchaseProtectionPlanData(PurchaseProtectionPlanData purchaseProtectionPlanData) {
        this.purchaseProtectionPlanData = purchaseProtectionPlanData;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPriceFmt() {
        return productPriceFmt;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public long getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public long getProductWholesalePrice() {
        return productWholesalePrice;
    }

    public String getProductWholesalePriceFmt() {
        return productWholesalePriceFmt;
    }

    public String getProductWeightFmt() {
        return productWeightFmt;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public boolean isProductReturnable() {
        return productReturnable;
    }

    public boolean isProductIsFreeReturns() {
        return productIsFreeReturns;
    }

    public boolean isProductIsPreorder() {
        return productIsPreorder;
    }

    public String getProductCashback() {
        return productCashback;
    }

    public int getProductMinOrder() {
        return productMinOrder;
    }

    public int getProductInvenageValue() {
        return productInvenageValue;
    }

    public int getProductSwitchInvenage() {
        return productSwitchInvenage;
    }

    public int getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public String getProductImageSrc200Square() {
        return productImageSrc200Square;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductMenuId() {
        return productMenuId;
    }

    public boolean isProductFinsurance() {
        return productFinsurance;
    }

    public boolean isProductFcancelPartial() {
        return productFcancelPartial;
    }

    public List<ProductShipment> getProductShipment() {
        return productShipment;
    }

    public List<ProductShipmentMapping> getProductShipmentMapping() {
        return productShipmentMapping;
    }

    public int getProductCatId() {
        return productCatId;
    }

    public int getProductCatalogId() {
        return productCatalogId;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public int getPreOrderDurationDay() {
        return preOrderDurationDay;
    }

    public void setPreOrderDurationDay(int preOrderDurationDay) {
        this.preOrderDurationDay = preOrderDurationDay;
    }

    public String getErrorMessageDescription() {
        return errorMessageDescription;
    }

    public void setErrorMessageDescription(String errorMessageDescription) {
        this.errorMessageDescription = errorMessageDescription;
    }

    public PurchaseProtectionPlanData getPurchaseProtectionPlanData() {
        return purchaseProtectionPlanData;
    }

    public TradeInInfoData getTradeInInfoData() {
        return tradeInInfoData;
    }

    public void setTradeInInfoData(TradeInInfoData tradeInInfoData) {
        this.tradeInInfoData = tradeInInfoData;
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public boolean isFreeShippingExtra() {
        return freeShippingExtra;
    }

    public void setFreeShippingExtra(boolean freeShippingExtra) {
        this.freeShippingExtra = freeShippingExtra;
    }

    public boolean isShowTicker() {
        return showTicker;
    }

    public void setShowTicker(boolean showTicker) {
        this.showTicker = showTicker;
    }

    public String getTickerMessage() {
        return tickerMessage;
    }

    public void setTickerMessage(String tickerMessage) {
        this.tickerMessage = tickerMessage;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getProductAlertMessage() {
        return productAlertMessage;
    }

    public void setProductAlertMessage(String productAlertMessage) {
        this.productAlertMessage = productAlertMessage;
    }

    public List<String> getProductInformation() {
        return productInformation;
    }

    public void setProductInformation(List<String> productInformation) {
        this.productInformation = productInformation;
    }

    public Product() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isError ? 1 : 0));
        dest.writeString(errorMessage);
        dest.writeString(errorMessageDescription);
        dest.writeLong(cartId);
        dest.writeLong(productId);
        dest.writeString(productName);
        dest.writeString(productPriceFmt);
        dest.writeLong(productPrice);
        dest.writeLong(productOriginalPrice);
        dest.writeLong(productWholesalePrice);
        dest.writeString(productWholesalePriceFmt);
        dest.writeString(productWeightFmt);
        dest.writeInt(productWeight);
        dest.writeInt(productCondition);
        dest.writeString(productUrl);
        dest.writeString(freeReturnLogo);
        dest.writeByte((byte) (productReturnable ? 1 : 0));
        dest.writeByte((byte) (productIsFreeReturns ? 1 : 0));
        dest.writeByte((byte) (productIsPreorder ? 1 : 0));
        dest.writeInt(preOrderDurationDay);
        dest.writeString(productCashback);
        dest.writeInt(productMinOrder);
        dest.writeInt(productInvenageValue);
        dest.writeInt(productSwitchInvenage);
        dest.writeInt(productPriceCurrency);
        dest.writeString(productImageSrc200Square);
        dest.writeString(productNotes);
        dest.writeInt(productQuantity);
        dest.writeInt(productMenuId);
        dest.writeByte((byte) (productFinsurance ? 1 : 0));
        dest.writeByte((byte) (productFcancelPartial ? 1 : 0));
        dest.writeTypedList(productShipment);
        dest.writeTypedList(productShipmentMapping);
        dest.writeInt(productCatId);
        dest.writeInt(productCatalogId);
        dest.writeParcelable(purchaseProtectionPlanData, flags);
        dest.writeString(productPreOrderInfo);
        dest.writeParcelable(tradeInInfoData, flags);
        dest.writeByte((byte) (freeShipping ? 1 : 0));
        dest.writeByte((byte) (freeShippingExtra ? 1 : 0));
        dest.writeByte((byte) (showTicker ? 1 : 0));
        dest.writeString(tickerMessage);
        dest.writeString(variant);
        dest.writeString(productAlertMessage);
        dest.writeStringList(productInformation);
        dest.writeParcelable(analyticsProductCheckoutData, flags);
    }

    protected Product(Parcel in) {
        isError = in.readByte() != 0;
        errorMessage = in.readString();
        errorMessageDescription = in.readString();
        cartId = in.readLong();
        productId = in.readLong();
        productName = in.readString();
        productPriceFmt = in.readString();
        productPrice = in.readLong();
        productOriginalPrice = in.readLong();
        productWholesalePrice = in.readLong();
        productWholesalePriceFmt = in.readString();
        productWeightFmt = in.readString();
        productWeight = in.readInt();
        productCondition = in.readInt();
        productUrl = in.readString();
        freeReturnLogo = in.readString();
        productReturnable = in.readByte() != 0;
        productIsFreeReturns = in.readByte() != 0;
        productIsPreorder = in.readByte() != 0;
        preOrderDurationDay = in.readInt();
        productCashback = in.readString();
        productMinOrder = in.readInt();
        productInvenageValue = in.readInt();
        productSwitchInvenage = in.readInt();
        productPriceCurrency = in.readInt();
        productImageSrc200Square = in.readString();
        productNotes = in.readString();
        productQuantity = in.readInt();
        productMenuId = in.readInt();
        productFinsurance = in.readByte() != 0;
        productFcancelPartial = in.readByte() != 0;
        productShipment = in.createTypedArrayList(ProductShipment.CREATOR);
        productShipmentMapping = in.createTypedArrayList(ProductShipmentMapping.CREATOR);
        productCatId = in.readInt();
        productCatalogId = in.readInt();
        purchaseProtectionPlanData = in.readParcelable(PurchaseProtectionPlanData.class.getClassLoader());
        productPreOrderInfo = in.readString();
        tradeInInfoData = in.readParcelable(TradeInInfoData.class.getClassLoader());
        freeShipping = in.readByte() != 0;
        freeShippingExtra = in.readByte() != 0;
        showTicker = in.readByte() != 0;
        tickerMessage = in.readString();
        variant = in.readString();
        productAlertMessage = in.readString();
        productInformation = in.createStringArrayList();
        analyticsProductCheckoutData = in.readParcelable(AnalyticsProductCheckoutData.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

}
