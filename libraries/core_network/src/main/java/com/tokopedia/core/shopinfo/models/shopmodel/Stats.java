
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Stats {

    @SerializedName("shop_service_rate")
    @Expose
    public int shopServiceRate;
    @SerializedName("shop_speed_rate")
    @Expose
    public int shopSpeedRate;
    @SerializedName("shop_total_transaction")
    @Expose
    public String shopTotalTransaction;
    @SerializedName("shop_speed_description")
    @Expose
    public String shopSpeedDescription;
    @SerializedName("hide_rate")
    @Expose
    public int hideRate;
    @SerializedName("tx_count")
    @Expose
    public String txCount;
    @SerializedName("rate_failure")
    @Expose
    public float rateFailure;
    @SerializedName("shop_badge_level")
    @Expose
    public ShopBadgeLevel shopBadgeLevel;
    @SerializedName("shop_last_one_month")
    @Expose
    public ShopLastOneMonth shopLastOneMonth;
    @SerializedName("shop_total_product")
    @Expose
    public String shopTotalProduct;
    @SerializedName("shop_reputation_score")
    @Expose
    public String shopReputationScore;
    @SerializedName("shop_total_transaction_canceled")
    @Expose
    public String shopTotalTransactionCanceled;
    @SerializedName("shop_last_six_months")
    @Expose
    public ShopLastSixMonths shopLastSixMonths;
    @SerializedName("shop_total_etalase")
    @Expose
    public String shopTotalEtalase;
    @SerializedName("tx_count_success")
    @Expose
    public String txCountSuccess;
    @SerializedName("shop_service_description")
    @Expose
    public String shopServiceDescription;
    @SerializedName("shop_item_sold")
    @Expose
    public String shopItemSold;
    @SerializedName("shop_last_twelve_months")
    @Expose
    public ShopLastTwelveMonths shopLastTwelveMonths;
    @SerializedName("shop_accuracy_rate")
    @Expose
    public int shopAccuracyRate;
    @SerializedName("rate_success")
    @Expose
    public int rateSuccess;
    @SerializedName("shop_accuracy_description")
    @Expose
    public String shopAccuracyDescription;

    public int getShopServiceRate() {
        return shopServiceRate;
    }

    public int getShopSpeedRate() {
        return shopSpeedRate;
    }

    public String getShopTotalTransaction() {
        return shopTotalTransaction;
    }

    public String getShopSpeedDescription() {
        return shopSpeedDescription;
    }

    public int getHideRate() {
        return hideRate;
    }

    public String getTxCount() {
        return txCount;
    }

    public float getRateFailure() {
        return rateFailure;
    }

    public ShopBadgeLevel getShopBadgeLevel() {
        return shopBadgeLevel;
    }

    public ShopLastOneMonth getShopLastOneMonth() {
        return shopLastOneMonth;
    }

    public String getShopTotalProduct() {
        return shopTotalProduct;
    }

    public String getShopReputationScore() {
        return shopReputationScore;
    }

    public String getShopTotalTransactionCanceled() {
        return shopTotalTransactionCanceled;
    }

    public ShopLastSixMonths getShopLastSixMonths() {
        return shopLastSixMonths;
    }

    public String getShopTotalEtalase() {
        return shopTotalEtalase;
    }

    public String getTxCountSuccess() {
        return txCountSuccess;
    }

    public String getShopServiceDescription() {
        return shopServiceDescription;
    }

    public String getShopItemSold() {
        return shopItemSold;
    }

    public ShopLastTwelveMonths getShopLastTwelveMonths() {
        return shopLastTwelveMonths;
    }

    public int getShopAccuracyRate() {
        return shopAccuracyRate;
    }

    public int getRateSuccess() {
        return rateSuccess;
    }

    public String getShopAccuracyDescription() {
        return shopAccuracyDescription;
    }
}
