package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.product.addedit.common.constant.ProductStatus
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.callPrivateFunc
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.util.getPrivateProperty
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.variant.presentation.model.MultipleVariantEditInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantDetailInputLayoutModel
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductVariantDetailViewModelTest: AddEditProductVariantDetailViewModelTestFixture() {

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
    }

    @Test
    fun `When collapsedFields increement and decreement Expect valid collapsedFields`() {
        // test for 2x2 variant
        viewModel.setInputFieldSize(2)
        // test increement/ decreement
        viewModel.increaseCollapsedFields(viewModel.getInputFieldSize())
        assert(viewModel.getCollapsedFields() == 2)
        viewModel.decreaseCollapsedFields(viewModel.getInputFieldSize())
        assert(viewModel.getCollapsedFields() == 0)

        // reset field
        viewModel.resetCollapsedFields()
        assert(viewModel.getCollapsedFields() == 0)
    }

    @Test
    fun `When validate MultipleSelect data Expect valid error message`() {
        val messagePriceMin = viewModel.validateVariantPriceInput(0.toBigInteger())
        val messagePriceExact = viewModel.validateVariantPriceInput(MIN_PRODUCT_PRICE_LIMIT.toBigInteger())
        assert(messagePriceMin == viewModel.provider.getMinLimitProductPriceErrorMessage())
        assert(messagePriceExact.isEmpty())

        val messageStockMin = viewModel.validateProductVariantStockInput(0.toBigInteger())
        val messageStockExact = viewModel.validateProductVariantStockInput(MIN_PRODUCT_STOCK_LIMIT.toBigInteger())
        assert(messageStockMin == viewModel.provider.getMinLimitProductStockErrorMessage())
        assert(messageStockExact.isEmpty())
    }

    @Test
    fun `When validate submit button data Expect valid boolean`() {
        val variantInputs = listOf(
                VariantDetailInputLayoutModel(price = "5000", stock = "1"),
                VariantDetailInputLayoutModel(price = "3000", stock = "2"),
                VariantDetailInputLayoutModel(price = "1000", stock = "1")
        )

        var isResultInvalid = viewModel.validateSubmitDetailField(variantInputs)
        assert(!isResultInvalid)

        // check invalid price
        variantInputs[1].price = "0"
        isResultInvalid = viewModel.validateSubmitDetailField(variantInputs)
        assert(isResultInvalid)

        // check invalid stock
        variantInputs[1].price = "3000"
        variantInputs[1].stock = "0"
        isResultInvalid = viewModel.validateSubmitDetailField(variantInputs)
        assert(isResultInvalid)
    }

    private val EXPECTED_AVAILABLE_FIELDS = 4
    private fun initVariantDetailInputMap(){
        viewModel.productInputModel.value = productInputModel

        // test for 2x2 variant
        viewModel.setInputFieldSize(2)
        viewModel.updateCurrentHeaderPositionMap(0, 0) // create first header
        viewModel.updateCurrentHeaderPositionMap(3, 3) // create second header
        viewModel.updateVariantDetailHeaderMap(0, false)
        viewModel.updateVariantDetailHeaderMap(3, false)

        // create
        viewModel.updateVariantDetailInputMap(1,
                VariantDetailInputLayoutModel(
                        headerPosition=0,
                        visitablePosition=1,
                        unitValueLabel="8",
                        isActive=true,
                        price="9.999",
                        isPriceError=false,
                        stock="1",
                        isStockError=false,
                        isSkuFieldVisible=true,
                        priceEditEnabled=true,
                        isPrimary=false,
                        combination= listOf(0, 0)))

        viewModel.updateVariantDetailInputMap(2,
                VariantDetailInputLayoutModel(
                        headerPosition=0,
                        visitablePosition=2,
                        unitValueLabel="10",
                        isActive=true,
                        price="9.999",
                        isPriceError=false,
                        stock="1",
                        isStockError=false,
                        isSkuFieldVisible=true,
                        priceEditEnabled=true,
                        isPrimary=false,
                        combination= listOf(0, 1)))

        viewModel.updateVariantDetailInputMap(4,
                VariantDetailInputLayoutModel(
                        headerPosition=3,
                        visitablePosition=4,
                        unitValueLabel="8",
                        isActive=true,
                        price="9.999",
                        isPriceError=false,
                        stock="1000000000000000000000000000",
                        isStockError=false,
                        isSkuFieldVisible=true,
                        priceEditEnabled=true,
                        isPrimary=false,
                        combination= listOf(1, 0)))

        viewModel.editVariantDetailInputMap(4,
                viewModel.generateVariantDetailInputModel(2, 3, "8", true))

        viewModel.updateVariantDetailInputMap(5,
                viewModel.generateVariantDetailInputModel(3, 3, "10", true))

    }

    @Test
    fun `When getAvailableFields Expect valid number of fields`() {
        assert(viewModel.getAvailableFields().isEmpty())

        initVariantDetailInputMap()
        assert(viewModel.getAvailableFields().size == EXPECTED_AVAILABLE_FIELDS)

        viewModel.updateVariantDetailHeaderMap(0, true)
        viewModel.increaseCollapsedFields(EXPECTED_AVAILABLE_FIELDS/ 2)
        assert(viewModel.getAvailableFields().size == EXPECTED_AVAILABLE_FIELDS/ 2)

        viewModel.updateVariantDetailHeaderMap(3, true)
        viewModel.increaseCollapsedFields(EXPECTED_AVAILABLE_FIELDS/ 2)
        assert(viewModel.getAvailableFields().isEmpty())
    }

    @Test
    fun `When validate variant input Expect valid error`() {
        initVariantDetailInputMap()
        var inputModel: VariantDetailInputLayoutModel? = null

        // price test
        inputModel = viewModel.validateVariantPriceInput("0", 1)
        assert(inputModel.isPriceError)

        inputModel = viewModel.validateVariantPriceInput("", 1)
        assert(inputModel.isPriceError)

        inputModel = viewModel.validateVariantPriceInput("1000", 1)
        assert(!inputModel.isPriceError)

        // stock test
        inputModel = viewModel.validateProductVariantStockInput("0", 1)
        assert(inputModel.isStockError)

        inputModel = viewModel.validateProductVariantStockInput("", 1)
        assert(inputModel.isStockError)

        inputModel = viewModel.validateProductVariantStockInput("10", 1)
        assert(!inputModel.isStockError)

        // check error count
        assert(viewModel.errorCounter.getOrAwaitValue() == 0)
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
    fun `When update multiple input variant Expect correct variant`() {
        initVariantDetailInputMap()
        val multipleVariantEditInputModel = MultipleVariantEditInputModel(price="666", stock="6",
                sku="SK-U", selection= mutableListOf(
                mutableListOf(0, 0),
                mutableListOf(0, 1),
                mutableListOf(1, 0),
                mutableListOf(1, 1)))
        val privateInputPriceErrorStatusMap = viewModel::class.java.getDeclaredField("inputPriceErrorStatusMap").apply {
            isAccessible = true
        }
        val privateInputStockErrorStatusMap = viewModel::class.java.getDeclaredField("inputStockErrorStatusMap").apply {
            isAccessible = true
        }
        privateInputPriceErrorStatusMap.set(viewModel, hashMapOf(1 to true, 2 to true))
        privateInputStockErrorStatusMap.set(viewModel, hashMapOf(1 to true, 2 to true))

        viewModel.updateProductInputModel(multipleVariantEditInputModel)

        val isChanged = viewModel.productInputModel.value?.variantInputModel?.products?.all {
            it.price == 666.toBigInteger() && it.stock == 6 && it.sku == "SK-U"
        } ?: false
        assert(isChanged)
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
        val validateProductVariantStockInputResult = viewModel.validateProductVariantStockInput("KK", 999)
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

}