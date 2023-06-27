package com.tokopedia.tkpd.flashsale.presentation.list.container

import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.usecase.GetTargetedTickerUseCase
import com.tokopedia.campaign.utils.constant.TickerType
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.domain.entity.SellerEligibility
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductSubmissionProgressUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleSellerStatusUseCase
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEffect
import com.tokopedia.tkpd.flashsale.presentation.list.child.uimodel.FlashSaleListUiEvent
import com.tokopedia.tkpd.flashsale.util.TickerUtil
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FlashSaleContainerViewModelTest {

    @RelaxedMockK
    lateinit var getFlashSaleListForSellerMetaUseCase: GetFlashSaleListForSellerMetaUseCase

    @RelaxedMockK
    lateinit var getTargetedTickerUseCase: GetTargetedTickerUseCase

    @RelaxedMockK
    lateinit var getFlashSaleSellerStatusUseCase: GetFlashSaleSellerStatusUseCase

    @RelaxedMockK
    lateinit var getFlashSaleProductSubmissionProgressUseCase: GetFlashSaleProductSubmissionProgressUseCase

    private lateinit var viewModel: FlashSaleContainerViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = FlashSaleContainerViewModel(
            CoroutineTestDispatchersProvider,
            getFlashSaleListForSellerMetaUseCase,
            getFlashSaleSellerStatusUseCase,
            getFlashSaleProductSubmissionProgressUseCase,
            getTargetedTickerUseCase
        )
    }

    @Test
    fun `When fetch prerequisite data success, rbac rule is active and user is allowed, should successfully receive the data`() = runBlockingTest {
        //Given
        val tabsMetadata = TabMetadata(
            listOf(
                TabMetadata.Tab(
                    TabConstant.TAB_ID_UPCOMING,
                    "upcoming",
                    100,
                    "Akan Datang"
                )
            )
        )
        val tickerData = listOf(
            RemoteTicker(
                title = "some ticker title",
                description = "some ticker description",
                type = TickerType.INFO,
                actionLabel = "some ticker action label",
                actionType = "link",
                actionAppUrl = "https://tokopedia.com"
            )
        )
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)

        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        //Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isLoading)
        assertEquals(null, actual.error)
        assertEquals(tabsMetadata.tabs, actual.tabs)
        assertEquals(true, actual.showTicker)
        assertEquals(tickerData, actual.tickerList)
        assertEquals(true, actual.isEligibleUsingFeature)

        job.cancel()
    }

    @Test
    fun `When rbac rule is inactive but user is allowed, isEligibleUsingFeature should be true`() = runBlockingTest {
        //Given
        val tabsMetadata = TabMetadata(
            listOf(
                TabMetadata.Tab(
                    TabConstant.TAB_ID_UPCOMING,
                    "upcoming",
                    100,
                    "Akan Datang"
                )
            )
        )
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val sellerEligibility = SellerEligibility(isDeviceAllowed = false, isUserAllowed = true)

        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        //Then
        val actual = emittedValues.last()

        assertEquals(true, actual.isEligibleUsingFeature)

        job.cancel()
    }

    @Test
    fun `When rbac rule is active but user is not allowed, isEligibleUsingFeature should be false`() = runBlockingTest {
        //Given
        val tabsMetadata = TabMetadata(
            listOf(
                TabMetadata.Tab(
                    TabConstant.TAB_ID_UPCOMING,
                    "upcoming",
                    100,
                    "Akan Datang"
                )
            )
        )
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = false)

        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        //Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isEligibleUsingFeature)

        job.cancel()
    }

    @Test
    fun `When user is not allowed to use feature, isEligibleUsingFeature should be false`() = runBlockingTest {
        //Given
        val tabsMetadata = TabMetadata(
            listOf(
                TabMetadata.Tab(
                    TabConstant.TAB_ID_UPCOMING,
                    "upcoming",
                    100,
                    "Akan Datang"
                )
            )
        )
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = false)

        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        //Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isEligibleUsingFeature)

        job.cancel()
    }

//    @Test
//    fun `When fetch tabs metadata from remote success and has showed multi location ticker previously , showTicker should be false`() = runBlockingTest {
//        //Given
//        val tabsMetadata = TabMetadata(
//            listOf(
//                TabMetadata.Tab(
//                    TabConstant.TAB_ID_UPCOMING,
//                    "upcoming",
//                    100,
//                    "Akan Datang"
//                )
//            )
//        )
//        val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
//
//        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
//        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata
//
//        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
//        val job = launch {
//            viewModel.uiState.toList(emittedValues)
//        }
//
////        coEvery { preferenceDataStore.isMultiLocationTickerDismissed() } returns true
//
//        //When
//        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData)
//
//        //Then
//        val actual = emittedValues.last()
//
//        assertEquals(false, actual.showTicker)
//
//        job.cancel()
//    }

//    @Test
//    fun `When fetch tabs metadata from remote success and has not showed multi location ticker previously , showTicker should be true`() = runBlockingTest {
//        //Given
//        val tabsMetadata = TabMetadata(
//            listOf(
//                TabMetadata.Tab(
//                    TabConstant.TAB_ID_UPCOMING,
//                    "upcoming",
//                    100,
//                    "Akan Datang"
//                )
//            )
//        )
//
//        val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
//
//        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
//        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata
//
//        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
//        val job = launch {
//            viewModel.uiState.toList(emittedValues)
//        }
//
////        coEvery { preferenceDataStore.isMultiLocationTickerDismissed() } returns false
//
//        //When
//        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData)
//
//        //Then
//        val actual = emittedValues.last()
//
//        assertEquals(true, actual.showTicker)
//
//        job.cancel()
//    }

    @Test
    fun `When fetch tabs metadata from remote error, should emit correct error event`() = runBlockingTest {
        //Given
        val error = MessageErrorException("Server Error")
        val expectedEvent = FlashSaleContainerViewModel.UiEffect.ErrorFetchTabsMetaData(error)
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")

        coEvery { getFlashSaleSellerStatusUseCase.execute() } throws  error
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } throws error

        val emittedEvent = arrayListOf<FlashSaleContainerViewModel.UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEvent)
        }

        //When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

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
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")

        coEvery { getFlashSaleSellerStatusUseCase.execute() } throws  error
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } throws error


        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        //When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        //Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isLoading)
        assertEquals(error, actual.error)

        job.cancel()
    }

//    @Test
//    fun `When dismiss multi location ticker, should update the multi location ticker state as dismissed`() = runBlockingTest {
//        //When
//        viewModel.processEvent(FlashSaleContainerViewModel.UiEvent.DismissMultiLocationTicker)
//
//        //Then
////        coVerify { preferenceDataStore.markMultiLocationTickerAsDismissed() }
//    }
}
