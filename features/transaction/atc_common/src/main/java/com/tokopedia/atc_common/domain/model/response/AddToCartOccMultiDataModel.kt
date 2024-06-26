package com.tokopedia.atc_common.domain.model.response

import com.tokopedia.atc_common.domain.mapper.AddToCartDataMapper
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.data.response.updatecart.ToasterAction

data class AddToCartOccMultiDataModel(
    val errorMessage: List<String> = emptyList(),
    val status: String = "",
    val data: AddToCartOccMultiData = AddToCartOccMultiData(),
    val nextPage: Int = 0
) {
    /**
     * This method is for checking if ATC error without capability of custom error handling when no message provided from backend
     *
     * This is recommended for sending tracking
     */
    fun isStatusError(): Boolean {
        return (data.success == 0 || !status.equals(STATUS_OK, true))
    }

    /**
     * This method is to get ATC error message from BackEnd
     */
    fun getAtcErrorMessage(): String? {
        return data.message.firstOrNull() ?: errorMessage.firstOrNull()
    }

    /**
     * This method is to map this class into AddToCartDataModel with only first product data
     *
     * This is useful for funnel that only allow single product OCC
     *
     * @see AddToCartDataModel
     */
    fun mapToAddToCartDataModel(): AddToCartDataModel {
        return AddToCartDataMapper().mapAddToCartOccMultiDataModel(this)
    }

    fun isNewCheckoutPaymentPage(): Boolean {
        return nextPage == NEW_CHECKOUT_PAYMENT_PAGE
    }

    companion object {
        const val STATUS_OK = "OK"
        const val STATUS_ERROR = "ERROR"

        const val NEW_CHECKOUT_PAYMENT_PAGE = 1
    }
}

data class AddToCartOccMultiData(
    val success: Int = 0,
    val message: List<String> = emptyList(),
    val cart: List<AddToCartOccMultiCartData> = emptyList(),
    val outOfService: OutOfService = OutOfService(),
    val toasterAction: ToasterAction = ToasterAction()
)

data class AddToCartOccMultiCartData(
    val cartId: String = "",
    val productId: String = "",
    val quantity: Int = 0,
    val notes: String = "",
    val shopId: String = "",
    val customerId: String = "",
    val warehouseId: String = ""
)
