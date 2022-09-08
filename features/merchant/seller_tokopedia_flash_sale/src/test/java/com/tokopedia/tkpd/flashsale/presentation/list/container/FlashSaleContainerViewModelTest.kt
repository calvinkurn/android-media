package com.tokopedia.tkpd.flashsale.presentation.list.container

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.tkpd.flashsale.util.preference.PreferenceDataStore
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FlashSaleContainerViewModelTest {

    @RelaxedMockK
    lateinit var  getFlashSaleListForSellerMetaUseCase: GetFlashSaleListForSellerMetaUseCase

    @RelaxedMockK
    lateinit var preferenceDataStore: PreferenceDataStore

    private lateinit var viewModel: FlashSaleContainerViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = FlashSaleContainerViewModel(
            CoroutineTestDispatchersProvider,
            getFlashSaleListForSellerMetaUseCase,
            preferenceDataStore
        )
    }

    @Test
    fun `When fetch tabs metadata from remote success, should successfully receive the data`() = runBlockingTest {
        //Given
        val tabsMetadata = listOf(TabMetadata(TabConstant.TAB_ID_UPCOMING, "upcoming", 100, "Akan Datang"))

        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        coEvery { preferenceDataStore.isMultiLocationTickerDismissed() } returns false

        //When
        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetTabsMetadata)

        //Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isLoading)
        assertEquals(null, actual.error)
        assertEquals(tabsMetadata, actual.tabsMetadata)

        job.cancel()
    }

    @Test
    fun `When fetch tabs metadata from remote success and has showed multi location ticker previously , showTicker should be false`() = runBlockingTest {
        //Given
        val tabsMetadata = listOf(TabMetadata(TabConstant.TAB_ID_UPCOMING, "upcoming", 100, "Akan Datang"))

        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        coEvery { preferenceDataStore.isMultiLocationTickerDismissed() } returns true

        //When
        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetTabsMetadata)

        //Then
        val actual = emittedValues.last()

        assertEquals(false, actual.showTicker)

        job.cancel()
    }

    @Test
    fun `When fetch tabs metadata from remote success and has not showed multi location ticker previously , showTicker should be true`() = runBlockingTest {
        //Given
        val tabsMetadata = listOf(TabMetadata(TabConstant.TAB_ID_UPCOMING, "upcoming", 100, "Akan Datang"))

        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        coEvery { preferenceDataStore.isMultiLocationTickerDismissed() } returns false

        //When
        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetTabsMetadata)

        //Then
        val actual = emittedValues.last()

        assertEquals(true, actual.showTicker)

        job.cancel()
    }

    @Test
    fun `When fetch tabs metadata from remote error, should emit correct error event`() = runBlockingTest {
        //Given
        val error = MessageErrorException("Server Error")
        val expectedEvent = FlashSaleContainerViewModel.UiEffect.ErrorFetchTabsMetaData(error)

        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } throws error

        val emittedEvent = arrayListOf<FlashSaleContainerViewModel.UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEvent)
        }

        //When
        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetTabsMetadata)

        //Then
        val actualEvent = emittedEvent.last()

        assertEquals(expectedEvent, actualEvent)
        assertEquals(expectedEvent.throwable, (actualEvent as? FlashSaleContainerViewModel.UiEffect.ErrorFetchTabsMetaData)?.throwable)

        job.cancel()
    }

    @Test
    fun `When fetch tabs metadata from remote error, isLoading should be false and error value should be set`() = runBlockingTest {
        //Given
        val error = MessageErrorException("Server Error")
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } throws error


        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetTabsMetadata)

        //Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isLoading)
        assertEquals(error, actual.error)

        job.cancel()
    }

    @Test
    fun `When dismiss multi location ticker, should update the multi location ticker state as dismissed`() = runBlockingTest {
        //When
        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.DismissMultiLocationTicker)

        //Then
        coVerify { preferenceDataStore.markMultiLocationTickerAsDismissed() }
    }
}