package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getStringValue
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCardMapper.mapRecomWidgetToProductList
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.getVisitableId
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel

object ProductCarouselChipsMapper {

    private const val FIRST_ITEM_INDEX = 0
    private const val PARAM_PAGENAME = "pagename"

    fun mapResponseToProductCarouselChips(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val header = TokoNowDynamicHeaderUiModel(
            title = response.header.name,
        )

        val chipList = response.grids.mapIndexed { index, grid ->
            val pageName = grid.param.getStringValue(PARAM_PAGENAME)

            TokoNowChipUiModel(
                id = grid.id,
                text = grid.name,
                param = pageName,
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
                chipList[selectedChipIndex] = chipItem.copy(selected = selected)
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
        item: HomeProductCarouselChipsUiModel
    ) {
        updateItemById(item.id) {
            val newItem = item.copy(state = TokoNowProductRecommendationState.LOADING)
            HomeLayoutItemUiModel(newItem, HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel?>.getProductCarouselChipsItem(
        id: String
    ): HomeProductCarouselChipsUiModel {
        val visitableItem = first { it?.layout?.getVisitableId() == id }
        return visitableItem?.layout as HomeProductCarouselChipsUiModel
    }
}
