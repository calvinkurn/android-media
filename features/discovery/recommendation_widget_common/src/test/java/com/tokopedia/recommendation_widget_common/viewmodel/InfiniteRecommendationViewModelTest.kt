package com.tokopedia.recommendation_widget_common.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.infinite.component.loading.InfiniteLoadingUiModel
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
    fun `fetch recommendation success return components and hasNext`() {
        val title = "This is Title"

        val response = listOf(
            RecommendationWidget(
                title = title,
                recommendationItemList = listOf(
                    RecommendationItem()
                ),
                hasNext = true,
                currentPage = 1
            )
        )
        val params = GetRecommendationRequestParam()

        coEvery {
            getRecommendationUseCase.getData(params)
        } returns response

        viewModel.init()
        viewModel.fetchComponents(params)

        val components = viewModel.components.value
        Assert.assertTrue(components != null)
        Assert.assertTrue(components!!.isNotEmpty())

        val firstComponent = components.first()
        Assert.assertTrue(firstComponent is InfiniteTitleUiModel)
        val titleModel = firstComponent as InfiniteTitleUiModel
        Assert.assertTrue(titleModel.title == title)

        val lastComponent = components.last()
        Assert.assertTrue(lastComponent is InfiniteLoadingUiModel)

        Assert.assertTrue(components.size == 3)
    }

    @Test
    fun `fetch recommendation success return components and not hasNext`() {
        val title = "This is Title"

        val response = listOf(
            RecommendationWidget(
                title = title,
                recommendationItemList = listOf(
                    RecommendationItem()
                ),
                hasNext = false,
                currentPage = 1
            )
        )
        val params = GetRecommendationRequestParam()

        coEvery {
            getRecommendationUseCase.getData(params)
        } returns response

        viewModel.init()
        viewModel.fetchComponents(params)

        val components = viewModel.components.value
        Assert.assertTrue(components != null)
        Assert.assertTrue(components!!.isNotEmpty())

        val firstComponent = components.first()
        Assert.assertTrue(firstComponent is InfiniteTitleUiModel)
        val titleModel = firstComponent as InfiniteTitleUiModel
        Assert.assertTrue(titleModel.title == title)

        Assert.assertTrue(components.size == 2)
    }

    @Test
    fun `fetch recommendation return empty`() {
        val params = GetRecommendationRequestParam()

        coEvery {
            getRecommendationUseCase.getData(params)
        } returns emptyList()

        viewModel.init()
        viewModel.fetchComponents(params)

        val components = viewModel.components.value
        Assert.assertTrue(components != null)
        Assert.assertTrue(components!!.isEmpty())
    }
}
