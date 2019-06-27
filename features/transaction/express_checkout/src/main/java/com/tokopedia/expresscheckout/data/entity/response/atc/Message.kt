package com.tokopedia.expresscheckout.data.entity.response.atc

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class Message(
        @SerializedName("index")
        val index: String,

        @SerializedName("message")
        val message: String
) {
    companion object {
        val ERROR_FIELD_BETWEEN = "ErrorFieldBetween"
        val ERROR_FIELD_MAX_CHAR = "ErrorFieldMaxChar"
        val ERROR_FIELD_REQUIRED = "ErrorFieldRequired"
        val ERROR_PRODUCT_AVAILABLE_STOCK = "ErrorProductAvailableStock"
        val ERROR_PRODUCT_AVAILABLE_STOCK_DETAIL = "ErrorProductAvailableStockDetail"
        val ERROR_PRODUCT_MAX_QUANTITY = "ErrorProductMaxQuantity"
        val ERROR_PRODUCT_MIN_QUANTITY = "ErrorProductMinQuantity"
    }
}