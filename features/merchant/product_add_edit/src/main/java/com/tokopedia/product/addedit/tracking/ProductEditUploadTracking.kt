package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductClickWithoutScreen
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductImpression

object ProductEditUploadTracking {
    private const val TYPE_SERVER_TOME = "tome server error"
    private const val TYPE_SERVER_VALIDATION_TOME = "tome validation error"
    private const val TYPE_SERVER_UPLOADPEDIA = "uploadpedia server error"

    fun uploadProductFinish(category: String, shopId: String, isSuccess: Boolean, isValidationError: Boolean = false, errorName: String = "") {
        if (isSuccess) {
            sendEditProductClickWithoutScreen(shopId, "click finish success", "")
        } else {
            val label = if (isValidationError) {
                "$TYPE_SERVER_VALIDATION_TOME - $errorName"
            } else {
                "$TYPE_SERVER_TOME - $errorName"
            }

            sendEditProductClickWithoutScreen(shopId, "click finish error", label)
        }
    }

    fun uploadImageFailed(category: String, userId: String, errorName: String) {
        sendEditProductImpression(userId, "impression edit product error", "$TYPE_SERVER_UPLOADPEDIA - $errorName")
    }
}