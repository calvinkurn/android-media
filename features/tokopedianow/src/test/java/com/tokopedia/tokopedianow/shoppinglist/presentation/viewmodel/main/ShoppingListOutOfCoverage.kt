package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addEmptyStateOoc
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addHeader
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addProductRecommendationOoc
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LayoutModel
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import org.junit.Test

class ShoppingListOutOfCoverage: TokoNowShoppingListViewModelFixture() {
    @Test
    fun `When processing to load layout but it turns out to be out of coverage then ooc ui should be returned`() {
        // stub section
        stubOutOfCoverage(
            isOoc = true
        )

        // load loading state
        viewModel
            .loadLayout()

        // provide expected result
        val expectedResult: MutableList<Visitable<*>> = mutableListOf()

        expectedResult
            .addHeader(
                headerModel = HeaderModel(
                    backgroundGradientColor = TokoNowThematicHeaderUiModel.GradientColor(
                        startColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Background),
                        endColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_Background)
                    ),
                    isChooseAddressShown = true,
                    iconPullRefreshType = LayoutIconPullRefreshView.TYPE_GREEN
                ),
                state = SHOW
            )
            .addEmptyStateOoc()
            .addProductRecommendationOoc()

        // verify section
        viewModel
            .layoutState
            .verifySuccess(
                LayoutModel(
                    layout = expectedResult
                )
            )
    }
}
