package com.tokopedia.cart.domain.mapper

import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data

fun mapUpdateCartData(updateCartGqlResponse: UpdateCartV2Data): UpdateCartData {
    val updateCartData = UpdateCartData()
    updateCartGqlResponse.data.let {
        updateCartData.isSuccess = updateCartGqlResponse.status.equals("OK", true) && it.status
        updateCartData.message = it.error
        updateCartData.outOfServiceData = it.outOfService
        updateCartData.toasterActionData = it.toasterAction
    }
    return updateCartData
}
