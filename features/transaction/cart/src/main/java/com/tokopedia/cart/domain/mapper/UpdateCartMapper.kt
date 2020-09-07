package com.tokopedia.cart.domain.mapper

import com.tokopedia.cart.data.model.response.updatecart.Data
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cart.domain.model.cartlist.ButtonData
import com.tokopedia.cart.domain.model.cartlist.OutOfServiceData
import com.tokopedia.cart.domain.model.updatecart.ToasterActionData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData

fun mapUpdateCartData(updateCartGqlResponse: UpdateCartGqlResponse, data: Data): UpdateCartData {
    val updateCartData = UpdateCartData()
    updateCartGqlResponse.updateCartDataResponse.data.let {
        updateCartData.isSuccess = updateCartGqlResponse.updateCartDataResponse.status.equals("OK", true) && it.status
        updateCartData.message = it.error
        updateCartData.outOfServiceData = OutOfServiceData().apply {
            id = it.outOfService.id
            image = it.outOfService.image
            title = it.outOfService.title
            description = it.outOfService.description
            val tmpButtons = mutableListOf<ButtonData>()
            it.outOfService.buttons.forEach {
                tmpButtons.add(ButtonData().apply {
                    id = it.id
                    code = it.code
                    message = it.message
                    color = it.color
                })
            }
            buttons = tmpButtons
        }
        updateCartData.toasterActionData = ToasterActionData().apply {
            text = it.toasterAction.text
            showCta = it.toasterAction.showCta
        }
    }
    return updateCartData
}
