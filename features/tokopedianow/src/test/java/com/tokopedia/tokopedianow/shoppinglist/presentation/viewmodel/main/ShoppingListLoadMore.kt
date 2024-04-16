package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.ConstantValue
import com.tokopedia.tokopedianow.data.ShoppingListDataFactory.Main.createRecommendationWidget
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.addProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.doIf
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addLoadingState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addRetry
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.removeLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.removeRetry
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListLoadingMoreUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import org.junit.Test

class ShoppingListLoadMore: TokoNowShoppingListViewModelFixture() {
    private fun onSuccessLoadingMoreProductRecommendation(
        productRecommendationData: RecommendationWidget
    ) {
        mutableLayout
            .removeLoadMore()
            .doIf(
                predicate = productRecommendationData.recommendationItemList.isNotEmpty(),
                then = layout@ {
                    recommendedProducts.addAll(
                        ProductRecommendationMapper.mapRecommendedProducts(
                            productRecommendationData
                        )
                    )
                    this@layout
                        .addProducts(recommendedProducts)
                        .doIf(
                            predicate = recommendationModel.hasNext,
                            then = {
                                addLoadMore()
                                recommendationModel.pageCounter = recommendationModel.pageCounter.inc()
                            }
                        )
                }
            )
    }

    private fun onErrorLoadingMoreProductRecommendation() {
        mutableLayout
            .removeLoadMore()
            .addRetry()
    }

    private fun switchRetryToLoadMore() {
        mutableLayout
            .removeRetry()
            .addLoadMore()
    }

    @Test
    fun `When loading more product recommendation is successful then getting product recommendation list and true for hasNext, the result should return the layout with loading to load more`() {
        loadLayout(
            needExpandCollapse = false,
            hasNext = true
        )

        val recommendationWidget = createRecommendationWidget(
            hasNext = true,
            title = "Rekomendasi Untuk Anda"
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ONE,
                userId = userSession.userId.toIntSafely(),
                pageName = TokoNowShoppingListViewModel.PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
                xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        viewModel.loadMoreProductRecommendation(
            isLastVisibleLoadingMore = true
        )

        // update expected layout
        onSuccessLoadingMoreProductRecommendation(recommendationWidget)

        // verify load more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListLoadingMoreUiModel>().isNotEmpty()
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                false
            )
    }

    @Test
    fun `When loading more product recommendation is successful then getting empty product recommendation, the result should return the layout without loading to load more`() {
        loadLayout(
            needExpandCollapse = false,
            hasNext = true
        )

        val recommendationWidget = createRecommendationWidget(
            recommendationItemList = emptyList(),
            hasNext = true,
            title = "Rekomendasi Untuk Anda"
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ONE,
                userId = userSession.userId.toIntSafely(),
                pageName = TokoNowShoppingListViewModel.PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
                xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        viewModel.loadMoreProductRecommendation(
            isLastVisibleLoadingMore = true
        )

        // update expected layout
        onSuccessLoadingMoreProductRecommendation(recommendationWidget)

        // verify load more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListLoadingMoreUiModel>().isEmpty()
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                true
            )
    }

    @Test
    fun `When loading more product recommendation is successful then getting false for hasNext, the result should return the layout without loading to load more`() {
        loadLayout(
            needExpandCollapse = false,
            hasNext = false
        )

        val recommendationWidget = createRecommendationWidget(
            hasNext = true,
            title = "Rekomendasi Untuk Anda"
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ONE,
                userId = userSession.userId.toIntSafely(),
                pageName = TokoNowShoppingListViewModel.PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
                xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            response = recommendationWidget
        )

        viewModel.loadMoreProductRecommendation(
            isLastVisibleLoadingMore = true
        )

        // update expected layout
        onSuccessLoadingMoreProductRecommendation(recommendationWidget)

        // verify load more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListLoadingMoreUiModel>().isEmpty()
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                true
            )
    }

    @Test
    fun `When loading more product recommendation is failed then switching retry to load more, the result should return the layout with loading to load more`() {
        loadLayout(
            needExpandCollapse = false,
            hasNext = true
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ONE,
                userId = userSession.userId.toIntSafely(),
                pageName = TokoNowShoppingListViewModel.PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
                xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        viewModel.loadMoreProductRecommendation(
            isLastVisibleLoadingMore = true
        )

        // update expected layout
        onErrorLoadingMoreProductRecommendation()

        viewModel.switchRetryToLoadMore()

        switchRetryToLoadMore()

        // verify load more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListLoadingMoreUiModel>().isNotEmpty()
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                false
            )
    }

    @Test
    fun `When loading more product recommendation is failed from gql response then the result should return the layout without loading to load more`() {
        loadLayout(
            needExpandCollapse = false,
            hasNext = true
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ONE,
                userId = userSession.userId.toIntSafely(),
                pageName = TokoNowShoppingListViewModel.PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
                xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = MessageErrorException()
        )

        viewModel.loadMoreProductRecommendation(
            isLastVisibleLoadingMore = true
        )

        // update expected layout
        onErrorLoadingMoreProductRecommendation()

        // verify load more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListLoadingMoreUiModel>().isEmpty()
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                false
            )
    }

    @Test
    fun `When loading more product recommendation is failed then the result should return the layout without loading to load more`() {
        loadLayout(
            needExpandCollapse = false,
            hasNext = true
        )

        stubGetProductRecommendation(
            param = GetRecommendationRequestParam(
                pageNumber = Int.ONE,
                userId = userSession.userId.toIntSafely(),
                pageName = TokoNowShoppingListViewModel.PRODUCT_RECOMMENDATION_PAGE_NAME,
                xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
                xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                isTokonow = true
            ),
            throwable = Throwable()
        )

        viewModel.loadMoreProductRecommendation(
            isLastVisibleLoadingMore = true
        )

        // update expected layout
        onErrorLoadingMoreProductRecommendation()

        // verify load more existing
        verifyIsTrue(
            expectedResult = mutableLayout.filterIsInstance<ShoppingListLoadingMoreUiModel>().isEmpty()
        )

        // other verifications
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = mutableLayout
                )
            )

        viewModel
            .isOnScrollNotNeeded
            .verifyValue(
                false
            )
    }


    @Test
    fun `When try to load more product recommendation but isLastVisible is false then should do nothing`() {
        viewModel.loadMoreProductRecommendation(
            isLastVisibleLoadingMore = false
        )

        viewModel
            .layoutState
            .verifyLoading(
                LayoutModel(
                    layout = mutableLayout.addLoadingState()
                )
            )
    }

    @Test
    fun `When try to load more product recommendation but loadLayoutJob is null then should do nothing`() {
        viewModel.loadMoreProductRecommendation(
            isLastVisibleLoadingMore = true
        )

        viewModel
            .layoutState
            .verifyLoading(
                LayoutModel(
                    layout = mutableLayout.addLoadingState()
                )
            )
    }
}
