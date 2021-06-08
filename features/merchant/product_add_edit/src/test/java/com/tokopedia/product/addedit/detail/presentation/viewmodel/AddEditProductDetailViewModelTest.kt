package com.tokopedia.product.addedit.detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.domain.model.PriceSuggestionSuggestedPriceGet
import com.tokopedia.product.addedit.detail.domain.model.ProductValidateData
import com.tokopedia.product.addedit.detail.domain.model.ProductValidateV3
import com.tokopedia.product.addedit.detail.domain.model.ValidateProductResponse
import com.tokopedia.product.addedit.detail.domain.usecase.*
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_MIN_ORDER_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_MIN_ORDER_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryResponse
import com.tokopedia.product.addedit.specification.domain.model.DrogonAnnotationCategoryV2
import com.tokopedia.product.addedit.specification.domain.model.Values
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.util.getPrivateProperty
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.*
import org.mockito.ArgumentMatchers.any
import kotlin.reflect.KFunction0

@FlowPreview
@ExperimentalCoroutinesApi
class AddEditProductDetailViewModelTest {

    @RelaxedMockK
    lateinit var provider: ResourceProvider

    @RelaxedMockK
    lateinit var getCategoryRecommendationUseCase: GetCategoryRecommendationUseCase

    @RelaxedMockK
    lateinit var getNameRecommendationUseCase: GetNameRecommendationUseCase

    @RelaxedMockK
    lateinit var validateProductUseCase: ValidateProductUseCase

    @RelaxedMockK
    lateinit var getShopEtalaseUseCase: GetShopEtalaseUseCase

    @RelaxedMockK
    lateinit var annotationCategoryUseCase: AnnotationCategoryUseCase

    @RelaxedMockK
    lateinit var productPriceSuggestionSuggestedPriceGetUseCase: PriceSuggestionSuggestedPriceGetUseCase

    @RelaxedMockK
    lateinit var priceSuggestionSuggestedPriceGetByKeywordUseCase: PriceSuggestionSuggestedPriceGetByKeywordUseCase

    @RelaxedMockK
    lateinit var mIsInputValidObserver: Observer<Boolean>

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mIsInputValid.observeForever(mIsInputValidObserver)
    }

    @After
    fun cleanUp() {
        mIsInputValid.removeObserver(mIsInputValidObserver)
    }

    private val coroutineDispatcher = TestCoroutineDispatcher()

    @Suppress("UNCHECKED_CAST")
    private val mIsInputValid: MediatorLiveData<Boolean> by lazy {
        getPrivateField(viewModel, "mIsInputValid") as MediatorLiveData<Boolean>
    }

    @Suppress("UNCHECKED_CAST")
    private val mIsProductPhotoError: MutableLiveData<Boolean> by lazy {
        getPrivateField(viewModel, "mIsProductPhotoError") as MutableLiveData<Boolean>
    }

    @Suppress("UNCHECKED_CAST")
    private val mIsProductNameInputError: MutableLiveData<Boolean> by lazy {
        getPrivateField(viewModel, "mIsProductNameInputError") as MutableLiveData<Boolean>
    }

    @Suppress("UNCHECKED_CAST")
    private val mIsProductPriceInputError: MutableLiveData<Boolean> by lazy {
        getPrivateField(viewModel, "mIsProductPriceInputError") as MutableLiveData<Boolean>
    }

    @Suppress("UNCHECKED_CAST")
    private val mIsPreOrderDurationInputError: MutableLiveData<Boolean> by lazy {
        getPrivateField(viewModel, "mIsPreOrderDurationInputError") as MutableLiveData<Boolean>
    }

    @Suppress("UNCHECKED_CAST")
    private val mIsProductStockInputError: MutableLiveData<Boolean> by lazy {
        getPrivateField(viewModel, "mIsProductStockInputError") as MutableLiveData<Boolean>
    }

    private val stockAllocationDefaultMessage: String by lazy {
        (getPrivateField(viewModel, "stockAllocationDefaultMessage") as? String) ?: "invalid"
    }

    private val viewModel: AddEditProductDetailViewModel by lazy {
        AddEditProductDetailViewModel(provider, coroutineDispatcher, getNameRecommendationUseCase,
                getCategoryRecommendationUseCase, validateProductUseCase, getShopEtalaseUseCase,
                annotationCategoryUseCase, productPriceSuggestionSuggestedPriceGetUseCase,
                priceSuggestionSuggestedPriceGetByKeywordUseCase, userSession)
    }

    @Test
    fun `success get category recommendation`() = coroutineTestRule.runBlockingTest {
        val successResult = listOf(any<ListItemUnify>(), any<ListItemUnify>(), any<ListItemUnify>())

        coEvery {
            getCategoryRecommendationUseCase.executeOnBackground()
        } returns successResult

        viewModel.getCategoryRecommendation("baju")
        val result = viewModel.productCategoryRecommendationLiveData.getOrAwaitValue()

        coVerify {
            getCategoryRecommendationUseCase.executeOnBackground()
        }

        Assert.assertTrue(result != null && result == Success(successResult))
    }

    @Test
    fun `failed get category recommendation`() = coroutineTestRule.runBlockingTest {
        coEvery {
            getCategoryRecommendationUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getCategoryRecommendation("baju")
        val result = viewModel.productCategoryRecommendationLiveData.getOrAwaitValue()

        coVerify {
            getCategoryRecommendationUseCase.executeOnBackground()
        }

        Assert.assertTrue(result != null && result is Fail)
    }

    @Test
    fun `success get name recommendation`() = coroutineTestRule.runBlockingTest {
        val resultNameRecommendation = listOf("batik", "batik couple", "baju batik wanita", "baju batik pria", "batik kultut")

        coEvery {
            getNameRecommendationUseCase.executeOnBackground()
        } returns resultNameRecommendation

        viewModel.getProductNameRecommendation(query = "batik")
        viewModel.isNameRecommendationSelected = true
        val resultViewmodel = viewModel.productNameRecommendations.getOrAwaitValue()

        coVerify {
            getNameRecommendationUseCase.executeOnBackground()
        }

        Assert.assertTrue(resultViewmodel == Success(resultNameRecommendation))
        Assert.assertTrue(viewModel.isNameRecommendationSelected)
    }

    @Test
    fun `failed get name recommendation`() = coroutineTestRule.runBlockingTest {
        coEvery {
            getNameRecommendationUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getProductNameRecommendation(query = "baju")
        viewModel.isNameRecommendationSelected = false
        val result = viewModel.productNameRecommendations.getOrAwaitValue()

        coVerify {
            getNameRecommendationUseCase.executeOnBackground()
        }

        Assert.assertTrue(result is Fail)
        Assert.assertFalse(viewModel.isNameRecommendationSelected)
    }

    @Test
    fun `isInputValid should return true when no input is error and isAdding is false`() {
        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isInputValid should return true when no input is error and isAdding is true also isEditing is false`() {
        viewModel.isAdding = true
        viewModel.isEditing = false

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertTrue(isValid)
    }

    @Test
    fun `isInputValid should return false when there is no photo`() {
        viewModel.validateProductPhotoInput(0)

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isInputValid should return false when name is error`() {
        viewModel.validateProductNameInput("")

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isInputValid should return false when name is error and constants true`() {
        viewModel.isAdding = true
        viewModel.isEditing = true
        viewModel.isFirstMoved = true
        mIsProductNameInputError.value = true
        mIsProductPriceInputError.value = true

        viewModel.validateProductNameInput("")

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
        Assert.assertTrue(viewModel.isAdding)
        Assert.assertTrue(viewModel.isEditing)
        Assert.assertTrue(viewModel.isFirstMoved)
    }

    @Test
    fun `isInputValid should return false when price is error`() {
        viewModel.validateProductPriceInput("")

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isInputValid should return false when wholesale price activated and error counter is more than zero`() {
        viewModel.isAddingWholeSale = false
        viewModel.isAddingValidationWholeSale = false
        viewModel.isWholeSalePriceActivated.value = true
        viewModel.wholeSaleErrorCounter.value = 1

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
        Assert.assertFalse(viewModel.isAddingWholeSale)
        Assert.assertFalse(viewModel.isAddingValidationWholeSale)
    }

    @Test
    fun `isInputValid should return true when wholesale price activated and error counter is more zero`() {
        viewModel.isAddingWholeSale = true
        viewModel.isAddingValidationWholeSale = true
        viewModel.isWholeSalePriceActivated.value = true
        viewModel.wholeSaleErrorCounter.value = 0

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertTrue(isValid)
        Assert.assertTrue(viewModel.isAddingWholeSale)
        Assert.assertTrue(viewModel.isAddingValidationWholeSale)
    }

    @Test
    fun `isInputValid should return false when pre-order is activated and pre order duration input is error`() {
        viewModel.isPreOrderActivated.value = true
        mIsPreOrderDurationInputError.value = true

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isInputValid should return true when pre-order price activated and pre order duration input is not error`() {
        viewModel.isPreOrderActivated.value = true
        mIsPreOrderDurationInputError.value = false

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertTrue(isValid)
    }

    @Test
    fun `validateProductPhotoInput should valid when product have photo`() {
        viewModel.validateProductPhotoInput(1)

        val isError = mIsProductPhotoError.getOrAwaitValue()
        Assert.assertTrue(!isError)
    }

    @Test
    fun `validateProductPhotoInput should invalid when product doesn't have photo`() {
        viewModel.validateProductPhotoInput(0)

        val isError = mIsProductPhotoError.getOrAwaitValue()
        Assert.assertTrue(isError)
    }

    @Test
    fun `validateProductNameInput should valid when product name isn't empty`() {
        val stringResMessage = "Tips: Jenis Produk + Merek Produk + Keterangan Tambahan"

        runValidationAndProvideMessage(provider::getProductNameTips, stringResMessage) {
            viewModel.validateProductNameInput("Baju")
        }

        val isError = viewModel.isProductNameInputError.getOrAwaitValue()
        Assert.assertTrue(!isError && viewModel.productNameMessage.isNotBlank() && viewModel.productNameMessage == stringResMessage)
    }

    @Test
    fun `validateProductNameInput should invalid when product name is empty`() {
        val stringResMessage = "Tips: Jenis Produk + Merek Produk + Keterangan Tambahan"

        runValidationAndProvideMessage(provider::getEmptyProductNameErrorMessage, stringResMessage) {
            viewModel.validateProductNameInput("")
        }

        val isError = viewModel.isProductNameInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.productNameMessage.isNotBlank() && viewModel.productNameMessage == stringResMessage)
    }

    @Test
    fun `validateProductNameInput should invalid when product name is exist`() = coroutineTestRule.runBlockingTest {
        val resultMessage = listOf("error 1", "error 2")

        coEvery {
            validateProductUseCase.executeOnBackground()
        } returns ValidateProductResponse(
                ProductValidateV3(
                        isSuccess = false,
                        data = ProductValidateData(resultMessage)
                )
        )

        viewModel.isProductNameChanged = true
        viewModel.validateProductNameInput( "batik cociks")
        viewModel.isProductNameInputError.getOrAwaitValue()

        coVerify(timeout = 300) {
            validateProductUseCase.executeOnBackground()
        }

        delay(200) // delay to receive latest result
        Assert.assertEquals(resultMessage.joinToString("\n"), viewModel.productNameMessage)
    }

    @Test
    fun `validateProductNameInput should valid when product name is not exist`() = coroutineTestRule.runBlockingTest {
        val resultMessage = listOf<String>()

        coEvery {
            validateProductUseCase.executeOnBackground()
        } returns ValidateProductResponse(
                ProductValidateV3(
                        isSuccess = true,
                        data = ProductValidateData(resultMessage)
                )
        )

        viewModel.isProductNameChanged = true
        viewModel.validateProductNameInput( "batik cociks")
        val resultViewmodel = viewModel.isProductNameInputError.getOrAwaitValue()

        coVerify(timeout = 300) {
            validateProductUseCase.executeOnBackground()
        }

        Assert.assertFalse(resultViewmodel)
    }

    @Test
    fun `validateProductPriceInput should valid when product price isn't empty and not less than min price`() {
        viewModel.validateProductPriceInput("$MIN_PRODUCT_PRICE_LIMIT")

        val isError = viewModel.isProductPriceInputError.getOrAwaitValue()
        Assert.assertTrue(!isError && viewModel.productPriceMessage.isBlank())
    }

    @Test
    fun `validateProductPriceInput should invalid when product price is empty`() {
        val stringResErrorMessage = "Harga produk tidak boleh kosong"

        runValidationAndProvideMessage(provider::getEmptyProductPriceErrorMessage, stringResErrorMessage) {
            viewModel.validateProductPriceInput("")
        }

        val isError = viewModel.isProductPriceInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.productPriceMessage.isNotBlank() && viewModel.productPriceMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductPriceInput should invalid when product price isn't empty but less than min price`() {
        val stringResErrorMessage = "Harga kurang dari batas min. Rp100."

        runValidationAndProvideMessage(provider::getMinLimitProductPriceErrorMessage, stringResErrorMessage) {
            viewModel.validateProductPriceInput("${MIN_PRODUCT_PRICE_LIMIT - 1}")
        }

        val isError = viewModel.isProductPriceInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.productPriceMessage.isNotBlank() && viewModel.productPriceMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductWholeSaleQuantityInput should valid when wholeSaleQuantityInput is more than zero also minOrderInput and previousInput is blank`() {
        val errorMessage = viewModel.validateProductWholeSaleQuantityInput("10", "", "")

        verify(inverse = true) {
            provider.getEmptyWholeSaleQuantityErrorMessage()
            provider.getZeroWholeSaleQuantityErrorMessage()
            provider.getMinLimitWholeSaleQuantityErrorMessage()
            provider.getPrevInputWholeSaleQuantityErrorMessage()
        }

        Assert.assertTrue(errorMessage.isBlank())
    }

    @Test
    fun `validateProductWholeSaleQuantityInput should valid when wholeSaleQuantityInput is more than zero, minOrderInput is less than or equal wholeSaleQuantityInput and previousInput is blank`() {
        val errorMessage = viewModel.validateProductWholeSaleQuantityInput("10", "5", "")

        verify(inverse = true) {
            provider.getEmptyWholeSaleQuantityErrorMessage()
            provider.getZeroWholeSaleQuantityErrorMessage()
            provider.getMinLimitWholeSaleQuantityErrorMessage()
            provider.getPrevInputWholeSaleQuantityErrorMessage()
        }

        Assert.assertTrue(errorMessage.isBlank())
    }

    @Test
    fun `validateProductWholeSaleQuantityInput should valid when wholeSaleQuantityInput is more than zero, minOrderInput is less than or equal wholeSaleQuantityInput and previousInput is less than wholeSaleQuantityInput`() {
        val errorMessage = viewModel.validateProductWholeSaleQuantityInput("10", "5", "6")

        verify(inverse = true) {
            provider.getEmptyWholeSaleQuantityErrorMessage()
            provider.getZeroWholeSaleQuantityErrorMessage()
            provider.getMinLimitWholeSaleQuantityErrorMessage()
            provider.getPrevInputWholeSaleQuantityErrorMessage()
        }

        Assert.assertTrue(errorMessage.isBlank())
    }

    @Test
    fun `validateProductWholeSaleQuantityInput should invalid when wholeSaleQuantityInput is blank`() {
        val stringResErrorMessage = "Jumlah minimal tidak boleh kosong"

        val errorMessage = runValidationAndProvideMessage(provider::getEmptyWholeSaleQuantityErrorMessage, stringResErrorMessage) {
            viewModel.validateProductWholeSaleQuantityInput("", "", "")
        }

        Assert.assertTrue(errorMessage.isNotBlank() && errorMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductWholeSaleQuantityInput should invalid when wholeSaleQuantityInput is zero`() {
        val stringResErrorMessage = "Minimum pemesanan tidak boleh 0"

        val errorMessage = runValidationAndProvideMessage(provider::getZeroWholeSaleQuantityErrorMessage, stringResErrorMessage) {
            viewModel.validateProductWholeSaleQuantityInput("0", "", "")
        }

        Assert.assertTrue(errorMessage.isNotBlank() && errorMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductWholeSaleQuantityInput should invalid when minOrderInput is more than wholeSaleQuantityInput`() {
        val stringResErrorMessage = "Jumlah harus lebih > dari min. pemesanan"

        val errorMessage = runValidationAndProvideMessage(provider::getMinLimitWholeSaleQuantityErrorMessage, stringResErrorMessage) {
            viewModel.validateProductWholeSaleQuantityInput("5", "6", "")
        }

        Assert.assertTrue(errorMessage.isNotBlank() && errorMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductWholeSaleQuantityInput should invalid when previousInput is more than or equal to wholeSaleQuantityInput`() {
        val stringResErrorMessage = "Jumlah harus lebih > dari sebelumnya"

        val errorMessage = runValidationAndProvideMessage(provider::getPrevInputWholeSaleQuantityErrorMessage, stringResErrorMessage) {
            viewModel.validateProductWholeSaleQuantityInput("5", "", "5")
        }

        Assert.assertTrue(errorMessage.isNotBlank() && errorMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductWholeSalePriceInput should valid when wholeSalePriceInput is more than zero and productPriceInput also previousInput is blank`() {
        val errorMessage = viewModel.validateProductWholeSalePriceInput("500", "", "")

        verify(inverse = true) {
            provider.getEmptyWholeSalePriceErrorMessage()
            provider.getZeroWholeSalePriceErrorMessage()
            provider.getWholeSalePriceTooExpensiveErrorMessage()
            provider.getPrevInputWholeSalePriceErrorMessage()
        }

        Assert.assertTrue(errorMessage.isBlank())
    }

    @Test
    fun `validateProductWholeSalePriceInput should valid when wholeSalePriceInput is more than zero, productPriceInput is less than wholeSalePriceInput and previousInput is blank`() {
        val errorMessage = viewModel.validateProductWholeSalePriceInput("350", "500", "")

        verify(inverse = true) {
            provider.getEmptyWholeSalePriceErrorMessage()
            provider.getZeroWholeSalePriceErrorMessage()
            provider.getWholeSalePriceTooExpensiveErrorMessage()
            provider.getPrevInputWholeSalePriceErrorMessage()
        }

        Assert.assertTrue(errorMessage.isBlank())
    }

    @Test
    fun `validateProductWholeSalePriceInput should valid when wholeSalePriceInput is more than zero, productPriceInput is less than wholeSalePriceInput and previousInput is more than wholeSalePriceInput`() {
        val errorMessage = viewModel.validateProductWholeSalePriceInput("400", "500", "350")

        verify(inverse = true) {
            provider.getEmptyWholeSalePriceErrorMessage()
            provider.getZeroWholeSalePriceErrorMessage()
            provider.getWholeSalePriceTooExpensiveErrorMessage()
            provider.getPrevInputWholeSalePriceErrorMessage()
        }

        Assert.assertTrue(errorMessage.isBlank())
    }

    @Test
    fun `validateProductWholeSalePriceInput should invalid when wholeSalePriceInput is blank`() {
        val stringResErrorMessage = "Harga grosir tidak boleh kosong"

        val errorMessage = runValidationAndProvideMessage(provider::getEmptyWholeSalePriceErrorMessage, stringResErrorMessage) {
            viewModel.validateProductWholeSalePriceInput("", "500", "")
        }

        Assert.assertTrue(errorMessage.isNotBlank() && errorMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductWholeSalePriceInput should invalid when wholeSalePriceInput is zero`() {
        val stringResErrorMessage = "Harga grosir tidak boleh 0"

        val errorMessage = runValidationAndProvideMessage(provider::getZeroWholeSalePriceErrorMessage, stringResErrorMessage) {
            viewModel.validateProductWholeSalePriceInput("0", "500", "")
        }

        Assert.assertTrue(errorMessage.isNotBlank() && errorMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductWholeSalePriceInput should invalid when wholeSalePriceInput is more than or equal productPriceInput`() {
        val stringResErrorMessage = "Harga grosir harus lebih murah dari harga satuan"

        val errorMessage = runValidationAndProvideMessage(provider::getWholeSalePriceTooExpensiveErrorMessage, stringResErrorMessage) {
            viewModel.validateProductWholeSalePriceInput("1000", "500", "")
        }

        Assert.assertTrue(errorMessage.isNotBlank() && errorMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductWholeSalePriceInput should invalid when previousInput is less or equal to wholeSalePriceInput`() {
        val stringResErrorMessage = "Harga harus lebih murah dari harga sebelumnya"

        val errorMessage = runValidationAndProvideMessage(provider::getPrevInputWholeSalePriceErrorMessage, stringResErrorMessage) {
            viewModel.validateProductWholeSalePriceInput("400", "500", "400")
        }

        Assert.assertTrue(errorMessage.isNotBlank() && errorMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductStockInput should do nothing when product has variant`() {
        mIsProductStockInputError.value = false
        val previousValue = mIsProductStockInputError.getOrAwaitValue()

        val selection = SelectionInputModel()
        viewModel.productInputModel.variantInputModel.selections = listOf(selection)

        viewModel.validateProductStockInput("")
        val newValue = viewModel.isProductStockInputError.getOrAwaitValue()
        Assert.assertTrue(newValue == previousValue)
    }

    @Test
    fun `validateProductStockInput should valid when productStockInput is between min and max stock limit`() {
        viewModel.validateProductStockInput("$MIN_PRODUCT_STOCK_LIMIT")
        val isError = viewModel.isProductStockInputError.getOrAwaitValue()
        Assert.assertTrue(!isError && viewModel.productStockMessage.isBlank())
    }

    @Test
    fun `validateProductStockInput should invalid when productStockInput is empty`() {
        val stringResErrorMessage = "Stok produk tidak boleh kosong"

        runValidationAndProvideMessage(provider::getEmptyProductStockErrorMessage, stringResErrorMessage) {
            viewModel.validateProductStockInput("")
        }

        val isError = viewModel.isProductStockInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.productStockMessage.isNotBlank() && viewModel.productStockMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductStockInput should invalid when productStockInput is less than min stock limit`() {
        val stringResErrorMessage = "Stok produk tidak boleh kosong"

        runValidationAndProvideMessage(provider::getEmptyProductStockErrorMessage, stringResErrorMessage) {
            viewModel.validateProductStockInput("${MIN_PRODUCT_STOCK_LIMIT - 1}")
        }

        val isError = viewModel.isProductStockInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.productStockMessage.isNotBlank() && viewModel.productStockMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductStockInput should invalid when productStockInput is greater than max stock limit`() {
        val stringResErrorMessage = "Stok melebihi batas maks. 999.999"

        runValidationAndProvideMessage(provider::getMaxLimitProductStockErrorMessage, stringResErrorMessage) {
            viewModel.validateProductStockInput("${MAX_PRODUCT_STOCK_LIMIT + 1}")
        }

        val isError = viewModel.isProductStockInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.productStockMessage.isNotBlank() && viewModel.productStockMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductMinOrderInput should valid when productStockInput is blank and minOrderQuantityInput is greater or equal to min order quantity`() {
        viewModel.validateProductMinOrderInput("", "$MIN_MIN_ORDER_QUANTITY")

        val isError = viewModel.isOrderQuantityInputError.getOrAwaitValue()
        Assert.assertTrue(!isError && viewModel.orderQuantityMessage.isBlank())
    }

    @Test
    fun `validateProductMinOrderInput should valid when productStockInput is blank or not, minOrderQuantityInput is greater or equal to min order quantity and product has variant`() {
        val selection = SelectionInputModel()
        viewModel.productInputModel.variantInputModel.selections = listOf(selection)
        viewModel.validateProductMinOrderInput("${MIN_MIN_ORDER_QUANTITY - 1}", "$MIN_MIN_ORDER_QUANTITY")

        val isError = viewModel.isOrderQuantityInputError.getOrAwaitValue()
        Assert.assertTrue(!isError && viewModel.orderQuantityMessage.isBlank())
    }

    @Test
    fun `validateProductMinOrderInput should invalid when minOrderQuantityInput is empty`() {
        val stringResErrorMessage = "Minimum pemesanan tidak boleh kosong"

        runValidationAndProvideMessage(provider::getEmptyOrderQuantityErrorMessage, stringResErrorMessage) {
            viewModel.validateProductMinOrderInput("${MIN_MIN_ORDER_QUANTITY - 1}", "")
        }

        val isError = viewModel.isOrderQuantityInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.orderQuantityMessage.isNotBlank() && viewModel.orderQuantityMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductMinOrderInput should invalid when minOrderQuantityInput is less than minimum`() {
        val stringResErrorMessage = "Minimum pemesanan tidak boleh kosong"

        runValidationAndProvideMessage(provider::getEmptyOrderQuantityErrorMessage, stringResErrorMessage) {
            viewModel.validateProductMinOrderInput("${MIN_MIN_ORDER_QUANTITY - 1}", "${MIN_MIN_ORDER_QUANTITY - 1}")
        }

        val isError = viewModel.isOrderQuantityInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.orderQuantityMessage.isNotBlank() && viewModel.orderQuantityMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductMinOrderInput should invalid when minOrderQuantityInput is greater than productStockInput`() {
        val stringResErrorMessage = "Minimum pemesanan tidak boleh melebihi jumlah stok"

        runValidationAndProvideMessage(provider::getMinOrderExceedStockErrorMessage, stringResErrorMessage) {
            viewModel.validateProductMinOrderInput("${MIN_MIN_ORDER_QUANTITY - 1}", "$MIN_MIN_ORDER_QUANTITY")
        }

        val isError = viewModel.isOrderQuantityInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.orderQuantityMessage.isNotBlank() && viewModel.orderQuantityMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductMinOrderInput should invalid when minOrderQuantityInput is greater than limit quantity`() {
        val stringResErrorMessage = "Minimum pemesanan tidak boleh melebihi jumlah stok"

        runValidationAndProvideMessage(provider::getMinOrderExceedLimitQuantityErrorMessage, stringResErrorMessage) {
            viewModel.validateProductMinOrderInput("0", "${MAX_MIN_ORDER_QUANTITY + 1}")
        }

        val isError = viewModel.isOrderQuantityInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.orderQuantityMessage.isNotBlank() && viewModel.orderQuantityMessage == stringResErrorMessage)
    }

    @Test
    fun `validatePreOrderDurationInput should valid when timeUnit is DAY and preOrderDurationInput is between min and max day duration`() {
        viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_DAY, "${AddEditProductDetailConstants.MIN_PREORDER_DURATION}")
        val isError = viewModel.isPreOrderDurationInputError.getOrAwaitValue()
        Assert.assertTrue(!isError && viewModel.preOrderDurationMessage.isBlank())
    }

    @Test
    fun `validatePreOrderDurationInput should valid when timeUnit is WEEK and preOrderDurationInput is between min and max week duration`() {
        viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_WEEK, "${AddEditProductDetailConstants.MIN_PREORDER_DURATION}")
        val isError = viewModel.isPreOrderDurationInputError.getOrAwaitValue()
        Assert.assertTrue(!isError && viewModel.preOrderDurationMessage.isBlank())
    }

    @Test
    fun `validatePreOrderDurationInput should invalid when preOrderDurationInput is blank`() {
        val stringResErrorMessage = "Durasi minimal 1"

        runValidationAndProvideMessage(provider::getEmptyPreorderDurationErrorMessage, stringResErrorMessage) {
            viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_DAY, "")
        }

        val isError = viewModel.isPreOrderDurationInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.preOrderDurationMessage.isNotBlank() && viewModel.preOrderDurationMessage == stringResErrorMessage)
    }

    @Test
    fun `validatePreOrderDurationInput should invalid when preOrderDurationInput is less than minimum`() {
        val stringResErrorMessage = "Durasi minimal 1"

        runValidationAndProvideMessage(provider::getMinLimitPreorderDurationErrorMessage, stringResErrorMessage) {
            viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_DAY, "${AddEditProductDetailConstants.MIN_PREORDER_DURATION - 1}")
        }

        val isError = viewModel.isPreOrderDurationInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.preOrderDurationMessage.isNotBlank() && viewModel.preOrderDurationMessage == stringResErrorMessage)
    }

    @Test
    fun `validatePreOrderDurationInput should invalid when preOrderDurationInput is greater than max duration in day`() {
        val stringResErrorMessage = "Durasi melebihi batas maks. \n90 hari"

        runValidationAndProvideMessage(provider::getMaxDaysLimitPreorderDuratioErrorMessage, stringResErrorMessage) {
            viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_DAY, "${AddEditProductDetailConstants.MAX_PREORDER_DAYS + 1}")
        }

        val isError = viewModel.isPreOrderDurationInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.preOrderDurationMessage.isNotBlank() && viewModel.preOrderDurationMessage == stringResErrorMessage)
    }

    @Test
    fun `validatePreOrderDurationInput should invalid when preOrderDurationInput is greater than max duration in week`() {
        val stringResErrorMessage = "Durasi melebihi batas maks. \n13 minggu"

        runValidationAndProvideMessage(provider::getMaxWeeksLimitPreorderDuratioErrorMessage, stringResErrorMessage) {
            viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_WEEK, "${AddEditProductDetailConstants.MAX_PREORDER_WEEKS + 1}")
        }

        val isError = viewModel.isPreOrderDurationInputError.getOrAwaitValue()
        Assert.assertTrue(isError && viewModel.preOrderDurationMessage.isNotBlank() && viewModel.preOrderDurationMessage == stringResErrorMessage)
    }

    @Test
    fun `validateProductSkuInput should valid when productSkuInput is not contains space char`() = coroutineTestRule.runBlockingTest {
        val resultMessage = listOf<String>()

        coEvery {
            validateProductUseCase.executeOnBackground().productValidateV3
        } returns ProductValidateV3(isSuccess = true,
                data = ProductValidateData(resultMessage, resultMessage))

        viewModel.validateProductSkuInput("ESKU")
        val isError = viewModel.isProductSkuInputError.getOrAwaitValue()

        coVerify {
            validateProductUseCase.executeOnBackground()
        }
        Assert.assertTrue(!isError && viewModel.productSkuMessage.isBlank())
    }

    @Test
    fun `validateProductSkuInput should invalid when productSkuInput is contains space char`() = coroutineTestRule.runBlockingTest  {
        val resultMessage = listOf("error 1", "error 2")

        coEvery {
            validateProductUseCase.executeOnBackground().productValidateV3
        } returns ProductValidateV3(isSuccess = false,
                data = ProductValidateData(resultMessage, resultMessage))

        viewModel.validateProductSkuInput("ES KU")
        val isError = viewModel.isProductSkuInputError.getOrAwaitValue()

        coVerify {
            validateProductUseCase.executeOnBackground()
        }
        Assert.assertTrue(isError && viewModel.productSkuMessage.isNotBlank() && viewModel.productSkuMessage == resultMessage.joinToString("\n"))
    }

    @Test
    fun `updateProductPhotos should not change any image url's`() {
        val sampleProductPhotos = getSampleProductPhotos()
        viewModel.productInputModel.detailInputModel.pictureList = sampleProductPhotos
        val imagePickerResult = arrayListOf("local/path/to/image1.jpg", "local/path/to/image2.jpg")
        val originalImageUrl = viewModel.productInputModel.detailInputModel.pictureList.map { it.urlOriginal }
        val editedStatus = arrayListOf(false, false)

        val newUpdatedPhotos = viewModel.updateProductPhotos(imagePickerResult, ArrayList(originalImageUrl), editedStatus)
        viewModel.productInputModel.detailInputModel.apply {
            pictureList = newUpdatedPhotos.pictureList
            imageUrlOrPathList = newUpdatedPhotos.imageUrlOrPathList
        }

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList == sampleProductPhotos)
    }

    @Test
    fun `updateProductPhotos should update image list when user add one image`() {
        val sampleProductPhotos = getSampleProductPhotos()
        viewModel.productInputModel.detailInputModel.pictureList = sampleProductPhotos
        val imagePickerResult = arrayListOf("local/path/to/image1.jpg", "local/path/to/image2.jpg", "local/path/to/image3.jpg")
        val originalImageUrl = viewModel.productInputModel.detailInputModel.pictureList.map { it.urlOriginal }
        val editedStatus = arrayListOf(false, false, true)

        val newUpdatedPhotos = viewModel.updateProductPhotos(imagePickerResult, ArrayList(originalImageUrl), editedStatus)
        viewModel.productInputModel.detailInputModel.apply {
            pictureList = newUpdatedPhotos.pictureList
            imageUrlOrPathList = newUpdatedPhotos.imageUrlOrPathList
        }

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList == sampleProductPhotos &&
                viewModel.productPhotoPaths.size == 3 &&
                viewModel.productPhotoPaths[0] == sampleProductPhotos[0].urlThumbnail &&
                viewModel.productPhotoPaths[1] == sampleProductPhotos[1].urlThumbnail &&
                viewModel.productPhotoPaths.last() == imagePickerResult.last())
    }

    @Test
    fun `updateProductPhotos should update image list when user remove one image from the front`() {
        val sampleProductPhotos = getSampleProductPhotos()
        viewModel.productInputModel.detailInputModel.pictureList = sampleProductPhotos
        val imagePickerResult = arrayListOf("local/path/to/image2.jpg")
        val originalImageUrl = viewModel.productInputModel.detailInputModel.pictureList.map { it.urlOriginal }
        val editedStatus = arrayListOf(true, false)

        val newUpdatedPhotos = viewModel.updateProductPhotos(imagePickerResult, ArrayList(originalImageUrl), editedStatus)
        viewModel.productInputModel.detailInputModel.apply {
            pictureList = newUpdatedPhotos.pictureList
            imageUrlOrPathList = newUpdatedPhotos.imageUrlOrPathList
        }

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList.size != sampleProductPhotos.size &&
                viewModel.productInputModel.detailInputModel.pictureList.size == 1 &&
                viewModel.productInputModel.detailInputModel.pictureList.first().picID == sampleProductPhotos[1].picID &&
                viewModel.productPhotoPaths.size == 1 &&
                viewModel.productPhotoPaths == imagePickerResult)
    }

    @Test
    fun `updateProductPhotos should update image list when user remove one image from the back`() {
        val sampleProductPhotos = getSampleProductPhotos()
        viewModel.productInputModel.detailInputModel.pictureList = sampleProductPhotos
        val imagePickerResult = arrayListOf("local/path/to/image1.jpg")
        val originalImageUrl = viewModel.productInputModel.detailInputModel.pictureList.map { it.urlOriginal }
        val editedStatus = arrayListOf(false, true)

        val newUpdatedPhotos = viewModel.updateProductPhotos(imagePickerResult, ArrayList(originalImageUrl), editedStatus)
        viewModel.productInputModel.detailInputModel.apply {
            pictureList = newUpdatedPhotos.pictureList
            imageUrlOrPathList = newUpdatedPhotos.imageUrlOrPathList
        }

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList.size != sampleProductPhotos.size &&
                viewModel.productInputModel.detailInputModel.pictureList.size == 1 &&
                viewModel.productInputModel.detailInputModel.pictureList.first().picID == sampleProductPhotos[0].picID &&
                viewModel.productPhotoPaths.size == 1 &&
                viewModel.productPhotoPaths[0] == sampleProductPhotos[0].urlThumbnail)
    }

    @Test
    fun `updateProductPhotos should update image list when user edit a photo`() {
        val sampleProductPhotos = getSampleProductPhotos()
        viewModel.productInputModel.detailInputModel.pictureList = sampleProductPhotos
        val imagePickerResult = arrayListOf("local/path/to/editedImage1.jpg", "local/path/to/image2.jpg")
        val originalImageUrl = viewModel.productInputModel.detailInputModel.pictureList.map { it.urlOriginal }
        val editedStatus = arrayListOf(true, false)

        val newUpdatedPhotos = viewModel.updateProductPhotos(imagePickerResult, ArrayList(originalImageUrl), editedStatus)
        viewModel.productInputModel.detailInputModel.apply {
            pictureList = newUpdatedPhotos.pictureList
            imageUrlOrPathList = newUpdatedPhotos.imageUrlOrPathList
        }

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList.size != sampleProductPhotos.size &&
                viewModel.productInputModel.detailInputModel.pictureList.size == 1 &&
                viewModel.productPhotoPaths.size == 2 &&
                viewModel.productPhotoPaths[0] == imagePickerResult[0] &&
                viewModel.productPhotoPaths[1] == sampleProductPhotos[1].urlThumbnail)
    }

    @Test
    fun `disable productNameField when product has transaction`() {
        // positive case
        viewModel.productInputModel.itemSold = 199
        Assert.assertTrue(viewModel.hasTransaction)

        // negative case
        viewModel.productInputModel.itemSold = 0
        Assert.assertFalse(viewModel.hasTransaction)
    }

    @Test
    fun `get showcase list should get the list`() = coroutineTestRule.runBlockingTest {
        coEvery {
            getShopEtalaseUseCase.executeOnBackground().shopShowcases.result
        } returns listOf()

        viewModel.getShopShowCasesUseCase()
        val expectedResponse = Success(listOf<ShopEtalaseModel>())
        val actualResponse = viewModel.shopShowCases.getOrAwaitValue()

        coVerify {
            getShopEtalaseUseCase.executeOnBackground()
        }

        assertEquals(expectedResponse, actualResponse)

    }

    @Test
    fun `getAnnotationCategory should return unfilled data when productId is not provided`() = coroutineTestRule.runBlockingTest {
        val annotationCategoryData = listOf<AnnotationCategoryData>()

        coEvery {
            annotationCategoryUseCase.executeOnBackground()
        } returns AnnotationCategoryResponse(
                DrogonAnnotationCategoryV2(annotationCategoryData)
        )

        viewModel.getAnnotationCategory("", "")
        val result = viewModel.annotationCategoryData.getOrAwaitValue(time = 3)

        coVerify {
            annotationCategoryUseCase.executeOnBackground()
        }

        Assert.assertTrue(result is Success)

        if (result is Success) {
            viewModel.updateSpecificationByAnnotationCategory(result.data)
            val specificationText = viewModel.specificationText.getOrAwaitValue()
            Assert.assertTrue(specificationText.isEmpty())
        }
    }

    @Test
    fun `getAnnotationCategory should return specification data when productId is provided`() = coroutineTestRule.runBlockingTest {
        val annotationCategoryData = listOf(
                AnnotationCategoryData(
                        variant = "Merek",
                        data = listOf(
                                Values(1, "Indomie", true, ""),
                                Values(1, "Seedap", false, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa",
                        data = listOf(
                                Values(1, "Soto", false, ""),
                                Values(1, "Bawang", true, ""))
                )
        )

        coEvery {
            annotationCategoryUseCase.executeOnBackground()
        } returns AnnotationCategoryResponse(
                DrogonAnnotationCategoryV2(annotationCategoryData)
        )

        viewModel.getAnnotationCategory("", "11090")
        val result = viewModel.annotationCategoryData.getOrAwaitValue(time = 3)

        coVerify {
            annotationCategoryUseCase.executeOnBackground()
        }

        Assert.assertTrue(result is Success)

        if (result is Success) {
            viewModel.updateSpecificationByAnnotationCategory(result.data)
            val specificationText = viewModel.specificationText.getOrAwaitValue()
            Assert.assertEquals("Indomie, Bawang", specificationText)
        }
    }

    @Test
    fun `getAnnotationCategory should return simplified specification data when having more than 5 specification`() = coroutineTestRule.runBlockingTest {
        val annotationCategoryData = listOf(
                AnnotationCategoryData(
                        variant = "Merek",
                        data = listOf(
                                Values(1, "Indomie", true, ""),
                                Values(1, "Seedap", false, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa1",
                        data = listOf(
                                Values(1, "Soto1", false, ""),
                                Values(1, "Bawang1", true, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa2",
                        data = listOf(
                                Values(1, "Soto2", false, ""),
                                Values(1, "Bawang2", true, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa3",
                        data = listOf(
                                Values(1, "Soto3", false, ""),
                                Values(1, "Bawang3", true, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa4",
                        data = listOf(
                                Values(1, "Soto4", false, ""),
                                Values(1, "Bawang4", true, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa5",
                        data = listOf(
                                Values(1, "Soto5", false, ""),
                                Values(1, "Bawang5", true, ""))
                )
        )

        coEvery {
            annotationCategoryUseCase.executeOnBackground()
        } returns AnnotationCategoryResponse(
                DrogonAnnotationCategoryV2(annotationCategoryData)
        )

        every { provider.getProductSpecificationCounter(any()) } returns ", +1 lainnya"

        viewModel.getAnnotationCategory("", "11090")
        val result = viewModel.annotationCategoryData.getOrAwaitValue(time = 3)

        coVerify {
            annotationCategoryUseCase.executeOnBackground()
        }

        Assert.assertTrue(result is Success)

        if (result is Success) {
            viewModel.updateSpecificationByAnnotationCategory(result.data)
            val specificationText = viewModel.specificationText.getOrAwaitValue()
            Assert.assertEquals("Indomie, Bawang1, Bawang2, Bawang3, Bawang4, +1 lainnya", specificationText)
        }
    }

    @Test
    fun `updateSpecificationByAnnotationCategory should return empty when annotation category is not selected`() = runBlocking {
        val annotationCategoryData = listOf(
                AnnotationCategoryData(
                        variant = "Merek",
                        data = listOf(
                                Values(1, "Indomie", false, ""),
                                Values(1, "Seedap", false, ""))
                )
        )
        viewModel.updateSpecificationByAnnotationCategory(annotationCategoryData)
        val specificationText = viewModel.specificationText.getOrAwaitValue()
        Assert.assertTrue(specificationText.isEmpty())
    }

    @Test
    fun `when is not shop admin or not shop owner, stock message should be empty`() {
        every { userSession.isShopAdmin } returns false
        every { userSession.isShopOwner } returns false

        viewModel.setupMultiLocationShopValues()

        assert(stockAllocationDefaultMessage.isEmpty())
        assert(viewModel.productStockMessage.isEmpty())
    }

    @Test
    fun `when either is shop admin or shop owner and doesn't have multi location shop, stock message should be empty`() {
        every { userSession.isShopAdmin } returns true
        every { userSession.isShopOwner } returns false
        every { userSession.isMultiLocationShop } returns false

        viewModel.setupMultiLocationShopValues()

        assert(stockAllocationDefaultMessage.isEmpty())
        assert(viewModel.productStockMessage.isEmpty())
    }

    @Test
    fun `when either is shop admin or shop owner and has multi location shop, but not is editing or adding, stock message should be empty`() {
        every { userSession.isShopAdmin } returns false
        every { userSession.isShopOwner } returns true
        every { userSession.isMultiLocationShop } returns true

        viewModel.isAdding = false
        viewModel.isEditing = false
        viewModel.setupMultiLocationShopValues()

        assert(stockAllocationDefaultMessage.isEmpty())
        assert(viewModel.productStockMessage.isEmpty())
    }

    @Test
    fun `when either is shop admin or shop owner, has multi location shop, and is editing, stock message should be present`() {
        val editMessage = "edit"
        every { userSession.isShopAdmin } returns false
        every { userSession.isShopOwner } returns true
        every { userSession.isMultiLocationShop } returns true
        every { provider.getEditProductMultiLocationMessage() } returns editMessage

        viewModel.isEditing = true
        viewModel.setupMultiLocationShopValues()

        assert(stockAllocationDefaultMessage == editMessage)
        assert(viewModel.productStockMessage == editMessage)
    }

    @Test
    fun `when either is shop admin or shop owner, has multi location shop, and is adding, but is not editing, stock message should be present`() {
        val addMessage = "add"
        every { userSession.isShopAdmin } returns false
        every { userSession.isShopOwner } returns true
        every { userSession.isMultiLocationShop } returns true
        every { provider.getAddProductMultiLocationMessage() } returns addMessage

        viewModel.isEditing = false
        viewModel.isAdding = true
        viewModel.setupMultiLocationShopValues()

        assert(stockAllocationDefaultMessage == addMessage)
        assert(viewModel.productStockMessage == addMessage)
    }

    @Test
    fun `when either is shop admin or shop owner and not has multi location shop, stock message should be empty`() {
        every { userSession.isShopAdmin } returns true
        every { userSession.isShopOwner } returns false

        assert(stockAllocationDefaultMessage.isEmpty())
    }

    @Test
    fun `getProductPriceRecommendation should return unfilled data when productId is not provided`() = coroutineTestRule.runBlockingTest {
        coEvery {
            productPriceSuggestionSuggestedPriceGetUseCase.executeOnBackground()
                    .priceSuggestionSuggestedPriceGet
        } returns PriceSuggestionSuggestedPriceGet(suggestedPrice = 1000.0)

        viewModel.getProductPriceRecommendation()
        val result = viewModel.productPriceRecommendation.getOrAwaitValue()

        coVerify {
            productPriceSuggestionSuggestedPriceGetUseCase.executeOnBackground()
        }

        assertEquals(1000.0, result.suggestedPrice)
    }

    @Test
    fun `updateProductShowCases should change productShowCases value`() {
        viewModel.updateProductShowCases(ArrayList())
        assert(viewModel.productShowCases.isEmpty())

        viewModel.productShowCases = arrayListOf(ShowcaseItemPicker())
        assert(viewModel.productShowCases.isNotEmpty())
    }

    @Test
    fun `setProductNameInput should change productNameInputLiveData value`() {
        viewModel.setProductNameInput("A")
        assert(viewModel.isProductNameChanged)

        val mProductNameInputLiveData = viewModel
                .getPrivateProperty<AddEditProductDetailViewModel, MutableLiveData<String>>("mProductNameInputLiveData")
        assert(mProductNameInputLiveData?.value.orEmpty().isNotEmpty())
    }

    @Test
    fun `when changing view model property should change as expected value`() {
        viewModel.shouldUpdateVariant = true
        viewModel.isDrafting = true
        viewModel.isReloadingShowCase = true
        viewModel.isProductNameChanged = true
        assert(viewModel.shouldUpdateVariant)
        assert(viewModel.isDrafting)
        assert(viewModel.isReloadingShowCase)
        assert(viewModel.isProductNameChanged)

        viewModel.shouldUpdateVariant = false
        viewModel.isDrafting = false
        viewModel.isReloadingShowCase = false
        viewModel.isProductNameChanged = false
        assertFalse(viewModel.shouldUpdateVariant)
        assertFalse(viewModel.isDrafting)
        assertFalse(viewModel.isReloadingShowCase)
        assertFalse(viewModel.isProductNameChanged)

        viewModel.productPhotoPaths = mutableListOf("sss")
        assert(viewModel.productPhotoPaths[0] == "sss")

        viewModel.specificationList = mutableListOf(SpecificationInputModel(id="123"))
        assert(viewModel.specificationList[0].id == "123")

        viewModel.productInputModel = ProductInputModel(productId = 11L)
        assert(viewModel.productInputModel.productId == 11L)

        viewModel.productPriceMessage = "x"
        viewModel.isWholeSalePriceActivated = MutableLiveData<Boolean>(true)
        viewModel.wholeSaleErrorCounter = MutableLiveData(10)
        viewModel.isTheLastOfWholeSale = MutableLiveData<Boolean>(true)
        viewModel.productStockMessage = "x"
        viewModel.orderQuantityMessage = "x"
        viewModel.isPreOrderActivated = MutableLiveData<Boolean>(true)
        viewModel.preOrderDurationMessage = "x"
        assert(viewModel.provider != null)
        assert(viewModel.productPriceMessage.isNotEmpty())
        assert(viewModel.isWholeSalePriceActivated.value == true)
        assert(viewModel.wholeSaleErrorCounter.value == 10)
        assert(viewModel.isTheLastOfWholeSale.value == true)
        assert(viewModel.productStockMessage.isNotEmpty())
        assert(viewModel.orderQuantityMessage.isNotEmpty())
        assert(viewModel.isPreOrderActivated.value == true)
        assert(viewModel.preOrderDurationMessage.isNotEmpty())
    }

    private fun getSampleProductPhotos(): List<PictureInputModel> {
        return listOf(
                PictureInputModel(picID = "1", urlOriginal = "url 1", urlThumbnail = "thumb 1", url300 = "300 1"),
                PictureInputModel(picID = "2", urlOriginal = "url 2", urlThumbnail = "thumb 2", url300 = "300 2")
        )
    }

    private fun getPrivateField(owner: Any, name: String): Any? {
        return owner::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(owner)
        }
    }

    private fun <T: Any> runValidationAndProvideMessage(provider: KFunction0<String?>, value: String, funcToCall: () -> T): T {
        every { provider() } returns value
        val result = funcToCall.invoke()
        verify { provider() }
        return result
    }
}