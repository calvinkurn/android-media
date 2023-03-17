package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutType
import com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.ui.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets.AnchorTabMapper.KEYWOARD_CHANNEL_GROUP_ID
import com.tokopedia.dilayanitokopedia.home.domain.mapper.widgets.AnchorTabMapper.KEY_ANCHOR_IDENTIFIER
import com.tokopedia.dilayanitokopedia.home.domain.model.GetHomeAnchorTabResponse
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetAnchorTabUseCase
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetLayoutDataUseCase
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeLoadingStateUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DtHomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getLayoutDataUseCase = mockk<GetLayoutDataUseCase>(relaxed = true)
    private val getHomeAnchorTabUseCase = mockk<GetAnchorTabUseCase>(relaxed = true)

    private val homeLayoutListObserver =
        mockk<Observer<Result<HomeLayoutListUiModel>>>(relaxed = true)

    private val homeAnchorListObserver =
        mockk<Observer<Result<List<AnchorTabUiModel>>>>(relaxed = true)

    lateinit var viewModel: DtHomeViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = DtHomeViewModel(
            getLayoutDataUseCase,
            getHomeAnchorTabUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.homeLayoutList.observeForever(homeLayoutListObserver)
        viewModel.anchorTabState.observeForever(homeAnchorListObserver)
    }

    @Test
    fun `verify get home layout and get anchor tab menu success with same group id is correctly`() {
        // Inject
        val groupId = "1"
        val feParam = "$KEY_ANCHOR_IDENTIFIER=${KEYWOARD_CHANNEL_GROUP_ID}$groupId"
        val mockMenuResponse = spyk(
            GetHomeAnchorTabResponse.GetHomeIconV2(
                icons = arrayListOf(
                    spyk(
                        GetHomeAnchorTabResponse.GetHomeIconV2.Icon(
                            feParam = feParam
                        )
                    )
                )
            )
        )
        val mockResponse = spyk(
            arrayListOf(
                spyk(
                    HomeLayoutResponse(
                        layout = DtLayoutType.LEGO_6_IMAGE,
                        groupId = groupId
                    )
                )
            )
        )

        // Given
        coEvery {
            getHomeAnchorTabUseCase.execute(any())
        } returns mockMenuResponse
        coEvery {
            getLayoutDataUseCase.execute(localCacheModel = any())
        } returns mockResponse

        // When
        viewModel.getHomeLayout(mockk())

        // Then
        Assert.assertNotNull(viewModel.isLastWidgetIsRecommendationForYou())
        Assert.assertTrue(viewModel.isLastWidgetIsRecommendationForYou() ?: false)
        Assert.assertNotNull(viewModel.getPositionUsingGroupId(groupId))
        Assert.assertFalse(viewModel.isOnLoading)
    }

    /**
     *
     * buat test sendiri si anchor tab
     * getAnchorTabMenu
     *
     */

    @Test
    fun `verify get anchor tab list`() {
        // Inject
        val groupId = "1"
        val feParam = "$KEY_ANCHOR_IDENTIFIER=${KEYWOARD_CHANNEL_GROUP_ID}$groupId"
        val mockMenuResponse = spyk(
            GetHomeAnchorTabResponse.GetHomeIconV2(
                icons = arrayListOf(spyk(GetHomeAnchorTabResponse.GetHomeIconV2.Icon(feParam = feParam)))
            )
        )
        val data = listOf(AnchorTabUiModel("0", "", "", groupId))

        // Given
        coEvery {
            getHomeAnchorTabUseCase.execute(any())
        } returns mockMenuResponse

        // When
        viewModel.getAnchorTabMenu(mockk())

        // Then

        verify {
            homeAnchorListObserver.onChanged(Success(data))
        }
    }

    @Test
    fun `verify get home layout and get anchor tab menu success but different group id is correctly`() {
        // Inject
        val groupId = "1"
        val feParam = "$KEY_ANCHOR_IDENTIFIER=${KEYWOARD_CHANNEL_GROUP_ID}$groupId"
        val mockMenuResponse = spyk(
            GetHomeAnchorTabResponse.GetHomeIconV2(
                icons = arrayListOf(
                    spyk(
                        GetHomeAnchorTabResponse.GetHomeIconV2.Icon(
                            feParam = feParam
                        )
                    )
                )
            )
        )
        val mockResponse = spyk(
            arrayListOf(
                spyk(
                    HomeLayoutResponse()
                )
            )
        )

        // Given
        coEvery {
            getHomeAnchorTabUseCase.execute(any())
        } returns mockMenuResponse
        coEvery {
            getLayoutDataUseCase.execute(localCacheModel = any())
        } returns mockResponse

        // When
        viewModel.getHomeLayout(mockk())

        // Then
        Assert.assertNotNull(viewModel.isLastWidgetIsRecommendationForYou())
        Assert.assertTrue(viewModel.isLastWidgetIsRecommendationForYou() ?: false)
        Assert.assertNull(viewModel.getAnchorTabByVisitablePosition(0))
        Assert.assertNull(viewModel.getPositionUsingGroupId(groupId))
    }

    @Test
    fun `verify succedd get home layout and failed get anchor tab menu is correctly`() {
        // Inject
        val groupId = "1"
        val mockResponse = spyk(
            arrayListOf(
                spyk(
                    HomeLayoutResponse()
                )
            )
        )

        // Given
        coEvery {
            getHomeAnchorTabUseCase.execute(any())
        } throws mockThrowable
        coEvery {
            getLayoutDataUseCase.execute(localCacheModel = any())
        } returns mockResponse

        // When
        viewModel.getHomeLayout(mockk())

        // Then
        Assert.assertNotNull(viewModel.isLastWidgetIsRecommendationForYou())
        Assert.assertTrue(viewModel.isLastWidgetIsRecommendationForYou() ?: false)
        Assert.assertNull(viewModel.getAnchorTabByVisitablePosition(0))
        Assert.assertNull(viewModel.getPositionUsingGroupId(groupId))
        Assert.assertFalse(viewModel.isOnLoading)
    }

    @Test
    fun `verify when get home layout request error`() {
        // Given
        coEvery {
            getLayoutDataUseCase.execute(localCacheModel = any())
        } throws mockThrowable

        // When
        viewModel.getHomeLayout(mockk())

        // Then
        Assert.assertNull(viewModel.isLastWidgetIsRecommendationForYou())
        Assert.assertNull(viewModel.getAnchorTabByVisitablePosition(0))
        Assert.assertNull(viewModel.getPositionUsingGroupId(""))
        verify {
            homeLayoutListObserver.onChanged(Fail(mockThrowable))
        }
    }

    @Test
    fun `verify function switchServiceOrLoadLayout is correctly`() {
        // Inject
        val loadingLayout = HomeLoadingStateUiModel(id = com.tokopedia.dilayanitokopedia.ui.home.constant.HomeStaticLayoutId.LOADING_STATE)
        val data = HomeLayoutListUiModel(
            items = HomeLayoutItemUiModel(
                loadingLayout,
                com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState.LOADED,
                null
            ).layout?.let {
                listOf(it)
            } ?: viewModel.getHomeVisitableList(),
            state = DtLayoutState.LOADING
        )

        // When
        viewModel.refreshLayout()

        // Then
        Assert.assertTrue(viewModel.isOnLoading)
        verify {
            homeLayoutListObserver.onChanged(Success(data))
        }
    }

    @Test
    fun `verify function loadLayout is correctly`() {
        // Inject
        val loadingLayout = HomeLoadingStateUiModel(id = com.tokopedia.dilayanitokopedia.ui.home.constant.HomeStaticLayoutId.LOADING_STATE)
        val data = HomeLayoutListUiModel(
            items = HomeLayoutItemUiModel(
                loadingLayout,
                com.tokopedia.dilayanitokopedia.ui.home.constant.HomeLayoutItemState.LOADED,
                null
            ).layout?.let {
                listOf(it)
            } ?: viewModel.getHomeVisitableList(),
            state = DtLayoutState.LOADING
        )

        // When
        viewModel.loadLayout()

        // Then
        verify {
            homeLayoutListObserver.onChanged(Success(data))
        }
    }
}
