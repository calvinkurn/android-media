package com.tokopedia.search.result.product.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.DEFAULT_VALUE_X_SOURCE
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.search.result.presentation.mapper.RecommendationViewModelMapper
import com.tokopedia.search.result.presentation.model.RecommendationTitleDataView
import com.tokopedia.search.result.product.ViewUpdater
import rx.Subscriber
import javax.inject.Inject

class RecommendationPresenterDelegate @Inject constructor(
    private val view: ViewUpdater,
    private val recommendationUseCase: GetRecommendationUseCase,
) {

    fun getRecommendation() {
        recommendationUseCase.execute(
            recommendationUseCase.getRecomParams(
                pageNumber = PAGE_NUMBER,
                xSource = DEFAULT_VALUE_X_SOURCE,
                pageName = SEARCH_PAGE_NAME_RECOMMENDATION,
                productIds = listOf()
            ),
            createRecommendationSubscriber(),
        )
    }

    private fun createRecommendationSubscriber() =
        object : Subscriber<List<RecommendationWidget>>() {
            override fun onCompleted() {
                view.removeLoading()
            }

            override fun onError(e: Throwable?) {}

            override fun onNext(recommendationWidgets: List<RecommendationWidget>) {
                if (recommendationWidgets.isEmpty()) return

                val recommendationItemDataView =
                    RecommendationViewModelMapper().convertToRecommendationItemViewModel(
                        recommendationWidgets[0]
                    )
                val items = mutableListOf<Visitable<*>>()
                val recommendationWidget = recommendationWidgets[0]
                val recommendationWidgetTitle =
                    if (recommendationWidget.title.isEmpty()) DEFAULT_PAGE_TITLE_RECOMMENDATION
                    else recommendationWidget.title
                val recommendationTitleDataView = RecommendationTitleDataView(
                    recommendationWidgetTitle,
                    recommendationWidget.seeMoreAppLink,
                    recommendationWidget.pageName
                )
                items.add(recommendationTitleDataView)
                items.addAll(recommendationItemDataView)

                view.appendItems(items)
            }
        }

    fun detachView() {
        recommendationUseCase.unsubscribe()
    }

    companion object {
        private const val SEARCH_PAGE_NAME_RECOMMENDATION = "empty_search"
        private const val DEFAULT_PAGE_TITLE_RECOMMENDATION = "Rekomendasi untukmu"
        private const val PAGE_NUMBER = 1
    }
}
