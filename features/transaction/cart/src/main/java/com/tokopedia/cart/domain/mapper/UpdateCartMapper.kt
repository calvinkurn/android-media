package com.tokopedia.cart.domain.mapper

import com.tokopedia.cart.data.model.response.updatecart.Data
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cart.domain.model.updatecart.ButtonData
import com.tokopedia.cart.domain.model.updatecart.PromptPageData
import com.tokopedia.cart.domain.model.updatecart.ToasterActionData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData

fun mapUpdateCartData(updateCartGqlResponse: UpdateCartGqlResponse, data: Data): UpdateCartData {
    val updateCartData = UpdateCartData()
    updateCartGqlResponse.updateCartDataResponse.data?.let {
        updateCartData.isSuccess = updateCartGqlResponse.updateCartDataResponse.status.equals("OK", true) && it.status
        updateCartData.message = it.error
        updateCartData.promptPageData = PromptPageData().apply {
            image = it.promptPage.image
            title = it.promptPage.title
            descriptions = it.promptPage.descriptions
            val tmpButtons = mutableListOf<ButtonData>()
            it.promptPage.buttons.forEach {
                tmpButtons.add(ButtonData().apply {
                    text = it.text
                    link = it.link
                    action = it.action
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
