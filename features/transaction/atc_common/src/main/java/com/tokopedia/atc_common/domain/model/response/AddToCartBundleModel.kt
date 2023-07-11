package com.tokopedia.atc_common.domain.model.response

import com.tokopedia.atc_common.AtcConstant
import com.tokopedia.network.exception.ResponseErrorException

data class AddToCartBundleModel(
    var status: String = "",
    var errorMessage: String = "",
    var addToCartBundleDataModel: AddToCartBundleDataModel = AddToCartBundleDataModel()
) {

    fun validateResponse(
        onSuccess: () -> Unit,
        onFailedWithMessages: (messages: List<String>) -> Unit,
        onFailedWithException: (throwable: Throwable) -> Unit
    ) {
        if (status == "OK") {
            if (addToCartBundleDataModel.success == 1) {
                // ATC success
                onSuccess()
            } else {
                // ATC error with message for user
                // Example case : bundle product is out of stock
                // Should be shown as dialog (Bundle Selection Page case)
                onFailedWithMessages(addToCartBundleDataModel.message)
            }
        } else {
            // ATC error without message for user
            // Example case : invalid params, invalid session etc
            // Should be shown as toaster
            onFailedWithException(ResponseErrorException(AtcConstant.ATC_ERROR_GLOBAL))
        }
    }
}

data class AddToCartBundleDataModel(
    var success: Int = 0,
    var message: List<String> = emptyList(),
    var data: List<ProductDataModel> = emptyList()
)

data class ProductDataModel(
    var cartId: String = "",
    var customerId: String = "",
    var notes: String = "",
    var productId: String = "",
    var quantity: Int = 0,
    var shopId: String = "",
    var warehouseId: String = ""
)
