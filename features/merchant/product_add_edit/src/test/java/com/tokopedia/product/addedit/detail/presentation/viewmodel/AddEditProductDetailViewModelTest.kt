package com.tokopedia.product.addedit.detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.domain.usecase.GetCategoryRecommendationUseCase
import com.tokopedia.product.addedit.detail.domain.usecase.GetNameRecommendationUseCase
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_MIN_ORDER_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.*
import org.mockito.Matchers.any
import kotlin.reflect.KFunction0

@ExperimentalCoroutinesApi
class AddEditProductDetailViewModelTest {

    @RelaxedMockK
    lateinit var provider: ResourceProvider

    @RelaxedMockK
    lateinit var getCategoryRecommendationUseCase: GetCategoryRecommendationUseCase

    @RelaxedMockK
    lateinit var getNameRecommendationUseCase: GetNameRecommendationUseCase

    @RelaxedMockK
    lateinit var mIsInputValidObserver: Observer<Boolean>

    @get:Rule
    val rule = InstantTaskExecutorRule()

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

    private val viewModel: AddEditProductDetailViewModel by lazy {
        AddEditProductDetailViewModel(provider, coroutineDispatcher, getNameRecommendationUseCase, getCategoryRecommendationUseCase)
    }

    @Test
    fun `success get category recommendation`() = runBlocking {
        val successResult = listOf(any<ListItemUnify>(), any<ListItemUnify>(), any<ListItemUnify>())

        coEvery {
            getCategoryRecommendationUseCase.executeOnBackground()
        } returns successResult

        viewModel.getCategoryRecommendation("baju")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getCategoryRecommendationUseCase.executeOnBackground()
        }

        val result = viewModel.productCategoryRecommendationLiveData.value
        Assert.assertTrue(result != null && result == Success(successResult))
    }

    @Test
    fun `failed get category recommendation`() = runBlocking {
        coEvery {
            getCategoryRecommendationUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getCategoryRecommendation("baju")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getCategoryRecommendationUseCase.executeOnBackground()
        }

        val result = viewModel.productCategoryRecommendationLiveData.value
        Assert.assertTrue(result != null && result is Fail)
    }

    @Test
    fun `success get name recommendation`() = runBlocking {
        val resultNameRecommendation = listOf("batik", "batik couple", "baju batik wanita", "baju batik pria", "batik kultut")

        coEvery {
            getNameRecommendationUseCase.executeOnBackground()
        } returns resultNameRecommendation

        viewModel.getProductNameRecommendation(query = "batik")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getNameRecommendationUseCase.executeOnBackground()
        }

        val resultViewmodel = viewModel.productNameRecommendations.value
        Assert.assertTrue(resultViewmodel != null && resultViewmodel == Success(resultNameRecommendation))
    }

    @Test
    fun `failed get name recommendation`() = runBlocking {
        coEvery {
            getNameRecommendationUseCase.executeOnBackground()
        } throws MessageErrorException("")

        viewModel.getProductNameRecommendation(query = "baju")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            getNameRecommendationUseCase.executeOnBackground()
        }

        val result = viewModel.productNameRecommendations.value
        Assert.assertTrue(result != null && result is Fail)
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
    fun `isInputValid should return false when no input is error and isAdding is true also isEditing is false`() {
        viewModel.isAdding = true
        viewModel.isEditing = false
        mIsProductNameInputError.value = null
        mIsProductNameInputError.value = null

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
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
    fun `isInputValid should return false when price is error`() {
        viewModel.validateProductPriceInput("")

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isInputValid should return false when wholesale price activated and error counter is more than zero`() {
        viewModel.isWholeSalePriceActivated.value = true
        viewModel.wholeSaleErrorCounter.value = 1

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertFalse(isValid)
    }

    @Test
    fun `isInputValid should return true when wholesale price activated and error counter is more zero`() {
        viewModel.isWholeSalePriceActivated.value = true
        viewModel.wholeSaleErrorCounter.value = 0

        val isValid = viewModel.isInputValid.getOrAwaitValue()
        Assert.assertTrue(isValid)
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