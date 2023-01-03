package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class SearchProductRecomTest: SearchTestFixtures() {

    @Test
    fun `when calling removeProductRecommendationWidget function should remove TokoNowProductRecommendationUiModel from visitable list`() {
        /**
         * create test data
         */
        val fieldName = "visitableList"
        val fieldValue = mutableListOf<Visitable<*>>(
            TokoNowProductRecommendationUiModel(
                requestParam = GetRecommendationRequestParam()
            )
        )

        /**
         * mock private field from viewModel
         */
        tokoNowSearchViewModel.mockSuperClassField(
            name = fieldName,
            value = fieldValue
        )

        /**
         * update wishlist status
         */
        tokoNowSearchViewModel.removeProductRecommendationWidget()

        /**
         * verify the data test
         */
        tokoNowSearchViewModel.visitableListLiveData.verifyValueEquals(listOf<List<Visitable<*>>>())
    }
}
