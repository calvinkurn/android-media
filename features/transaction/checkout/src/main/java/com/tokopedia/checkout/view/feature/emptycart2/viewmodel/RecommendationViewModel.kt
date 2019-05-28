package com.tokopedia.checkout.view.feature.emptycart2.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecommendationItemUiModel
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecommendationUiModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationModel
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-05-24.
 */

class RecommendationViewModel @Inject constructor(private val getRecommendationUseCase: GetRecommendationUseCase) : ViewModel() {

    companion object {
        val X_SOURCE_RECOM_WIDGET = "recom_widget"
        val PAGE_EMPTY_CART = "empty_cart"
    }

    val recommendationData = MutableLiveData<RecommendationUiModel>()

    fun unsubscribeSubscription() {
        getRecommendationUseCase.unsubscribe()
    }

    fun getRecommendation() {
        getRecommendationUseCase.execute(getRecommendationUseCase.getRecomParams(
                recommendationData.value?.currentPage ?: 0, X_SOURCE_RECOM_WIDGET, PAGE_EMPTY_CART),
                object : Subscriber<RecommendationModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        recommendationData.value?.lastLoadResult = false
                    }

                    override fun onNext(recommendationModel: RecommendationModel) {
                        val recommendationUiModel = RecommendationUiModel()
                        recommendationUiModel.currentPage = recommendationData.value?.currentPage?.plus(1) ?: 0
                        recommendationUiModel.lastLoadResult = true

                        val recommendationItemUiModels = ArrayList<RecommendationItemUiModel>()
                        val currentRecommendationItems = recommendationData.value?.recommendationItems ?: arrayListOf()
                        recommendationItemUiModels.addAll(currentRecommendationItems)
                        for (item in recommendationModel.recommendationItemList) {
                            val recommendationItemUiModel = RecommendationItemUiModel()
                            recommendationItemUiModel.recommendationItem = item
                            recommendationItemUiModels.add(recommendationItemUiModel)
                        }
                        for (recommendationItemUiModel: RecommendationItemUiModel in recommendationItemUiModels) {
                            recommendationItemUiModel.isLastItem = false
                        }
                        recommendationItemUiModels[recommendationItemUiModels.size - 1].isLastItem = true
                        recommendationUiModel.recommendationItems = recommendationItemUiModels
                        recommendationData.value = recommendationUiModel
                    }
                })
    }

}