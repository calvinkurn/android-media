package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection
import com.tokopedia.carouselproductcard.paging.CarouselPagingGroupChangeDirection.PREVIOUS
import com.tokopedia.carouselproductcard.paging.CarouselPagingModel
import com.tokopedia.home_component.visitable.BestSellerChipDataModel
import com.tokopedia.home_component.visitable.BestSellerProductDataModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import dagger.Lazy
import java.lang.Exception
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
        } catch (e: Exception) {
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
        filterChip: BestSellerChipDataModel,
        scrollDirection: CarouselPagingGroupChangeDirection,
    ): BestSellerRevampDataModel =
        try {
            tryGetBestSellerFromSelectedFilter(currentBestSellerDataModel, filterChip, scrollDirection)
        } catch (e: Exception) {
            currentBestSellerDataModel
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
            updatedBestSellerDataModel(
                recommendationData,
                currentBestSellerDataModel,
                selectedFilterChip,
                scrollDirection
            )
        else
            currentBestSellerDataModel
    }

    private fun updatedBestSellerDataModel(
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
            productList
        )

        return currentBestSellerDataModel.copy(
            chipProductList = chipProductList,
            title = recommendationWidget.title,
            currentPageInGroup = getCurrentPageInGroup(scrollDirection),
        )
    }

    private fun updateBestSellerChipProductList(
        currentBestSellerDataModel: BestSellerRevampDataModel,
        selectedFilterChip: BestSellerChipDataModel,
        productList: List<BestSellerProductDataModel>
    ) = currentBestSellerDataModel.chipProductList.map {
        if (it.title == selectedFilterChip.title)
            it.copy(chip = it.chip.activate(), productModelList = productList)
        else
            it.copy(chip = it.chip.deactivate())
    }

    private fun getCurrentPageInGroup(scrollDirection: CarouselPagingGroupChangeDirection) =
        when (scrollDirection) {
            PREVIOUS -> CarouselPagingModel.LAST_PAGE_IN_GROUP
            else -> CarouselPagingModel.FIRST_PAGE_IN_GROUP
        }
}
