package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClickWithoutScreen
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductImpression
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductUpload

object ProductAddUploadTracking {
    private const val TYPE_SERVER_TOME = "tome server error"
    private const val TYPE_SERVER_VALIDATION_TOME = "tome validation error"
    private const val TYPE_SERVER_UPLOADPEDIA = "uploadpedia server error"

    fun uploadProductFinish(shopId: String, isSuccess: Boolean, isValidationError: Boolean = false, errorName: String = "") {
        if (isSuccess) {
            sendAddProductClickWithoutScreen(shopId, "click finish success", "")
        } else {
            val label = if (isValidationError) {
                "$TYPE_SERVER_VALIDATION_TOME - $errorName"
            } else {
                "$TYPE_SERVER_TOME - $errorName"
            }

            sendAddProductClickWithoutScreen(shopId, "click finish error", label)
        }
    }

    fun uploadImageFailed(userId: String, errorName: String) {
        sendAddProductImpression(userId, "impression add product error", "$TYPE_SERVER_UPLOADPEDIA - $errorName")
    }

    fun uploadGqlTimeout(category: String, userId: String, errorName: String) {
        sendAddProductUpload(category, userId, errorName)
    }
}