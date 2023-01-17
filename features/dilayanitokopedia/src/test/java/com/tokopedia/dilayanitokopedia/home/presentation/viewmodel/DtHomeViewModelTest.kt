package com.tokopedia.dilayanitokopedia.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.dilayanitokopedia.common.constant.DtLayoutState
import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.domain.model.HomeLayoutResponse
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetHomeAnchorTabUseCase
import com.tokopedia.dilayanitokopedia.home.domain.usecase.GetHomeLayoutDataUseCase
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeLoadingStateUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutListUiModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DtHomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeLayoutDataUseCase = mockk<GetHomeLayoutDataUseCase>(relaxed = true)
    private val getChooseAddressWarehouseLocUseCase = mockk<GetChosenAddressWarehouseLocUseCase>(relaxed = true)
    private val getAnchorTabUseCase = mockk<GetHomeAnchorTabUseCase>(relaxed = true)
    private val homeLayoutListObserver = mockk<Observer<Result<HomeLayoutListUiModel>>>(relaxed = true)

    lateinit var viewModel: DtHomeViewModel

    private val mockThrowable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModel = DtHomeViewModel(
            getHomeLayoutDataUseCase,
            getAnchorTabUseCase,
            CoroutineTestDispatchersProvider
        )
        viewModel.homeLayoutList.observeForever(homeLayoutListObserver)
    }

    @Test
    fun `verify when get empty state is success`() {
        // Inject
        val id = "1"
        val serviceType = "serviceType"
        val data = HomeLayoutListUiModel(
            items = viewModel.getHomeVisitableList(),
            state = DtLayoutState.HIDE
        )

        // When
        viewModel.getEmptyState(id, serviceType)

        // Then
        verify {
            homeLayoutListObserver.onChanged(Success(data))
        }
    }

    @Test
    fun `verify when get home layout is success`() {
        // Inject
        val mockResponse = spyk(
            arrayListOf(
                spyk(
                    HomeLayoutResponse()
                )
            )
        )
        val data = HomeLayoutListUiModel(
            items = viewModel.getHomeVisitableList(),
            state = DtLayoutState.SHOW
        )

        // Given
        coEvery {
            getHomeLayoutDataUseCase.execute(localCacheModel = any())
        } returns mockResponse

        // When
        viewModel.getHomeLayout(mockk())

        // Then
        verify {
            homeLayoutListObserver.onChanged(Success(data))
        }
    }

    @Test
    fun `verify when get home layout request error`() {
        // Given
        coEvery {
            getHomeLayoutDataUseCase.execute(localCacheModel = any())
        } throws mockThrowable

        // When
        viewModel.getHomeLayout(mockk())

        // Then
        verify {
            homeLayoutListObserver.onChanged(Fail(mockThrowable))
        }
    }

    @Test
    fun `verify function switchServiceOrLoadLayout is correctly`() {
        // Inject
        val loadingLayout = HomeLoadingStateUiModel(id = HomeStaticLayoutId.LOADING_STATE)
        val data = HomeLayoutListUiModel(
            items = HomeLayoutItemUiModel(loadingLayout, HomeLayoutItemState.LOADED, null).layout?.let {
                listOf(it)
            } ?: viewModel.getHomeVisitableList(),
            state = DtLayoutState.LOADING
        )

        // When
        viewModel.switchService()

        // Then
        verify {
            homeLayoutListObserver.onChanged(Success(data))
        }
    }
}
