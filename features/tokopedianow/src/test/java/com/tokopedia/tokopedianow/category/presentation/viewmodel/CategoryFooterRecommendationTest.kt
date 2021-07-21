package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_FAILED
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.category.utils.TOKONOW_CLP
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.core.Is.`is` as shouldBe

class CategoryFooterRecommendationTest: CategoryTestFixtures() {

    private val recommendationEntity = "recom/recom-carousel.json".jsonToObject<RecommendationEntity>()
    private val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()

    private val recommendationWidgetList = recommendationEntity.mapToRecommendationWidgetList()
    private val getRecommendationParamsSlot = slot<GetRecommendationRequestParam>()

    @Test
    fun `load category recommendation success`() {
        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given get recommendation will be successful`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then assert get recommendation request`(defaultCategoryL1)
        `Then assert recommendation data view is updated to ready with recom widget`(recomWidget)
        `Then assert updated visitable list`(recomWidgetPosition)
    }

    private fun `Given get recommendation will be successful`() {
        coEvery {
            getRecommendationUseCase.getData(capture(getRecommendationParamsSlot))
        } returns recommendationWidgetList
    }

    private fun RecommendationEntity.mapToRecommendationWidgetList() =
            productRecommendationWidget
                    .data
                    .mappingToRecommendationModel()

    private fun List<Visitable<*>>.findRecomWidget(): RecommendationCarouselDataView =
            find { it is RecommendationCarouselDataView } as? RecommendationCarouselDataView
                ?: throw Throwable("Cannot find recom widget")

    private fun `When bind recommendation carousel`(
            recomWidget: RecommendationCarouselDataView,
            recomWidgetPosition: Int,
    ) {
        tokoNowCategoryViewModel.onBindRecommendationCarousel(recomWidget, recomWidgetPosition)
    }

    private fun `Then assert get recommendation request`(
            expectedCategoryId: String,
    ) {
        val getRecommendationParams = getRecommendationParamsSlot.captured

        assertThat(getRecommendationParams.pageName, shouldBe(TOKONOW_CLP))
        assertThat(getRecommendationParams.categoryIds, shouldBe(listOf(expectedCategoryId)))
    }

    private fun `Then assert recommendation data view is updated to ready with recom widget`(
            recomWidget: RecommendationCarouselDataView
    ) {
        val recommendationWidget = recommendationWidgetList.first()

        assertThat(recomWidget.carouselData.state, shouldBe(STATE_READY))
        assertThat(recomWidget.carouselData.recommendationData, shouldBe(recommendationWidget))
    }

    private fun `Then assert updated visitable list`(recomWidgetPosition: Int) {
        val updatedIndex = tokoNowCategoryViewModel.updatedVisitableIndicesLiveData.value!!

        assertThat(updatedIndex, shouldBe(listOf(recomWidgetPosition)))
    }

    @Test
    fun `load category recommendation failed`() {
        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given get recommendation will fail`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then assert remmendation widget data view state is updated to fail`(recomWidget)
        `Then assert updated visitable list`(recomWidgetPosition)
    }

    private fun `Given get recommendation will fail`() {
        coEvery {
            getRecommendationUseCase.getData(capture(getRecommendationParamsSlot))
        } throws Throwable("Get recommendation failed")
    }

    private fun `Then assert remmendation widget data view state is updated to fail`(
            recomWidget: RecommendationCarouselDataView
    ) {
        assertThat(recomWidget.carouselData.state, shouldBe(STATE_FAILED))
    }

    @Test
    fun `should not load recommendation again after success`() {
        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given get recommendation will be successful`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `Given recommendation carousel already success`(recomWidget, recomWidgetPosition)

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then verify get recommendation only called once`()
    }

    private fun `Given recommendation carousel already success`(
            recomWidget: RecommendationCarouselDataView,
            recomWidgetPosition: Int,
    ) {
        tokoNowCategoryViewModel.onBindRecommendationCarousel(recomWidget, recomWidgetPosition)
    }

    private fun `Then verify get recommendation only called once`() {
        coVerify(exactly = 1) {
            getRecommendationUseCase.getData(any())
        }
    }

    @Test
    fun `load category recommendation for category L2`() {
        val categoryL2Id = "456"
        `Given category view model`(defaultCategoryL1, categoryL2Id)

        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given get recommendation will be successful`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then assert get recommendation request`(categoryL2Id)
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

        `Given get category first page use case will be successful`(categoryModel)
        `Given view already created`()
        `Given get recommendation will be successful`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        `Then assert get recommendation request`(categoryL3Id)
    }
}
