package com.tokopedia.tkpd.flashsale.presentation.list.container

import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.usecase.GetTargetedTickerUseCase
import com.tokopedia.campaign.utils.constant.TickerConstant
import com.tokopedia.campaign.utils.constant.TickerType
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.domain.entity.SellerEligibility
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleListForSellerMetaUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductSubmissionProgressUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleSellerStatusUseCase
import com.tokopedia.tkpd.flashsale.util.constant.TabConstant
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
        // Given
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
        val tickerData = emptyList<RemoteTicker>()
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
        val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
            GetTargetedTickerUseCase.Param.Target(
                type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                values = rollenceValueList
            )
        )
        val tickerParams = GetTargetedTickerUseCase.Param(
            page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_CAMPAIGN_LIST,
            targets = targetParams
        )

        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata
        coEvery { getTargetedTickerUseCase.execute(tickerParams) } returns tickerData

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        // When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        // Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isLoading)
        assertEquals(tabsMetadata.tabs, actual.tabs)
        assertEquals(false, actual.showTicker)
        assertEquals(true, actual.isEligibleUsingFeature)
        assertEquals(null, actual.error)

        job.cancel()
    }

    @Test
    fun `When fetch prerequisite data success, rbac rule is active and user is allowed, should successfully receive the datas & show the ticker`() = runBlockingTest {
        // Given
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
        val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
            GetTargetedTickerUseCase.Param.Target(
                type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                values = rollenceValueList
            )
        )
        val tickerParams = GetTargetedTickerUseCase.Param(
            page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_CAMPAIGN_LIST,
            targets = targetParams
        )

        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata
        coEvery { getTargetedTickerUseCase.execute(tickerParams) } returns tickerData

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        // When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        // Then
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
        // Given
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

        // When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        // Then
        val actual = emittedValues.last()

        assertEquals(true, actual.isEligibleUsingFeature)

        job.cancel()
    }

    @Test
    fun `When rbac rule is active but user is not allowed, isEligibleUsingFeature should be false`() = runBlockingTest {
        // Given
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
        val tickerData = emptyList<RemoteTicker>()
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = false)
        val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
            GetTargetedTickerUseCase.Param.Target(
                type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                values = rollenceValueList
            )
        )
        val tickerParams = GetTargetedTickerUseCase.Param(
            page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_CAMPAIGN_LIST,
            targets = targetParams
        )

        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata
        coEvery { getTargetedTickerUseCase.execute(tickerParams) } returns tickerData

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        // When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        // Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isEligibleUsingFeature)

        job.cancel()
    }

    @Test
    fun `When user is not allowed to use feature, isEligibleUsingFeature should be false`() = runBlockingTest {
        // Given
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
        val tickerData = emptyList<RemoteTicker>()
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val sellerEligibility = SellerEligibility(isDeviceAllowed = true, isUserAllowed = false)
        val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
            GetTargetedTickerUseCase.Param.Target(
                type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                values = rollenceValueList
            )
        )
        val tickerParams = GetTargetedTickerUseCase.Param(
            page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_CAMPAIGN_LIST,
            targets = targetParams
        )

        coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } returns tabsMetadata
        coEvery { getTargetedTickerUseCase.execute(tickerParams) } returns tickerData

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        // When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        // Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isEligibleUsingFeature)

        job.cancel()
    }

    @Test
    fun `When fetch tabs metadata from remote error, should emit correct error event`() = runBlockingTest {
        // Given
        val error = MessageErrorException("Server Error")
        val expectedEvent = FlashSaleContainerViewModel.UiEffect.ErrorFetchTabsMetaData(error)
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
            GetTargetedTickerUseCase.Param.Target(
                type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                values = rollenceValueList
            )
        )
        val tickerParams = GetTargetedTickerUseCase.Param(
            page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_CAMPAIGN_LIST,
            targets = targetParams
        )

        coEvery { getFlashSaleSellerStatusUseCase.execute() } throws error
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } throws error
        coEvery { getTargetedTickerUseCase.execute(tickerParams) } throws error

        val emittedEvent = arrayListOf<FlashSaleContainerViewModel.UiEffect>()
        val job = launch {
            viewModel.uiEffect.toList(emittedEvent)
        }

        // When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        // Then
        val actualEvent = emittedEvent.last()

        assertEquals(expectedEvent, actualEvent)
        assertEquals(expectedEvent.throwable, (actualEvent as? FlashSaleContainerViewModel.UiEffect.ErrorFetchTabsMetaData)?.throwable)

        job.cancel()
    }

    @Test
    fun `When fetch tabs metadata from remote error, isLoading should be false and error value should be set`() = runBlockingTest {
        // Given
        val error = MessageErrorException("Server Error")
        val rollenceValueList: List<String> = listOf("ct_ticker_1", "ct_ticker_2")
        val tickerData = emptyList<RemoteTicker>()
        val targetParams: List<GetTargetedTickerUseCase.Param.Target> = listOf(
            GetTargetedTickerUseCase.Param.Target(
                type = GetTargetedTickerUseCase.KEY_TYPE_ROLLENCE_NAME,
                values = rollenceValueList
            )
        )
        val tickerParams = GetTargetedTickerUseCase.Param(
            page = TickerConstant.REMOTE_TICKER_KEY_FLASH_SALE_TOKOPEDIA_CAMPAIGN_LIST,
            targets = targetParams
        )

        coEvery { getFlashSaleSellerStatusUseCase.execute() } throws error
        coEvery { getFlashSaleListForSellerMetaUseCase.execute() } throws error
        coEvery { getTargetedTickerUseCase.execute(tickerParams) } returns tickerData

        val emittedValues = arrayListOf<FlashSaleContainerViewModel.UiState>()
        val job = launch {
            viewModel.uiState.toList(emittedValues)
        }

        // When
        viewModel.processEvent(
            event = FlashSaleContainerViewModel.UiEvent.GetPrerequisiteData,
            rollenceValueList = rollenceValueList
        )

        // Then
        val actual = emittedValues.last()

        assertEquals(false, actual.isLoading)
        assertEquals(error, actual.error)

        job.cancel()
    }
}
