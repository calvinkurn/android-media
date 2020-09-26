package com.tokopedia.product.addedit.tracking

object ProductAddUploadTracking {
    const val SCREEN = "/addproductpage - shipping"

    fun uploadProductFinish(category: String, shopId: String, isSuccess: Boolean, errorName: String = "", errorMessage: String = "") {
        if (isSuccess) {
            ProductAddEditTracking.sendAddProductClick(SCREEN, shopId, "click finish success", "")
        } else {
            ProductAddEditTracking.sendAddProductUpload(category, shopId, "$errorMessage - $errorName")
        }
    }

    fun uploadImageFailed(category: String, userId: String, errorName: String) {
        ProductAddEditTracking.sendAddProductUpload(category, userId, "validation error - (Upload Image) $errorName")
    }
}