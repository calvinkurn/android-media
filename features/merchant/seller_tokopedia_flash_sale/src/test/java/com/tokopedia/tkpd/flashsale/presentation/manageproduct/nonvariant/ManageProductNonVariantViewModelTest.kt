package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductCriteriaCheckingUseCase
import io.mockk.impl.annotations.RelaxedMockK
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.SINGLE_LOCATION
import com.tokopedia.tkpd.flashsale.util.tracker.ManageProductNonVariantTracker
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNull

class ManageProductNonVariantViewModelTest {

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase

    @RelaxedMockK
    lateinit var productObserver: Observer<in Product>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ManageProductNonVariantViewModel

    @RelaxedMockK
    lateinit var doTrackingNominalObserver: Observer<in String>

    @RelaxedMockK
    lateinit var doTrackingPercentObserver: Observer<in String>

    @RelaxedMockK
    lateinit var tracker: ManageProductNonVariantTracker

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = ManageProductNonVariantViewModel(
            CoroutineTestDispatchersProvider,
            ErrorMessageHelper(context),
            getFlashSaleProductCriteriaCheckingUseCase,
            tracker
        )

        with(viewModel) {
            product.observeForever(productObserver)
            doTrackingPercent.observeForever(doTrackingPercentObserver)
            doTrackingNominal.observeForever(doTrackingNominalObserver)
        }
    }

    @After
    fun tearDown() {
        with(viewModel) {
            product.removeObserver(productObserver)
            doTrackingPercent.removeObserver(doTrackingPercentObserver)
            doTrackingNominal.removeObserver(doTrackingNominalObserver)
        }

        unmockkAll()
    }

    @Test
    fun `When getting products success, then products are set`() {
        val expected = ProductGenerator.createNonVariantProduct(isMultiLocation = false)

        viewModel.setProduct(expected)

        val actual = viewModel.product.value
        assertEquals(expected, actual)
    }

    @Test
    fun `When every validation failed (max)`() {
        val criteria = ProductGenerator.generateCriteria(
            minCustomStock = 1,
            maxCustomStock = 10,
            minDiscount = 5,
            maxDiscount = 50,
            minFinalPrice = 1000,
            maxFinalPrice = 100000,
        )
        val discount = ProductGenerator.generateDiscountSetup(
            discount = 100,
            price = 1000000,
            stock = 100,
        )
        val expected = ProductGenerator.generateValidationResult(
            isPriceError = true,
            isPricePercentError = true,
            isStockError = true,
            priceMessage = ErrorMessageHelper(context).getPriceMessage(
                criteria = criteria,
                discountSetup = discount
            ),
            pricePercentMessage = ErrorMessageHelper(context).getDiscountMessage(
                criteria = criteria,
                discountSetup = discount
            )
        )

        val actual = viewModel.validateInput(criteria = criteria, discountSetup = discount)
        assertEquals(expected, actual)
        assert(!actual.isAllFieldValid())
        assert(actual.priceMessage == expected.priceMessage)
        assert(actual.pricePercentMessage == expected.pricePercentMessage)
        assert(actual.isPriceError)
        assert(actual.isPricePercentError)
        assert(actual.isStockError)
    }

    @Test
    fun `When every validation failed (min)`() {
        val criteria = ProductGenerator.generateCriteria(
            minCustomStock = 10,
            maxCustomStock = 100,
            minDiscount = 25,
            maxDiscount = 50,
            minFinalPrice = 1000,
            maxFinalPrice = 100000,
        )
        val discount = ProductGenerator.generateDiscountSetup(
            discount = 10,
            price = 250,
            stock = 5,
        )
        val expected = ProductGenerator.generateValidationResult(
            isPriceError = true,
            isPricePercentError = true,
            isStockError = true,
            priceMessage = ErrorMessageHelper(context).getPriceMessage(
                criteria = criteria,
                discountSetup = discount
            ),
            pricePercentMessage = ErrorMessageHelper(context).getDiscountMessage(
                criteria = criteria,
                discountSetup = discount
            )
        )

        val actual = viewModel.validateInput(criteria = criteria, discountSetup = discount)
        assertEquals(expected, actual)
        assert(!actual.isAllFieldValid())
        assert(actual.priceMessage == expected.priceMessage)
        assert(actual.pricePercentMessage == expected.pricePercentMessage)
        assert(actual.isPriceError)
        assert(actual.isPricePercentError)
        assert(actual.isStockError)
    }

    @Test
    fun `When every validation passed`() {
        val criteria = ProductGenerator.generateCriteria(
            minCustomStock = 1,
            maxCustomStock = 10,
            minDiscount = 5,
            maxDiscount = 50,
            minFinalPrice = 1000,
            maxFinalPrice = 100000,
        )
        val discount = ProductGenerator.generateDiscountSetup(
            discount = 10,
            price = 10000,
            stock = 5,
        )
        val expected = ProductGenerator.generateValidationResult(
            isPriceError = false,
            isPricePercentError = false,
            isStockError = false,
            priceMessage = ErrorMessageHelper(context).getPriceMessage(
                criteria = criteria,
                discountSetup = discount
            ),
            pricePercentMessage = ErrorMessageHelper(context).getDiscountMessage(
                criteria = criteria,
                discountSetup = discount
            )
        )

        val actual = viewModel.validateInput(criteria = criteria, discountSetup = discount)
        assertEquals(expected, actual)
        assert(actual.isAllFieldValid())
        assert(actual.priceMessage == expected.priceMessage)
        assert(actual.pricePercentMessage == expected.pricePercentMessage)
        assert(!actual.isPriceError)
        assert(!actual.isPricePercentError)
        assert(!actual.isStockError)
    }

    @Test
    fun `When calculation of percent to nominal 50 percent`() {
        val price = 5000L
        val percent = 50L
        val expected = "2500"
        val actual = viewModel.calculatePrice(percentInput = percent, originalPrice = price)
        assertEquals(expected, actual)
    }

    @Test
    fun `When calculation of percent to nominal 29 percent`() {
        val price = 5000L
        val percent = 29L
        val expected = "3550"
        val actual = viewModel.calculatePrice(percentInput = percent, originalPrice = price)
        assertEquals(expected, actual)
    }

    @Test
    fun `When calculation of nominal to percent 29 percent`() {
        val price = 5000L
        val discount = 3550L
        val expected = "29"
        val actual = viewModel.calculatePercent(priceInput = discount, originalPrice = price)
        assertEquals(expected, actual)
    }

    @Test
    fun `When validateInput return all good`() {
        val price = 5000L
        val discount = 3750L
        val expected = "25"
        val actual = viewModel.calculatePercent(priceInput = discount, originalPrice = price)
        assertEquals(expected, actual)
    }

    @Test
    fun `When fetching criteria result success`() {
        runBlocking {
            with(viewModel) {
                val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true)
                val campaignId = 1001L
                val expected = listOf(ProductGenerator.generateLocationCriteria())

                coEvery {
                    getFlashSaleProductCriteriaCheckingUseCase.execute(
                        campaignId = campaignId,
                        productId = product.productId,
                        productCriteriaId = product.productCriteria.criteriaId
                    )
                } returns expected

                checkCriteria(product = product, campaignId = campaignId)

                val actual = getCriteria()
                assertEquals(expected[0], actual)
            }
        }
    }

    @Test
    fun `When fetching criteria result success but null`() {
        runBlocking {
            with(viewModel) {
                val product = ProductGenerator.createNonVariantProduct(name = "Jet", isMultiLocation = true)
                val campaignId = 1001L
                val expected = listOf(ProductGenerator.generateLocationCriteria())

                coEvery {
                    getFlashSaleProductCriteriaCheckingUseCase.execute(
                        campaignId = campaignId,
                        productId = product.productId,
                        productCriteriaId = product.productCriteria.criteriaId
                    )
                } returns expected

                checkCriteria(product = product, campaignId = campaignId)

                val actual = getCriteria()
                assertNull(actual)
            }
        }
    }

    @Test
    fun `When fetching criteria result failed`() {
        runBlocking {
            with(viewModel) {
                val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true)
                val campaignId = 1001L
                val expected = MessageErrorException("Error")

                coEvery {
                    getFlashSaleProductCriteriaCheckingUseCase.execute(
                        campaignId = campaignId,
                        productId = product.productId,
                        productCriteriaId = product.productCriteria.criteriaId
                    )
                } throws expected

                checkCriteria(product = product, campaignId = campaignId)

                val actual = getCriteria()
                val error = error.value
                assertNull(actual)
                assertEquals(expected, error)
            }
        }
    }

    @Test
    fun `When Nominal Discount Value Change Tracker Will Send Event`() {
        runBlocking {
            with(viewModel) {
                val campaignId = "1001"
                val product = ProductGenerator.createNonVariantProduct(isMultiLocation = false)
                val expected = "Rp10.000"
                onNominalDiscountTrackerInput(expected)
                val actual = doTrackingNominal.getOrAwaitValue(time = 5)
                onDiscountPriceEdited(
                    campaignId = campaignId,
                    productId = "${product.productId}",
                    locationType = SINGLE_LOCATION
                )
                coVerify {
                    tracker.sendClickFillInCampaignPriceEvent(
                        campaignId = campaignId,
                        productId = "${product.productId}",
                        locationType = SINGLE_LOCATION
                    )
                }
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When Percent Discount Value Change Tracker Will Send Event`() {
        runBlocking {
            with(viewModel) {
                val campaignId = "1001"
                val product = ProductGenerator.createNonVariantProduct(isMultiLocation = false)
                val expected = "10%"
                onPercentDiscountTrackerInput(expected)
                val actual = doTrackingPercent.getOrAwaitValue(time = 5)
                onDiscountPercentEdited(
                    campaignId = campaignId,
                    productId = "${product.productId}",
                    locationType = SINGLE_LOCATION
                )
                coVerify {
                    tracker.sendClickFillInCampaignDiscountPercentageEvent(
                        campaignId = campaignId,
                        productId = "${product.productId}",
                        locationType = SINGLE_LOCATION
                    )
                }
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun `When save button is clicked tracker will send the click event`() {
        runBlocking {
            with(viewModel) {
                val campaignId = "1001"
                val product = ProductGenerator.createNonVariantProduct(isMultiLocation = false)
                onSaveButtonClicked(
                    campaignId = campaignId,
                    productId = "${product.productId}",
                    locationType = SINGLE_LOCATION
                )
                coVerify {
                    tracker.sendClickSaveEvent(
                        campaignId = campaignId,
                        productId = "${product.productId}",
                        locationType = SINGLE_LOCATION
                    )
                }
            }
        }
    }

    @Test
    fun `When edit switch is toggled tracker will send the toggle event`() {
        runBlocking {
            with(viewModel) {
                val campaignId = "1001"
                val product = ProductGenerator.createNonVariantProduct(isMultiLocation = false)
                onEditSwitchToggled(
                    campaignId = campaignId,
                    productId = "${product.productId}",
                    locationType = SINGLE_LOCATION,
                    warehouseId = "${product.warehouses[0].warehouseId}"
                )
                coVerify {
                    tracker.sendClickAdjustToggleLocationEvent(
                        campaignId = campaignId,
                        productId = "${product.productId}",
                        locationType = SINGLE_LOCATION,
                        warehouseId = "${product.warehouses[0].warehouseId}"
                    )
                }
            }
        }
    }

    @Test
    fun `When check detail button is clicked tracker will send the click event`() {
        runBlocking {
            with(viewModel) {
                val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true)
                val campaignId = 1001L
                val expected = listOf(ProductGenerator.generateLocationCriteria())

                coEvery {
                    getFlashSaleProductCriteriaCheckingUseCase.execute(
                        campaignId = campaignId,
                        productId = product.productId,
                        productCriteriaId = product.productCriteria.criteriaId
                    )
                } returns expected

                checkCriteria(product = product, campaignId = campaignId)

                val actual = getCriteria()
                assertEquals(expected[0], actual)

                onCheckDetailButtonClicked(
                    campaignId = campaignId.toString(),
                    productId = "${product.productId}",
                    locationType = SINGLE_LOCATION,
                    warehouseId = "${product.warehouses[0].warehouseId}"
                )
                coVerify {
                    tracker.sendClickDetailCheckPartialIneligibleLocationEvent(
                        campaignId = campaignId.toString(),
                        productId = "${product.productId}",
                        locationType = SINGLE_LOCATION,
                        warehouseId = "${product.warehouses[0].warehouseId}"
                    )
                }
            }
        }
    }

    @Test
    fun `When every product is toggled on then bulk apply caption will be shown`() {
        with(viewModel) {
            val expected = "Atur Sekaligus 5 Lokasi"
            val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true)

            viewModel.setProduct(product)

            every {
                context.getString(R.string.manageproductnonvar_bulk_apply_text, any())
            } returns expected

            val actual = bulkApplyCaption.getOrAwaitValue(500)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `When bulk apply is enabled`() {
        with(viewModel) {
            val expected = true
            val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true)

            viewModel.setProduct(product)

            val actual = enableBulkApply.getOrAwaitValue()
            assertEquals(expected, actual)
            assertTrue(actual)
        }
    }

    @Test
    fun `When bulk apply is disabled`() {
        with(viewModel) {
            val expected = false
            val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true, isPartialEligible = true)

            viewModel.setProduct(product)

            val actual = enableBulkApply.getOrAwaitValue()
            assertEquals(expected, actual)
            assertFalse(actual)
        }
    }

    @Test
    fun `When non variant product is multi location`() {
        with(viewModel) {
            val expected = true
            val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true)

            viewModel.setProduct(product)

            val actual = isMultiloc.getOrAwaitValue()
            assertEquals(expected, actual)
            assertTrue(product.isMultiWarehouse)
        }
    }

    @Test
    fun `When non variant product is single location`() {
        with(viewModel) {
            val expected = false
            val product = ProductGenerator.createNonVariantProduct(isMultiLocation = false)

            viewModel.setProduct(product)

            val actual = isMultiloc.getOrAwaitValue()
            assertEquals(expected, actual)
            assertFalse(product.isMultiWarehouse)
        }
    }

    @Test
    fun `When page discount and stock input are valid`() {
        with(viewModel) {
            val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true, isInputValid = true)

            val expected = true

            viewModel.setProduct(product)

            val actual = isInputPageValid.getOrAwaitValue()
            assertEquals(expected, actual)
            assertTrue(actual)
        }
    }

    @Test
    fun `When page discount and stock input are invalid`() {
        with(viewModel) {
            val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true, isInputValid = false)

            val expected = false

            viewModel.setProduct(product)

            val actual = isInputPageValid.getOrAwaitValue()
            assertEquals(expected, actual)
            assertFalse(actual)
        }
    }

    @Test
    fun `When page discount and stock input are valid and some locations are partially eligible`() {
        with(viewModel) {
            val product = ProductGenerator.createNonVariantProduct(isMultiLocation = true, isInputValid = true, isPartialEligible = true)

            val expected = true

            viewModel.setProduct(product)

            val actual = isInputPageValid.getOrAwaitValue()
            assertEquals(expected, actual)
            assertTrue(actual)
        }
    }

}
