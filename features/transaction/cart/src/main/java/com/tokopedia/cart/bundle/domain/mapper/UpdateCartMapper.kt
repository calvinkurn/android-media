package com.tokopedia.cart.bundle.domain.mapper

import com.tokopedia.cart.bundle.data.model.response.updatecart.Data
import com.tokopedia.cart.bundle.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cart.bundle.domain.model.updatecart.UpdateCartData

fun mapUpdateCartData(updateCartGqlResponse: UpdateCartGqlResponse, data: Data): UpdateCartData {
    val updateCartData = UpdateCartData()
    updateCartGqlResponse.updateCartDataResponse.data.let {
        updateCartData.isSuccess = updateCartGqlResponse.updateCartDataResponse.status.equals("OK", true) && it.status
        updateCartData.message = it.error
        updateCartData.outOfServiceData = it.outOfService
        updateCartData.toasterActionData = it.toasterAction
    }
    return updateCartData
}
