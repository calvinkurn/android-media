package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.product.addedit.common.constant.ProductStatus
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.callPrivateFunc
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import com.tokopedia.shop.common.data.source.cloud.model.MaxStockThresholdResponse
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers

@ExperimentalCoroutinesApi
class AddEditProductVariantDetailViewModelTest : AddEditProductVariantDetailViewModelTestFixture() {

    @Test
    fun `When updateProductInputModel Expect correct initialize values`() {
        viewModel.updateProductInputModel(productInputModel)

        // check variant should have primary variant
        var isPrimaryVariantExist = false
        viewModel.productInputModel.value?.variantInputModel?.products?.let { products ->
            isPrimaryVariantExist = products.any {
                it.isPrimary
            }
        }

        val selectedVariantSize = viewModel.selectedVariantSize.getOrAwaitValue()
        assert(selectedVariantSize == 2)
        assert(viewModel.hasVariantCombination(selectedVariantSize))
        assert(isPrimaryVariantExist)
        assert(viewModel.hasWholesale.getOrAwaitValue() == false)
        assert(viewModel.isEditMode)
    }

    @Test
    fun `When updateVariantDetailHeaderMap Expect headerStatusMap changes`() {
        viewModel.updateVariantDetailHeaderMap(0, true)
        viewModel.updateVariantDetailHeaderMap(1, false)

        assert(viewModel.isVariantDetailHeaderCollapsed(0))
        assert(!viewModel.isVariantDetailHeaderCollapsed(1))
        assert(!viewModel.isVariantDetailHeaderCollapsed(999))
    }

    @Test
    fun `When updateCurrentHeaderPositionMap Expect currentHeaderPositionMap changes`() {
        // test for 2x2 variant
        viewModel.setInputFieldSize(2)
        viewModel.updateCurrentHeaderPositionMap(0, 0) // create first header
        viewModel.updateCurrentHeaderPositionMap(3, 3) // create second header

        // test first element header
        viewModel.collapseHeader(0, 0)
        assert(viewModel.getCurrentHeaderPosition(0) == 0)
        assert(viewModel.getCurrentHeaderPosition(3) == 1)

        viewModel.expandHeader(0, 0)
        assert(viewModel.getCurrentHeaderPosition(0) == 0)
        assert(viewModel.getCurrentHeaderPosition(3) == 3)

        // test last element header
        viewModel.collapseHeader(3, 3)
        assert(viewModel.getCurrentHeaderPosition(0) == 0)
        assert(viewModel.getCurrentHeaderPosition(3) == 3)

        viewModel.expandHeader(3, 3)
        assert(viewModel.getCurrentHeaderPosition(0) == 0)
        assert(viewModel.getCurrentHeaderPosition(3) == 3)

        // test overflowed element
        assert(viewModel.getCurrentHeaderPosition(999999) == 0)

        // test input size change
        assert(viewModel.getInputFieldSize() == 2)
    }

    private fun initVariantDetailInputMap() {
        viewModel.productInputModel.value = productInputModel

        // test for 2x2 variant
        viewModel.setInputFieldSize(2)
        viewModel.updateCurrentHeaderPositionMap(0, 0) // create first header
        viewModel.updateCurrentHeaderPositionMap(3, 3) // create second header
        viewModel.updateVariantDetailHeaderMap(0, false)
        viewModel.updateVariantDetailHeaderMap(3, false)

        // create
        viewModel.addToVariantDetailInputMap(
            1,
            VariantDetailInputLayoutModel(
                headerPosition = 0,
                visitablePosition = 1,
                unitValueLabel = "8",
                isActive = true,
                price = "9.999",
                isPriceError = false,
                stock = 1,
                isStockError = false,
                isSkuFieldVisible = true,
                priceEditEnabled = true,
                isPrimary = false,
                combination = listOf(0, 0)
            )
        )

        viewModel.addToVariantDetailInputMap(
            2,
            VariantDetailInputLayoutModel(
                headerPosition = 0,
                visitablePosition = 2,
                unitValueLabel = "10",
                isActive = true,
                price = "9.999",
                isPriceError = false,
                stock = 1,
                isStockError = false,
                isSkuFieldVisible = true,
                priceEditEnabled = true,
                isPrimary = false,
                combination = listOf(0, 1)
            )
        )

        viewModel.addToVariantDetailInputMap(
            4,
            VariantDetailInputLayoutModel(
                headerPosition = 3,
                visitablePosition = 4,
                unitValueLabel = "8",
                isActive = true,
                price = "9.999",
                isPriceError = false,
                stock = 1000000000,
                isStockError = false,
                isSkuFieldVisible = true,
                priceEditEnabled = true,
                isPrimary = false,
                combination = listOf(1, 0)
            )
        )

        viewModel.updateVariantDetailInputMap(
            4,
            viewModel.generateVariantDetailInputModel(2, 3, "8", true)
        )

        viewModel.addToVariantDetailInputMap(
            5,
            viewModel.generateVariantDetailInputModel(3, 3, "10", true)
        )
    }

    @Test
    fun `When validate variant input Expect valid error`() {
        initVariantDetailInputMap()
        val dummyError = "error"
        var inputModel: VariantDetailInputLayoutModel?

        coEvery { imsResourceProvider.getEmptyProductPriceErrorMessage() } returns dummyError
        coEvery { imsResourceProvider.getMinLimitProductPriceErrorMessage(MIN_PRODUCT_PRICE_LIMIT) } returns dummyError
        coEvery { imsResourceProvider.getEmptyProductStockErrorMessage() } returns dummyError
        coEvery { imsResourceProvider.getMinLimitProductStockErrorMessage(MIN_PRODUCT_STOCK_LIMIT) } returns dummyError
        coEvery { imsResourceProvider.getEmptyProductWeightErrorMessage() } returns dummyError
        coEvery { imsResourceProvider.getMinLimitProductWeightErrorMessage(AddEditProductVariantConstants.MIN_PRODUCT_WEIGHT_LIMIT) } returns dummyError
        coEvery { imsResourceProvider.getMaxLimitProductWeightErrorMessage(AddEditProductVariantConstants.MAX_PRODUCT_WEIGHT_LIMIT) } returns dummyError

        // price test
        inputModel = viewModel.validateVariantPriceInput("0", 1)
        assert(inputModel.isPriceError)

        inputModel = viewModel.validateVariantPriceInput("", 1)
        assert(inputModel.isPriceError)

        inputModel = viewModel.validateVariantPriceInput("1000", 1)
        assert(!inputModel.isPriceError)

        // stock test
        inputModel = viewModel.validateProductVariantStockInput(0, 1)
        assert(inputModel.isStockError)

        inputModel = viewModel.validateProductVariantStockInput(null, 1)
        assert(inputModel.isStockError)

        inputModel = viewModel.validateProductVariantStockInput(10, 1)
        assert(!inputModel.isStockError)

        // weight test
        inputModel = viewModel.validateProductVariantWeightInput(0, 1)
        assert(inputModel.isWeightError)

        inputModel = viewModel.validateProductVariantWeightInput(null, 1)
        assert(inputModel.isWeightError)

        inputModel = viewModel.validateProductVariantWeightInput(AddEditProductVariantConstants.MAX_PRODUCT_WEIGHT_LIMIT.inc(), 1)
        assert(inputModel.isWeightError)

        inputModel = viewModel.validateProductVariantWeightInput(10, 1)
        assert(!inputModel.isWeightError)
    }

    @Test
    fun `When update all sku visibility Expect sku visibility changes`() {
        initVariantDetailInputMap()
        viewModel.updateSkuVisibilityStatus(false)
        val isAllField1SkuVisible = viewModel.getVariantDetailHeaderData(0).all {
            it.isSkuFieldVisible
        }
        val isAllField2SkuVisible = viewModel.getVariantDetailHeaderData(3).all {
            it.isSkuFieldVisible
        }
        assert(!isAllField1SkuVisible)
        assert(!isAllField2SkuVisible)
    }

    @Test
    fun `When update productInputModel Expect update main product price`() {
        val expectedPrice = 100.toBigInteger()
        initVariantDetailInputMap()
        viewModel.updateVariantDetailInputMap(
            1,
            VariantDetailInputLayoutModel(
                headerPosition = 0,
                visitablePosition = 1,
                unitValueLabel = "8",
                price = expectedPrice.toString(),
                stock = 1,
                isActive = false,
                combination = listOf(0, 0)
            )
        )
        viewModel.updateProductInputModel()

        val mainPrice = viewModel.productInputModel.getOrAwaitValue().detailInputModel.price
        assertEquals(expectedPrice, mainPrice)
    }

    @Test
    fun `When update sku data Expect sku data changes`() {
        initVariantDetailInputMap()
        val testSku = "New SK-U"

        val inputModel = viewModel.updateVariantSkuInput(testSku, 1)
        assert(inputModel.sku == testSku)
    }

    @Test
    fun `When update switch status Expect switch status changes`() {
        initVariantDetailInputMap()
        val testSwitch1 = false
        val testSwitch2 = true

        val inputModel1 = viewModel.updateSwitchStatus(testSwitch1, 1)
        assert(inputModel1.isActive == testSwitch1)

        val inputModel2 = viewModel.updateSwitchStatus(testSwitch2, 1)
        assert(inputModel2.isActive == testSwitch2)
    }

    @Test
    fun `When update primary variant Expect primary variant changes`() {
        initVariantDetailInputMap()
        viewModel.addToVariantDetailInputMap(99, VariantDetailInputLayoutModel())
        viewModel.updatePrimaryVariant(listOf(1, 1))
        val variantInput = viewModel.productInputModel.value?.variantInputModel?.products?.first {
            it.combination[0] == 1 && it.combination[1] == 1
        } ?: ProductVariantInputModel()
        assert(variantInput.isPrimary)
    }

    @Test
    fun `When get primary variant title Expect correct primary variant title`() {
        initVariantDetailInputMap()
        val expectedTitle = "Biru Muda-10"
        val title = viewModel.getPrimaryVariantTitle(listOf(1, 1))
        assert(title == expectedTitle)
    }

    @Test
    fun `When productInput model became null Expect empty objects`() {
        viewModel.productInputModel = MutableLiveData<ProductInputModel>()
        assert(viewModel.productInputModel.value == null)
        viewModel.callPrivateFunc("setDefaultPrimaryVariant")

        viewModel.collapseHeader(1, 1)
        viewModel.expandHeader(1, 1)
        Assert.assertFalse(viewModel.isEditMode)
        Assert.assertTrue(viewModel.hasVariantCombination(MAX_SELECTED_VARIANT_TYPE))
        Assert.assertFalse(viewModel.hasVariantCombination(99999))

        val updateSwitchStatusResult = viewModel.updateSwitchStatus(false, 999)
        val updateVariantSkuInputResult = viewModel.updateVariantSkuInput("KK", 999)
        val validateVariantPriceInputResult = viewModel.validateVariantPriceInput("KK", 999)
        val validateProductVariantStockInputResult = viewModel.validateProductVariantStockInput(null, 999)
        val generateVariantDetailInputModelResult = viewModel.generateVariantDetailInputModel(0, 0, "", true)

        assert(updateSwitchStatusResult.headerPosition == 0)
        assert(updateVariantSkuInputResult.headerPosition == 0)
        assert(validateVariantPriceInputResult.headerPosition == 0)
        assert(validateProductVariantStockInputResult.headerPosition == 0)
        assert(generateVariantDetailInputModelResult.headerPosition == 0)
    }

    @Test
    fun `When setupMultiLocationValue, isMultiLocationShop will be true if all conditions met`() {
        every { userSession.isMultiLocationShop } returns true
        every { userSession.isShopAdmin } returns true

        viewModel.setupMultiLocationValue()

        assert(viewModel.isMultiLocationShop)
    }

    @Test
    fun `When setupMultiLocationValue, isMultiLocationShop will be false if is not multi location shop`() {
        every { userSession.isMultiLocationShop } returns false
        every { userSession.isShopAdmin } returns true

        viewModel.setupMultiLocationValue()

        assert(!viewModel.isMultiLocationShop)
    }

    @Test
    fun `When setupMultiLocationValue, isMultiLocationShop will be false if is not shop admin or shop owner`() {
        every { userSession.isMultiLocationShop } returns true
        every { userSession.isShopAdmin } returns false
        every { userSession.isShopAdmin } returns false

        viewModel.setupMultiLocationValue()

        assert(!viewModel.isMultiLocationShop)
    }

    @Test
    fun `When updateProductStatus with all variant is inactive, then set all product status to inactive`() {
        viewModel.productInputModel.value = productInputModel
        viewModel.callPrivateFunc("updateProductStatus", hashMapOf(0 to VariantDetailInputLayoutModel(isActive = false)))
        assert(viewModel.productInputModel.value?.detailInputModel?.status == ProductStatus.STATUS_INACTIVE)
    }

    @Test
    fun `When editVariantDetailInputMap using invalid fieldPosition, expect inputLayoutModelMap unchanged`() {
        viewModel.updateVariantDetailInputMap(999, VariantDetailInputLayoutModel())
        val result = viewModel.getVariantDetailHeaderData(999)
        assert(result.isEmpty())
    }

    @Test
    fun `When refreshInputDataValidStatus, expect inputDataValid value correctness`() {
        viewModel.addToVariantDetailInputMap(
            0,
            VariantDetailInputLayoutModel(
                isPriceError = true,
                isWeightError = true,
                isStockError = true
            )
        )
        val test0False = viewModel.getInputDataValidStatus()

        viewModel.addToVariantDetailInputMap(
            0,
            VariantDetailInputLayoutModel(
                isPriceError = false,
                isWeightError = true,
                isStockError = true
            )
        )
        val test1False = viewModel.getInputDataValidStatus()

        viewModel.addToVariantDetailInputMap(
            0,
            VariantDetailInputLayoutModel(
                isPriceError = false,
                isWeightError = true,
                isStockError = false
            )
        )
        val test2False = viewModel.getInputDataValidStatus()

        viewModel.addToVariantDetailInputMap(
            0,
            VariantDetailInputLayoutModel(
                isPriceError = false,
                isWeightError = false,
                isStockError = false
            )
        )
        val test3False = viewModel.getInputDataValidStatus()

        assertFalse(test0False)
        assertFalse(test1False)
        assertFalse(test2False)
        assertTrue(test3False)
    }

    @Test
    fun `getMaxStockThreshold and validateProductStockInput should be successful in getting the threshold value and getting an error`() {
        /*
         * Init Data:
         * 1. expectedErrorMessage is an error message will be shown provided that current stock more than the maximum stock
         * 2. expectedMaxStockThreshold is max stock threshold which we need to use to compare the expected and actual threshold
         * 3. stockInput is current stock, we need current more than expectedMaxStockThreshold for this test case
         */
        val expectedErrorMessage = "Stok melebihi batas maks. 100.000"
        val expectedMaxStockThreshold = "100000"
        val stockInput = "200000"

        // create stub
        coEvery {
            getMaxStockThresholdUseCase.execute(ArgumentMatchers.anyString())
        } returns MaxStockThresholdResponse(
            getIMSMeta = MaxStockThresholdResponse.GetIMSMeta(
                data = MaxStockThresholdResponse.GetIMSMeta.Data(
                    maxStockThreshold = expectedMaxStockThreshold
                ),
                header = Header()
            )
        )

        // create stub
        every {
            imsResourceProvider.getMaxLimitProductStockErrorMessage(expectedMaxStockThreshold)
        } returns expectedErrorMessage

        // fetch the threshold
        viewModel.getMaxStockThreshold(ArgumentMatchers.anyString())

        // need to wait the response of threshold because the validation using maxStockThreshold value inside of the function validation
        val actualMaxStockThreshold = viewModel.maxStockThreshold.getOrAwaitValue()

        // validate product stock input
        val actualErrorMessage = viewModel.validateProductVariantStockInput(stockInput.toBigInteger())

        /*
         * Expected Result:
         * 1. Max stock threshold equals to the actual one
         * 2. Expected error message equals to the actual one
         */
        assertEquals(expectedMaxStockThreshold, actualMaxStockThreshold)
        assertEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun `getMaxStockThreshold and validateProductStockInput should be successful in getting the threshold value and not getting an error`() {
        /*
         * Init Data:
         * 1. expectedErrorMessage is an error message will be shown provided that current stock more than the maximum stock
         * 2. expectedMaxStockThreshold is max stock threshold which we need to use to compare the expected and actual threshold
         * 3. stockInput is current stock, we need current stock less than expectedMaxStockThreshold for this test case
         */
        val expectedErrorMessage = "Stok melebihi batas maks. 100.000"
        val expectedMaxStockThreshold = "100000"
        val stockInput = "212"

        // create stub
        coEvery {
            getMaxStockThresholdUseCase.execute(ArgumentMatchers.anyString())
        } returns MaxStockThresholdResponse(
            getIMSMeta = MaxStockThresholdResponse.GetIMSMeta(
                data = MaxStockThresholdResponse.GetIMSMeta.Data(
                    maxStockThreshold = expectedMaxStockThreshold
                ),
                header = Header()
            )
        )

        // create stub
        every {
            imsResourceProvider.getMaxLimitProductStockErrorMessage(expectedMaxStockThreshold)
        } returns expectedErrorMessage

        // fetch the threshold
        viewModel.getMaxStockThreshold(ArgumentMatchers.anyString())

        // need to wait the response of threshold because the validation using maxStockThreshold value inside of the function validation
        val actualMaxStockThreshold = viewModel.maxStockThreshold.getOrAwaitValue()

        // validate product stock input
        val actualErrorMessage = viewModel.validateProductVariantStockInput(stockInput.toBigInteger())

        /*
         * Expected Result:
         * 1. Max stock threshold equals to the actual one
         * 2. Expected error message does not equal to the actual one
         */
        assertEquals(expectedMaxStockThreshold, actualMaxStockThreshold)
        assertNotEquals(expectedErrorMessage, actualErrorMessage)
    }

    @Test
    fun `getMaxStockThreshold and validateProductStockInput should fail in getting the threshold value and not getting an error`() {
        /*
         * Init Data:
         * 1. expectedErrorMessage is an error message will be shown provided that current stock more than the maximum stock
         * 2. expectedMaxStockThreshold is max stock threshold which we need to use to compare the expected and actual threshold
         * 3. stockInput is current stock, we need current stock more than expectedMaxStockThreshold for this test case
         */
        val expectedErrorMessage = "Stok melebihi batas maks. 100.000"
        val expectedMaxStockThreshold: String? = null
        val stockInput = "200000"

        // throw a throwable
        coEvery {
            getMaxStockThresholdUseCase.execute(ArgumentMatchers.anyString())
        } throws Throwable()

        // create stub
        every {
            imsResourceProvider.getMaxLimitProductStockErrorMessage(expectedMaxStockThreshold)
        } returns expectedErrorMessage

        // fetch the threshold
        viewModel.getMaxStockThreshold(ArgumentMatchers.anyString())

        // need to wait the response of threshold because the validation using maxStockThreshold value inside of the function validation
        val actualMaxStockThreshold = viewModel.maxStockThreshold.getOrAwaitValue()

        // validate product stock input
        val actualErrorMessage = viewModel.validateProductVariantStockInput(stockInput.toBigInteger())

        /*
         * Expected Result:
         * 1. Max stock threshold equals to the actual one
         * 2. Expected error message does not equal to the actual one
         */
        assertEquals(expectedMaxStockThreshold, actualMaxStockThreshold)
        assertNotEquals(expectedErrorMessage, actualErrorMessage)
    }
}
