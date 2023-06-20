package com.tokopedia.product.manage.feature.list.view.model

import com.tokopedia.product.manage.feature.multiedit.data.response.MultiEditProductResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

sealed class MultiEditResult(
    open val success: List<MultiEditProductResult>,
    open val failed: List<MultiEditProductResult>,
    open val failedDT: List<MultiEditProductResult>

) {

    data class EditByStatus(
        val status: ProductStatus,
        override val success: List<MultiEditProductResult>,
        override val failed: List<MultiEditProductResult>,
        override val failedDT: List<MultiEditProductResult>
    ) : MultiEditResult(success, failed, failedDT)

    data class EditByMenu(
        val menuId: String,
        val menuName: String,
        override val success: List<MultiEditProductResult>,
        override val failed: List<MultiEditProductResult>,
        override val failedDT: List<MultiEditProductResult> = listOf()
    ) : MultiEditResult(success, failed, failedDT)
}
