package com.tokopedia.product.addedit.tracking

object ProductEditUploadTracking {

    fun uploadProductFinish(category: String, shopId: String, isSuccess: Boolean, errorName: String = "", errorMessage: String = "") {
        if (isSuccess) {
            ProductAddEditTracking.sendEditProductClick(shopId, "click finish success", "")
        } else {
            ProductAddEditTracking.sendEditProductUpload(category, shopId, "$errorMessage - $errorName")
        }
    }

    fun uploadImageFailed(category: String, userId: String, errorName: String) {
        ProductAddEditTracking.sendEditProductUpload(category, userId, "validation error - (Upload Image) $errorName")
    }
}