package com.tokopedia.shop.common.graphql.data.shopinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

data class ShopInfoTxStats(
        @SerializedName("shop_tx_has_transaction")
        @Expose
        val shopTxHasTransaction: Int = 0,
        @SerializedName("shop_tx_has_transaction_6_month")
        @Expose
        val shopTxHasTransaction6Month: Int = 0,
        @SerializedName("shop_tx_has_transaction_3_month")
        @Expose
        val shopTxHasTransaction3Month: Int = 0,
        @SerializedName("shop_tx_has_transaction_1_month")
        @Expose
        val shopTxHasTransaction1Month: Int = 0,
        @SerializedName("shop_tx_show_percentage_6_month")
        @Expose
        val shopTxShowPercentage6Month: Int = 0,
        @SerializedName("shop_tx_show_percentage_3_month")
        @Expose
        val shopTxShowPercentage3Month: Int = 0,
        @SerializedName("shop_tx_show_percentage_1_month")
        @Expose
        val shopTxShowPercentage1Month: Int = 0,
        @SerializedName("shop_tx_success_rate_6_month")
        @Expose
        val shopTxSuccessRate6Month: String = "",
        @SerializedName("shop_tx_success_rate_3_month")
        @Expose
        val shopTxSuccessRate3Month: String = "",
        @SerializedName("shop_tx_success_rate_1_month")
        @Expose
        val shopTxSuccessRate1Month: String = "",
        @SerializedName("shop_tx_success_6_month_fmt")
        @Expose
        val shopTxSuccess6MonthFmt: String = "",
        @SerializedName("shop_tx_success_3_month_fmt")
        @Expose
        val shopTxSuccess3MonthFmt: String = "",
        @SerializedName("shop_tx_success_1_month_fmt")
        @Expose
        val shopTxSuccess1MonthFmt: String = ""
) {
    data class Response(
            @SerializedName("shopTransactionStatistic")
            @Expose
            val shopTransactionStatistic: ShopTransactionStatistic
    ) {
        data class ShopTransactionStatistic(
                @SerializedName("shop_tx_stats")
                val shopTxStats: ShopInfoTxStats
        )
    }
}