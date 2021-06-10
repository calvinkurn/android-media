package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoTxStats {
    @SerializedName("shop_tx_has_transaction")
    @Expose
    var shopTxHasTransaction: Long = 0

    @SerializedName("shop_tx_has_transaction_1_month")
    @Expose
    var shopTxHasTransaction1Month: Long = 0

    @SerializedName("shop_tx_has_transaction_1_year")
    @Expose
    var shopTxHasTransaction1Year: Long = 0

    @SerializedName("shop_tx_has_transaction_3_month")
    @Expose
    var shopTxHasTransaction3Month: Long = 0

    @SerializedName("shop_tx_rate_success_1_month")
    @Expose
    var shopTxRateSuccess1Month: String? = null

    @SerializedName("shop_tx_show_percentage_1_month")
    @Expose
    var shopTxShowPercentage1Month: Long = 0

    @SerializedName("shop_tx_show_percentage_1_year")
    @Expose
    var shopTxShowPercentage1Year: Long = 0

    @SerializedName("shop_tx_show_percentage_3_month")
    @Expose
    var shopTxShowPercentage3Month: Long = 0

    @SerializedName("shop_tx_success_1_month_fmt")
    @Expose
    var shopTxSuccess1MonthFmt: String? = null

    @SerializedName("shop_tx_success_1_year_fmt")
    @Expose
    var shopTxSuccess1YearFmt: String? = null

    @SerializedName("shop_tx_success_3_month_fmt")
    @Expose
    var shopTxSuccess3MonthFmt: String? = null

    @SerializedName("shop_tx_success_rate_1_month")
    @Expose
    var shopTxSuccessRate1Month: String? = null

    @SerializedName("shop_tx_success_rate_1_year")
    @Expose
    var shopTxSuccessRate1Year: String? = null

    @SerializedName("shop_tx_success_rate_3_month")
    @Expose
    var shopTxSuccessRate3Month: String? = null
}