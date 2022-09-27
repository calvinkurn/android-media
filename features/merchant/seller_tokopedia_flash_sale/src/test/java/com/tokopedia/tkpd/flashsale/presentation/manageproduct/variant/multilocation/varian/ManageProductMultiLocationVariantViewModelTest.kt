package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.DataDummyOfVariantMultiLocationType.createDummyOfDiscountSetup
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.DataDummyOfVariantMultiLocationType.createDummyOfProductCriteria
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ManageProductMultiLocationVariantViewModelTest {

    companion object {
        const val MINIMUM_PRICE_MESSAGE = "Rp2.000"
        const val MAX_PRICE_MESSAGE = "Rp10.000"
        const val BETWEEN_PRICE_MESSAGE = "Rp2.000-Rp10.000"
    }

    @RelaxedMockK
    lateinit var context: Context

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var viewModel: ManageProductMultiLocationVariantViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ManageProductMultiLocationVariantViewModel(
            coroutineTestRule.dispatchers,
            ErrorMessageHelper(context)
        )
    }

    @Test
    fun `set Product and variant into viewModel`() {
        val dummyData = DataDummyOfVariantMultiLocationType.createDummyProduct()
        val target = DataDummyOfVariantMultiLocationType.createDummyChildsProduct()
        val positionTarget = 0
        viewModel.setProduct(dummyData, positionTarget)
        val dataProductInViewModel = viewModel.product.value
        Assert.assertEquals(dummyData, dataProductInViewModel)
        Assert.assertEquals(
            dataProductInViewModel?.childProducts.orEmpty()[positionTarget],
            target[positionTarget]
        )
    }

    @Test
    fun `test calculation of percent to nominal 50 percent`() {
        val actualOutput = viewModel.calculatePrice(50, 5000)
        Assert.assertEquals("2500", actualOutput)
    }

    @Test
    fun `test calculation of percent to nominal 29 percent`() {
        val actualOutput = viewModel.calculatePrice(29, 5000)
        Assert.assertEquals("3550", actualOutput)
    }

    @Test
    fun `test calculation of nominal to percent 29 percent`() {
        val actualOutput = viewModel.calculatePercent(3550, 5000)
        Assert.assertEquals("29", actualOutput)
    }

    @Test
    fun `test validateInput return all good`() {
        val actualOutput = viewModel.calculatePercent(3550, 5000)
        Assert.assertEquals("29", actualOutput)
    }

    @Test
    fun `test validateInput when all validate`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup()
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertTrue(validateActualResult.isAllFieldValid())
    }

    @Test
    fun `test validateInput when all is invalid`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 80
            price = 11000
            stock = 1
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isAllFieldValid())
    }

    @Test
    fun `test validateInput when discount is invalid maximum`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 80
            price = 4000
            stock = 11
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isAllFieldValid())
        Assert.assertTrue(validateActualResult.isPricePercentError)
    }

    @Test
    fun `test validateInput when discount is invalid minimum`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 1
            price = 4000
            stock = 11
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isAllFieldValid())
        Assert.assertTrue(validateActualResult.isPricePercentError)
    }

    @Test
    fun `test validateInput when discount is valid`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 40
            price = 4000
            stock = 11
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isPricePercentError)
    }

    @Test
    fun `test validateInput when stock is invalid minimum`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 40
            price = 4000
            stock = 1
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isAllFieldValid())
        Assert.assertTrue(validateActualResult.isStockError)
    }

    @Test
    fun `test validateInput when stock is invalid maximum`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 40
            price = 4000
            stock = 101
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isAllFieldValid())
        Assert.assertTrue(validateActualResult.isStockError)
    }

    @Test
    fun `test validateInput when stock is valid`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 40
            price = 4000
            stock = 40
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isStockError)
    }

    @Test
    fun `test validateInput when price is invalid`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 40
            price = 40000
            stock = 11
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isAllFieldValid())
        Assert.assertTrue(validateActualResult.isPriceError)
    }

    @Test
    fun `test validateInput when price invalid in under minimum criteria`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        declareDummyReturnTextStringResource()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 40
            price = 200
            stock = 11
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isAllFieldValid())
        Assert.assertTrue(validateActualResult.isPriceError)
        Assert.assertEquals("Min. Rp2.000", validateActualResult.priceMessage)
    }

    @Test
    fun `test validateInput when price invalid in max criteria`() {
        val dummyOfCriteria = createDummyOfProductCriteria()
        declareDummyReturnTextStringResource()
        val dummyOfDiscountSetup = createDummyOfDiscountSetup().apply {
            discount = 40
            price = 20000
            stock = 11
        }
        val validateActualResult = viewModel.validateInput(dummyOfCriteria, dummyOfDiscountSetup)
        Assert.assertFalse(validateActualResult.isAllFieldValid())
        Assert.assertTrue(validateActualResult.isPriceError)
        Assert.assertEquals("Max. Rp10.000", validateActualResult.priceMessage)
    }

    @Test
    fun `enableBulkApply is return false`() {
        setValueOfProductAndVariantAttribute()
        val actualResult = viewModel.enableBulkApply.getOrAwaitValue(500)
        Assert.assertFalse(actualResult)
    }

    @Test
    fun `enableBulkApply is return true`() {
        runBlockingTest {
            setValueOfProductAndVariantAttribute(true)
            val actualResult = viewModel.enableBulkApply.getOrAwaitValue(500)
            Assert.assertTrue(actualResult)
        }
    }

    @Test
    fun `bulkApplyCaption when all product toggle is on `() {
        val target = "Atur Sekaligus 5 Lokasi"
        setValueOfProductAndVariantAttribute(true)

        every {
            context.getString(
                R.string.manageproductnonvar_bulk_apply_text,
                any()
            )
        } returns target

        val actualResult = viewModel.bulkApplyCaption.getOrAwaitValue(500)
        Assert.assertEquals(target, actualResult)
    }

    @Test
    fun `isInputPageValid test when toggle is on and all valid`() {
        val dummyProduct = setDummyChild(0, 2, 2000, 30, 50)
        val targetPositionOfProduct = 0
        viewModel.setProduct(dummyProduct, targetPositionOfProduct)
        val actualResult = viewModel.isInputPageValid.getOrAwaitValue(500)
        Assert.assertTrue(actualResult)
    }

    @Test
    fun `isInputPageValid test when toggle is on and all invalid`() {
        val dummyProduct = setDummyChild(0, 2, 20000, 30, 50)
        val targetPositionOfProduct = 0
        viewModel.setProduct(dummyProduct, targetPositionOfProduct)
        val actualResult = viewModel.isInputPageValid.getOrAwaitValue(500)
        Assert.assertFalse(actualResult)
    }

    private fun setDummyChild(
        start: Int,
        end : Int,
        price: Long,
        stock: Long,
        discount: Int
    ): ReservedProduct.Product {
        val dummyData = DataDummyOfVariantMultiLocationType.createDummyProduct()
        val dummyChild = dummyData.childProducts[0]
        for (i in start..end){
            dummyChild.warehouses[i].apply {
                isToggleOn = true
                discountSetup.apply {
                    this.discount = discount
                    this.price = price
                    this.stock = stock
                }
            }
        }

        return dummyData
    }

    private fun setValueOfProductAndVariantAttribute(isToggleOn: Boolean = false) {
        val dummyData = DataDummyOfVariantMultiLocationType.createDummyProduct()
        val positionTarget = 0
        dummyData.childProducts[positionTarget].warehouses.forEach { it.isToggleOn = isToggleOn }
        viewModel.setProduct(dummyData, positionTarget)
    }

    private fun declareDummyReturnTextStringResource() {
        every {
            context.getString(
                R.string.manageproductnonvar_min_message_format,
                any()
            )
        } returns "Min. $MINIMUM_PRICE_MESSAGE"
        every {
            context.getString(
                R.string.manageproductnonvar_max_message_format,
                any()
            )
        } returns "Max. $MAX_PRICE_MESSAGE"
        every {
            context.getString(
                R.string.manageproductnonvar_range_message_format,
                any()
            )
        } returns BETWEEN_PRICE_MESSAGE
    }
}