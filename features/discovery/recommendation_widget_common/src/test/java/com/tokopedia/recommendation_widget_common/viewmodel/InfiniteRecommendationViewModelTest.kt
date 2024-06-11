package com.tokopedia.recommendation_widget_common.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.infinite.component.loading.InfiniteLoadingUiModel
import com.tokopedia.recommendation_widget_common.infinite.component.separator.InfiniteSeparatorUiModel
import com.tokopedia.recommendation_widget_common.infinite.component.title.InfiniteTitleUiModel
import com.tokopedia.recommendation_widget_common.infinite.main.InfiniteRecommendationViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class InfiniteRecommendationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    private val viewModel = InfiniteRecommendationViewModel(
        getRecommendationUseCase,
        CoroutineTestDispatchersProvider
    )

    @Test
    fun `fetch recommendation success return components without title and hasNext`() {
        val title = "This is Title"

        val response = listOf(
            RecommendationWidget(
                title = title,
                recommendationItemList = listOf(
                    RecommendationItem()
                ),
                hasNext = false,
                currentPage = 2
            )
        )
        val params = GetRecommendationRequestParam()

        coEvery {
            getRecommendationUseCase.getData(params)
        } returns response

        viewModel.init(AppLogAdditionalParam.None)
        viewModel.fetchComponents(params)

        val components = viewModel.components.value
        Assert.assertTrue(components != null)
        Assert.assertTrue(components!!.isNotEmpty())

        Assert.assertTrue(components.size == 1)
    }

    @Test
    fun `fetch recommendation return empty`() {
        val params = GetRecommendationRequestParam()

        coEvery {
            getRecommendationUseCase.getData(params)
        } returns emptyList()

        viewModel.init(AppLogAdditionalParam.None)
        viewModel.fetchComponents(params)

        val components = viewModel.components.value
        Assert.assertTrue(components != null)
        Assert.assertTrue(components!!.isEmpty())
    }

    @Test
    fun `fetch recommendation success return components should add to last when no loading`() {
        val title = "This is Title"

        val response = listOf(
            RecommendationWidget(
                title = title,
                recommendationItemList = listOf(
                    RecommendationItem()
                ),
                hasNext = false,
                currentPage = 2
            )
        )
        val params = GetRecommendationRequestParam()

        coEvery {
            getRecommendationUseCase.getData(params)
        } returns response

        viewModel.fetchComponents(params)

        val components = viewModel.components.value
        Assert.assertTrue(components != null)
        Assert.assertTrue(components!!.isNotEmpty())

        Assert.assertTrue(components.size == 1)
    }
}
