package com.tokopedia.tokopedianow.repurchase.presentation.viewmodel

import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.ConstantValue
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.data.*
import com.tokopedia.tokopedianow.data.createChooseAddressLayout
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_FILTER
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_SEARCH
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_RESULT
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_OOC
import com.tokopedia.tokopedianow.recentpurchase.constant.RepurchaseStaticLayoutId.Companion.ERROR_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.recentpurchase.domain.model.TokoNowRepurchasePageResponse.*
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel
import org.junit.Test

class TokoNowRepurchaseViewModelTest: TokoNowRepurchaseViewModelTestFixture() {
    @Test
    fun `when showing loading layout should run and give the success result`() {
        viewModel.showLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = createRepurchaseLoadingLayout(),
            state = TokoNowLayoutState.LOADING
        )

        verifyShowLayoutSuccess(layout)
    }

    @Test
    fun `when showing empty state no history search layout should run and give the success result`() {
        viewModel.showEmptyState(EMPTY_STATE_NO_HISTORY_SEARCH)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_NO_HISTORY_SEARCH),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateNoHistoryLayoutSuccess(layout)
    }

    @Test
    fun `when showing empty state no history filter layout should run and give the success result`() {
        viewModel.showEmptyState(EMPTY_STATE_NO_HISTORY_FILTER)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_NO_HISTORY_FILTER),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateNoHistoryLayoutSuccess(layout)
    }

    @Test
    fun `when showing empty state ooc layout should run and give the success result`() {
        viewModel.showEmptyState(EMPTY_STATE_OOC)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_OOC),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateOocLayoutSuccess(layout)
    }

    @Test
    fun `when showing error state failed layout should run and give the success result`() {
        viewModel.showEmptyState(ERROR_STATE_FAILED_TO_FETCH_DATA)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(ERROR_STATE_FAILED_TO_FETCH_DATA),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateFailedLayoutSuccess(layout)
    }

    @Test
    fun `when showing empty state no result layout should run and give the success result`() {
        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createEmptyStateLayout(EMPTY_STATE_NO_RESULT),
            state = TokoNowLayoutState.EMPTY
        )

        verifyEmptyStateNoResultLayoutSuccess(layout)
    }

    @Test
    fun `when showing choose address widget layout should run and give the success result`() {
        viewModel.getLayoutList()

        val layout = RepurchaseLayoutUiModel(
            layoutList = createChooseAddressLayout(),
            state = TokoNowLayoutState.SHOW
        )

        verifyChooseAddressWidgetLayoutSuccess(layout)
    }

    @Test
    fun `when getting product recom ooc layout should run and give the success result`() {
        val recommendationItems = listOf(RecommendationItem())
        val recommendationWidget = RecommendationWidget(
            recommendationItemList = recommendationItems
        )

        onGetProductRecommendation_thenReturn(listOf(recommendationWidget))

        val expectedResult = RepurchaseLayoutUiModel(
            layoutList = createProductRecomLayout(
                pageName = ConstantValue.PAGE_NAME_RECOMMENDATION_OOC_PARAM,
                carouselData = RecommendationCarouselData(
                    recommendationData = recommendationWidget,
                    state = STATE_READY
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        viewModel.showEmptyState(EMPTY_STATE_OOC)

        verifyGetProductRecommendatioUseCaseCalled()
        verifyGetProductRecommendationWidgetLayoutSuccess(expectedResult)
    }

    @Test
    fun `when getting product recom empty no result layout should run and give the success result`() {
        val recommendationItems = listOf(RecommendationItem())
        val recommendationWidget = RecommendationWidget(
            recommendationItemList = recommendationItems
        )

        onGetProductRecommendation_thenReturn(listOf(recommendationWidget))

        val expectedResult = RepurchaseLayoutUiModel(
            layoutList = createProductRecomLayout(
                pageName = ConstantValue.PAGE_NAME_RECOMMENDATION_NO_RESULT_PARAM,
                carouselData = RecommendationCarouselData(
                    recommendationData = recommendationWidget,
                    state = STATE_READY
                )
            ),
            state = TokoNowLayoutState.SHOW
        )

        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        verifyGetProductRecommendatioUseCaseCalled()
        verifyGetProductRecommendationWidgetLayoutSuccess(expectedResult)
    }

    @Test
    fun `when getting category list layout should run and give the success result`() {
        onGetCategoryList_thenReturn(CategoryListResponse(
            header = Header(),
            data = listOf(
                CategoryResponse(
                    id = "3",
                    name = "Category 3",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf()
                )
            )
        ))

        privateLocalCacheModel.set(viewModel, LocalCacheModel(warehouse_id = "1"))

        viewModel.showEmptyState(EMPTY_STATE_NO_RESULT)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createCategoryGridLayout(),
            state = TokoNowLayoutState.SHOW
        )

        verifyGetCategoryListUseCaseCalled()
        verifyGetCategoryGridLayoutSuccess(layout)
    }

    @Test
    fun `when getting sort filter layout should run and give the success result`() {
        onGetRepurchaseProductList_thenReturn(
            GetRepurchaseProductListResponse(
                meta = GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(RepurchaseProduct())
            )
        )

        viewModel.getLayoutList()
        viewModel.getLayoutData()

        val layout = RepurchaseLayoutUiModel(
            layoutList = createSortFilterLayout(),
            state = TokoNowLayoutState.UPDATE
        )

        verifySortFilterLayoutSuccess(layout)
    }

    @Test
    fun `when applying sort filter layout should run and give the success result`() {
        onGetRepurchaseProductList_thenReturn(
            GetRepurchaseProductListResponse(
                meta = GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(RepurchaseProduct())
            )
        )

        viewModel.getLayoutList()
        viewModel.getLayoutData()
        viewModel.applySortFilter(2)

        val layout = RepurchaseLayoutUiModel(
            layoutList = createSortFilterLayout(2),
            state = TokoNowLayoutState.UPDATE
        )

        verifySortFilterLayoutSuccess(layout)
    }

    @Test
    fun `when applying date filter layout should run and give the success result`() {
        onGetRepurchaseProductList_thenReturn(
            GetRepurchaseProductListResponse(
                meta = GetRepurchaseProductMetaResponse(
                    page = 1,
                    hasNext = false,
                    totalScan = 1,
                ),
                products = listOf(RepurchaseProduct())
            )
        )

        viewModel.getLayoutList()
        viewModel.getLayoutData()
        viewModel.applyDateFilter(RepurchaseSortFilterUiModel.SelectedDateFilter())

        val layout = RepurchaseLayoutUiModel(
            layoutList = createDateFilterLayout(RepurchaseSortFilterUiModel.SelectedDateFilter()),
            state = TokoNowLayoutState.UPDATE
        )

        verifySortFilterLayoutSuccess(layout)
    }

    @Test
    fun `when removing choose address widget layout should run and give the success result`() {
        `when showing choose address widget layout should run and give the success result`()

        viewModel.removeChooseAddressWidget()

        verifyChooseAddressWidgetLayoutRemovedSuccess()
    }

    @Test
    fun `when getting chooseAddress should run and give the success result`() {
        onGetChooseAddress_thenReturn(createChooseAddress())

        viewModel.getChooseAddress(TokoNowHomeFragment.SOURCE)

        verifyGetChooseAddress()

        val expectedResponse = createChooseAddress().response
        verfifyGetChooseAddressSuccess(expectedResponse)
    }

    @Test
    fun `when getting chooseAddress should throw chooseAddress's exception and get failed result`() {
        onGetChooseAddress_thenReturn(Exception())

        viewModel.getChooseAddress(TokoNowHomeFragment.SOURCE)

        verifyGetChooseAddressFail()
    }
}