package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class CategoryProductRecomTest: CategoryTestFixtures() {

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
        tokoNowCategoryViewModel.mockSuperClassField(
            name = fieldName,
            value = fieldValue
        )

        /**
         * update wishlist status
         */
        tokoNowCategoryViewModel.removeProductRecommendationWidget()

        /**
         * verify the data test
         */
        tokoNowCategoryViewModel.visitableListLiveData.verifyValueEquals(listOf<List<Visitable<*>>>())
    }
}
