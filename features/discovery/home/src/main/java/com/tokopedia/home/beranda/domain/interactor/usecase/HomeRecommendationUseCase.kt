package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import dagger.Lazy
import java.lang.Exception
import javax.inject.Inject

class HomeRecommendationUseCase @Inject constructor(
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val bestSellerMapper: Lazy<BestSellerMapper>
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
                val newBestSellerDataModel = bestSellerMapper.get().mappingRecommendationWidget(recomWidget)
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
}