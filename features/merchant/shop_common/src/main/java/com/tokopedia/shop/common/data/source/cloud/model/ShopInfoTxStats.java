
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoTxStats {

    @SerializedName("shop_tx_has_transaction")
    @Expose
    private long shopTxHasTransaction;
    @SerializedName("shop_tx_has_transaction_1_month")
    @Expose
    private long shopTxHasTransaction1Month;
    @SerializedName("shop_tx_has_transaction_1_year")
    @Expose
    private long shopTxHasTransaction1Year;
    @SerializedName("shop_tx_has_transaction_3_month")
    @Expose
    private long shopTxHasTransaction3Month;
    @SerializedName("shop_tx_rate_success_1_month")
    @Expose
    private String shopTxRateSuccess1Month;
    @SerializedName("shop_tx_show_percentage_1_month")
    @Expose
    private long shopTxShowPercentage1Month;
    @SerializedName("shop_tx_show_percentage_1_year")
    @Expose
    private long shopTxShowPercentage1Year;
    @SerializedName("shop_tx_show_percentage_3_month")
    @Expose
    private long shopTxShowPercentage3Month;
    @SerializedName("shop_tx_success_1_month_fmt")
    @Expose
    private String shopTxSuccess1MonthFmt;
    @SerializedName("shop_tx_success_1_year_fmt")
    @Expose
    private String shopTxSuccess1YearFmt;
    @SerializedName("shop_tx_success_3_month_fmt")
    @Expose
    private String shopTxSuccess3MonthFmt;
    @SerializedName("shop_tx_success_rate_1_month")
    @Expose
    private String shopTxSuccessRate1Month;
    @SerializedName("shop_tx_success_rate_1_year")
    @Expose
    private String shopTxSuccessRate1Year;
    @SerializedName("shop_tx_success_rate_3_month")
    @Expose
    private String shopTxSuccessRate3Month;

    public long getShopTxHasTransaction() {
        return shopTxHasTransaction;
    }

    public void setShopTxHasTransaction(long shopTxHasTransaction) {
        this.shopTxHasTransaction = shopTxHasTransaction;
    }

    public long getShopTxHasTransaction1Month() {
        return shopTxHasTransaction1Month;
    }

    public void setShopTxHasTransaction1Month(long shopTxHasTransaction1Month) {
        this.shopTxHasTransaction1Month = shopTxHasTransaction1Month;
    }

    public long getShopTxHasTransaction1Year() {
        return shopTxHasTransaction1Year;
    }

    public void setShopTxHasTransaction1Year(long shopTxHasTransaction1Year) {
        this.shopTxHasTransaction1Year = shopTxHasTransaction1Year;
    }

    public long getShopTxHasTransaction3Month() {
        return shopTxHasTransaction3Month;
    }

    public void setShopTxHasTransaction3Month(long shopTxHasTransaction3Month) {
        this.shopTxHasTransaction3Month = shopTxHasTransaction3Month;
    }

    public String getShopTxRateSuccess1Month() {
        return shopTxRateSuccess1Month;
    }

    public void setShopTxRateSuccess1Month(String shopTxRateSuccess1Month) {
        this.shopTxRateSuccess1Month = shopTxRateSuccess1Month;
    }

    public long getShopTxShowPercentage1Month() {
        return shopTxShowPercentage1Month;
    }

    public void setShopTxShowPercentage1Month(long shopTxShowPercentage1Month) {
        this.shopTxShowPercentage1Month = shopTxShowPercentage1Month;
    }

    public long getShopTxShowPercentage1Year() {
        return shopTxShowPercentage1Year;
    }

    public void setShopTxShowPercentage1Year(long shopTxShowPercentage1Year) {
        this.shopTxShowPercentage1Year = shopTxShowPercentage1Year;
    }

    public long getShopTxShowPercentage3Month() {
        return shopTxShowPercentage3Month;
    }

    public void setShopTxShowPercentage3Month(long shopTxShowPercentage3Month) {
        this.shopTxShowPercentage3Month = shopTxShowPercentage3Month;
    }

    public String getShopTxSuccess1MonthFmt() {
        return shopTxSuccess1MonthFmt;
    }

    public void setShopTxSuccess1MonthFmt(String shopTxSuccess1MonthFmt) {
        this.shopTxSuccess1MonthFmt = shopTxSuccess1MonthFmt;
    }

    public String getShopTxSuccess1YearFmt() {
        return shopTxSuccess1YearFmt;
    }

    public void setShopTxSuccess1YearFmt(String shopTxSuccess1YearFmt) {
        this.shopTxSuccess1YearFmt = shopTxSuccess1YearFmt;
    }

    public String getShopTxSuccess3MonthFmt() {
        return shopTxSuccess3MonthFmt;
    }

    public void setShopTxSuccess3MonthFmt(String shopTxSuccess3MonthFmt) {
        this.shopTxSuccess3MonthFmt = shopTxSuccess3MonthFmt;
    }

    public String getShopTxSuccessRate1Month() {
        return shopTxSuccessRate1Month;
    }

    public void setShopTxSuccessRate1Month(String shopTxSuccessRate1Month) {
        this.shopTxSuccessRate1Month = shopTxSuccessRate1Month;
    }

    public String getShopTxSuccessRate1Year() {
        return shopTxSuccessRate1Year;
    }

    public void setShopTxSuccessRate1Year(String shopTxSuccessRate1Year) {
        this.shopTxSuccessRate1Year = shopTxSuccessRate1Year;
    }

    public String getShopTxSuccessRate3Month() {
        return shopTxSuccessRate3Month;
    }

    public void setShopTxSuccessRate3Month(String shopTxSuccessRate3Month) {
        this.shopTxSuccessRate3Month = shopTxSuccessRate3Month;
    }

}
