package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCardMapper.mapRecomWidgetToProductList
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.getVisitableId
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel

object ProductCarouselChipsMapper {

    private const val FIRST_ITEM_INDEX = 0

    fun mapResponseToProductCarouselChips(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val header = TokoNowDynamicHeaderUiModel(
            title = response.header.name
        )

        val chipList = response.grids.mapIndexed { index, grid ->
            TokoNowChipUiModel(
                id = grid.id,
                text = grid.name,
                param = grid.param,
                selected = index == FIRST_ITEM_INDEX
            )
        }

        val uiModel = HomeProductCarouselChipsUiModel(
            id = response.id,
            header = header,
            chipList = chipList,
            carouselItemList = emptyList(),
            state = TokoNowProductRecommendationState.LOADING
        )

        return HomeLayoutItemUiModel(uiModel, state)
    }

    fun MutableList<HomeLayoutItemUiModel?>.mapProductCarouselChipsWidget(
        item: HomeProductCarouselChipsUiModel,
        recomWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?,
        selectedChip: TokoNowChipUiModel
    ) {
        updateItemById(item.id) {
            val chipList = item.chipList.toMutableList()
            val selectedChipIndex = chipList.indexOf(selectedChip)

            chipList.forEachIndexed { index, chipItem ->
                val selected = index == selectedChipIndex
                chipList[index] = chipItem.copy(selected = selected)
            }

            val productList = mapRecomWidgetToProductList(
                headerName = item.header?.title.orEmpty(),
                recomWidget = recomWidget,
                miniCartData = miniCartData,
                needToChangeMaxLinesName = true
            )

            val newItem = item.copy(
                chipList = chipList,
                carouselItemList = productList,
                state = TokoNowProductRecommendationState.LOADED
            )

            HomeLayoutItemUiModel(newItem, HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel?>.setProductCarouselChipsLoading(
        item: HomeProductCarouselChipsUiModel,
        selectedChip: TokoNowChipUiModel
    ) {
        updateItemById(item.id) {
            val chipList = item.chipList.toMutableList()
            val selectedChipIndex = chipList.indexOf(selectedChip)

            chipList.forEachIndexed { index, chipItem ->
                val selected = index == selectedChipIndex
                chipList[index] = chipItem.copy(selected = selected)
            }

            val newItem = item.copy(
                chipList = chipList,
                state = TokoNowProductRecommendationState.LOADING
            )

            HomeLayoutItemUiModel(newItem, HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel?>.getProductCarouselChipsItem(
        id: String
    ): HomeProductCarouselChipsUiModel? {
        val visitableItem = firstOrNull { it?.layout?.getVisitableId() == id }
        return visitableItem?.layout as? HomeProductCarouselChipsUiModel
    }

    fun MutableList<HomeLayoutItemUiModel?>.getProductCarouselChipByProductId(
        productId: String
    ): HomeProductCarouselChipsUiModel? {
        filter { it?.layout is HomeProductCarouselChipsUiModel }.firstOrNull {
            val model = it?.layout as HomeProductCarouselChipsUiModel
            val products = model.carouselItemList.filterIsInstance<ProductCardCompactCarouselItemUiModel>()
            products.firstOrNull { product -> product.getProductId() == productId } != null
        }.let {
            return it?.layout as? HomeProductCarouselChipsUiModel
        }
    }

    fun getCurrentSelectedChipId(
        carouselModel: HomeProductCarouselChipsUiModel?
    ): String {
        return carouselModel?.chipList?.firstOrNull { it.selected }?.id.orEmpty()
    }
}
