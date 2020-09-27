package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductUpload

object ProductAddUploadTracking {
    private const val SCREEN = "/addproductpage - shipping"
    private const val TYPE_SERVER_TOME = "tome server error"
    private const val TYPE_SERVER_VALIDATION_TOME = "tome validation error"
    private const val TYPE_SERVER_UPLOADPEDIA = "uploadpedia server error"

    fun uploadProductFinish(category: String, shopId: String, isSuccess: Boolean, isValidationError: Boolean = false, errorName: String = "") {
        if (isSuccess) {
            sendAddProductClick(SCREEN, shopId, "click finish success", "")
        } else {
            val label = if (isValidationError) {
                "$TYPE_SERVER_VALIDATION_TOME - $errorName"
            } else {
                "$TYPE_SERVER_TOME - $errorName"
            }

            sendAddProductUpload(category, shopId, label)
        }
    }

    fun uploadImageFailed(category: String, userId: String, errorName: String) {
        sendAddProductUpload(category, userId, "$TYPE_SERVER_UPLOADPEDIA - $errorName")
    }
}