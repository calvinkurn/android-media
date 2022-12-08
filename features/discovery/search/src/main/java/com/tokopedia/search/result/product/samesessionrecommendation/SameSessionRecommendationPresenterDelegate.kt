package com.tokopedia.search.result.product.samesessionrecommendation

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.kotlin.extensions.view.isOdd
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchSameSessionRecommendationModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.productfilterindicator.ProductFilterIndicator
import com.tokopedia.search.result.product.requestparamgenerator.RequestParamsGenerator
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationConstant.HIDE_RECOMMENDATION_ID
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationDataView.Feedback.FeedbackItem
import com.tokopedia.usecase.UseCase
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

@SearchScope
class SameSessionRecommendationPresenterDelegate @Inject constructor(
    private val viewUpdater: ViewUpdater,
    private val requestParamsGenerator: RequestParamsGenerator,
    @param:Named(SearchConstant.SearchProduct.SEARCH_SAME_SESSION_RECOMMENDATION_USE_CASE)
    private val sameSessionRecommendationUseCase: UseCase<SearchSameSessionRecommendationModel>,
    private val preference: SameSessionRecommendationPreference,
    private val queryKeyProvider: QueryKeyProvider,
    private val productFilterIndicator: ProductFilterIndicator,
) {

    private val isHideRecommendationExceedThreshold: Boolean
        get() {
            val hideRecommendationTimestamp = preference.hideRecommendationTimestamp
            if (hideRecommendationTimestamp.isZero()) return true
            val currentTime = System.currentTimeMillis()
            return currentTime - hideRecommendationTimestamp > HIDE_RECOMMENDATION_THRESHOLD
        }

    private val isFilterOrFeedbackActive: Boolean
        get() = productFilterIndicator.isAnyFilterOrSortActive
            || !isHideRecommendationExceedThreshold

    private val productItemList: List<ProductItemDataView>?
        get() = viewUpdater.itemList?.filterIsInstance<ProductItemDataView>()

    fun requestSameSessionRecommendation(
        item: ProductItemDataView,
        adapterPosition: Int,
        dimension90: String,
        externalReference: String,
        chooseAddressParams: Map<String, String>?,
    ) {
        if (viewUpdater.itemCount < adapterPosition) return

        if (isFilterOrFeedbackActive) return

        if (!item.isKeywordIntentionLow) return

        val sameSessionRequestParams = requestParamsGenerator
            .createSameSessionRecommendationParam(item, chooseAddressParams)

        sameSessionRecommendationUseCase.execute(
            sameSessionRequestParams,
            createSameSessionRecommendationUseCaseSubscriber(item, dimension90, externalReference)
        )
    }

    private fun createSameSessionRecommendationUseCaseSubscriber(
        item: ProductItemDataView,
        dimension90: String,
        externalReference: String,
    ) = object : Subscriber<SearchSameSessionRecommendationModel>() {
        override fun onCompleted() {
        }

        override fun onError(e: Throwable) {
            Timber.e(e)
        }

        override fun onNext(data: SearchSameSessionRecommendationModel) {
            handleSameSessionRecommendationUseCaseOnNext(data, item, dimension90, externalReference)
        }
    }

    private fun handleSameSessionRecommendationUseCaseOnNext(
        data: SearchSameSessionRecommendationModel,
        item: ProductItemDataView,
        dimension90: String,
        externalReference: String,
    ) {
        if (data.products.size < MIN_RECOMMENDED_PRODUCTS) return

        val recommendationData = SameSessionRecommendationMapper()
            .convertToSameSessionRecommendationDataView(
                data,
                item.productID,
                queryKeyProvider.queryKey,
                dimension90,
                externalReference,
            )
        removePreviousSameSessionRecommendation()
        addSameSessionRecommendation(recommendationData, item)
    }

    private fun removePreviousSameSessionRecommendation() {
        viewUpdater.removeFirstItemWithCondition { it is SameSessionRecommendationDataView }
    }

    private fun addSameSessionRecommendation(
        recommendation: SameSessionRecommendationDataView,
        selectedProduct: ProductItemDataView,
    ) {
        val selectedProductIndex = viewUpdater.itemList?.indexOf(selectedProduct) ?: -1
        val nextItem = getNextOfSelectedItem(selectedProductIndex, selectedProduct)

        val selectedProductPosition = productItemList?.indexOf(selectedProduct) ?: -1

        val targetItem = if (selectedProductPosition.isOdd()) selectedProduct else nextItem
        viewUpdater.insertItemAfter(recommendation, targetItem)
        // prevent same session recommendation is displayed directly
        viewUpdater.scrollToPosition(selectedProductIndex)
    }

    private fun getNextOfSelectedItem(
        selectedProductIndex: Int,
        selectedProduct: ProductItemDataView,
    ) = viewUpdater.getItemAtIndex(selectedProductIndex + 1)?.takeIf {
        it is ProductItemDataView
    } ?: selectedProduct

    fun handleFeedbackItemClick(
        feedback: Feedback,
        feedbackItem: FeedbackItem,
    ) {
        feedback.selectedFeedbackItem = feedbackItem
        if (HIDE_RECOMMENDATION_ID == feedbackItem.componentId) {
            handleHideRecommendationClick()
        }
    }

    private fun handleHideRecommendationClick() {
        preference.hideRecommendationTimestamp = System.currentTimeMillis()
    }

    companion object {
        private const val MIN_RECOMMENDED_PRODUCTS = 2
        private const val HIDE_RECOMMENDATION_THRESHOLD = 24 * 60 * 60 * 1000L
    }
}
