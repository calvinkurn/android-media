package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection.PREVIOUS
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.home.beranda.data.mapper.ShopFlashSaleMapper
import com.tokopedia.home_component.visitable.BestSellerChipDataModel
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.home_component.visitable.BestSellerProductDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import dagger.Lazy
import java.lang.Exception
import java.lang.Integer.min
import javax.inject.Inject
import com.tokopedia.home.beranda.data.mapper.BestSellerMapper as BestSellerRevampMapper
import com.tokopedia.home_component.visitable.BestSellerDataModel as BestSellerRevampDataModel

class HomeRecommendationUseCase @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val bestSellerMapper: Lazy<BestSellerMapper>,
    private val bestSellerRevampMapper: Lazy<BestSellerRevampMapper>,
) {
    suspend fun onHomeBestSellerFilterClick(
            currentBestSellerDataModel: BestSellerDataModel,
            filterChip: RecommendationFilterChipsEntity.RecommendationFilterChip,
            selectedChipPosition: Int
    ): BestSellerDataModel {
        try {
            val recomData = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                            pageName = currentBestSellerDataModel.pageName,
                            queryParam = if(filterChip.isActivated) filterChip.value else ""
                    )
            )
            if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                val recomWidget = recomData.first().copy(
                        recommendationFilterChips = currentBestSellerDataModel.filterChip
                )
                val newBestSellerDataModel = bestSellerMapper.get().mappingRecommendationWidget(recomWidget, cardInteraction = true, currentBestSellerDataModel)
                val newModel = currentBestSellerDataModel.copy(
                        seeMoreAppLink = newBestSellerDataModel.seeMoreAppLink,
                        recommendationItemList = newBestSellerDataModel.recommendationItemList,
                        productCardModelList = newBestSellerDataModel.productCardModelList,
                        height = newBestSellerDataModel.height,
                        filterChip = newBestSellerDataModel.filterChip.map{
                            it.copy(isActivated = filterChip.name == it.name
                                    && filterChip.isActivated)
                        },
                        dividerType = currentBestSellerDataModel.dividerType,
                        chipsPosition = (selectedChipPosition+1)
                )
                return newModel
            } else {
                val newModel = currentBestSellerDataModel.copy(
                        filterChip = currentBestSellerDataModel.filterChip.map{
                            it.copy(isActivated = filterChip.name == it.name
                                    && !filterChip.isActivated)
                        }
                )
                return newModel
            }
        } catch (_: Exception) {
            val newModel = currentBestSellerDataModel.copy(
                    filterChip = currentBestSellerDataModel.filterChip.map{
                        it.copy(isActivated = filterChip.name == it.name
                                && !filterChip.isActivated)
                    }
            )
            return newModel
        }
    }

    suspend fun onHomeBestSellerFilterClick(
        currentBestSellerDataModel: BestSellerRevampDataModel,
        selectedFilterChip: BestSellerChipDataModel,
        scrollDirection: CarouselPagingGroupChangeDirection,
    ): BestSellerRevampDataModel =
        try {
            tryGetBestSellerFromSelectedFilter(
                currentBestSellerDataModel = currentBestSellerDataModel,
                selectedFilterChip = selectedFilterChip,
                scrollDirection = scrollDirection,
            )
        } catch (_: Exception) {
            errorGetBestSellerFromSelectedFilter(
                currentBestSellerDataModel = currentBestSellerDataModel,
                selectedFilterChip = selectedFilterChip,
            )
        }

    private suspend fun tryGetBestSellerFromSelectedFilter(
        currentBestSellerDataModel: BestSellerRevampDataModel,
        selectedFilterChip: BestSellerChipDataModel,
        scrollDirection: CarouselPagingGroupChangeDirection,
    ): BestSellerRevampDataModel {
        val recommendationData = getRecommendationUseCase.getData(
            GetRecommendationRequestParam(
                pageName = currentBestSellerDataModel.pageName,
                queryParam = selectedFilterChip.value,
            )
        )

        val hasData = recommendationData.isNotEmpty()
            && recommendationData.first().recommendationItemList.isNotEmpty()

        return if (hasData)
            successBestSellerDataModel(
                recommendationData = recommendationData,
                currentBestSellerDataModel = currentBestSellerDataModel,
                selectedFilterChip = selectedFilterChip,
                scrollDirection = scrollDirection,
            )
        else
            errorGetBestSellerFromSelectedFilter(
                currentBestSellerDataModel = currentBestSellerDataModel,
                selectedFilterChip = selectedFilterChip,
            )
    }

    private fun successBestSellerDataModel(
        recommendationData: List<RecommendationWidget>,
        currentBestSellerDataModel: BestSellerRevampDataModel,
        selectedFilterChip: BestSellerChipDataModel,
        scrollDirection: CarouselPagingGroupChangeDirection,
    ): BestSellerRevampDataModel {
        val recommendationWidget = recommendationData.firstOrNull() ?: return currentBestSellerDataModel
        val productList = bestSellerRevampMapper.get()
            .mapBestSellerProductList(recommendationWidget.recommendationItemList)

        val chipProductList = updateBestSellerChipProductList(
            currentBestSellerDataModel,
            selectedFilterChip,
            recommendationWidget.seeMoreAppLink,
            productList,
        )

        return currentBestSellerDataModel.copy(
            chipProductList = chipProductList,
            currentPageInGroup = getCurrentPageInGroup(scrollDirection),
        )
    }

    private fun updateBestSellerChipProductList(
        currentBestSellerDataModel: BestSellerRevampDataModel,
        selectedFilterChip: BestSellerChipDataModel,
        seeMoreApplink: String,
        activeChipProductList: List<BestSellerProductDataModel>
    ) = currentBestSellerDataModel.chipProductList.map {
        if (it.title == selectedFilterChip.title)
            it.copy(
                chip = it.chip.activate(),
                seeMoreApplink = seeMoreApplink,
                productModelList = activeChipProductList,
            )
        else
            it.copy(chip = it.chip.deactivate())
    }

    private fun getCurrentPageInGroup(scrollDirection: CarouselPagingGroupChangeDirection) =
        when (scrollDirection) {
            PREVIOUS -> CarouselPagingModel.LAST_PAGE_IN_GROUP
            else -> CarouselPagingModel.FIRST_PAGE_IN_GROUP
        }

    private fun errorGetBestSellerFromSelectedFilter(
        currentBestSellerDataModel: BestSellerRevampDataModel,
        selectedFilterChip: BestSellerChipDataModel,
    ) = currentBestSellerDataModel.copy(
        chipProductList = activateNextSelectedBestSellerChip(
            currentBestSellerDataModel,
            selectedFilterChip,
        ),
        currentPageInGroup = CarouselPagingModel.FIRST_PAGE_IN_GROUP,
    )

    private fun activateNextSelectedBestSellerChip(
        currentBestSellerDataModel: BestSellerRevampDataModel,
        selectedFilterChip: BestSellerChipDataModel
    ): List<BestSellerChipProductDataModel> {
        val currentSelectedIndex = currentBestSellerDataModel.chipProductList.indexOfFirst {
            it.chip == selectedFilterChip
        }

        val lastIndex = currentBestSellerDataModel.chipProductList.lastIndex
        val nextIndexToActivate = min(currentSelectedIndex + 1, lastIndex)

        return currentBestSellerDataModel.chipProductList.mapIndexed { index, it ->
            if (index == nextIndexToActivate)
                it.copy(chip = it.chip.activate())
            else
                it.copy(chip = it.chip.deactivate())
        }
    }

    suspend fun onHomeShopFlashSaleTabClick(
        currentDataModel: ShopFlashSaleWidgetDataModel,
        shopId: String,
    ): ShopFlashSaleWidgetDataModel {
        return try {
            val recomData = getRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageName = currentDataModel.channelModel.pageName,
                    queryParam = currentDataModel.channelModel.widgetParam,
                    shopIds = listOf(shopId),
                )
            )
            ShopFlashSaleMapper.mapShopFlashSaleItemList(currentDataModel, recomData)
        } catch (_: Exception) {
            currentDataModel
        }
    }
}
