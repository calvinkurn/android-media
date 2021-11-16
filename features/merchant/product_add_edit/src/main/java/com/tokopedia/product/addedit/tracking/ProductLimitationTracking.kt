package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.productlimitation.domain.mapper.ProductLimitationMapper
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClickWithoutScreenAndUserId

object ProductLimitationTracking {

    fun clickInfoTicker() {
        sendAddProductClickWithoutScreenAndUserId("click learn more on ticker", "")
    }

    fun clickEduTicker() {
        sendAddProductClickWithoutScreenAndUserId("click edu article on pop up page", "")
    }

    fun clickActionItem(actionCategory: String, articleTitle: String, destinationLink: String) {
        val articleCategory = when (actionCategory) {
            ProductLimitationMapper.UPGRADE_TO_PM -> "power merchant"
            ProductLimitationMapper.UPGRADE_TO_PM_PRO -> "power merchant"
            ProductLimitationMapper.USE_VARIANT -> "variant"
            ProductLimitationMapper.DELETE_PRODUCTS -> "delete product"
            ProductLimitationMapper.USE_PROMOTION -> "ads and promotion"
            else -> ""
        }
        sendAddProductClickWithoutScreenAndUserId("click learn article on pop up page",
            "$articleCategory - $articleTitle - $destinationLink")
    }

    fun clickSaveAsDraft() {
        sendAddProductClickWithoutScreenAndUserId("click save as draft - product limitation", "")
    }
}