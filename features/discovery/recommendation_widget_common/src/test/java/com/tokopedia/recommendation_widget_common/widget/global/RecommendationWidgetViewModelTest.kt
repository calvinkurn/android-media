package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.recommendation_widget_common.MainDispatcherRule
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst.PAGENAME_VERTICAL
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.jsonToObject
import com.tokopedia.recommendation_widget_common.mvvm.ViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecommendationComparisonBpcModel
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetState
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetState as State

@OptIn(ExperimentalCoroutinesApi::class)
class RecommendationWidgetViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getRecommendationWidgetUseCase = mockk<GetRecommendationUseCase>(relaxed = true)

    private fun ViewModel(state: State = State()): RecommendationWidgetViewModel =
        RecommendationWidgetViewModel(
            state,
            getRecommendationWidgetUseCase,
        )

    private val ViewModel<RecommendationWidgetState>.stateValue
        get() = stateFlow.value

    private fun String.jsonToRecommendationWidgetList(): List<RecommendationWidget> =
        this.jsonToObject<RecommendationEntity>()
            .productRecommendationWidget
            .data.mappingToRecommendationModel()

    private val requestParamSlot = slot<GetRecommendationRequestParam>()
    private val requestParam: GetRecommendationRequestParam by lazy { requestParamSlot.captured }

    @Test
    fun `bind model will call use case`() {
        val viewModel = ViewModel()

        val metadata = RecommendationWidgetMetadata(
            pageNumber = 1,
            productIds = listOf("123456"),
            queryParam = "test=test&test2=test2",
            pageName = "pageName",
            categoryIds = listOf("1", "2", "3"),
            keyword = listOf("samsung", "iphone", "xiaomi"),
            isTokonow = true,
        )
        val model = RecommendationWidgetModel(metadata = metadata)

        viewModel.bind(model)

        coVerify { getRecommendationWidgetUseCase.getData(capture(requestParamSlot)) }
        assertEquals(requestParam.pageNumber, model.metadata.pageNumber)
        assertEquals(requestParam.productIds, model.metadata.productIds)
        assertEquals(requestParam.queryParam, model.metadata.queryParam)
        assertEquals(requestParam.pageName, model.metadata.pageName)
        assertEquals(requestParam.categoryIds, model.metadata.categoryIds)
        assertEquals(requestParam.keywords, model.metadata.keyword)
        assertEquals(requestParam.isTokonow, model.metadata.isTokonow)
    }

    @Test
    fun `bind model with use case throws exception will hide the view`() {
        val viewModel = ViewModel()

        coEvery { getRecommendationWidgetUseCase.getData(any()) } throws Exception()

        val metadata = RecommendationWidgetMetadata(
            pageNumber = 1,
            productIds = listOf("123456"),
            queryParam = "test=test&test2=test2",
            pageName = "pageName",
            categoryIds = listOf("1", "2", "3"),
            keyword = listOf("samsung", "iphone", "xiaomi"),
            isTokonow = true,
        )
        val model = RecommendationWidgetModel(metadata = metadata)

        viewModel.bind(model)

        assertEquals(1, viewModel.stateFlow.value.widgetMap.size)
        assertEquals(0, viewModel.stateValue.widgetMap[model.id]!!.size)
    }

    // Test case for our temporary solution
    // RecommendationWidgetModel should not contain RecommendationWidget
    // RecommendationWidget should only exists by calling getRecommendationWidgetUseCase
    @Test
    fun `bind model containing widget data will not call use case`() {
        val viewModel = ViewModel()

        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val model = RecommendationWidgetModel(
            metadata = metadata,
            widget = RecommendationWidget(
                layoutType = TYPE_COMPARISON_BPC_WIDGET,
            ),
        )

        viewModel.bind(model)

        coVerify (exactly = 0) { getRecommendationWidgetUseCase.getData(any()) }
        assertEquals(1, viewModel.stateFlow.value.widgetMap.size)
        assertThat(
            viewModel.stateValue.widgetMap[model.id]!!.first(),
            `is`(instanceOf(RecommendationComparisonBpcModel::class.java))
        )
    }

    @Test
    fun `use case return type carousel will render carousel visitable`() {
        val viewModel = ViewModel()

        val recommendationWidgetList = "carousel_hatc.json".jsonToRecommendationWidgetList()
        coEvery { getRecommendationWidgetUseCase.getData(any()) } returns recommendationWidgetList

        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)

        viewModel.bind(model)

        assertEquals(1, viewModel.stateValue.widgetMap.size)

        val expectedVisitableList = viewModel.stateValue.widgetMap[model.id]!!
        assertThat(
            expectedVisitableList.first(),
            `is`(instanceOf(RecommendationCarouselModel::class.java))
        )

        val carouselWidget = expectedVisitableList.first() as RecommendationCarouselModel
        assertEquals(carouselWidget.metadata, metadata)
        assertEquals(carouselWidget.trackingModel, trackingModel)
        assertEquals(carouselWidget.widget, recommendationWidgetList.first())
    }

    @Test
    fun `layout name vertical will render carousel vertical`() {
        val viewModel = ViewModel()

        val recommendationWidgetList = "recom_vertical.json".jsonToRecommendationWidgetList()
        coEvery { getRecommendationWidgetUseCase.getData(any()) } returns recommendationWidgetList

        val metadata = RecommendationWidgetMetadata(pageName = PAGENAME_VERTICAL)
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)

        viewModel.bind(model)

        assertEquals(1, viewModel.stateValue.widgetMap.size)

        val expectedVisitableList = viewModel.stateValue.widgetMap[model.id]!!
        assertThat(
            expectedVisitableList.first(),
            `is`(instanceOf(RecommendationVerticalModel::class.java))
        )

        val verticalWidget = expectedVisitableList.first() as RecommendationVerticalModel
        assertEquals(verticalWidget.metadata, metadata)
        assertEquals(verticalWidget.trackingModel, trackingModel)
        assertEquals(verticalWidget.widget, recommendationWidgetList.first())
    }

    @Test
    fun `refresh will clear all data in the states`() {
        val metadata = RecommendationWidgetMetadata(pageName = "pageName")
        val trackingModel = RecommendationWidgetTrackingModel(androidPageName = "pageName")
        val model = RecommendationWidgetModel(metadata = metadata, trackingModel = trackingModel)
        val recommendationWidgetList = "carousel_hatc.json".jsonToRecommendationWidgetList()
        val currentState = RecommendationWidgetState().from(model, recommendationWidgetList)
        val viewModel = ViewModel(currentState)

        viewModel.refresh()

        assertEquals(0, viewModel.stateValue.widgetMap.size)
    }

    @Test
    fun `bind model will not call use case if state has data for that model`() {
        val metadata = RecommendationWidgetMetadata(
            pageNumber = 1,
            productIds = listOf("123456"),
            queryParam = "test=test&test2=test2",
            pageName = "pageName",
            categoryIds = listOf("1", "2", "3"),
            keyword = listOf("samsung", "iphone", "xiaomi"),
            isTokonow = true,
        )
        val model = RecommendationWidgetModel(metadata = metadata)
        val state = RecommendationWidgetState().loading(model)
        val viewModel = ViewModel(state)

        viewModel.bind(model)

        coVerify (exactly = 0) { getRecommendationWidgetUseCase.getData(any()) }
    }
}
