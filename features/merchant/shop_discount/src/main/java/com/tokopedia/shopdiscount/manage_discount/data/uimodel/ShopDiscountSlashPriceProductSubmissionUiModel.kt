package com.tokopedia.shopdiscount.manage_discount.data.uimodel

import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class ShopDiscountSlashPriceProductSubmissionUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val listSubmittedProductData: List<SubmittedProductData> = listOf()
) {
    data class SubmittedProductData(
        val name: String = "",
        val success: Boolean = false,
        val message: String = "",
        val listSubmittedWarehouse: List<WarehouseData> = listOf()
    ) {
        data class WarehouseData(
            val warehouseId: String = "",
            val success: Boolean = false,
            val message: String = ""
        )
    }
}