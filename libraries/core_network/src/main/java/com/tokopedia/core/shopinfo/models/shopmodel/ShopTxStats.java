
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ShopTxStats {

    @SerializedName("shop_tx_has_transaction_3_month")
    @Expose
    public int shopTxHasTransaction3Month;
    @SerializedName("shop_tx_success_rate_1_month")
    @Expose
    public String shopTxSuccessRate1Month;
    @SerializedName("shop_tx_show_percentage_3_month")
    @Expose
    public int shopTxShowPercentage3Month;
    @SerializedName("shop_tx_has_transaction")
    @Expose
    public int shopTxHasTransaction;
    @SerializedName("shop_tx_success_3_month_fmt")
    @Expose
    public String shopTxSuccess3MonthFmt;
    @SerializedName("shop_tx_show_percentage_1_month")
    @Expose
    public int shopTxShowPercentage1Month;
    @SerializedName("shop_tx_success_1_year_fmt")
    @Expose
    public String shopTxSuccess1YearFmt;
    @SerializedName("shop_tx_has_transaction_1_month")
    @Expose
    public int shopTxHasTransaction1Month;
    @SerializedName("shop_tx_success_rate_1_year")
    @Expose
    public String shopTxSuccessRate1Year;
    @SerializedName("shop_tx_has_transaction_1_year")
    @Expose
    public int shopTxHasTransaction1Year;
    @SerializedName("shop_tx_success_1_month_fmt")
    @Expose
    public String shopTxSuccess1MonthFmt;
    @SerializedName("shop_tx_show_percentage_1_year")
    @Expose
    public int shopTxShowPercentage1Year;
    @SerializedName("shop_tx_success_rate_3_month")
    @Expose
    public String shopTxSuccessRate3Month;

}
