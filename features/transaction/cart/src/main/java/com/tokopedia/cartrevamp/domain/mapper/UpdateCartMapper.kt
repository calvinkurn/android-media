package com.tokopedia.cartrevamp.domain.mapper

import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartrevamp.domain.model.updatecart.UpdateCartData

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
