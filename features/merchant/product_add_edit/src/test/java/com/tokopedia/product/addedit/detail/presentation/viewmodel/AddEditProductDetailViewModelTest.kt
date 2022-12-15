package com.tokopedia.product.addedit.detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.DOUBLE_ZERO
import com.tokopedia.product.addedit.common.constant.ProductStatus
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.domain.model.*
import com.tokopedia.product.addedit.detail.domain.usecase.*
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_MIN_ORDER_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_WHOLESALE_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_MIN_ORDER_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.model.*
import com.tokopedia.product.addedit.preview.data.model.responses.ValidateProductNameResponse
import com.tokopedia.product.addedit.preview.domain.usecase.ValidateProductNameUseCase
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryResponse
import com.tokopedia.product.addedit.specification.domain.model.DrogonAnnotationCategoryV2
import com.tokopedia.product.addedit.specification.domain.model.Values
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import com.tokopedia.product.addedit.specification.presentation.constant.AddEditProductSpecificationConstants.SIGNAL_STATUS_VARIANT
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.util.callPrivateFunc
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.util.getPrivateProperty
import com.tokopedia.product.addedit.util.setPrivateProperty
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.shop.common.constant.ShopStatusLevelDef
import com.tokopedia.shop.common.constant.ShopStatusLevelDef.Companion.LEVEL_GOLD
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.shop.common.data.source.cloud.model.MaxStockThresholdResponse
import com.tokopedia.shop.common.data.source.cloud.model.MaxStockThresholdResponse.GetIMSMeta
import com.tokopedia.shop.common.data.source.cloud.model.MaxStockThresholdResponse.GetIMSMeta.Data
import com.tokopedia.shop.common.domain.interactor.GetMaxStockThresholdUseCase
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import java.io.IOException
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1

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
    lateinit var validateProductNameUseCase: ValidateProductNameUseCase

    @RelaxedMockK
    lateinit var getShopEtalaseUseCase: GetShopEtalaseUseCase

    @RelaxedMockK
    lateinit var annotationCategoryUseCase: AnnotationCategoryUseCase

    @RelaxedMockK
    lateinit var getAddProductPriceSuggestionUseCase: GetAddProductPriceSuggestionUseCase

    @RelaxedMockK
    lateinit var getEditProductPriceSuggestionUseCase: PriceSuggestionSuggestedPriceGetUseCase

    @RelaxedMockK
    lateinit var getProductTitleValidationUseCase: GetProductTitleValidationUseCase

    @RelaxedMockK
    lateinit var getMaxStockThresholdUseCase: GetMaxStockThresholdUseCase

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GetShopInfoUseCase

    @RelaxedMockK
    lateinit var getDefaultCommissionRulesUseCase: GetDefaultCommissionRulesUseCase

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

    @Suppress("UNCHECKED_CAST")
    private val mProductPriceRecommendation: MutableLiveData<PriceSuggestionSuggestedPriceGet> by lazy {
        getPrivateField(viewModel, "mProductPriceRecommendation") as MutableLiveData<PriceSuggestionSuggestedPriceGet>
    }

    @Suppress("UNCHECKED_CAST")
    private val mAddProductPriceSuggestion: MutableLiveData<PriceSuggestionByKeyword> by lazy {
        getPrivateField(viewModel, "mAddProductPriceSuggestion") as MutableLiveData<PriceSuggestionByKeyword>
    }

    private val viewModel: AddEditProductDetailViewModel by lazy {
        AddEditProductDetailViewModel(provider, CoroutineTestDispatchersProvider,
                getNameRecommendationUseCase, getCategoryRecommendationUseCase,
                validateProductUseCase,
                validateProductNameUseCase,
                getShopEtalaseUseCase,
                annotationCategoryUseCase,
                getAddProductPriceSuggestionUseCase,
                getEditProductPriceSuggestionUseCase,
                getProductTitleValidationUseCase,
                getMaxStockThresholdUseCase,
                getShopInfoUseCase,
                getDefaultCommissionRulesUseCase,
                userSession)
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
        val resultViewmodel = viewModel.productNameRecommendations.getOrAwaitValue()

        coVerify {
            getNameRecommendationUseCase.executeOnBackground()
        }

        Assert.assertTrue(resultViewmodel == Success(resultNameRecommendation))
    }

    @Test
    fun `failed get name recommendation`() = coroutineTestRule.runBlockingTest {
        coEvery {
            getNameRecommendationUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getProductNameRecommendation(query = "baju")
        val result = viewModel.productNameRecommendations.getOrAwaitValue()

        coVerify {
            getNameRecommendationUseCase.executeOnBackground()
        }

        Assert.assertTrue(result is Fail)
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
    fun `isInputValid should return true if wholesale params is valid`() {
        var isValid = false

        isValid = getIsTheLastOfWholeSaleTestResult(true, false)
        Assert.assertTrue(isValid)

        isValid = getIsTheLastOfWholeSaleTestResult(true, true)
        Assert.assertFalse(isValid)

        isValid = getIsTheLastOfWholeSaleTestResult(false, false)
        Assert.assertFalse(isValid)

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
    fun `validateProductNameInput should invalid when product name is blacklisted`() = coroutineTestRule.runBlockingTest {
        val errorMessageBlacklist = "error blacklist"
        val errorMessageTypo = "error typo"
        val errorMessageNegative = "error blacklist"

        getValidateProductNameInputTestResult(
            true,
            false,
            false,
            errorMessageBlacklist,
            errorMessageTypo,
            errorMessageNegative
        )
        Assert.assertEquals(errorMessageBlacklist, viewModel.productNameMessage)

        getValidateProductNameInputTestResult(
            false,
            true,
            false,
            errorMessageBlacklist,
            errorMessageTypo,
            errorMessageNegative
        )
        Assert.assertEquals(errorMessageTypo, viewModel.productNameMessage)

        getValidateProductNameInputTestResult(
            false,
            false,
            true,
            errorMessageBlacklist,
            errorMessageTypo,
            errorMessageNegative
        )
        Assert.assertEquals(errorMessageNegative, viewModel.productNameMessage)
    }

    @Test
    fun `validateProductNameInput should valid when product name no error`() = coroutineTestRule.runBlockingTest {
        val productNameInput = "indomilk"

        coEvery {
            getProductTitleValidationUseCase.getDataModelOnBackground()
        } returns TitleValidationModel()

        viewModel.validateProductNameInput(productNameInput)

        coVerify {
            getProductTitleValidationUseCase.getDataModelOnBackground()
        }

        var isError = viewModel.isProductNameInputError.getOrAwaitValue()
        Assert.assertFalse(isError)

        // test equal product name with the saved one
        viewModel.productInputModel.detailInputModel.currentProductName = productNameInput
        viewModel.validateProductNameInput(productNameInput)
        isError = viewModel.isProductNameInputError.getOrAwaitValue()
        Assert.assertFalse(isError)
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
    fun `validateProductWholeSaleQuasdsaoleSaleQuantityInput`() {
        val stringResErrorMessage = "Jasd"

        val errorMessage = runValidationAndProvideMessage(
            provider::getWholeSaleMaxErrorMessage,
            MAX_WHOLESALE_QUANTITY,
            stringResErrorMessage
        ) {
            viewModel.validateProductWholeSaleQuantityInput(MAX_WHOLESALE_QUANTITY.toString(), "", "5")
        }

        Assert.assertTrue(errorMessage == stringResErrorMessage)
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
    fun `getMaxStockThreshold and validateProductStockInput should be successful in getting the threshold value and getting an error`() {
        /*
         * Init Data:
         * 1. stringResErrorMessage is an error message will be shown provided that current stock more than the maximum stock
         * 2. expectedMaxStockThreshold is max stock threshold which we need to use to compare the expected and actual threshold
         * 3. stockInput is current stock, we need current more than expectedMaxStockThreshold for this test case
         */
        val stringResErrorMessage = "Stok melebihi batas maks. 100.000"
        val expectedMaxStockThreshold = "100000"
        val stockInput = "200000"

        // create stub
        coEvery {
            getMaxStockThresholdUseCase.execute(anyString())
        } returns MaxStockThresholdResponse(getIMSMeta = GetIMSMeta(
                data = Data(
                    maxStockThreshold = expectedMaxStockThreshold
                ),
                header = Header()
            )
        )

        // create stub
        every {
            provider.getMaxLimitProductStockErrorMessage(expectedMaxStockThreshold)
        } returns stringResErrorMessage

        // fetch the threshold
        viewModel.getMaxStockThreshold(anyString())

        // need to wait the response of threshold because the validation using maxStockThreshold value inside of the function validation
        val actualMaxStockThreshold = viewModel.maxStockThreshold.getOrAwaitValue()

        // validate product stock input
        viewModel.validateProductStockInput(stockInput)

        // wait until isProductStockInputError gets the value
        val isError = viewModel.isProductStockInputError.getOrAwaitValue()

        /*
         * Expected Result:
         * 1. Max stock threshold equals to the actual one
         * 2. Error and the error message is not blank
         * 3. Error message equals to the actual one
         */
        Assert.assertEquals(expectedMaxStockThreshold, actualMaxStockThreshold)
        Assert.assertTrue(isError && viewModel.productStockMessage.isNotBlank())
        Assert.assertEquals(stringResErrorMessage, viewModel.productStockMessage)
    }

    @Test
    fun `getMaxStockThreshold and validateProductStockInput should be successful in getting the threshold value and not getting an error`() {
        /*
         * Init Data:
         * 1. stringResErrorMessage is an error message will be shown provided that current stock more than the maximum stock
         * 2. expectedMaxStockThreshold is max stock threshold which we need to use to compare the expected and actual threshold
         * 3. stockInput is current stock, we need current stock less than expectedMaxStockThreshold for this test case
         */
        val stringResErrorMessage = "Stok melebihi batas maks. 100.000"
        val expectedMaxStockThreshold = "100000"
        val stockInput = "212"

        // create stub
        coEvery {
            getMaxStockThresholdUseCase.execute(anyString())
        } returns MaxStockThresholdResponse(getIMSMeta = GetIMSMeta(
                data = Data(
                    maxStockThreshold = expectedMaxStockThreshold
                ),
                header = Header()
            )
        )

        // create stub
        every {
            provider.getMaxLimitProductStockErrorMessage(expectedMaxStockThreshold)
        } returns stringResErrorMessage

        // fetch the threshold
        viewModel.getMaxStockThreshold(anyString())

        // need to wait the response of threshold because the validation using maxStockThreshold value inside of the function validation
        val actualMaxStockThreshold = viewModel.maxStockThreshold.getOrAwaitValue()

        // validate product stock input
        viewModel.validateProductStockInput(stockInput)

        // wait until isProductStockInputError gets the value
        val isError = viewModel.isProductStockInputError.getOrAwaitValue()

        /*
         * Expected Result:
         * 1. Max stock threshold equals to the actual one.
         * 2. Not error and the error message is blank
         */
        Assert.assertEquals(expectedMaxStockThreshold, actualMaxStockThreshold)
        Assert.assertTrue(!isError && viewModel.productStockMessage.isBlank())
    }

    @Test
    fun `getMaxStockThreshold and validateProductStockInput should fail in getting the threshold value and not getting an error`() {
        /*
         * Init Data:
         * 1. stringResErrorMessage is an error message will be shown provided that current stock more than the maximum stock
         * 2. expectedMaxStockThreshold is max stock threshold which we need to use to compare the expected and actual threshold
         * 3. stockInput is current stock, we need current stock more than expectedMaxStockThreshold for this test case
         */
        val stringResErrorMessage = "Stok melebihi batas maks. 100.000"
        val expectedMaxStockThreshold: String? = null
        val stockInput = "200000"

        // throw a throwable
        coEvery {
            getMaxStockThresholdUseCase.execute(anyString())
        } throws Throwable()

        // create stub
        every {
            provider.getMaxLimitProductStockErrorMessage(expectedMaxStockThreshold)
        } returns stringResErrorMessage

        // fetch the threshold
        viewModel.getMaxStockThreshold(anyString())

        // need to wait the response of threshold because the validation using maxStockThreshold value inside of the function validation
        val actualMaxStockThreshold = viewModel.maxStockThreshold.getOrAwaitValue()

        // validate product stock input
        viewModel.validateProductStockInput(stockInput)

        // wait until isProductStockInputError gets the value
        val isError = viewModel.isProductStockInputError.getOrAwaitValue()

        /*
         * Expected Result:
         * 1. Max stock threshold equals to the actual one.
         * 2. Not error and the error message is blank
         */
        Assert.assertEquals(expectedMaxStockThreshold, actualMaxStockThreshold)
        Assert.assertTrue(!isError && viewModel.productStockMessage.isBlank())
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
    fun `validateProductNameInputFromNetwork should valid when productNameValidationFromNetwork returns blank message`() = coroutineTestRule.runBlockingTest {
        coEvery {
            validateProductNameUseCase.executeOnBackground()
        } returns ValidateProductNameResponse()

        viewModel.validateProductNameInputFromNetwork("book")

        coVerify {
            validateProductNameUseCase.executeOnBackground()
        }

        val result = viewModel.productNameValidationFromNetwork.getOrAwaitValue()
        Assert.assertTrue(result is Success)
    }

    @Test
    fun `validateProductNameInputFromNetwork should valid when productNameValidationFromNetwork returns message`() = coroutineTestRule.runBlockingTest {
        val errorMessage = "Error Message"

        coEvery {
            validateProductNameUseCase.executeOnBackground()
        } returns ValidateProductNameResponse(
            productValidateV3 = com.tokopedia.product.addedit.preview.data.model.responses.ProductValidateV3(
                data = com.tokopedia.product.addedit.preview.data.model.responses.ProductValidateData(
                    validationResults = listOf(errorMessage)
                )
            )
        )

        viewModel.validateProductNameInputFromNetwork("book")

        coVerify {
            validateProductNameUseCase.executeOnBackground()
        }

        val result = viewModel.productNameValidationFromNetwork.getOrAwaitValue()
        Assert.assertTrue((result as Success).data == errorMessage)
    }

    @Test
    fun `validateProductNameInputFromNetwork should invalid when Error throws`() = coroutineTestRule.runBlockingTest {
        val errorMessage = "Error Message"

        coEvery {
            validateProductNameUseCase.executeOnBackground()
        } throws MessageErrorException(errorMessage)

        viewModel.validateProductNameInputFromNetwork("book")

        coVerify {
            validateProductNameUseCase.executeOnBackground()
        }

        val result = viewModel.productNameValidationFromNetwork.getOrAwaitValue()
        Assert.assertTrue((result as Fail).throwable.message == errorMessage)
    }

    @Test
    fun `set data to productNameInputFromNetwork and isProductNameInputError, the value should the same with the latest provided variable`()  {
        var productName: Success<String>? = null
        viewModel.setProductNameInputFromNetwork(productName)
        productName = Success("Error Message")
        viewModel.setProductNameInputFromNetwork(productName)

        var isProductNameInput = false
        viewModel.setIsProductNameInputError(isProductNameInput)
        isProductNameInput = true
        viewModel.setIsProductNameInputError(isProductNameInput)

        Assert.assertTrue(viewModel.productNameValidationFromNetwork.value == productName)
        Assert.assertTrue(viewModel.isProductNameInputError.value == isProductNameInput)
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
    fun `When validate product sku error, should log error to crashlytics`() {
        coEvery { validateProductUseCase.executeOnBackground() } throws MessageErrorException("")

        //Mock FirebaseCrashlytics because .getInstance() method is a static method
        mockkStatic(FirebaseCrashlytics::class)

        every { FirebaseCrashlytics.getInstance().recordException(any()) } returns mockk(relaxed = true)

        viewModel.validateProductSkuInput("ESKU")

        coVerify { validateProductUseCase.executeOnBackground() }

        coVerify { AddEditProductErrorHandler.logExceptionToCrashlytics(any()) }
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

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList.size == 2)
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

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList.size == sampleProductPhotos.size &&
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

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList.size == 1 &&
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
        val imagePickerResult = arrayListOf("local/path/to/editedImage1.jpg", "local/path/to/image2.0")
        val originalImageUrl = viewModel.productInputModel.detailInputModel.pictureList.map { it.urlOriginal }
        val editedStatus = arrayListOf(true, false)

        val newUpdatedPhotos = viewModel.updateProductPhotos(imagePickerResult, ArrayList(originalImageUrl), editedStatus)
        viewModel.productInputModel.detailInputModel.apply {
            pictureList = newUpdatedPhotos.pictureList
            imageUrlOrPathList = newUpdatedPhotos.imageUrlOrPathList
        }

        Assert.assertTrue(viewModel.productInputModel.detailInputModel.pictureList.size == 1 &&
                viewModel.productPhotoPaths.size == 2 &&
                viewModel.productPhotoPaths[0] == imagePickerResult[0] &&
                viewModel.productPhotoPaths[1] == sampleProductPhotos[1].urlThumbnail)
    }

    @Test
    fun `updateProductPhotos with changed imagePickerResult path then replace to latest path`() {
        val imagePickerResult = arrayListOf("local/path/to/image1x.jpg", "local/path/to/image2.jpg")
        val originalImageUrl = arrayListOf("local/path/to/image1.jpg", "local/path/to/image2.jpg")
        val editedStatus = arrayListOf(false, false)

        val newUpdatedPhotos = viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, editedStatus)
        Assert.assertTrue(newUpdatedPhotos.imageUrlOrPathList.size == 2)
    }

    @Test
    fun `updateProductPhotos with expected edit picture`(){
        //params
        val imagePickerResult = arrayListOf("a/0/tkpd/102012.jpg", "", "a/0/tkpd/102014.jpg")
        val originalImage = arrayListOf("", "a/0/tkpd/102013.jpg", "")

        //data existing
        val list = listOf(
            PictureInputModel(picID = "1", urlOriginal = "a/0/tkpd/102012.jpg", urlThumbnail = "thumb 1", url300 = "300 1"),
            PictureInputModel(picID = "2", urlOriginal = "a/0/tkpd/102014.jpg", urlThumbnail = "thumb 2", url300 = "300 2")
        )
        val sampleProductPhotos = list

        //target
        val targetImageUrlOrPathList = arrayListOf("thumb 1","a/0/tkpd/102013.jpg", "thumb 2")
        val objectTarget = DetailInputModel().apply {
            this.pictureList = list
            this.imageUrlOrPathList = targetImageUrlOrPathList
        }


        viewModel.productInputModel.detailInputModel.pictureList = sampleProductPhotos
        val actual = viewModel.updateProductPhotos(imagePickerResult, originalImage)
        assertEquals(objectTarget, actual)
    }

    @Test
    fun `updateProductPhotos with expected all new image`(){
        //params
        val imagePickerResult = arrayListOf("a/0/tkpd/102012.jpg", "", "a/tkpd/102014.jpg")
        val originalImage = arrayListOf("", "a/0/tkpd/102013.jpg", "")

        //data existing
        val list = listOf(
            PictureInputModel(picID = "1", urlOriginal = "a/0/tkpd/102020.jpg", urlThumbnail = "thumb 1", url300 = "300 1"),
            PictureInputModel(picID = "2", urlOriginal = "a/0/tkpd/102021.jpg", urlThumbnail = "thumb 2", url300 = "300 2")
        )
        val sampleProductPhotos = list

        //target
        val targetImageUrlOrPathList = arrayListOf("a/0/tkpd/102012.jpg","a/0/tkpd/102013.jpg", "a/tkpd/102014.jpg")
        val objectTarget = DetailInputModel().apply {
            this.pictureList = emptyList()
            this.imageUrlOrPathList = targetImageUrlOrPathList
        }

        viewModel.productInputModel.detailInputModel.pictureList = sampleProductPhotos
        val actual = viewModel.updateProductPhotos(imagePickerResult, originalImage)
        assertEquals(objectTarget, actual)
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
    fun `When get shop showcases error, should post error to observer`() = runBlocking {
        coEvery {
            getShopEtalaseUseCase.executeOnBackground()
        } throws IOException()

        viewModel.getShopShowCasesUseCase()

        coVerify {
            getShopEtalaseUseCase.executeOnBackground()
        }

        assert(viewModel.shopShowCases.value is Fail)
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
                                Values("1", "Indomie", true, ""),
                                Values("1", "Seedap", false, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa",
                        data = listOf(
                                Values("1", "Soto", false, ""),
                                Values("1", "Bawang", true, ""))
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
            val specificationList = viewModel.selectedSpecificationList.getOrAwaitValue()
            val specificationText = viewModel.specificationText.getOrAwaitValue()
            Assert.assertEquals("Indomie", specificationList[0].data)
            Assert.assertEquals("Bawang", specificationList[1].data)
            Assert.assertEquals("Indomie, Bawang", specificationText)
        }
    }

    @Test
    fun `getAnnotationCategory should return simplified specification data when having more than 5 specification`() = coroutineTestRule.runBlockingTest {
        val annotationCategoryData = listOf(
                AnnotationCategoryData(
                        variant = "Merek",
                        data = listOf(
                                Values("1", "Indomie", true, ""),
                                Values("1", "Seedap", false, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa1",
                        data = listOf(
                                Values("1", "Soto1", false, ""),
                                Values("1", "Bawang1", true, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa2",
                        data = listOf(
                                Values("1", "Soto2", false, ""),
                                Values("1", "Bawang2", true, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa3",
                        data = listOf(
                                Values("1", "Soto3", false, ""),
                                Values("1", "Bawang3", true, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa4",
                        data = listOf(
                                Values("1", "Soto4", false, ""),
                                Values("1", "Bawang4", true, ""))
                ),
                AnnotationCategoryData(
                        variant = "Rasa5",
                        data = listOf(
                                Values("1", "Soto5", false, ""),
                                Values("1", "Bawang5", true, ""))
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
    fun `When get annotation category error, should post error to observer`() = runBlocking {
        coEvery {
            annotationCategoryUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getAnnotationCategory("", "11090")

        val result = viewModel.annotationCategoryData.getOrAwaitValue()

        coVerify {
            annotationCategoryUseCase.executeOnBackground()
        }

        assertTrue(result is Fail)
    }

    @Test
    fun `updateSpecificationByAnnotationCategory should return empty when annotation category is not selected`() = runBlocking {
        val annotationCategoryData = listOf(
                AnnotationCategoryData(
                        variant = "Merek",
                        data = listOf(
                                Values("1", "Indomie", false, ""),
                                Values("1", "Seedap", false, ""))
                )
        )
        viewModel.updateSpecificationByAnnotationCategory(annotationCategoryData)
        val specificationText = viewModel.specificationText.getOrAwaitValue()
        Assert.assertTrue(specificationText.isEmpty())
    }

    @Test
    fun `when getMaxProductPhotos, expect correct max product picture`() {
        every { userSession.isShopOfficialStore } returns true
        var maxPicture = viewModel.getMaxProductPhotos()
        assertEquals(AddEditProductDetailConstants.MAX_PRODUCT_PHOTOS_OS, maxPicture)

        every { userSession.isShopOfficialStore } returns false
        maxPicture = viewModel.getMaxProductPhotos()
        assertEquals(AddEditProductDetailConstants.MAX_PRODUCT_PHOTOS, maxPicture)
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
        every { provider.getEditProductPriceMultiLocationMessage() } returns editMessage
        every { provider.getEditProductStockMultiLocationMessage() } returns editMessage

        viewModel.isEditing = true
        viewModel.setupMultiLocationShopValues()

        assert(viewModel.productPriceMessage == editMessage)
        assert(stockAllocationDefaultMessage == editMessage)
        assert(viewModel.productStockMessage == editMessage)
    }

    @Test
    fun `when either is shop admin or shop owner, has multi location shop, and is adding, but is not editing, stock message should be present`() {
        val addMessage = "add"
        every { userSession.isShopAdmin } returns false
        every { userSession.isShopOwner } returns true
        every { userSession.isMultiLocationShop } returns true
        every { provider.getAddProductPriceMultiLocationMessage() } returns addMessage
        every { provider.getAddProductStockMultiLocationMessage() } returns addMessage

        viewModel.isEditing = false
        viewModel.isAdding = true
        viewModel.setupMultiLocationShopValues()

        assert(viewModel.productPriceMessage == addMessage)
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
            getEditProductPriceSuggestionUseCase.executeOnBackground()
                    .priceSuggestionSuggestedPriceGet
        } returns PriceSuggestionSuggestedPriceGet(suggestedPrice = 1000.0)

        viewModel.getProductPriceRecommendation()
        val result = viewModel.productPriceRecommendation.getOrAwaitValue()

        coVerify {
            getEditProductPriceSuggestionUseCase.executeOnBackground()
        }

        assertEquals(1000.0, result.suggestedPrice)
    }

    @Test
    fun `getProductPriceRecommendation should return throwable if error happen`() = coroutineTestRule.runBlockingTest {
        val expectedErrorMessage = "error happen"
        coEvery {
            getEditProductPriceSuggestionUseCase.executeOnBackground()
                    .priceSuggestionSuggestedPriceGet
        } throws MessageErrorException(expectedErrorMessage)

        viewModel.getProductPriceRecommendation()
        val resultErrorMessage = viewModel.productPriceRecommendationError.getOrAwaitValue()

        assertEquals(expectedErrorMessage, resultErrorMessage.localizedMessage)
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

        val mProductNameInputLiveData = viewModel
                .getPrivateProperty<AddEditProductDetailViewModel, MutableLiveData<String>>("mProductNameInputLiveData")
        assert(mProductNameInputLiveData?.value.orEmpty().isNotEmpty())
    }

    @Test
    fun `when removeKeywords from product name input, should generate clean product name`() {
        val blacklistedWords = listOf("shopee", "lazada")
        val result = viewModel.removeKeywords("shopee lazada   samsung", blacklistedWords)

        assertEquals("samsung", result)
    }

    @Test
    fun `when updateHasRequiredSpecification updated with SIGNAL_STATUS_VARIANT, should update hasRequiredSpecification`() {
        val annotationData = listOf(AnnotationCategoryData(
            variant = SIGNAL_STATUS_VARIANT
        ))
        viewModel.updateHasRequiredSpecification(annotationData)
        val result = viewModel.hasRequiredSpecification.getOrAwaitValue()

        assertTrue(result)
    }

    @Test
    fun `when updateHasRequiredSpecification without SIGNAL_STATUS_VARIANT, should update hasRequiredSpecification`() {
        val annotationData = listOf(AnnotationCategoryData())
        viewModel.updateHasRequiredSpecification(annotationData)
        val result = viewModel.hasRequiredSpecification.getOrAwaitValue()

        assertFalse(result)
    }

    @Test
    fun `when validateSelectedSpecificationList, should return valid result`() {
        val annotationData = listOf(AnnotationCategoryData(
            variant = SIGNAL_STATUS_VARIANT
        ))
        val selectedSpec = listOf(SpecificationInputModel(
            specificationVariant = SIGNAL_STATUS_VARIANT
        ))

        val resultEmptyState = viewModel.validateSelectedSpecificationList()
        viewModel.updateHasRequiredSpecification(annotationData)
        viewModel.updateSelectedSpecification(selectedSpec)
        val resultFilledState = viewModel.validateSelectedSpecificationList()

        assertTrue(resultEmptyState)
        assertTrue(resultFilledState)
    }

    @Test
    fun `when changing view model property should change as expected value`() {
        viewModel.isWholeSalePriceActivated.value = null
        viewModel.isPreOrderActivated.value = null
        assert(viewModel.isWholeSalePriceActivated.value == null)
        assert(viewModel.isPreOrderActivated.value == null)

        viewModel.isAdding = true
        viewModel.isFirstMoved = true
        var isValid = viewModel.callPrivateFunc("isInputValid") as Boolean
        Assert.assertFalse(isValid)

        viewModel.isAdding = true
        viewModel.isFirstMoved = false
        isValid = viewModel.callPrivateFunc("isInputValid") as Boolean
        Assert.assertTrue(isValid)

        viewModel.isDrafting = true
        viewModel.isReloadingShowCase = true
        assert(viewModel.isDrafting)
        assert(viewModel.isReloadingShowCase)

        viewModel.isDrafting = false
        viewModel.isReloadingShowCase = false
        assertFalse(viewModel.isDrafting)
        assertFalse(viewModel.isReloadingShowCase)

        viewModel.productPhotoPaths = mutableListOf("sss")
        assert(viewModel.productPhotoPaths[0] == "sss")

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

    @Test
    fun `when provider is null expect default return`() {
        runValidationAndProvideMessage(provider::getEmptyProductNameErrorMessage, null) {
            viewModel.validateProductNameInput("")
        }
        runValidationAndProvideMessage(provider::getProductNameTips, null) {
            viewModel.validateProductNameInput("toped")
        }

        runValidationAndProvideMessage(provider::getEmptyWholeSaleQuantityErrorMessage, null) {
            viewModel.validateProductWholeSaleQuantityInput("", "", "")
        }
        runValidationAndProvideMessage(provider::getZeroWholeSaleQuantityErrorMessage, null) {
            viewModel.validateProductWholeSaleQuantityInput("", "", "")
        }
        runValidationAndProvideMessage(provider::getMinLimitWholeSaleQuantityErrorMessage, null) {
            viewModel.validateProductWholeSaleQuantityInput("1", "10", "")
        }
        runValidationAndProvideMessage(provider::getPrevInputWholeSaleQuantityErrorMessage, null) {
            viewModel.validateProductWholeSaleQuantityInput("", "", "9999")
        }

        runValidationAndProvideMessage(provider::getEmptyWholeSalePriceErrorMessage, null) {
            viewModel.validateProductWholeSalePriceInput("", "", "")
        }
        runValidationAndProvideMessage(provider::getZeroWholeSalePriceErrorMessage, null) {
            viewModel.validateProductWholeSalePriceInput("0", "", "")
        }
        runValidationAndProvideMessage(provider::getWholeSalePriceTooExpensiveErrorMessage, null) {
            viewModel.validateProductWholeSalePriceInput("10", "1", "")
        }
        runValidationAndProvideMessage(provider::getPrevInputWholeSalePriceErrorMessage, null) {
            viewModel.validateProductWholeSalePriceInput("1", "10", "-1")
        }
        runValidationAndProvideMessage(provider::getPrevInputWholeSalePriceErrorMessage, null) {
            viewModel.validateProductWholeSalePriceInput("-1", "10", "1")
        }
        runValidationAndProvideMessage(provider::getEmptyOrderQuantityErrorMessage, null) {
            viewModel.validateProductMinOrderInput("", "")
        }
        runValidationAndProvideMessage(provider::getEmptyOrderQuantityErrorMessage, null) {
            viewModel.validateProductMinOrderInput("", (MIN_MIN_ORDER_QUANTITY-1).toString())
        }
        runValidationAndProvideMessage(provider::getMinOrderExceedLimitQuantityErrorMessage, null) {
            viewModel.validateProductMinOrderInput("", (MAX_MIN_ORDER_QUANTITY+1).toString())
        }
        runValidationAndProvideMessage(provider::getEmptyOrderQuantityErrorMessage, null) {
            viewModel.validateProductMinOrderInput("2", "2")
        }
        runValidationAndProvideMessage(provider::getMinOrderExceedStockErrorMessage, null) {
            viewModel.validateProductMinOrderInput("${MIN_MIN_ORDER_QUANTITY - 1}", "$MIN_MIN_ORDER_QUANTITY")
        }
        runValidationAndProvideMessage(provider::getEmptyOrderQuantityErrorMessage, null) {
            viewModel.isEditing = true
            viewModel.setPrivateProperty("isMultiLocationShop", true)
            viewModel.validateProductMinOrderInput("0", "2")
        }

        runValidationAndProvideMessage(provider::getEmptyPreorderDurationErrorMessage, null) {
            viewModel.validatePreOrderDurationInput(0, "")
        }
        runValidationAndProvideMessage(provider::getMinLimitPreorderDurationErrorMessage, null) {
            viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_DAY, "${AddEditProductDetailConstants.MIN_PREORDER_DURATION-1}")
        }
        runValidationAndProvideMessage(provider::getMaxDaysLimitPreorderDuratioErrorMessage, null) {
            viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_DAY, "${AddEditProductDetailConstants.MAX_PREORDER_DAYS + 1}")
        }
        runValidationAndProvideMessage(provider::getMaxWeeksLimitPreorderDuratioErrorMessage, null) {
            viewModel.validatePreOrderDurationInput(AddEditProductDetailConstants.UNIT_WEEK, "${AddEditProductDetailConstants.MAX_PREORDER_WEEKS + 1}")
        }

        runValidationAndProvideMessage(provider::getEditProductStockMultiLocationMessage, null) {
            viewModel.isEditing = true
            viewModel.isAdding = false
            viewModel.callPrivateFunc("getMultiLocationStockAllocationMessage") as String
        }
        runValidationAndProvideMessage(provider::getAddProductStockMultiLocationMessage, null) {
            viewModel.isEditing = false
            viewModel.isAdding = true
            viewModel.callPrivateFunc("getMultiLocationStockAllocationMessage") as String
        }
    }

    @Test
    fun `getAddProductPriceSuggestion should return expected data when request params is provided`() = coroutineTestRule.runBlockingTest {
        coEvery {
            getAddProductPriceSuggestionUseCase.executeOnBackground()
                    .priceSuggestionByKeyword
        } returns PriceSuggestionByKeyword(
                summary = PriceSuggestionSuggestedPriceByKeywordSummary(
                        suggestedPrice = 8000.0,
                        suggestedPriceMin = 6000.0,
                        suggestedPriceMax = 10000.0
                )
        )

        viewModel.getAddProductPriceSuggestion("keyword","categoryL3")
        val result = viewModel.addProductPriceSuggestion.getOrAwaitValue()

        coVerify {
            getAddProductPriceSuggestionUseCase.executeOnBackground()
        }

        assertEquals(8000.0, result.summary?.suggestedPrice)
    }

    @Test
    fun `getAddProductPriceSuggestion should return throwable if error happen`() = coroutineTestRule.runBlockingTest {
        val expectedErrorMessage = "error happen"
        coEvery {
            getAddProductPriceSuggestionUseCase.executeOnBackground()
                    .priceSuggestionByKeyword
        } throws MessageErrorException(expectedErrorMessage)

        viewModel.getAddProductPriceSuggestion("keyword","categoryL3")
        val resultErrorMessage = viewModel.addProductPriceSuggestionError.getOrAwaitValue()

        assertEquals(expectedErrorMessage, resultErrorMessage.localizedMessage)
    }

    @Test
    fun `when the list of similar product from response is empty the expect list of similar product to be empty`() {
        val testData = PriceSuggestionByKeyword(
                summary = PriceSuggestionSuggestedPriceByKeywordSummary(
                        suggestedPrice = 8000.0,
                        suggestedPriceMin = 6000.0,
                        suggestedPriceMax = 10000.0
                ), suggestions = listOf()
        )
        val actualResult = viewModel.mapAddPriceSuggestionToPriceSuggestionUiModel(testData)
        assertTrue(actualResult.similarProducts.isEmpty())
    }

    @Test
    fun `when price suggestion data is null expect default values and empty list of similar product`() {
        val testData = PriceSuggestionByKeyword(summary = null, suggestions = null)
        val actualResult = viewModel.mapAddPriceSuggestionToPriceSuggestionUiModel(testData)
        assertTrue(actualResult.similarProducts.isEmpty())
    }

    @Test
    fun `when mapping add price suggestion model to ui model expect legit result`() {
        val testData = PriceSuggestionByKeyword(
                summary = PriceSuggestionSuggestedPriceByKeywordSummary(
                        suggestedPrice = 8000.0,
                        suggestedPriceMin = 6000.0,
                        suggestedPriceMax = 10000.0
                ),
                suggestions = listOf(
                        PriceSuggestionSuggestedPriceGetResponseV2(
                                productId = "productId",
                                displayPrice = 2000.0,
                                imageURL = "imageUrl",
                                totalSold = 10,
                                rating = 4.5
                        )
                )
        )
        val expectedResult = PriceSuggestion(
                suggestedPrice = 8000.0,
                suggestedPriceMin = 6000.0,
                suggestedPriceMax = 10000.0,
                similarProducts = listOf(
                        SimilarProduct(
                                productId = "productId",
                                displayPrice = 2000.0,
                                imageURL = "imageUrl",
                                totalSold = 10,
                                rating = 4.5
                        )
                )
        )
        val actualResult = viewModel.mapAddPriceSuggestionToPriceSuggestionUiModel(testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when mapping edit price suggestion to ui model expect legit result`() {
        val testData = PriceSuggestionSuggestedPriceGet(
                suggestedPrice = 8000.0,
                suggestedPriceMin = 6000.0,
                suggestedPriceMax = 10000.0,
                price = 2000.0,
                title = "title",
                productRecommendation = listOf(
                        ProductRecommendationResponse(
                                productID = "productId",
                                price = 2000.0,
                                imageURL = "imageUrl",
                                sold = 10,
                                rating = 4.5,
                                title = "title"
                        )
                )
        )
        val expectedResult = PriceSuggestion(
                suggestedPrice = 8000.0,
                suggestedPriceMin = 6000.0,
                suggestedPriceMax = 10000.0,
                similarProducts = listOf(
                        SimilarProduct(
                                productId = "productId",
                                displayPrice = 2000.0,
                                imageURL = "imageUrl",
                                title = "title",
                                totalSold = 10,
                                rating = 4.5
                        )
                )
        )
        val actualResult = viewModel.mapEditPriceSuggestionToPriceSuggestionUiModel(testData)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when the price range is null expect the price suggestion range to be pair of zeroes`() {
        mProductPriceRecommendation.value = null
        mAddProductPriceSuggestion.value = null
        val expectedResult = DOUBLE_ZERO to DOUBLE_ZERO
        val firstActualResult = viewModel.getProductPriceSuggestionRange(true)
        val secondActualResult = viewModel.getProductPriceSuggestionRange(false)
        assertEquals(expectedResult, firstActualResult)
        assertEquals(expectedResult, secondActualResult)
    }

    @Test
    fun `when editing product expect the price suggestion range compiled from productPriceRecommendation`() {
        mProductPriceRecommendation.value = PriceSuggestionSuggestedPriceGet(
                suggestedPriceMin = 10000.0,
                suggestedPriceMax = 20000.0
        )
        val expectedResult = 10000.0 to 20000.0
        val actualResult = viewModel.getProductPriceSuggestionRange(true)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when adding product expect the price suggestion range compiled from addProductPriceSuggestion`() {
        mAddProductPriceSuggestion.value = PriceSuggestionByKeyword(
                summary = PriceSuggestionSuggestedPriceByKeywordSummary(
                        suggestedPriceMin = 10000.0,
                        suggestedPriceMax = 20000.0
                )
        )
        val expectedResult = 10000.0 to 20000.0
        val actualResult = viewModel.getProductPriceSuggestionRange(false)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when the price input is below the price suggestion range expect is competitive true`() {
        val priceSuggestionRange = 5000.0 to 10000.0
        val productPrice = 2000.0
        val isCompetitive = viewModel.isProductPriceCompetitive(productPrice, priceSuggestionRange)
        assertTrue(isCompetitive)
    }

    @Test
    fun `when the price input is error expect isCompetitive false`() {
        val priceSuggestionRange = 5000.0 to 10000.0
        val productPrice = 6000.0
        val isError = true
        val isCompetitive = viewModel.isProductPriceCompetitive(productPrice, priceSuggestionRange, isError)
        assertFalse(isCompetitive)
    }

    @Test
    fun `when the price input below the price suggestion range expect isCompetitive true`() {
        val priceSuggestionRange = 5000.0 to 10000.0
        val productPrice = 4000.0
        val isCompetitive = viewModel.isProductPriceCompetitive(productPrice, priceSuggestionRange)
        assertTrue(isCompetitive)
    }

    @Test
    fun `when the price input within the price suggestion range expect isCompetitive true`() {
        val priceSuggestionRange = 5000.0 to 10000.0
        val productPrice = 6000.0
        val isCompetitive = viewModel.isProductPriceCompetitive(productPrice, priceSuggestionRange)
        assertTrue(isCompetitive)
    }

    @Test
    fun `when the price input is over the price suggestion range expect isCompetitive false`() {
        val priceSuggestionRange = 5000.0 to 10000.0
        val productPrice = 11000.0
        val isCompetitive = viewModel.isProductPriceCompetitive(productPrice, priceSuggestionRange)
        assertFalse(isCompetitive)
    }

    @Test
    fun `when product is active, new , and price suggestion is not empty expect price suggestion layout visible`() {
        val isVisible = viewModel.isPriceSuggestionLayoutVisible(
                isRangeEmpty = false,
                productStatus = ProductStatus.STATUS_ACTIVE,
                isNew = true,
                hasVariant = false
        )
        assertTrue(isVisible)
    }

    @Test
    fun `when product price suggestion is empty expect hidden price suggestion layout `() {
        val isVisible = viewModel.isPriceSuggestionLayoutVisible(
                isRangeEmpty = true,
                productStatus = ProductStatus.STATUS_ACTIVE,
                isNew = true,
                hasVariant = false
        )
        assertFalse(isVisible)
    }

    @Test
    fun `when product is not active expect hidden price suggestion layout `() {
        val isVisible = viewModel.isPriceSuggestionLayoutVisible(
                isRangeEmpty = false,
                productStatus = ProductStatus.STATUS_INACTIVE,
                isNew = true,
                hasVariant = false
        )
        assertFalse(isVisible)
    }

    @Test
    fun `when product is second hand expect hidden price suggestion layout`() {
        val isVisible = viewModel.isPriceSuggestionLayoutVisible(
                isRangeEmpty = false,
                productStatus = ProductStatus.STATUS_ACTIVE,
                isNew = false,
                hasVariant = false
        )
        assertFalse(isVisible)
    }

    @Test
    fun `when product has variant expect hidden price suggestion layout`() {
        val isVisible = viewModel.isPriceSuggestionLayoutVisible(
                isRangeEmpty = false,
                productStatus = ProductStatus.STATUS_ACTIVE,
                isNew = true,
                hasVariant = true
        )
        assertFalse(isVisible)
    }

    @Test
    fun `when price suggestion max and min limit is zero expect isPriceSuggestionRangeIsEmpty true`() {
        val isRangeEmpty = viewModel.isPriceSuggestionRangeIsEmpty(DOUBLE_ZERO, DOUBLE_ZERO)
        assertTrue(isRangeEmpty)
    }

    @Test
    fun `when price suggestion min limit is not zero expect isPriceSuggestionRangeIsEmpty false`() {
        val isRangeEmpty = viewModel.isPriceSuggestionRangeIsEmpty(1000.0, DOUBLE_ZERO)
        assertFalse(isRangeEmpty)
    }

    @Test
    fun `when price suggestion max limit is not zero expect isPriceSuggestionRangeIsEmpty false`() {
        val isRangeEmpty = viewModel.isPriceSuggestionRangeIsEmpty(DOUBLE_ZERO, 1000.0)
        assertFalse(isRangeEmpty)
    }

    @Test
    fun `when successful transactions is less than 100 and shop type is regular merchant expect no service fee`() {
        val isFreeOfServiceFee = viewModel.isFreeOfServiceFee(99, ShopStatusLevelDef.LEVEL_REGULAR)
        assertTrue(isFreeOfServiceFee)
    }

    @Test
    fun `when successful transactions is less than 100 and shop type is official store expect no service fee`() {
        val isFreeOfServiceFee = viewModel.isFreeOfServiceFee(99, ShopStatusLevelDef.LEVEL_OFFICIAL_STORE)
        assertTrue(isFreeOfServiceFee)
    }

    @Test
    fun `when successful transactions is 100 and shop type is regular merchant expect no service fee`() {
        val isFreeOfServiceFee = viewModel.isFreeOfServiceFee(100, ShopStatusLevelDef.LEVEL_REGULAR)
        assertTrue(isFreeOfServiceFee)
    }

    @Test
    fun `when successful transactions is more than 100 and shop type is regular merchant expect service fee`() {
        val isFreeOfServiceFee = viewModel.isFreeOfServiceFee(101, ShopStatusLevelDef.LEVEL_REGULAR)
        assertFalse(isFreeOfServiceFee)
    }

    @Test
    fun `when successful transactions is 100 and shop type is not regular merchant expect service fee`() {
        val isFreeOfServiceFee = viewModel.isFreeOfServiceFee(100, LEVEL_GOLD)
        assertFalse(isFreeOfServiceFee)
    }

    @Test
    fun `when shop type is not regular merchant expect legit commission rate from commission rule`() {
        val commissionRules = listOf(
            CommissionRule(shopType = 1, commissionRate = 2.5),
            CommissionRule(shopType = 2, commissionRate = 3.5)
        )
        val shopType = 1
        val expectedResult = 2.5
        val actualResult = viewModel.getCommissionRate(commissionRules, shopType)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when commission rules is empty expect zero commission rate`() {
        val commissionRules = listOf<CommissionRule>()
        val shopType = 1
        val expectedResult = 0.0
        val actualResult = viewModel.getCommissionRate(commissionRules, shopType)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when shop tier is zero expect getting the commission rate from shop tier 999 - CE version`() {
        val commissionRules = listOf(
            CommissionRule(shopType = 999, commissionRate = 2.5),
            CommissionRule(shopType = 2, commissionRate = 3.5)
        )
        val shopType = 0
        val expectedResult = 2.5
        val actualResult = viewModel.getCommissionRate(commissionRules, shopType)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `getShopInfo should return expected data when request params is provided`() = coroutineTestRule.runBlockingTest {
        coEvery {
            getShopInfoUseCase.executeOnBackground()
                .shopInfoById
        } returns ShopInfoById(
            result = listOf(
                TokoShopInfoData(
                    shopStats = StatsData("100"),
                    goldOSData = GoldOSData(1)
                )
            )
        )
        viewModel.getShopInfo(1)
        val result = viewModel.shopInfo.getOrAwaitValue()
        coVerify {
            getShopInfoUseCase.executeOnBackground()
        }
        assertEquals("100", result.shopInfoById.result.first().shopStats.totalTxSuccess)
    }

    @Test
    fun `getShopInfo should return throwable if error happen`() = coroutineTestRule.runBlockingTest {
        val expectedErrorMessage = "error happen"
        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } throws MessageErrorException(expectedErrorMessage)
        viewModel.getShopInfo(1)
        val resultErrorMessage = viewModel.shopInfoError.getOrAwaitValue()
        assertEquals(expectedErrorMessage, resultErrorMessage.localizedMessage)
    }

    @Test
    fun `getCommissionInfo should return expected data when request params is provided`() = coroutineTestRule.runBlockingTest {
        coEvery {
            getDefaultCommissionRulesUseCase.executeOnBackground()
        } returns GetDefaultCommissionRulesResponse(
            GetDefaultCommissionRules(
                categoryRate = listOf(
                    CategoryRate(
                        listOf(
                            CommissionRule(shopType = 1, commissionRate = 2.5)
                        )
                    )
                )
            )
        )
        viewModel.getCommissionInfo(1)
        val result = viewModel.commissionInfo.getOrAwaitValue()
        coVerify {
            getDefaultCommissionRulesUseCase.executeOnBackground()
        }
        assertEquals(2.5, result.getDefaultCommissionRules.categoryRate.first().commissionRules.first().commissionRate)
    }

    @Test
    fun `getCommissionInfo should return throwable if error happen`() = coroutineTestRule.runBlockingTest {
        val expectedErrorMessage = "error happen"
        coEvery {
            getDefaultCommissionRulesUseCase.executeOnBackground()
        } throws MessageErrorException(expectedErrorMessage)
        viewModel.getCommissionInfo(1)
        val resultErrorMessage = viewModel.commissionInfoError.getOrAwaitValue()
        assertEquals(expectedErrorMessage, resultErrorMessage.localizedMessage)
    }

    @Test
    fun `get commission info variables in viewmodel should have expected default values`() {
        assertFalse(viewModel.isSavingPriceAdjustment)
        assertFalse(viewModel.isPriceSuggestionRangeEmpty)
        assertFalse(viewModel.isFreeOfServiceFee)
        assertEquals(0,viewModel.shopTier)
    }

    private fun getIsTheLastOfWholeSaleTestResult(
        isAddingWholeSale: Boolean,
        isAddingValidationWholeSale: Boolean
    ): Boolean {
        viewModel.isAddingWholeSale = isAddingWholeSale
        viewModel.isAddingValidationWholeSale = isAddingValidationWholeSale
        viewModel.isTheLastOfWholeSale.value = true

        return viewModel.isInputValid.getOrAwaitValue()
    }

    private fun getValidateProductNameInputTestResult(
        blacklistKeyword: Boolean,
        typoDetected: Boolean,
        negativeKeyword: Boolean,
        errorMessageBlacklisted: String,
        errorMessageTypo: String,
        errorMessageNegative: String,
    ) {
        coEvery {
            getProductTitleValidationUseCase.getDataModelOnBackground()
        } returns TitleValidationModel(
            isBlacklistKeyword = blacklistKeyword,
            isTypoDetected = typoDetected,
            isNegativeKeyword = negativeKeyword
        )

        coEvery {
            provider.getTitleValidationErrorBlacklisted()
        } returns errorMessageBlacklisted

        coEvery {
            provider.getTitleValidationErrorTypo()
        } returns errorMessageTypo

        coEvery {
            provider.getTitleValidationErrorNegative()
        } returns errorMessageNegative

        viewModel.validateProductNameInput("dummy")

        coVerify {
            getProductTitleValidationUseCase.getDataModelOnBackground()
        }
    }

    private fun getSampleProductPhotos(): List<PictureInputModel> {
        return listOf(
                PictureInputModel(picID = "1", urlOriginal = "local/path/to/image1.jpg", urlThumbnail = "thumb 1", url300 = "300 1"),
                PictureInputModel(picID = "2", urlOriginal = "local/path/to/image2.jpg", urlThumbnail = "thumb 2", url300 = "300 2")
        )
    }

    private fun getPrivateField(owner: Any, name: String): Any? {
        return owner::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(owner)
        }
    }

    private fun <T: Any> runValidationAndProvideMessage(provider: KFunction0<String?>, value: String?, funcToCall: () -> T): T {
        every { provider() } returns value
        val result = funcToCall.invoke()
        verify { provider() }
        return result
    }

    private fun <T: Any> runValidationAndProvideMessage(
        provider: KFunction1<Int, String>,
        arg: Int,
        value: String,
        funcToCall: () -> T
    ): T {
        every { provider(arg) } returns value
        val result = funcToCall.invoke()
        verify { provider(arg) }
        return result
    }

}
