package com.tokopedia.tokopedianow.home.mapper

import com.tokopedia.tokopedianow.buyercomm.domain.mapper.BuyerCommunicationMapper
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.GetBuyerCommunicationResponse
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.presentation.model.HomeHeaderBackgroundData
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel

object HomeHeaderMapper {

    fun mapToHomeHeaderUiModel(response: GetBuyerCommunicationResponse): HomeHeaderUiModel {
        val shopDetails = response.data.shopDetails
        val shippingDetails = response.data.shippingDetails
        val backgroundData = mapToHeaderBackgroundData(response)
        val buyerCommunicationData = BuyerCommunicationMapper
            .mapToBuyerCommunicationData(response)

        val title = shopDetails.title
        val logoUrl = shopDetails.logoURL
        val shippingHint = shippingDetails.hint

        return HomeHeaderUiModel(
            id = HomeStaticLayoutId.HOME_HEADER,
            title = title,
            shippingHint = shippingHint,
            logoUrl = logoUrl,
            background = backgroundData,
            buyerCommunication = buyerCommunicationData,
            state = HomeLayoutItemState.LOADED
        )
    }

    fun createHomeHeaderUiModel(
        state: HomeLayoutItemState = HomeLayoutItemState.ERROR
    ): HomeHeaderUiModel {
        return HomeHeaderUiModel(
            id = HomeStaticLayoutId.HOME_HEADER,
            state = state
        )
    }

    private fun mapToHeaderBackgroundData(
        response: GetBuyerCommunicationResponse?
    ): HomeHeaderBackgroundData {
        val background = response?.data?.background
        val color = background?.color.orEmpty()
        val animationUrl = background?.animationURL.orEmpty()
        val imageUrl = background?.imageURL.orEmpty()
        return HomeHeaderBackgroundData(color, animationUrl, imageUrl)
    }
}
