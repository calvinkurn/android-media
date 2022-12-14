package com.tokopedia.tkpd.flashsale.presentation.detail

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.data.mapper.FlashSaleMonitorSubmitProductSseMapper
import com.tokopedia.tkpd.flashsale.domain.entity.*
import com.tokopedia.tkpd.flashsale.domain.entity.enums.DetailBottomSheetType
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleListPageTab
import com.tokopedia.tkpd.flashsale.domain.entity.enums.FlashSaleStatus
import com.tokopedia.tkpd.flashsale.domain.usecase.*
import com.tokopedia.tkpd.flashsale.presentation.detail.mapper.ProductCheckingResultMapper
import com.tokopedia.tkpd.flashsale.util.tracker.CampaignDetailPageTracker
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class CampaignDetailViewModelTest {

    @RelaxedMockK
    lateinit var getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase

    @RelaxedMockK
    lateinit var getFlashSaleSubmittedProductListUseCase: GetFlashSaleSubmittedProductListUseCase

    @RelaxedMockK
    lateinit var getFlashSaleSellerStatusUseCase: GetFlashSaleSellerStatusUseCase

    @RelaxedMockK
    lateinit var doFlashSaleProductReserveUseCase: DoFlashSaleProductReserveUseCase

    @RelaxedMockK
    lateinit var doFlashSaleProductDeleteUseCase: DoFlashSaleProductDeleteUseCase

    @RelaxedMockK
    lateinit var doFlashSaleSellerRegistrationUseCase: DoFlashSaleSellerRegistrationUseCase

    @RelaxedMockK
    lateinit var getFlashSaleProductSubmissionProgressUseCase: GetFlashSaleProductSubmissionProgressUseCase

    @RelaxedMockK
    lateinit var flashSaleTkpdProductSubmissionMonitoringSse: FlashSaleTkpdProductSubmissionMonitoringSse

    @RelaxedMockK
    lateinit var flashSaleMonitorSubmitProductSseMapper: FlashSaleMonitorSubmitProductSseMapper

    @RelaxedMockK
    lateinit var doFlashSaleProductSubmitAcknowledgeUseCase: DoFlashSaleProductSubmitAcknowledgeUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var sharedPreference: SharedPreferences

    @RelaxedMockK
    lateinit var tracker: CampaignDetailPageTracker

    @RelaxedMockK
    lateinit var campaignObserver: Observer<in Result<FlashSale>>

    @RelaxedMockK
    lateinit var submittedProductObserver: Observer<in Result<List<DelegateAdapterItem>>>

    @RelaxedMockK
    lateinit var selectedProductObserver: Observer<in List<Pair<Long, Long>>>

    @RelaxedMockK
    lateinit var productReserveObserver: Observer<in Pair<ProductReserveResult, String>>

    @RelaxedMockK
    lateinit var productDeleteObserver: Observer<in ProductDeleteResult>

    @RelaxedMockK
    lateinit var flashSaleRegistrationObserver: Observer<in FlashSaleRegistrationResult>

    @RelaxedMockK
    lateinit var submittedProductVariantObserver: Observer<in List<ProductCheckingResult>>

    @RelaxedMockK
    lateinit var errorObserver: Observer<in Throwable>

    private lateinit var viewModel: CampaignDetailViewModel

    private val dummyCampaignId = 10L

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CampaignDetailViewModel(
            CoroutineTestDispatchersProvider,
            getFlashSaleDetailForSellerUseCase,
            getFlashSaleSubmittedProductListUseCase,
            getFlashSaleSellerStatusUseCase,
            doFlashSaleProductReserveUseCase,
            doFlashSaleProductDeleteUseCase,
            doFlashSaleSellerRegistrationUseCase,
            getFlashSaleProductSubmissionProgressUseCase,
            flashSaleTkpdProductSubmissionMonitoringSse,
            flashSaleMonitorSubmitProductSseMapper,
            doFlashSaleProductSubmitAcknowledgeUseCase,
            userSessionInterface,
            sharedPreference,
            tracker
        )
        with(viewModel) {
            campaign.observeForever(campaignObserver)
            submittedProduct.observeForever(submittedProductObserver)
            selectedProducts.observeForever(selectedProductObserver)
            productReserveResult.observeForever(productReserveObserver)
            productDeleteResult.observeForever(productDeleteObserver)
            submittedProductVariant.observeForever(submittedProductVariantObserver)
            flashSaleRegistrationResult.observeForever(flashSaleRegistrationObserver)
            error.observeForever(errorObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            campaign.removeObserver(campaignObserver)
            submittedProduct.removeObserver(submittedProductObserver)
            selectedProducts.removeObserver(selectedProductObserver)
            productReserveResult.removeObserver(productReserveObserver)
            productDeleteResult.removeObserver(productDeleteObserver)
            submittedProductVariant.removeObserver(submittedProductVariantObserver)
            flashSaleRegistrationResult.removeObserver(flashSaleRegistrationObserver)
            error.removeObserver(errorObserver)
        }
    }

    @Test
    fun `when fetch campaign detail data success, rbac rule is active and user is eligible, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyCampaignDetailData = DummyDataHelper.generateDummyCampaignDetailData()
                val sellerEligibility =
                    SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
                val expectedResult = Success(dummyCampaignDetailData)

                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData

                // When
                getCampaignDetail(dummyCampaignId)

                // Then
                val actualResult = campaign.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch campaign detail data success, rbac rule is active and user is not eligible, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyCampaignDetailData = DummyDataHelper.generateDummyCampaignDetailData()
                val sellerEligibility =
                    SellerEligibility(isDeviceAllowed = true, isUserAllowed = false)
                val expectedEvent = CampaignDetailViewModel.UiEffect.ShowIneligibleAccessWarning

                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData

                val emittedValue = arrayListOf<CampaignDetailViewModel.UiEffect>()
                val job = launch {
                    uiEffect.toList(emittedValue)
                }

                // When
                getCampaignDetail(dummyCampaignId)

                // Then
                val actualEvent = emittedValue.last()
                assertEquals(expectedEvent, actualEvent)

                job.cancel()
            }
        }
    }

    @Test
    fun `when fetch campaign detail data fail,  observer will return error`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyThrowable = MessageErrorException("Server Error")
                val expectedResult = Fail(dummyThrowable)

                coEvery { getFlashSaleSellerStatusUseCase.execute() } throws dummyThrowable
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } throws dummyThrowable

                // When
                getCampaignDetail(dummyCampaignId)

                // Then
                val actualResult = campaign.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when registration success, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyRegistrationResponse =
                    FlashSaleRegistrationResult(isSuccess = true, errorMessage = "")

                coEvery { doFlashSaleSellerRegistrationUseCase.execute(dummyCampaignId) } returns dummyRegistrationResponse

                // When
                register(dummyCampaignId)

                // Then
                val actualResult = flashSaleRegistrationResult.getOrAwaitValue()
                assertEquals(dummyRegistrationResponse, actualResult)
            }
        }
    }

    @Test
    fun `when registration fail, observer will return error`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyThrowable = MessageErrorException("Server Error")

                coEvery { doFlashSaleSellerRegistrationUseCase.execute(dummyCampaignId) } throws dummyThrowable

                // When
                register(dummyCampaignId)

                // Then
                val actualResult = error.getOrAwaitValue()
                assertEquals(dummyThrowable, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product success, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummySubmittedProductResponse = DummyDataHelper.generateDummySubmittedProductData()
                val dummyWaitingForSelectionProductData = DummyDataHelper.generateWaitingForSelectionProductData()
                val expectedResult = Success(dummyWaitingForSelectionProductData)

                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } returns dummySubmittedProductResponse

                // When
                getSubmittedProduct(dummyCampaignId)

                // Then
                val actualResult = submittedProduct.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product success and campaignStatus is WAITING_FOR_SELECTION, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyCampaignDetailData = DummyDataHelper.generateDummyCampaignDetailData(status = FlashSaleStatus.WAITING_FOR_SELECTION)
                val sellerEligibility =
                    SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
                getCampaignDetail(dummyCampaignId)

                val dummySubmittedProductResponse = DummyDataHelper.generateDummySubmittedProductData()
                val dummyWaitingForSelectionProductData = DummyDataHelper.generateWaitingForSelectionProductData()
                val expectedResult = Success(dummyWaitingForSelectionProductData)
                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } returns dummySubmittedProductResponse

                // When
                getSubmittedProduct(dummyCampaignId)

                // Then
                val actualResult = submittedProduct.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product success and campaignStatus is ON_SELECTION_PROCESS, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyCampaignDetailData = DummyDataHelper.generateDummyCampaignDetailData(status = FlashSaleStatus.ON_SELECTION_PROCESS)
                val sellerEligibility =
                    SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
                getCampaignDetail(dummyCampaignId)

                val dummySubmittedProductResponse = DummyDataHelper.generateDummySubmittedProductData()
                val dummyOnSelectionProcessItem = DummyDataHelper.generateOnSelectionProcessProductData()
                val expectedResult = Success(dummyOnSelectionProcessItem)
                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } returns dummySubmittedProductResponse

                // When
                getSubmittedProduct(dummyCampaignId)

                // Then
                val actualResult = submittedProduct.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product success and campaignStatus is FINISHED_PROCESS_SELECTION, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyCampaignDetailData = DummyDataHelper.generateDummyCampaignDetailData(status = FlashSaleStatus.SELECTION_FINISHED)
                val sellerEligibility =
                    SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
                getCampaignDetail(dummyCampaignId)

                val dummySubmittedProductResponse = DummyDataHelper.generateDummySubmittedProductData()
                val dummyFinishedProcessSelectionItem = DummyDataHelper.generateSelectionFinishedProductData()
                val expectedResult = Success(dummyFinishedProcessSelectionItem)
                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } returns dummySubmittedProductResponse

                // When
                getSubmittedProduct(dummyCampaignId)

                // Then
                val actualResult = submittedProduct.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product success and campaignStatus is ONGOING, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyCampaignDetailData = DummyDataHelper.generateDummyCampaignDetailData(status = FlashSaleStatus.ONGOING)
                val sellerEligibility =
                    SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
                getCampaignDetail(dummyCampaignId)

                val dummySubmittedProductResponse = DummyDataHelper.generateDummySubmittedProductData()
                val dummyOngoingItem = DummyDataHelper.generateOngoingAndFinishedProductData()
                val expectedResult = Success(dummyOngoingItem)
                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } returns dummySubmittedProductResponse

                // When
                getSubmittedProduct(dummyCampaignId)

                // Then
                val actualResult = submittedProduct.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product success and campaignStatus is REJECTED, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyCampaignDetailData = DummyDataHelper.generateDummyCampaignDetailData(status = FlashSaleStatus.REJECTED)
                val sellerEligibility =
                    SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
                getCampaignDetail(dummyCampaignId)

                val dummySubmittedProductResponse = DummyDataHelper.generateDummySubmittedProductData()
                val dummyRejectedItem = DummyDataHelper.generateOngoingAndRejectedProductData()
                val expectedResult = Success(dummyRejectedItem)
                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } returns dummySubmittedProductResponse

                // When
                getSubmittedProduct(dummyCampaignId)

                // Then
                val actualResult = submittedProduct.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product success and campaignStatus is FINISHED, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyCampaignDetailData = DummyDataHelper.generateDummyCampaignDetailData(status = FlashSaleStatus.FINISHED)
                val sellerEligibility =
                    SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)
                coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
                coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
                getCampaignDetail(dummyCampaignId)

                val dummySubmittedProductResponse = DummyDataHelper.generateDummySubmittedProductData()
                val dummyFinishedItem = DummyDataHelper.generateOngoingAndFinishedProductData()
                val expectedResult = Success(dummyFinishedItem)
                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } returns dummySubmittedProductResponse

                // When
                getSubmittedProduct(dummyCampaignId)

                // Then
                val actualResult = submittedProduct.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product fail, observer will return error`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyThrowable = MessageErrorException("Server Error")
                val expectedResult = Fail(dummyThrowable)
                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } throws dummyThrowable

                // When
                getSubmittedProduct(dummyCampaignId)

                // Then
                val actualResult = submittedProduct.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product variant success, observer will successfully receive the data`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummySubmittedProductVariantResponse =
                    DummyDataHelper.generateDummySubmittedProductData()
                val expectedResult = ProductCheckingResultMapper.map(
                    dummySubmittedProductVariantResponse.productList,
                    true,
                    "",
                    ""
                )

                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } returns dummySubmittedProductVariantResponse

                // When
                getSubmittedProductVariant(dummyCampaignId, 0, true, "")

                // Then
                val actualResult = submittedProductVariant.getOrAwaitValue()
                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch submitted product variant fail, observer will return error`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyThrowable = MessageErrorException("Server Error")

                coEvery { getFlashSaleSubmittedProductListUseCase.execute(dummyCampaignId) } throws dummyThrowable

                // When
                getSubmittedProductVariant(dummyCampaignId, 0, true, "")

                // Then
                val actualResult = error.getOrAwaitValue()
                assertEquals(dummyThrowable, actualResult)
            }
        }
    }

    @Test
    fun `when call reserveProduct success, observer will receive request status accordingly`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyProductReserveResultData = DummyDataHelper.generateDummyProductReserveResultData()

                coEvery { doFlashSaleProductReserveUseCase.execute(any()) } returns dummyProductReserveResultData

                // When
                reserveProduct(dummyCampaignId)

                // Then
                val actualResult = productReserveResult.getOrAwaitValue()
                assertEquals(dummyProductReserveResultData, actualResult.first)
            }
        }
    }

    @Test
    fun `when call reserveProduct fail, observer will return error`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyThrowable = MessageErrorException("Server Error")
                coEvery { doFlashSaleProductReserveUseCase.execute(any()) } throws dummyThrowable

                // When
                reserveProduct(dummyCampaignId)

                // Then
                val actualResult = error.getOrAwaitValue()
                assertEquals(dummyThrowable, actualResult)
            }
        }
    }

    @Test
    fun `when call deleteProduct success, observer will receive request status accordingly`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyDeleteProductResultData = ProductDeleteResult(true, "")
                coEvery { doFlashSaleProductDeleteUseCase.execute(any()) } returns dummyDeleteProductResultData

                // When
                deleteProduct(dummyCampaignId)

                // Then
                val actualResult = productDeleteResult.getOrAwaitValue()
                assertEquals(dummyDeleteProductResultData, actualResult)
            }
        }
    }

    @Test
    fun `when call deleteProduct fail, observer will return error`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyThrowable = MessageErrorException("Server Error")
                coEvery { doFlashSaleProductDeleteUseCase.execute(any()) } throws dummyThrowable

                // When
                deleteProduct(dummyCampaignId)

                // Then
                val actualResult = error.getOrAwaitValue()
                assertEquals(dummyThrowable, actualResult)
            }
        }
    }

    @Test
    fun `when call getBottomSheetData with type TIMELINE, will return data accordingly`() {
        with(viewModel) {
            // Given
            val dummyFlashSaleData = DummyDataHelper.generateDummyFlashSaleData()
            val bottomSheetType = DetailBottomSheetType.TIMELINE
            val expectedResult = DummyDataHelper.generateDummyTimelineBottomSheetModel()

            // When
            getBottomSheetData(bottomSheetType, dummyFlashSaleData)

            // Then
            val actualResult = getBottomSheetData(bottomSheetType, dummyFlashSaleData)
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call getBottomSheetData with type PRODUCT_CRITERIA, will return data accordingly`() {
        with(viewModel) {
            // Given
            val dummyFlashSaleData = DummyDataHelper.generateDummyFlashSaleData()
            val bottomSheetType = DetailBottomSheetType.PRODUCT_CRITERIA
            val expectedResult = DummyDataHelper.generateDummyProductCriteriaBottomSheetModel()

            // When
            getBottomSheetData(bottomSheetType, dummyFlashSaleData)

            // Then
            val actualResult = getBottomSheetData(bottomSheetType, dummyFlashSaleData)
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call getBottomSheetData with type GENERAL, will return data accordingly`() {
        with(viewModel) {
            // Given
            val dummyFlashSaleData = DummyDataHelper.generateDummyFlashSaleData()
            val bottomSheetType = DetailBottomSheetType.GENERAL
            val expectedResult = DummyDataHelper.generateDummyGeneralBottomSheetModel()

            // When
            getBottomSheetData(bottomSheetType, dummyFlashSaleData)

            // Then
            val actualResult = getBottomSheetData(bottomSheetType, dummyFlashSaleData)
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call isCampaignRegisterClosed, will return is campaign registration still open or not`() {
        with(viewModel) {
            // Given
            val dummyFlashSaleData = DummyDataHelper.generateDummyFlashSaleData()
            val expectedResult = true

            // When
            isCampaignRegisterClosed(dummyFlashSaleData)

            // Then
            val actualResult = isCampaignRegisterClosed(dummyFlashSaleData)
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call isFlashSalePeriodOnTheSameDate, will return is campaign held within one day or not`() {
        with(viewModel) {
            // Given
            val dummyFlashSaleData = DummyDataHelper.generateDummyFlashSaleData()
            val expectedResult = true

            // When
            isFlashSalePeriodOnTheSameDate(dummyFlashSaleData)

            // Then
            val actualResult = isCampaignRegisterClosed(dummyFlashSaleData)
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call setSelectedItem, will set the value accordingly`() {
        with(viewModel) {
            // Given
            val dummyFirstSelectedProducts = Pair(10L, 10L)
            val dummySecondSelectedProducts = Pair(20L, 20L)
            val expectedResult = listOf(dummyFirstSelectedProducts, dummySecondSelectedProducts)

            // When
            setSelectedItem(dummyFirstSelectedProducts)
            setSelectedItem(dummySecondSelectedProducts)

            // Then
            val actualResult = selectedProducts.getOrAwaitValue()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call removeSelectedItem, will remove the value accordingly`() {
        with(viewModel) {
            // Given
            val dummyFirstSelectedProducts = Pair(10L, 10L)
            val dummySecondSelectedProducts = Pair(20L, 20L)
            val expectedResult = listOf(dummyFirstSelectedProducts)
            setSelectedItem(dummyFirstSelectedProducts)
            setSelectedItem(dummySecondSelectedProducts)

            // When
            removeSelectedItem(dummySecondSelectedProducts)

            // Then
            val actualResult = selectedProducts.getOrAwaitValue()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call removeAllSelectedItems, will remove clear selectedProductList`() {
        with(viewModel) {
            // Given
            val dummyFirstSelectedProducts = Pair(10L, 10L)
            val dummySecondSelectedProducts = Pair(20L, 20L)
            val expectedResult = emptyList<Pair<Long, Long>>()
            setSelectedItem(dummyFirstSelectedProducts)
            setSelectedItem(dummySecondSelectedProducts)

            // When
            removeAllSelectedItems()

            // Then
            val actualResult = selectedProducts.getOrAwaitValue()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call getCampaignStatus, will return campaign status accordingly`() {
        with(viewModel) {
            // Given
            val dummyCampaignStatus = FlashSaleStatus.NO_REGISTERED_PRODUCT

            // When
            getCampaignStatus()

            // Then
            val actualResult = getCampaignStatus()
            assertEquals(dummyCampaignStatus, actualResult)
        }
    }

    @Test
    fun `when call getTabName with UPCOMING status, will return tab name accordingly`() {
        with(viewModel) {
            // Given
            val dummyTabName = FlashSaleListPageTab.UPCOMING
            val dummyCampaignDetailData =
                DummyDataHelper.generateDummyCampaignDetailData(dummyTabName)
            val sellerEligibility =
                SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)

            coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
            coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
            getCampaignDetail(dummyCampaignId)
            val expectedResult = "upcoming"

            // When
            getTabName()

            // Then
            val actualResult = getTabName()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call getTabName with REGISTERED status, will return tab name accordingly`() {
        with(viewModel) {
            // Given
            val dummyTabName = FlashSaleListPageTab.REGISTERED
            val dummyCampaignDetailData =
                DummyDataHelper.generateDummyCampaignDetailData(dummyTabName)
            val sellerEligibility =
                SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)

            coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
            coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
            getCampaignDetail(dummyCampaignId)
            val expectedResult = "registered"

            // When
            getTabName()

            // Then
            val actualResult = getTabName()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call getTabName with ONGOING status, will return tab name accordingly`() {
        with(viewModel) {
            // Given
            val dummyTabName = FlashSaleListPageTab.ONGOING
            val dummyCampaignDetailData =
                DummyDataHelper.generateDummyCampaignDetailData(dummyTabName)
            val sellerEligibility =
                SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)

            coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
            coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
            getCampaignDetail(dummyCampaignId)
            val expectedResult = "ongoing"

            // When
            getTabName()

            // Then
            val actualResult = getTabName()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call getTabName with FINISHED status, will return tab name accordingly`() {
        with(viewModel) {
            // Given
            val dummyTabName = FlashSaleListPageTab.FINISHED
            val dummyCampaignDetailData =
                DummyDataHelper.generateDummyCampaignDetailData(dummyTabName)
            val sellerEligibility =
                SellerEligibility(isDeviceAllowed = true, isUserAllowed = true)

            coEvery { getFlashSaleSellerStatusUseCase.execute() } returns sellerEligibility
            coEvery { getFlashSaleDetailForSellerUseCase.execute(dummyCampaignId) } returns dummyCampaignDetailData
            getCampaignDetail(dummyCampaignId)
            val expectedResult = "finished"

            // When
            getTabName()

            // Then
            val actualResult = getTabName()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call getAddProductButtonVisibility, will return value based on campaign status accordingly`() {
        with(viewModel) {
            // Given
            val expectedResult = true

            // When
            getAddProductButtonVisibility()

            // Then
            val actualResult = getAddProductButtonVisibility()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when call isOnCheckBoxState, will return check box state value accordingly`() {
        with(viewModel) {
            // Given
            val dummyCheckBoxValue = false

            // When
            isOnCheckBoxState()

            // Then
            val actualResult = isOnCheckBoxState()
            assertEquals(dummyCheckBoxValue, actualResult)
        }
    }

    @Test
    fun `when call setCheckBoxStateStatus, will set check box state value accordingly`() {
        with(viewModel) {
            // Given
            val dummyCheckBoxValue = true

            // When
            setCheckBoxStateStatus(dummyCheckBoxValue)

            // Then
            val actualResult = isOnCheckBoxState()
            assertEquals(dummyCheckBoxValue, actualResult)
        }
    }

    @Test
    fun `when call isTriggeredFromDelete, will return delete state value accordingly`() {
        with(viewModel) {
            // Given
            val dummyDeleteStateValue = false

            // When
            isTriggeredFromDelete()

            // Then
            val actualResult = isTriggeredFromDelete()
            assertEquals(dummyDeleteStateValue, actualResult)
        }
    }

    @Test
    fun `when call setDeleteStateStatus, will set delete state value accordingly`() {
        with(viewModel) {
            // Given
            val dummyDeleteStateValue = false

            // When
            setDeleteStateStatus(dummyDeleteStateValue)

            // Then
            val actualResult = isTriggeredFromDelete()
            assertEquals(dummyDeleteStateValue, actualResult)
        }
    }
}
