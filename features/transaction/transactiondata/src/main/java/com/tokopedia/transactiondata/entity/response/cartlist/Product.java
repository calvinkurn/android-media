package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class Product {

    @SerializedName("parent_id")
    @Expose
    private int parentId;
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_name")
    @Expose
    private String productName = "";
    @SerializedName("sku")
    @Expose
    private String sku = "";
    @SerializedName("campaign_id")
    @Expose
    private int campaignId;
    @SerializedName("free_returns")
    @Expose
    private FreeReturns freeReturns = new FreeReturns();
    @SerializedName("product_price_fmt")
    @Expose
    private String productPriceFmt = "";
    @SerializedName("product_price")
    @Expose
    private int productPrice;
    @SerializedName("product_original_price")
    @Expose
    private int productOriginalPrice;
    @SerializedName("is_slash_price")
    @Expose
    private boolean isSlashPrice;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("category")
    @Expose
    private String category = "";
    @SerializedName("catalog_id")
    @Expose
    private int catalogId;
    @SerializedName("wholesale_price")
    @Expose
    private List<WholesalePrice> wholesalePrice = new ArrayList<>();
    @SerializedName("product_weight_fmt")
    @Expose
    private String productWeightFmt = "";
    @SerializedName("product_condition")
    @Expose
    private int productCondition;
    @SerializedName("product_status")
    @Expose
    private int productStatus;
    @SerializedName("product_url")
    @Expose
    private String productUrl = "";
    @SerializedName("product_returnable")
    @Expose
    private int productReturnable;
    @SerializedName("is_freereturns")
    @Expose
    private int isFreereturns;
    @SerializedName("is_preorder")
    @Expose
    private int isPreorder;
    @SerializedName("is_cod")
    @Expose
    private boolean isCod;
    @SerializedName("product_cashback")
    @Expose
    private String productCashback = "";
    @SerializedName("product_min_order")
    @Expose
    private int productMinOrder;
    @SerializedName("product_rating")
    @Expose
    private double productRating;
    @SerializedName("product_invenage_value")
    @Expose
    private int productInvenageValue;
    @SerializedName("product_switch_invenage")
    @Expose
    private int productSwitchInvenage;
    @SerializedName("currency_rate")
    @Expose
    private int currencyRate;
    @SerializedName("product_price_currency")
    @Expose
    private int productPriceCurrency;
    @SerializedName("product_image")
    @Expose
    private ProductImage productImage = new ProductImage();
    @SerializedName("product_all_images")
    @Expose
    private String productAllImages = "";
    @SerializedName("product_notes")
    @Expose
    private String productNotes = "";
    @SerializedName("product_quantity")
    @Expose
    private int productQuantity;
    @SerializedName("product_weight")
    @Expose
    private int productWeight;
    @SerializedName("product_weight_unit_code")
    @Expose
    private int productWeightUnitCode;
    @SerializedName("product_weight_unit_text")
    @Expose
    private String productWeightUnitText = "";
    @SerializedName("last_update_price")
    @Expose
    private long lastUpdatePrice;
    @SerializedName("is_update_price")
    @Expose
    private boolean isUpdatePrice;
    @SerializedName("product_preorder")
    @Expose
    private ProductPreorder productPreorder = new ProductPreorder();
    @SerializedName("product_showcase")
    @Expose
    private ProductShowCase productShowcase = new ProductShowCase();
    @SerializedName("product_finsurance")
    @Expose
    private int productFinsurance;
    @SerializedName("product_shop_id")
    @Expose
    private int productShopId;
    @SerializedName("is_wishlisted")
    @Expose
    private boolean isWishlisted;
    @SerializedName("product_tracker_data")
    @Expose
    private ProductTrackerData productTrackerData = new ProductTrackerData();

    public ProductTrackerData getProductTrackerData() {
        return productTrackerData;
    }

    public int getParentId() {
        return parentId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPriceFmt() {
        return productPriceFmt;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategory() {
        return category;
    }

    public int getCatalogId() {
        return catalogId;
    }

    public List<WholesalePrice> getWholesalePrice() {
        return wholesalePrice;
    }

    public String getProductWeightFmt() {
        return productWeightFmt;
    }

    public int getProductCondition() {
        return productCondition;
    }

    public int getProductStatus() {
        return productStatus;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public int getProductReturnable() {
        return productReturnable;
    }

    public int getIsFreereturns() {
        return isFreereturns;
    }

    public int getIsPreorder() {
        return isPreorder;
    }

    public String getProductCashback() {
        return productCashback;
    }

    public int getProductMinOrder() {
        return productMinOrder;
    }

    public double getProductRating() {
        return productRating;
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

    public ProductImage getProductImage() {
        return productImage;
    }

    public String getProductAllImages() {
        return productAllImages;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductWeight() {
        return productWeight;
    }

    public int getProductWeightUnitCode() {
        return productWeightUnitCode;
    }

    public String getProductWeightUnitText() {
        return productWeightUnitText;
    }

    public long getLastUpdatePrice() {
        return lastUpdatePrice;
    }

    public boolean isUpdatePrice() {
        return isUpdatePrice;
    }

    public ProductPreorder getProductPreorder() {
        return productPreorder;
    }

    public ProductShowCase getProductShowcase() {
        return productShowcase;
    }

    public FreeReturns getFreeReturns() {
        return freeReturns;
    }

    public String getSku() {
        return sku;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public int getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public boolean isSlashPrice() {
        return isSlashPrice;
    }

    public int getCurrencyRate() {
        return currencyRate;
    }

    public int getProductFinsurance() {
        return productFinsurance;
    }

    public int getProductShopId() {
        return productShopId;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public boolean isCod() {
        return isCod;
    }

    public void setCod(boolean cod) {
        isCod = cod;
    }
}
