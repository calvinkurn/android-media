package com.tokopedia.search.result.product.samesessionrecommendation

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.fragment.RecyclerViewUpdater
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationConst.HIDE_RECOMMENDATION_ID
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationConst.IRRELEVANT_RECOMMENDATION_ID
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback.FeedbackItem
import com.tokopedia.usecase.UseCase
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@SearchScope
class SameSessionRecommendationPresenterDelegate @Inject constructor(
    private val recyclerViewUpdater: RecyclerViewUpdater,
    private val requestParamsGenerator: RequestParamsGenerator,
    @param:Named(SearchConstant.SearchProduct.SEARCH_SAME_SESSION_RECOMMENDATION_USE_CASE)
    private val sameSessionRecommendationUseCase: UseCase<SearchSameSessionRecommendationModel>,
    private val filterController: FilterController,
    private val preference: SameSessionRecommendationPreference,
    private val queryKeyProvider: QueryKeyProvider,
) {
    companion object {
        private const val MIN_RECOMMENDED_PRODUCTS = 2
        private const val HIDE_RECOMMENDATION_THRESHOLD = 24 * 60 * 60 * 1000L
    }

    private val isAnyFilterActive
        get() = filterController.isFilterActive()

    private val isHideRecommendationExceedThreshold: Boolean
        get() {
            val hideRecommendationTimestamp = preference.hideRecommendationTimestamp
            if (hideRecommendationTimestamp.isZero()) return true
            val currentTime = System.currentTimeMillis()
            return currentTime - hideRecommendationTimestamp > HIDE_RECOMMENDATION_THRESHOLD
        }

    private val isFilterOrFeedbackActive: Boolean
        get() = isAnyFilterActive
            || isIrrelevantRecommendationClicked
            || !isHideRecommendationExceedThreshold

    private var currentRecommendation: SameSessionRecommendationDataView? = null

    private var isIrrelevantRecommendationClicked: Boolean = false

    fun requestSameSessionRecommendation(
        item: ProductItemDataView,
        adapterPosition: Int,
        dimension90: String,
        externalReference: String,
    ) {
        val adapter = recyclerViewUpdater.productListAdapter ?: return
        if (adapter.itemCount < adapterPosition) return

        if (isFilterOrFeedbackActive) return

        val targetPosition = adapterPosition + 1
        val isNextItemIsSameSessionRecommendation =
            adapter.itemList[targetPosition] is SameSessionRecommendationDataView
        if (isNextItemIsSameSessionRecommendation) return

        if (!item.isKeywordIntentionLow) return

        val sameSessionRequestParams = requestParamsGenerator
            .createSameSessionRecommendationParam(item)

        sameSessionRecommendationUseCase.execute(
            sameSessionRequestParams,
            object : Subscriber<SearchSameSessionRecommendationModel>() {
                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                }

                override fun onNext(data: SearchSameSessionRecommendationModel) {
                    if (data.products.size < MIN_RECOMMENDED_PRODUCTS) return

                    val recommendationData = SameSessionRecommendationMapper()
                        .convertToSameSessionRecommendationDataView(
                            data,
                            item.productID,
                            queryKeyProvider.queryKey,
                            dimension90,
                            externalReference,
                        )
                    currentRecommendation = recommendationData
                    removePreviousSameSessionRecommendation()
                    addSameSessionRecommendation(recommendationData, targetPosition)
                }
            }
        )
    }

    private fun removePreviousSameSessionRecommendation() {
        recyclerViewUpdater.productListAdapter?.removeLastSameSessionRecommendation()
    }

    private fun addSameSessionRecommendation(
        recommendation: SameSessionRecommendationDataView,
        targetPosition: Int,
    ) {
        recyclerViewUpdater.insertItemAtIndex(recommendation, targetPosition)
    }

    fun handleFeedbackItemClick(feedbackItem: FeedbackItem) {
        if (IRRELEVANT_RECOMMENDATION_ID == feedbackItem.componentId) {
            handleIrrelevantRecommendationClick()
        } else if (HIDE_RECOMMENDATION_ID == feedbackItem.componentId) {
            handleHideRecommendationClick()
        }
    }

    private fun handleIrrelevantRecommendationClick() {
        isIrrelevantRecommendationClicked = true
    }

    private fun handleHideRecommendationClick() {
        preference.hideRecommendationTimestamp = System.currentTimeMillis()
    }
}
