package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.domain.model.GetBuyerCommunication.GetBuyerCommunicationResponse
import com.tokopedia.tokopedianow.home.presentation.model.HomeHeaderBackgroundData
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel

object HomeHeaderMapper {

    fun MutableList<HomeLayoutItemUiModel?>.mapToHomeHeaderUiModel(
        item: HomeHeaderUiModel,
        buyerCommunicationResponse: GetBuyerCommunicationResponse?
    ) {
        buyerCommunicationResponse?.let {
            val shopDetails = it.data.shopDetails
            val shippingDetails = it.data.shippingDetails
            val locationDetails = it.data.locationDetails
            val backgroundData = mapToHeaderBackgroundData(it)

            val title = shopDetails.title
            val logoUrl = shopDetails.logoURL
            val shippingHint = shippingDetails.hint
            val shopStatus = locationDetails.status

            val layoutUiModel = item.copy(
                id = HomeStaticLayoutId.HOME_HEADER,
                title = title,
                shippingHint = shippingHint,
                shopStatus = shopStatus,
                logoUrl = logoUrl,
                background = backgroundData,
                state = HomeLayoutItemState.LOADED
            )

            updateItemById(item.id) {
                HomeLayoutItemUiModel(layoutUiModel, HomeLayoutItemState.LOADED)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel?>.mapHomeHeaderErrorState(
        item: HomeHeaderUiModel,
    ) {
        updateItemById(item.id) {
            val layoutUiModel = item.copy(state = HomeLayoutItemState.ERROR)
            HomeLayoutItemUiModel(layoutUiModel, HomeLayoutItemState.LOADED)
        }
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
