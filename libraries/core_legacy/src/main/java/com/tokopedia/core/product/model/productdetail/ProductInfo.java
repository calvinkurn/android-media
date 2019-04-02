package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 28/10/2015.
 */
@Deprecated
public class ProductInfo implements Parcelable {
    private static final String TAG = ProductInfo.class.getSimpleName();

    public static final String PRD_STATE_ACTIVE = "1";
    public static final String PRD_STATE_PENDING = "-1";
    public static final String PRD_STATE_WAREHOUSE = "3";

    @SerializedName("product_weight_unit")
    @Expose
    private String productWeightUnit;
    @SerializedName("product_etalase_id")
    @Expose
    private String productEtalaseId;
    @SerializedName("product_already_wishlist")
    @Expose
    private Integer productAlreadyWishlist;
    @SerializedName("product_insurance")
    @Expose
    private String productInsurance;
    @SerializedName("product_condition")
    @Expose
    private String productCondition;
    @SerializedName("product_key")
    @Expose
    private String productKey;
    @SerializedName("product_etalase")
    @Expose
    private String productEtalase;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("product_returnable")
    @Expose
    private Integer productReturnable;
    @SerializedName("product_min_order")
    @Expose
    private String productMinOrder;
    @SerializedName("product_last_update")
    @Expose
    private String productLastUpdate;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("product_price_alert")
    @Expose
    private String productPriceAlert;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("catalog_id")
    @Expose
    private String productCatalogId;
    @SerializedName("catalog_name")
    @Expose
    private String productCatalogName;
    @SerializedName("catalog_url")
    @Expose
    private String productCatalogUrl;
    @SerializedName("product_status_message")
    @Expose
    private String productStatusMessage;
    @SerializedName("product_status_title")
    @Expose
    private String productStatusTitle;

    @SerializedName("return_info")
    @Expose
    private ReturnInfo returnInfo;

    @SerializedName("product_installments")
    @Expose
    private List<ProductInstallment> productInstallments = null;

    @SerializedName("wholesale_min_price")
    @Expose
    private String wholseSaleMinPrice;
    @SerializedName("wholesale_min_quantity")
    @Expose
    private String wholeSaleMinQuantity;
    @SerializedName("installment_min_percentage")
    @Expose
    private String installmentMinPercentage;
    @SerializedName("installment_min_price")
    @Expose
    private String installmentMinPrice;

    @SerializedName("product_price_unfmt")
    @Expose
    private Integer productPriceUnformatted;

    @SerializedName("product_original_price")
    @Expose
    private Integer productOriginalPrice;

    @SerializedName("has_variant")
    @Expose
    private Boolean hasVariant = false;

    @SerializedName("is_cod")
    @Expose
    private boolean isCod = false;

    private String productStockWording;
    private boolean alwaysAvailable;

    private boolean limitedStock = false;

    public ProductInfo() {
    }

    public List<ProductInstallment> getProductInstallments() {
        return productInstallments;
    }

    public void setProductInstallments(List<ProductInstallment> productInstallments) {
        this.productInstallments = productInstallments;
    }

    public boolean isAlwaysAvailable() {
        return alwaysAvailable;
    }

    public void setAlwaysAvailable(boolean alwaysAvailable) {
        this.alwaysAvailable = alwaysAvailable;
    }

    public String getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(String productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public String getProductEtalaseId() {
        return productEtalaseId;
    }

    public void setProductEtalaseId(String productEtalaseId) {
        this.productEtalaseId = productEtalaseId;
    }

    public Integer getProductAlreadyWishlist() {
        return productAlreadyWishlist;
    }

    public void setProductAlreadyWishlist(Integer productAlreadyWishlist) {
        this.productAlreadyWishlist = productAlreadyWishlist;
    }

    public String getProductInsurance() {
        return productInsurance;
    }

    public void setProductInsurance(String productInsurance) {
        this.productInsurance = productInsurance;
    }

    public String getProductCondition() {
        return productCondition;
    }

    public void setProductCondition(String productCondition) {
        this.productCondition = productCondition;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductEtalase() {
        return productEtalase;
    }

    public void setProductEtalase(String productEtalase) {
        this.productEtalase = productEtalase;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(Integer productReturnable) {
        this.productReturnable = productReturnable;
    }

    public String getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(String productMinOrder) {
        this.productMinOrder = productMinOrder;
    }

    public String getProductLastUpdate() {
        return productLastUpdate;
    }

    public void setProductLastUpdate(String productLastUpdate) {
        this.productLastUpdate = productLastUpdate;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductPriceAlert() {
        return productPriceAlert;
    }

    public void setProductPriceAlert(String productPriceAlert) {
        this.productPriceAlert = productPriceAlert;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductCatalogId() {
        return productCatalogId;
    }

    public void setProductCatalogId(String productCatalogId) {
        this.productCatalogId = productCatalogId;
    }

    public String getProductCatalogName() {
        return productCatalogName;
    }

    public void setProductCatalogName(String productCatalogName) {
        this.productCatalogName = productCatalogName;
    }

    public String getProductCatalogUrl() {
        return productCatalogUrl;
    }

    public void setProductCatalogUrl(String productCatalogUrl) {
        this.productCatalogUrl = productCatalogUrl;
    }

    public String getProductStatusMessage() {
        return productStatusMessage;
    }

    public void setProductStatusMessage(String productStatusMessage) {
        this.productStatusMessage = productStatusMessage;
    }

    public String getProductStatusTitle() {
        return productStatusTitle;
    }

    public void setProductStatusTitle(String productStatusTitle) {
        this.productStatusTitle = productStatusTitle;
    }

    public ReturnInfo getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(ReturnInfo returnInfo) {
        this.returnInfo = returnInfo;
    }

    public String getWholseSaleMinPrice() {
        return wholseSaleMinPrice;
    }

    public void setWholseSaleMinPrice(String wholseSaleMinPrice) {
        this.wholseSaleMinPrice = wholseSaleMinPrice;
    }

    public String getWholeSaleMinQuantity() {
        return wholeSaleMinQuantity;
    }

    public void setWholeSaleMinQuantity(String wholeSaleMinQuantity) {
        this.wholeSaleMinQuantity = wholeSaleMinQuantity;
    }

    public String getInstallmentMinPercentage() {
        return installmentMinPercentage;
    }

    public void setInstallmentMinPercentage(String installmentMinPercentage) {
        this.installmentMinPercentage = installmentMinPercentage;
    }

    public String getInstallmentMinPrice() {
        return installmentMinPrice;
    }

    public void setInstallmentMinPrice(String installmentMinPrice) {
        this.installmentMinPrice = installmentMinPrice;
    }

    public Integer getProductPriceUnformatted() {
        return productPriceUnformatted;
    }

    public void setProductPriceUnformatted(Integer productPriceUnformatted) {
        this.productPriceUnformatted = productPriceUnformatted;
    }

    public Boolean getHasVariant() {
        return hasVariant != null ? hasVariant : false;
    }

    public void setHasVariant(Boolean hasVariant) {
        this.hasVariant = hasVariant;
    }

    public boolean isCod() {
        return isCod;
    }

    public void setCod(boolean cod) {
        isCod = cod;
    }

    protected ProductInfo(Parcel in) {
        productWeightUnit = in.readString();
        productEtalaseId = in.readString();
        productAlreadyWishlist = in.readByte() == 0x00 ? null : in.readInt();
        productInsurance = in.readString();
        productCondition = in.readString();
        productKey = in.readString();
        productEtalase = in.readString();
        productStatus = in.readString();
        productId = in.readByte() == 0x00 ? null : in.readInt();
        productPrice = in.readString();
        productDescription = in.readString();
        productReturnable = in.readByte() == 0x00 ? null : in.readInt();
        productMinOrder = in.readString();
        productLastUpdate = in.readString();
        productWeight = in.readString();
        productPriceAlert = in.readString();
        productName = in.readString();
        productUrl = in.readString();
        productCatalogId = in.readString();
        productCatalogName = in.readString();
        productCatalogUrl = in.readString();
        returnInfo = (ReturnInfo) in.readValue(ReturnInfo.class.getClassLoader());
        if (in.readByte() == 0x01) {
            productInstallments = new ArrayList<ProductInstallment>();
            in.readList(productInstallments, ProductInstallment.class.getClassLoader());
        } else {
            productInstallments = null;
        }
        wholseSaleMinPrice = in.readString();
        wholeSaleMinQuantity = in.readString();
        installmentMinPercentage = in.readString();
        installmentMinPrice = in.readString();
        productPriceUnformatted = in.readByte() == 0x00 ? null : in.readInt();
        byte hasVariantVal = in.readByte();
        hasVariant = hasVariantVal == 0x02 ? null : hasVariantVal != 0x00;
        isCod = in.readInt() == 1;
        productStockWording = in.readString();
        limitedStock = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productWeightUnit);
        dest.writeString(productEtalaseId);
        if (productAlreadyWishlist == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productAlreadyWishlist);
        }
        dest.writeString(productInsurance);
        dest.writeString(productCondition);
        dest.writeString(productKey);
        dest.writeString(productEtalase);
        dest.writeString(productStatus);
        if (productId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productId);
        }
        dest.writeString(productPrice);
        dest.writeString(productDescription);
        if (productReturnable == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productReturnable);
        }
        dest.writeString(productMinOrder);
        dest.writeString(productLastUpdate);
        dest.writeString(productWeight);
        dest.writeString(productPriceAlert);
        dest.writeString(productName);
        dest.writeString(productUrl);
        dest.writeString(productCatalogId);
        dest.writeString(productCatalogName);
        dest.writeString(productCatalogUrl);
        dest.writeValue(returnInfo);
        if (productInstallments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(productInstallments);
        }
        dest.writeString(wholseSaleMinPrice);
        dest.writeString(wholeSaleMinQuantity);
        dest.writeString(installmentMinPercentage);
        dest.writeString(installmentMinPrice);
        if (productPriceUnformatted == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productPriceUnformatted);
        }
        if (hasVariant == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (hasVariant ? 0x01 : 0x00));
        }
        dest.writeInt(isCod? 1 : 0);
        dest.writeString(productStockWording);
        dest.writeByte((byte) (limitedStock ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel in) {
            return new ProductInfo(in);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };

    public String getProductStockWording() {
        return productStockWording;
    }

    public void setProductStockWording(String productStockWording) {
        this.productStockWording = productStockWording;
    }

    public boolean getLimitedStock() {
        return limitedStock;
    }

    public void setLimitedStock(boolean limitedStock) {
        this.limitedStock = limitedStock;
    }

    public Integer getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public void setProductOriginalPrice(Integer productOriginalPrice) {
        this.productOriginalPrice = productOriginalPrice;
    }


    public static class Builder {
        private String productWeightUnit;
        private String productEtalaseId;
        private Integer productAlreadyWishlist;
        private String productInsurance;
        private String productCondition;
        private String productKey;
        private String productEtalase;
        private String productStatus;
        private Integer productId;
        private String productPrice;
        private String productDescription;
        private Integer productReturnable;
        private String productMinOrder;
        private String productLastUpdate;
        private String productWeight;
        private String productPriceAlert;
        private String productName;
        private String productUrl;
        private String productCatalogId;
        private String productCatalogName;
        private String productCatalogUrl;
        private String productStockWording;
        private boolean limitedStock = false;

        private Builder() {
        }

        public static Builder aProductInfo() {
            return new Builder();
        }

        public Builder setProductWeightUnit(String productWeightUnit) {
            this.productWeightUnit = productWeightUnit;
            return this;
        }

        public Builder setProductEtalaseId(String productEtalaseId) {
            this.productEtalaseId = productEtalaseId;
            return this;
        }

        public Builder setProductAlreadyWishlist(Integer productAlreadyWishlist) {
            this.productAlreadyWishlist = productAlreadyWishlist;
            return this;
        }

        public Builder setProductInsurance(String productInsurance) {
            this.productInsurance = productInsurance;
            return this;
        }

        public Builder setProductCondition(String productCondition) {
            this.productCondition = productCondition;
            return this;
        }

        public Builder setProductKey(String productKey) {
            this.productKey = productKey;
            return this;
        }

        public Builder setProductEtalase(String productEtalase) {
            this.productEtalase = productEtalase;
            return this;
        }

        public Builder setProductStatus(String productStatus) {
            this.productStatus = productStatus;
            return this;
        }

        public Builder setProductId(Integer productId) {
            this.productId = productId;
            return this;
        }

        public Builder setProductPrice(String productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Builder setProductDescription(String productDescription) {
            this.productDescription = productDescription;
            return this;
        }

        public Builder setProductReturnable(Integer productReturnable) {
            this.productReturnable = productReturnable;
            return this;
        }

        public Builder setProductMinOrder(String productMinOrder) {
            this.productMinOrder = productMinOrder;
            return this;
        }

        public Builder setProductLastUpdate(String productLastUpdate) {
            this.productLastUpdate = productLastUpdate;
            return this;
        }

        public Builder setProductWeight(String productWeight) {
            this.productWeight = productWeight;
            return this;
        }

        public Builder setProductPriceAlert(String productPriceAlert) {
            this.productPriceAlert = productPriceAlert;
            return this;
        }

        public Builder setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder setProductUrl(String productUrl) {
            this.productUrl = productUrl;
            return this;
        }

        public Builder setProductCatalogId(String productCatalogId) {
            this.productCatalogId = productCatalogId;
            return this;
        }

        public Builder setProductCatalogName(String productCatalogName) {
            this.productCatalogName = productCatalogName;
            return this;
        }

        public Builder setProductCatalogUrl(String productCatalogUrl) {
            this.productCatalogUrl = productCatalogUrl;
            return this;
        }

        public void setProductStockWording(String productStockWording) {
            this.productStockWording = productStockWording;
        }

        public void setLimitedStock(boolean limitedStock) {
            this.limitedStock = limitedStock;
        }

        public Builder but() {
            return aProductInfo().setProductWeightUnit(productWeightUnit).setProductEtalaseId(productEtalaseId).setProductAlreadyWishlist(productAlreadyWishlist).setProductInsurance(productInsurance).setProductCondition(productCondition).setProductKey(productKey).setProductEtalase(productEtalase).setProductStatus(productStatus).setProductId(productId).setProductPrice(productPrice).setProductDescription(productDescription).setProductReturnable(productReturnable).setProductMinOrder(productMinOrder).setProductLastUpdate(productLastUpdate).setProductWeight(productWeight).setProductPriceAlert(productPriceAlert).setProductName(productName).setProductUrl(productUrl).setProductCatalogId(productCatalogId).setProductCatalogName(productCatalogName).setProductCatalogUrl(productCatalogUrl);
        }

        public ProductInfo build() {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setProductWeightUnit(productWeightUnit);
            productInfo.setProductEtalaseId(productEtalaseId);
            productInfo.setProductAlreadyWishlist(productAlreadyWishlist);
            productInfo.setProductInsurance(productInsurance);
            productInfo.setProductCondition(productCondition);
            productInfo.setProductKey(productKey);
            productInfo.setProductEtalase(productEtalase);
            productInfo.setProductStatus(productStatus);
            productInfo.setProductId(productId);
            productInfo.setProductPrice(productPrice);
            productInfo.setProductDescription(productDescription);
            productInfo.setProductReturnable(productReturnable);
            productInfo.setProductMinOrder(productMinOrder);
            productInfo.setProductLastUpdate(productLastUpdate);
            productInfo.setProductWeight(productWeight);
            productInfo.setProductPriceAlert(productPriceAlert);
            productInfo.setProductName(productName);
            productInfo.setProductUrl(productUrl);
            productInfo.setProductCatalogId(productCatalogId);
            productInfo.setProductCatalogName(productCatalogName);
            productInfo.setProductCatalogUrl(productCatalogUrl);
            productInfo.setProductStockWording(productStockWording);
            productInfo.setLimitedStock(limitedStock);
            return productInfo;
        }
    }
}
