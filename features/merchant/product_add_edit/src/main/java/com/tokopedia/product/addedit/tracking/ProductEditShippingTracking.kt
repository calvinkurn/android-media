package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductClick

object ProductEditShippingTracking {

    fun clickBack(shopId: String) {
        sendEditProductClick(shopId, "click back on shipping detail page")
    }

    fun clickInsurance(shopId: String) {
        sendEditProductClick(shopId,"click shipping insurance button")
    }

    fun clickCancelChangeWeight(shopId: String) {
        sendEditProductClick(shopId, "click cancel change weight","" )
    }

    fun clickWeightDropDown(shopId: String) {
        sendEditProductClick(shopId, "click weight dropdown menu", "")
    }

    fun clickChooseWeight(shopId: String, isGram: Boolean) {
        sendEditProductClick(shopId, "click choose weight", if (isGram) {
            "gram"
        } else {
            "kilogram"
        })
    }

    fun clickFinish(shopId: String, isSuccess: Boolean, errorName: String = "", errorMessage: String = "") {
        if (isSuccess) {
            sendEditProductClick(shopId, "click finish success", "")
        } else {
            sendEditProductClick(shopId, "click finish error", "$errorMessage - $errorName")
        }
    }

}