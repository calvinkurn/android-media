
package com.tokopedia.home.account.analytics.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopStats {

    @SerializedName("shop_service_rate")
    @Expose
    private String shopServiceRate = "";
    @SerializedName("shop_speed_rate")
    @Expose
    private String shopSpeedRate = "";
    @SerializedName("shop_total_transaction")
    @Expose
    private String shopTotalTransaction = "";
    @SerializedName("shop_speed_description")
    @Expose
    private String shopSpeedDescription = "";
    @SerializedName("shop_badge_level")
    @Expose
    private ShopBadgeLevel shopBadgeLevel = new ShopBadgeLevel();
    @SerializedName("shop_last_one_month")
    @Expose
    private ShopLastOneMonth shopLastOneMonth = new ShopLastOneMonth();
    @SerializedName("shop_total_product")
    @Expose
    private String shopTotalProduct = "";
    @SerializedName("shop_reputation_score")
    @Expose
    private String shopReputationScore = "";
    @SerializedName("shop_total_transaction_canceled")
    @Expose
    private String shopTotalTransactionCanceled = "";
    @SerializedName("shop_last_six_months")
    @Expose
    private ShopLastSixMonths shopLastSixMonths = new ShopLastSixMonths();
    @SerializedName("shop_total_etalase")
    @Expose
    private String shopTotalEtalase = "";
    @SerializedName("shop_service_description")
    @Expose
    private String shopServiceDescription = "";
    @SerializedName("shop_item_sold")
    @Expose
    private String shopItemSold = "";
    @SerializedName("shop_last_twelve_months")
    @Expose
    private ShopLastTwelveMonths shopLastTwelveMonths = new ShopLastTwelveMonths();
    @SerializedName("shop_accuracy_rate")
    @Expose
    private String shopAccuracyRate = "";
    @SerializedName("shop_accuracy_description")
    @Expose
    private String shopAccuracyDescription = "";

    public String getShopServiceRate() {
        return shopServiceRate;
    }

    public void setShopServiceRate(String shopServiceRate) {
        this.shopServiceRate = shopServiceRate;
    }

    public String getShopSpeedRate() {
        return shopSpeedRate;
    }

    public void setShopSpeedRate(String shopSpeedRate) {
        this.shopSpeedRate = shopSpeedRate;
    }

    public String getShopTotalTransaction() {
        return shopTotalTransaction;
    }

    public void setShopTotalTransaction(String shopTotalTransaction) {
        this.shopTotalTransaction = shopTotalTransaction;
    }

    public String getShopSpeedDescription() {
        return shopSpeedDescription;
    }

    public void setShopSpeedDescription(String shopSpeedDescription) {
        this.shopSpeedDescription = shopSpeedDescription;
    }

    public ShopBadgeLevel getShopBadgeLevel() {
        return shopBadgeLevel;
    }

    public void setShopBadgeLevel(ShopBadgeLevel shopBadgeLevel) {
        this.shopBadgeLevel = shopBadgeLevel;
    }

    public ShopLastOneMonth getShopLastOneMonth() {
        return shopLastOneMonth;
    }

    public void setShopLastOneMonth(ShopLastOneMonth shopLastOneMonth) {
        this.shopLastOneMonth = shopLastOneMonth;
    }

    public String getShopTotalProduct() {
        return shopTotalProduct;
    }

    public void setShopTotalProduct(String shopTotalProduct) {
        this.shopTotalProduct = shopTotalProduct;
    }

    public String getShopReputationScore() {
        return shopReputationScore;
    }

    public void setShopReputationScore(String shopReputationScore) {
        this.shopReputationScore = shopReputationScore;
    }

    public String getShopTotalTransactionCanceled() {
        return shopTotalTransactionCanceled;
    }

    public void setShopTotalTransactionCanceled(String shopTotalTransactionCanceled) {
        this.shopTotalTransactionCanceled = shopTotalTransactionCanceled;
    }

    public ShopLastSixMonths getShopLastSixMonths() {
        return shopLastSixMonths;
    }

    public void setShopLastSixMonths(ShopLastSixMonths shopLastSixMonths) {
        this.shopLastSixMonths = shopLastSixMonths;
    }

    public String getShopTotalEtalase() {
        return shopTotalEtalase;
    }

    public void setShopTotalEtalase(String shopTotalEtalase) {
        this.shopTotalEtalase = shopTotalEtalase;
    }

    public String getShopServiceDescription() {
        return shopServiceDescription;
    }

    public void setShopServiceDescription(String shopServiceDescription) {
        this.shopServiceDescription = shopServiceDescription;
    }

    public String getShopItemSold() {
        return shopItemSold;
    }

    public void setShopItemSold(String shopItemSold) {
        this.shopItemSold = shopItemSold;
    }

    public ShopLastTwelveMonths getShopLastTwelveMonths() {
        return shopLastTwelveMonths;
    }

    public void setShopLastTwelveMonths(ShopLastTwelveMonths shopLastTwelveMonths) {
        this.shopLastTwelveMonths = shopLastTwelveMonths;
    }

    public String getShopAccuracyRate() {
        return shopAccuracyRate;
    }

    public void setShopAccuracyRate(String shopAccuracyRate) {
        this.shopAccuracyRate = shopAccuracyRate;
    }

    public String getShopAccuracyDescription() {
        return shopAccuracyDescription;
    }

    public void setShopAccuracyDescription(String shopAccuracyDescription) {
        this.shopAccuracyDescription = shopAccuracyDescription;
    }
}
