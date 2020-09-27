package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductClick
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductUpload

object ProductEditUploadTracking {
    private const val TYPE_SERVER_TOME = "tome server error"
    private const val TYPE_SERVER_VALIDATION_TOME = "tome validation error"
    private const val TYPE_SERVER_UPLOADPEDIA = "uploadpedia server error"

    fun uploadProductFinish(category: String, shopId: String, isSuccess: Boolean, isValidationError: Boolean = false, errorName: String = "") {
        if (isSuccess) {
            sendEditProductClick(shopId, "click finish success", "")
        } else {
            val label = if (isValidationError) {
                "$TYPE_SERVER_VALIDATION_TOME - $errorName"
            } else {
                "$TYPE_SERVER_TOME - $errorName"
            }

            sendEditProductUpload(category, shopId, label)
        }
    }

    fun uploadImageFailed(category: String, userId: String, errorName: String) {
        sendEditProductUpload(category, userId, "$TYPE_SERVER_UPLOADPEDIA - $errorName")
    }
}