package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_FAILED
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData.Companion.STATE_READY
import com.tokopedia.tokopedianow.searchcategory.presentation.model.RecommendationCarouselDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import org.junit.Assert.assertThat
import org.hamcrest.core.Is.`is` as shouldBe

class BindRecommendationCarouselTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val callback: Callback,
) {

    private val recommendationEntity = "recom/recom-carousel.json".jsonToObject<RecommendationEntity>()
    private val recommendationWidgetList = recommendationEntity.mapToRecommendationWidgetList()
    private val getRecommendationParamsSlot = slot<GetRecommendationRequestParam>()

    private fun RecommendationEntity.mapToRecommendationWidgetList() =
            productRecommendationWidget
                    .data
                    .mappingToRecommendationModel()

    fun `bind recommendation success`() {
        callback.`Given view will show recommendation carousel`()
        `Given get recommendation will be successful`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val recomWidget = visitableList.findRecomWidget()
        val recomWidgetPosition = visitableList.lastIndex

        `When bind recommendation carousel`(recomWidget, recomWidgetPosition)

        callback.`Then assert get recommendation request`(getRecommendationParamsSlot.captured)
        `Then assert recommendation data view is updated to ready with recom widget`(recomWidget)
        `Then assert updated visitable list`(recomWidgetPosition)
    }

    private fun `Given get recommendation will be successful`() {
        coEvery {
            getRecommendationUseCase.getData(capture(getRecommendationParamsSlot))
        } returns recommendationWidgetList
    }

    private fun List<Visitable<*>>.findRecomWidget(): RecommendationCarouselDataView =
            find { it is RecommendationCarouselDataView } as? RecommendationCarouselDataView
                    ?: throw Throwable("Cannot find recom widget")

    private fun `When bind recommendation carousel`(
            recomWidget: RecommendationCarouselDataView,
            recomWidgetPosition: Int,
    ) {
        baseViewModel.onBindRecommendationCarousel(recomWidget, recomWidgetPosition)
    }

    private fun `Then assert recommendation data view is updated to ready with recom widget`(
            recomWidget: RecommendationCarouselDataView
    ) {
        val recommendationWidget = recommendationWidgetList.first()

        assertThat(recomWidget.carouselData.state, shouldBe(STATE_READY))
        assertThat(recomWidget.carouselData.recommendationData, shouldBe(recommendationWidget))
    }

    private fun `Then assert updated visitable list`(recomWidgetPosition: Int) {
        val updatedIndex = baseViewModel.updatedVisitableIndicesLiveData.value!!

        assertThat(updatedIndex, shouldBe(listOf(recomWidgetPosition)))
    }

    fun `bind recommendation fail`() {
        callback.`Given view will show recommendation carousel`()
        `Given get recommendation will fail`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
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

    fun `should not load recommendation again after success`() {
        callback.`Given view will show recommendation carousel`()
        `Given get recommendation will be successful`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
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
        baseViewModel.onBindRecommendationCarousel(recomWidget, recomWidgetPosition)
    }

    private fun `Then verify get recommendation only called once`() {
        coVerify(exactly = 1) {
            getRecommendationUseCase.getData(any())
        }
    }

    interface Callback {
        fun `Given view will show recommendation carousel`()
        fun `Then assert get recommendation request`(
                getRecommendationRequestParam: GetRecommendationRequestParam
        )
    }
}