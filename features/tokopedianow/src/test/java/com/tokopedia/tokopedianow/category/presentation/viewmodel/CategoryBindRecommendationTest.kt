package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.BindRecommendationCarouselTestHelper
import com.tokopedia.tokopedianow.searchcategory.BindRecommendationCarouselTestHelper.Callback
import com.tokopedia.tokopedianow.searchcategory.assertTokonowRecommendationCarouselRequestParams
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.utils.PAGE_NUMBER_RECOM_WIDGET
import com.tokopedia.tokopedianow.searchcategory.utils.RECOM_WIDGET
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_CLP
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.core.Is.`is` as shouldBe

class CategoryBindRecommendationTest: CategoryTestFixtures() {

    private val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()

    @Test
    fun `bind recommendation success`() {
        val testHelper = BindRecommendationCarouselTestHelper(
                tokoNowCategoryViewModel,
                getRecommendationUseCase,
                object: Callback {
                    override fun `Given view will show recommendation carousel`() {
                        `Given category first page will show recommendation carousel`()
                    }

                    override fun `Then assert get recommendation request`(
                            getRecommendationRequestParam: GetRecommendationRequestParam
                    ) {
                        `Then assert get recommendation request`(
                                getRecommendationRequestParam,
                                defaultCategoryL1,
                        )
                    }
                },
        )

        testHelper.`bind recommendation success`()
    }

    private fun `Given category first page will show recommendation carousel`() {
        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
    }

    private fun `Then assert get recommendation request`(
            getRecommendationRequestParam: GetRecommendationRequestParam,
            expectedCategoryId: String
    ) {
        assertTokonowRecommendationCarouselRequestParams(getRecommendationRequestParam)

        assertThat(getRecommendationRequestParam.categoryIds, shouldBe(listOf(expectedCategoryId)))
    }

    @Test
    fun `bind recommendation fail`() {
        val testHelper = BindRecommendationCarouselTestHelper(
                tokoNowCategoryViewModel,
                getRecommendationUseCase,
                object: Callback {
                    override fun `Given view will show recommendation carousel`() {
                        `Given category first page will show recommendation carousel`()
                    }

                    override fun `Then assert get recommendation request`(
                            getRecommendationRequestParam: GetRecommendationRequestParam
                    ) {
                        `Then assert get recommendation request`(
                                getRecommendationRequestParam,
                                defaultCategoryL1,
                        )
                    }
                },
        )

        testHelper.`bind recommendation fail`()
    }

    @Test
    fun `should not load recommendation again after success`() {
        val testHelper = BindRecommendationCarouselTestHelper(
                tokoNowCategoryViewModel,
                getRecommendationUseCase,
                object: Callback {
                    override fun `Given view will show recommendation carousel`() {
                        `Given category first page will show recommendation carousel`()
                    }

                    override fun `Then assert get recommendation request`(
                            getRecommendationRequestParam: GetRecommendationRequestParam
                    ) {
                        `Then assert get recommendation request`(
                                getRecommendationRequestParam,
                                defaultCategoryL1,
                        )
                    }
                },
        )

        testHelper.`should not load recommendation again after success`()
    }

    @Test
    fun `load category recommendation for category L2`() {
        val categoryL2Id = "456"
        `Given category view model`(defaultCategoryL1, categoryL2Id)

        val testHelper = BindRecommendationCarouselTestHelper(
                tokoNowCategoryViewModel,
                getRecommendationUseCase,
                object: Callback {
                    override fun `Given view will show recommendation carousel`() {
                        `Given category first page will show recommendation carousel`()
                    }

                    override fun `Then assert get recommendation request`(
                            getRecommendationRequestParam: GetRecommendationRequestParam
                    ) {
                        `Then assert get recommendation request`(
                                getRecommendationRequestParam,
                                categoryL2Id,
                        )
                    }
                },
        )

        testHelper.`bind recommendation success`()
    }

    @Test
    fun `load category recommendation for category L3`() {
        val categoryL2Id = "456"
        val categoryL3Id = "789"
        `Given category view model`(
                defaultCategoryL1,
                categoryL2Id,
                mapOf(SearchApiConst.SC to categoryL3Id)
        )

        val testHelper = BindRecommendationCarouselTestHelper(
                tokoNowCategoryViewModel,
                getRecommendationUseCase,
                object: Callback {
                    override fun `Given view will show recommendation carousel`() {
                        `Given category first page will show recommendation carousel`()
                    }

                    override fun `Then assert get recommendation request`(
                            getRecommendationRequestParam: GetRecommendationRequestParam
                    ) {
                        `Then assert get recommendation request`(
                                getRecommendationRequestParam,
                                categoryL3Id,
                        )
                    }
                },
        )

        testHelper.`bind recommendation success`()
    }
}
