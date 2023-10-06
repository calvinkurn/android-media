package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.tokopedianow.buyercomm.domain.mapper.BuyerCommunicationMapper
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.GetBuyerCommunicationResponse
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.presentation.model.HomeHeaderBackgroundData
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel

object HomeHeaderMapper {

    fun MutableList<HomeLayoutItemUiModel?>.mapToHomeHeaderUiModel(
        item: HomeHeaderUiModel,
        warehouses: List<WarehouseData>,
        buyerCommunicationResponse: GetBuyerCommunicationResponse?
    ) {
        buyerCommunicationResponse?.let {
            val shopDetails = it.data.shopDetails
            val title = shopDetails.title
            val logoUrl = shopDetails.logoURL

            if (title.isBlank() or logoUrl.isBlank()) {
                mapHomeHeaderErrorState(item)
            } else {
                mapHomeHeaderLoadedState(it, item, warehouses)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel?>.mapHomeHeaderLoadedState(
        it: GetBuyerCommunicationResponse,
        item: HomeHeaderUiModel,
        warehouses: List<WarehouseData>
    ) {
        val shopDetails = it.data.shopDetails
        val shippingDetails = it.data.shippingDetails
        val backgroundData = mapToHeaderBackgroundData(it)
        val buyerCommunicationData = BuyerCommunicationMapper
            .mapToBuyerCommunicationData(it)

        val title = shopDetails.title
        val logoUrl = shopDetails.logoURL
        val shippingHint = shippingDetails.hint

        val layoutUiModel = item.copy(
            id = HomeStaticLayoutId.HOME_HEADER,
            title = title,
            shippingHint = shippingHint,
            logoUrl = logoUrl,
            background = backgroundData,
            buyerCommunication = buyerCommunicationData,
            warehouses = warehouses,
            state = HomeLayoutItemState.LOADED
        )

        updateItemById(item.id) {
            HomeLayoutItemUiModel(layoutUiModel, HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel?>.mapHomeHeaderErrorState(item: HomeHeaderUiModel) {
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
