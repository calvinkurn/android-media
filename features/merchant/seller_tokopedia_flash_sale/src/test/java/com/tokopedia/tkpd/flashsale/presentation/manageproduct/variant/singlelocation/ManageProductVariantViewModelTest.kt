package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductCriteriaCheckingUseCase
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant
import com.tokopedia.tkpd.flashsale.util.tracker.ManageProductVariantTracker
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ManageProductVariantViewModelTest {

    @RelaxedMockK
    lateinit var getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase

    @RelaxedMockK
    lateinit var criteriaCheckingResultObserver: Observer<in List<CriteriaCheckingResult>>

    @RelaxedMockK
    lateinit var inputValidationObserver: Observer<in Boolean>

    @RelaxedMockK
    lateinit var errorMessageHelper: ErrorMessageHelper

    @RelaxedMockK
    lateinit var errorObserver: Observer<in Throwable>

    @RelaxedMockK
    lateinit var tracker: ManageProductVariantTracker

    private val dummyCampaignId = "dummy"

    private val dummyProductId = 10L

    private lateinit var viewModel: ManageProductVariantViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ManageProductVariantViewModel(
            CoroutineTestDispatchersProvider,
            errorMessageHelper,
            getFlashSaleProductCriteriaCheckingUseCase,
            tracker
        )
        with(viewModel) {
            criteriaCheckingResult.observeForever(criteriaCheckingResultObserver)
            isInputPageValid.observeForever(inputValidationObserver)
            error.observeForever(errorObserver)
        }
    }

    @After
    fun after() {
        unmockkAll()
        with(viewModel) {
            criteriaCheckingResult.removeObserver(criteriaCheckingResultObserver)
            isInputPageValid.removeObserver(inputValidationObserver)
            error.removeObserver(errorObserver)
        }
    }

    @Test
    fun `when product data initiated, will set the data accordingly`() {
        with(viewModel) {
            // Given
            val dummyProductData = ReservedProduct.Product()

            // When
            setupInitiateProductData(dummyProductData)

            // Then
            val actualResult = getProductData()
            assertEquals(dummyProductData, actualResult)
        }
    }

    @Test
    fun `set item toggle value, will set the data accordingly`() {
        with(viewModel) {
            // Given
            val dummyProductData = DummyDataHelper.generateDummyProductData()
            val position = 0
            val value = true
            setupInitiateProductData(dummyProductData)
            val expectedResult = true

            // When
            setItemToggleValue(position, value)

            // Then
            val actualResult = getProductData().childProducts[position].isToggleOn
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `set item discount value, will set the data accordingly`() {
        with(viewModel) {
            // Given
            val dummyProductData = DummyDataHelper.generateDummyProductData()
            val position = 0
            val priceValue = 11L
            val discountValue = 22
            setupInitiateProductData(dummyProductData)

            // When
            setDiscountValue(position, priceValue, discountValue)

            // Then
            val actualPriceResult =
                getProductData().childProducts[position].warehouses[position].discountSetup.price
            val actualDiscountResult =
                getProductData().childProducts[position].warehouses[position].discountSetup.discount
            assertEquals(priceValue, actualPriceResult)
            assertEquals(discountValue, actualDiscountResult)
        }
    }

    @Test
    fun `set item stock value, will set the data accordingly`() {
        with(viewModel) {
            // Given
            val dummyProductData = DummyDataHelper.generateDummyProductData()
            val position = 0
            val stockValue = 100L
            setupInitiateProductData(dummyProductData)

            // When
            setStockValue(position, stockValue)

            // Then
            val actualResult =
                getProductData().childProducts[position].warehouses[position].discountSetup.stock
            assertEquals(stockValue, actualResult)
        }
    }

    @Test
    fun `calculatePrice, will return value of a string accordingly`() {
        with(viewModel) {
            // Given
            val dummyPercentInput = 10L // equal to 10%
            val dummyPrice = 1000L // equal to Rp.1000
            val expectedResult = "900"

            // When
            val actualResult = calculatePrice(dummyPercentInput, dummyPrice)

            // Then
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `calculatePercent, will return value of a string accordingly`() {
        with(viewModel) {
            // Given
            val dummyPriceInput = 800L // equal to Rp.800
            val dummyOriginalPrice = 1000L // equal to Rp.1000
            val expectedResult = "20"

            // When
            val actualResult = calculatePercent(dummyPriceInput, dummyOriginalPrice)

            // Then
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when sendManageAllClickEvent called, will hit tracker accordingly`() {
        with(viewModel) {
            // Given
            val dummyTrackerLabel = "$dummyCampaignId - ${TrackerConstant.SINGLE_LOCATION}"

            // When
            sendManageAllClickEvent(dummyCampaignId)

            // Then
            coVerify { tracker.sendClickManageAllEvent(dummyTrackerLabel) }
        }
    }

    @Test
    fun `when sendAdjustToggleVariantEvent called, will hit tracker accordingly`() {
        with(viewModel) {
            // Given
            val dummyTrackerLabel = "$dummyCampaignId - ${TrackerConstant.SINGLE_LOCATION}"

            // When
            sendAdjustToggleVariantEvent(dummyCampaignId)

            // Then
            coVerify { tracker.sendAdjustToggleVariantEvent(dummyTrackerLabel) }
        }
    }

    @Test
    fun `when sendFillInColumnPriceEvent called, will hit tracker accordingly`() {
        with(viewModel) {
            // Given
            val dummyTrackerLabel = "$dummyCampaignId - ${TrackerConstant.SINGLE_LOCATION}"

            // When
            sendFillInColumnPriceEvent(dummyCampaignId)

            // Then
            coVerify { tracker.sendClickFillInCampaignPriceEvent(dummyTrackerLabel) }
        }
    }

    @Test
    fun `when sendFillInDiscountPercentageEvent called, will hit tracker accordingly`() {
        with(viewModel) {
            // Given
            val dummyTrackerLabel = "$dummyCampaignId - ${TrackerConstant.SINGLE_LOCATION}"

            // When
            sendFillInDiscountPercentageEvent(dummyCampaignId)

            // Then
            coVerify { tracker.sendClickFillInDiscountPercentageEvent(dummyTrackerLabel) }
        }
    }

    @Test
    fun `when sendSaveClickEvent called, will hit tracker accordingly`() {
        with(viewModel) {
            // Given
            val dummyTrackerLabel = "$dummyCampaignId - ${TrackerConstant.SINGLE_LOCATION}"

            // When
            sendSaveClickEvent(dummyCampaignId)

            // Then
            coVerify { tracker.sendClickSaveEvent(dummyTrackerLabel) }
        }
    }

    @Test
    fun `when sendCheckDetailClickEvent called, will hit tracker accordingly`() {
        with(viewModel) {
            // Given
            val dummyTrackerLabel =
                "$dummyCampaignId - $dummyProductId - ${TrackerConstant.SINGLE_LOCATION}"

            // When
            sendCheckDetailClickEvent(dummyCampaignId, dummyProductId)

            // Then
            coVerify { tracker.sendClickCheckDetailEvent(dummyTrackerLabel) }
        }
    }

    @Test
    fun `when sendManageAllLocationClickEvent called, will hit tracker accordingly`() {
        with(viewModel) {
            // Given
            val dummyTrackerLabel =
                "$dummyCampaignId - $dummyProductId - ${TrackerConstant.MULTI_LOCATION}"

            // When
            sendManageAllLocationClickEvent(dummyCampaignId, dummyProductId)

            // Then
            coVerify { tracker.sendClickManageAllLocationEvent(dummyTrackerLabel) }
        }
    }

    @Test
    fun `when call checkCriteria, observer will successfully receive the value`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyItem = DummyDataHelper.generateDummyChildProduct()
                val dummyCriteria = DummyDataHelper.generateDummyCriteria()
                val dummyCriteriaResult = DummyDataHelper.generateDummyCriteriaResult()
                setCampaignId("0")

                coEvery {
                    getFlashSaleProductCriteriaCheckingUseCase.execute(
                        0,
                        0,
                        0
                    )
                } returns dummyCriteriaResult

                // When
                checkCriteria(dummyItem, dummyCriteria)

                // Then
                val actualResult = criteriaCheckingResult.getOrAwaitValue()
                assertEquals(dummyCriteriaResult, actualResult)
            }
        }
    }

    @Test
    fun `when fetch campaign detail data fail,  observer will return error`() {
        runBlocking {
            with(viewModel) {
                // Given
                val dummyItem = DummyDataHelper.generateDummyChildProduct()
                val dummyCriteria = DummyDataHelper.generateDummyCriteria()
                val dummyThrowable = MessageErrorException("Server Error")

                coEvery {
                    getFlashSaleProductCriteriaCheckingUseCase.execute(
                        0,
                        0,
                        0
                    )
                } throws dummyThrowable

                // When
                checkCriteria(dummyItem, dummyCriteria)

                // Then
                val actualResult = error.getOrAwaitValue()
                assertEquals(dummyThrowable, actualResult)
            }
        }
    }

    @Test
    fun `when validateInputPage is called, will validate the product data based on given criteria`() {
        with(viewModel) {
            // Given
            val dummyProductData =
                ReservedProduct.Product(childProducts = listOf(DummyDataHelper.generateDummyChildProductToggledOn()))
            val dummyProductCriteria = DummyDataHelper.generateDummyCriteria()
            val expectedResult = true
            setupInitiateProductData(dummyProductData)

            // When
            validateInputPage(dummyProductCriteria)

            // Then
            val actualResult = isInputPageValid.getOrAwaitValue()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when validateInputPage is called and no product is enabled, will return false`() {
        with(viewModel) {
            // Given
            val dummyProductData =
                ReservedProduct.Product(childProducts = listOf(DummyDataHelper.generateDummyChildProduct()))
            val dummyProductCriteria = DummyDataHelper.generateDummyCriteria()
            val expectedResult = false
            setupInitiateProductData(dummyProductData)

            // When
            validateInputPage(dummyProductCriteria)

            // Then
            val actualResult = isInputPageValid.getOrAwaitValue()
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when validateInputPage is called and no warehouse is enabled, will return false`() {
        with(viewModel) {
            // Given
            val dummyProductData =
                ReservedProduct.Product(childProducts = listOf(DummyDataHelper.generateDummyChildProductToggledOn(false)))
            val dummyProductCriteria = DummyDataHelper.generateDummyCriteria()
            val expectedResult = false
            setupInitiateProductData(dummyProductData)

            // When
            validateInputPage(dummyProductCriteria)

            // Then
            val actualResult = isInputPageValid.getOrAwaitValue()
            assertEquals(expectedResult, actualResult)
        }
    }
}
