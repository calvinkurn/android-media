package com.tokopedia.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class Product {

    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("product_id")
    @Expose
    private long productId;
    @SerializedName("cart_id")
    @Expose
    private long cartId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_price_fmt")
    @Expose
    private String productPriceFmt;
    @SerializedName("product_price")
    @Expose
    private long productPrice;
    @SerializedName("product_original_price")
    @Expose
    private long productOriginalPrice;
    @SerializedName("product_wholesale_price")
    @Expose
    private long productWholesalePrice;
    @SerializedName("product_wholesale_price_fmt")
    @Expose
    private String productWholesalePriceFmt;
    @SerializedName("product_weight_fmt")
    @Expose
    private String productWeightFmt;
    @SerializedName("product_weight")
    @Expose
    private int productWeight;
    @SerializedName("product_condition")
    @Expose
    private int productCondition;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_returnable")
    @Expose
    private int productReturnable;
    @SerializedName("product_is_free_returns")
    @Expose
    private int productIsFreeReturns;
    @SerializedName("product_is_preorder")
    @Expose
    private int productIsPreorder;
    @SerializedName("product_cashback")
    @Expose
    private String productCashback;
    @SerializedName("product_min_order")
    @Expose
    private int productMinOrder;
    @SerializedName("product_invenage_value")
    @Expose
    private int productInvenageValue;
    @SerializedName("product_switch_invenage")
    @Expose
    private int productSwitchInvenage;
    @SerializedName("product_price_currency")
    @Expose
    private int productPriceCurrency;
    @SerializedName("product_image_src_200_square")
    @Expose
    private String productImageSrc200Square;
    @SerializedName("product_notes")
    @Expose
    private String productNotes;
    @SerializedName("product_quantity")
    @Expose
    private int productQuantity;
    @SerializedName("product_menu_id")
    @Expose
    private int productMenuId;
    @SerializedName("product_finsurance")
    @Expose
    private int productFinsurance;
    @SerializedName("product_fcancel_partial")
    @Expose
    private int productFcancelPartial;
    @SerializedName("product_shipment")
    @Expose
    private List<ProductShipment> productShipment = new ArrayList<>();
    @SerializedName("product_shipment_mapping")
    @Expose
    private List<ProductShipmentMapping> productShipmentMapping = new ArrayList<>();
    @SerializedName("product_cat_id")
    @Expose
    private int productCatId;
    @SerializedName("product_catalog_id")
    @Expose
    private int productCatalogId;
    @SerializedName("purchase_protection_plan_data")
    @Expose
    private PurchaseProtectionPlanDataResponse purchaseProtectionPlanDataResponse;
    @SerializedName("free_returns")
    @Expose
    private FreeReturns freeReturns;
    @SerializedName("product_category")
    @Expose
    private String productCategory;
    @SerializedName("product_tracker_data")
    @Expose
    private ProductTrackerData productTrackerData;
    @SerializedName("product_preorder")
    @Expose
    private ProductPreorder productPreorder;
    @SerializedName("trade_in_info")
    @Expose
    private TradeInInfo tradeInInfo;
    @SerializedName("free_shipping")
    private FreeShipping freeShipping;
    @SerializedName("free_shipping_extra")
    private FreeShipping freeShippingExtra;
    @SerializedName("product_ticker")
    @Expose
    private ProductTicker productTicker;
    @SerializedName("variant_description_detail")
    @Expose
    private VariantDescriptionDetail variantDescriptionDetail;
    @SerializedName("product_alert_message")
    @Expose
    private String productAlertMessage;
    @SerializedName("product_information")
    @Expose
    private List<String> productInformation;
    @SerializedName("campaign_id")
    @Expose
    private int campaignId;

    public String getProductCategory() {
        return productCategory;
    }

    public ProductTrackerData getProductTrackerData() {
        return productTrackerData;
    }

    public List<String> getErrors() {
        return errors;
    }

    public FreeReturns getFreeReturns() {
        return freeReturns;
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

    public int getProductReturnable() {
        return productReturnable;
    }

    public int getProductIsFreeReturns() {
        return productIsFreeReturns;
    }

    public int getProductIsPreorder() {
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

    public int getProductFinsurance() {
        return productFinsurance;
    }

    public int getProductFcancelPartial() {
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

    public ProductPreorder getProductPreorder() {
        return productPreorder;
    }

    public PurchaseProtectionPlanDataResponse getPurchaseProtectionPlanDataResponse() {
        return purchaseProtectionPlanDataResponse;
    }

    public TradeInInfo getTradeInInfo() {
        return tradeInInfo;
    }

    public FreeShipping getFreeShipping() {
        return freeShipping;
    }

    public FreeShipping getFreeShippingExtra() {
        return freeShippingExtra;
    }

    public ProductTicker getProductTicker() {
        return productTicker;
    }

    public VariantDescriptionDetail getVariantDescriptionDetail() {
        return variantDescriptionDetail;
    }

    public String getProductAlertMessage() {
        return productAlertMessage;
    }

    public List<String> getProductInformation() {
        return productInformation;
    }

    public int getCampaignId() {
        return campaignId;
    }
}
