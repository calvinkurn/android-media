package com.tokopedia.cart.domain.mapper

import com.tokopedia.cart.data.model.response.updatecart.Data
import com.tokopedia.cart.data.model.response.updatecart.UpdateCartGqlResponse
import com.tokopedia.cart.domain.model.updatecart.ButtonData
import com.tokopedia.cart.domain.model.updatecart.PromptPageData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData

fun mapUpdateCartData(updateCartGqlResponse: UpdateCartGqlResponse, data: Data): UpdateCartData {
    val updateCartData = UpdateCartData()
    updateCartData.isSuccess = !(updateCartGqlResponse.updateCartDataResponse.status != "OK" ||
            updateCartGqlResponse.updateCartDataResponse.error.isNotEmpty() ||
            updateCartGqlResponse.updateCartDataResponse.data == null || !data.status)

    updateCartData.message = if (updateCartGqlResponse.updateCartDataResponse.error.isNotEmpty()) {
        updateCartGqlResponse.updateCartDataResponse.error[0]
    } else {
        ""
    }
    updateCartData.promptPageData = PromptPageData().apply {
        image = updateCartGqlResponse.updateCartDataResponse.data?.promptPage?.image ?: ""
        title = updateCartGqlResponse.updateCartDataResponse.data?.promptPage?.title ?: ""
        descriptions = updateCartGqlResponse.updateCartDataResponse.data?.promptPage?.descriptions
                ?: ""
        val tmpButtons = mutableListOf<ButtonData>()
        updateCartGqlResponse.updateCartDataResponse.data?.promptPage?.buttons?.forEach {
            tmpButtons.add(ButtonData().apply {
                text = it.text
                link = it.link
                action = it.action
                color = it.color
            })
        }
        buttons = tmpButtons
    }

    return updateCartData
}
