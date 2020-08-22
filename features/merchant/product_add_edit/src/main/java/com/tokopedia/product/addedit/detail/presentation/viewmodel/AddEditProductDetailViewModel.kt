package com.tokopedia.product.addedit.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.domain.usecase.GetCategoryRecommendationUseCase
import com.tokopedia.product.addedit.detail.domain.usecase.GetNameRecommendationUseCase
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PREORDER_DAYS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PREORDER_WEEKS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_MIN_ORDER_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PREORDER_DURATION
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger
import javax.inject.Inject

class AddEditProductDetailViewModel @Inject constructor(
        val provider: ResourceProvider, dispatcher: CoroutineDispatcher,
        private val getNameRecommendationUseCase: GetNameRecommendationUseCase,
        private val getCategoryRecommendationUseCase: GetCategoryRecommendationUseCase
) : BaseViewModel(dispatcher) {

    var isEditing = false

    var isAdding = false

    var isDrafting = false

    var isFirstMoved = false

    var shouldUpdateVariant = false

    var productInputModel = ProductInputModel()
    val hasVariants get() = productInputModel.variantInputModel.selections.isNotEmpty()

    var productPhotoPaths: MutableList<String> = mutableListOf()

    var isAddingWholeSale = false

    var isAddingValidationWholeSale = false

    private val mIsProductPhotoError = MutableLiveData<Boolean>()

    var isProductNameChanged = false
    private val mIsProductNameInputError = MutableLiveData<Boolean>()
    val isProductNameInputError: LiveData<Boolean>
        get() = mIsProductNameInputError
    var productNameMessage: String = ""

    var isNameRecommendationSelected = false
    private val mProductNameRecommendations = MutableLiveData<Result<List<String>>>()
    val productNameRecommendations: LiveData<Result<List<String>>>
        get() = mProductNameRecommendations

    private val mIsProductPriceInputError = MutableLiveData<Boolean>()
    val isProductPriceInputError: LiveData<Boolean>
        get() = mIsProductPriceInputError
    var productPriceMessage: String = ""

    var isWholeSalePriceActivated = MutableLiveData<Boolean>(false)
    var wholeSaleErrorCounter = MutableLiveData(0)
    var isTheLastOfWholeSale = MutableLiveData<Boolean>()

    private val mIsProductStockInputError = MutableLiveData<Boolean>()
    val isProductStockInputError: LiveData<Boolean>
        get() = mIsProductStockInputError
    var productStockMessage: String = ""

    private val mIsOrderQuantityInputError = MutableLiveData<Boolean>()
    val isOrderQuantityInputError: LiveData<Boolean>
        get() = mIsOrderQuantityInputError
    var orderQuantityMessage: String = ""

    var isPreOrderActivated = MutableLiveData<Boolean>(false)
    private val mIsPreOrderDurationInputError = MutableLiveData<Boolean>()
    val isPreOrderDurationInputError: LiveData<Boolean>
        get() = mIsPreOrderDurationInputError
    var preOrderDurationMessage: String = ""

    private val mIsInputValid = MediatorLiveData<Boolean>().apply {
        addSource(mIsProductPhotoError) {
            this.value = isInputValid()
        }
        addSource(mIsProductNameInputError) {
            this.value = isInputValid()
        }
        addSource(mIsProductPriceInputError) {
            this.value = isInputValid()
        }
        addSource(isWholeSalePriceActivated) {
            this.value = isInputValid()
        }
        addSource(mIsProductStockInputError) {
            this.value = isInputValid()
        }
        addSource(mIsOrderQuantityInputError) {
            this.value = isInputValid()
        }
        addSource(isPreOrderActivated) {
            this.value = isInputValid()
        }
        addSource(mIsPreOrderDurationInputError) {
            this.value = isInputValid()
        }
        addSource(isTheLastOfWholeSale) {
            this.value = isInputValid()
            // to avoid using default value of wholeSaleErrorCounter
            // so we must check manual validation if we are in adding whole sale
            if (isAddingWholeSale) {
                this.value = !(isAddingValidationWholeSale)
            }
        }
        addSource(wholeSaleErrorCounter) {
            this.value = isInputValid()
        }
    }
    val isInputValid: LiveData<Boolean>
        get() = mIsInputValid

    val productCategoryRecommendationLiveData = MutableLiveData<Result<List<ListItemUnify>>>()

    private fun isInputValid(): Boolean {

        // by default the product photos are never empty
        val isProductPhotoError = mIsProductPhotoError.value ?: false

        // mandatory fields that empty by default (adding new product)
        val isProductNameError: Boolean
        val isProductPriceError: Boolean
        if (isAdding && isFirstMoved) {
            isProductNameError = mIsProductNameInputError.value ?: !isEditing
            isProductPriceError = mIsProductPriceInputError.value ?: !isEditing
        } else {
            isProductNameError = mIsProductNameInputError.value ?: false
            isProductPriceError = mIsProductPriceInputError.value ?: false
        }

        // by default the product stock is never empty
        val isProductStockError = mIsProductStockInputError.value ?: false

        // by default the product min order is never empty
        val isOrderQuantityError = mIsOrderQuantityInputError.value ?: false

        // if not activated; wholesale error is not countable
        val isWholeSaleActivated = isWholeSalePriceActivated.value ?: false
        val isProductWholeSaleError = isWholeSaleActivated && wholeSaleErrorCounter.value?.let { it > 0 } ?: false || isTheLastOfWholeSale.value ?: false

        // if not activated; pre order duration error is not countable
        val isPreOrderActivated = isPreOrderActivated.value ?: false
        val isPreOrderDurationError = isPreOrderActivated && mIsPreOrderDurationInputError.value ?: false

        return (!isProductPhotoError && !isProductNameError &&
                !isProductPriceError && !isProductStockError &&
                !isOrderQuantityError && !isProductWholeSaleError && !isPreOrderDurationError)
    }

    fun validateProductPhotoInput(productPhotoCount: Int) {
        mIsProductPhotoError.value = productPhotoCount == 0
    }

    fun validateProductNameInput(productNameInput: String) {
        if (productNameInput.isEmpty()) {
            val errorMessage = provider.getEmptyProductNameErrorMessage()
            errorMessage?.let { productNameMessage = it }
            mIsProductNameInputError.value = true
            return
        }
        val productNameTips = provider.getProductNameTips()
        productNameTips?.let { productNameMessage = it }
        mIsProductNameInputError.value = false
    }

    fun validateProductPriceInput(productPriceInput: String) {
        if (productPriceInput.isEmpty()) {
            val errorMessage = provider.getEmptyProductPriceErrorMessage()
            errorMessage?.let { productPriceMessage = it }
            mIsProductPriceInputError.value = true
            return
        }
        val productPrice: BigInteger = productPriceInput.toBigIntegerOrNull().orZero()
        if (productPrice < MIN_PRODUCT_PRICE_LIMIT.toBigInteger()) {
            val errorMessage = provider.getMinLimitProductPriceErrorMessage()
            errorMessage?.let { productPriceMessage = it }
            mIsProductPriceInputError.value = true
            return
        }
        productPriceMessage = ""
        mIsProductPriceInputError.value = false
    }

    fun validateProductWholeSaleQuantityInput(wholeSaleQuantityInput: String, minOrderInput: String, previousInput: String): String {
        if (wholeSaleQuantityInput.isEmpty()) {
            provider.getEmptyWholeSaleQuantityErrorMessage()?.let { return it }
        }
        val wholeSaleQuantity = wholeSaleQuantityInput.toBigIntegerOrNull().orZero()
        if (wholeSaleQuantity == 0.toBigInteger()) {
            provider.getZeroWholeSaleQuantityErrorMessage()?.let { return it }
        }
        if (minOrderInput.isNotBlank()) {
            if (wholeSaleQuantity < minOrderInput.toBigIntegerOrNull().orZero()) {
                provider.getMinLimitWholeSaleQuantityErrorMessage()?.let { return it }
            }
        }
        if (previousInput.isNotBlank()) {
            val previousQuantity = previousInput.toBigIntegerOrNull().orZero()
            if (previousQuantity >= wholeSaleQuantity) {
                provider.getPrevInputWholeSaleQuantityErrorMessage()?.let { return it }
            }
        }
        return ""
    }

    fun validateProductWholeSalePriceInput(wholeSalePriceInput: String, productPriceInput: String, previousInput: String): String {
        if (wholeSalePriceInput.isEmpty()) {
            provider.getEmptyWholeSalePriceErrorMessage()?.let { return it }
        }
        val wholeSalePrice = wholeSalePriceInput.toBigIntegerOrNull().orZero()
        if (wholeSalePrice == 0.toBigInteger()) {
            provider.getZeroWholeSalePriceErrorMessage()?.let { return it }
        }
        if (productPriceInput.isNotBlank()) {
            val productPrice = productPriceInput.toBigIntegerOrNull().orZero()
            if (wholeSalePrice >= productPrice) {
                provider.getWholeSalePriceTooExpensiveErrorMessage()?.let { return it }
            }
        }
        if (previousInput.isNotBlank()) {
            val previousPrice = previousInput.toBigIntegerOrNull().orZero()
            if (previousPrice <= wholeSalePrice) {
                provider.getPrevInputWholeSalePriceErrorMessage()?.let { return it }
            }
        }
        return ""
    }

    fun validateProductStockInput(productStockInput: String) {
        if (hasVariants) return
        if (productStockInput.isEmpty()) {
            val errorMessage = provider.getEmptyProductStockErrorMessage()
            errorMessage?.let { productStockMessage = it }
            mIsProductStockInputError.value = true
            return
        }
        val productStock = productStockInput.toBigIntegerOrNull().orZero()
        if (productStock < MIN_PRODUCT_STOCK_LIMIT.toBigInteger()) {
            val errorMessage = provider.getEmptyProductStockErrorMessage()
            errorMessage?.let { productStockMessage = it }
            mIsProductStockInputError.value = true
            return
        }
        if (productStock > MAX_PRODUCT_STOCK_LIMIT.toBigInteger()) {
            val errorMessage = provider.getMaxLimitProductStockErrorMessage()
            errorMessage?.let { productStockMessage = it }
            mIsProductStockInputError.value = true
            return
        }
        productStockMessage = ""
        mIsProductStockInputError.value = false
    }

    fun validateProductMinOrderInput(productStockInput: String, minOrderQuantityInput: String) {
        if (minOrderQuantityInput.isEmpty()) {
            val errorMessage = provider.getEmptyOrderQuantityErrorMessage()
            errorMessage?.let { orderQuantityMessage = it }
            mIsOrderQuantityInputError.value = true
            return
        }
        val productMinOrder = minOrderQuantityInput.toBigIntegerOrNull().orZero()
        if (productMinOrder < MIN_MIN_ORDER_QUANTITY.toBigInteger()) {
            val errorMessage = provider.getEmptyOrderQuantityErrorMessage()
            errorMessage?.let { orderQuantityMessage = it }
            mIsOrderQuantityInputError.value = true
            return
        }
        if (!hasVariants && productStockInput.isNotEmpty()) {
            val productStock = productStockInput.toBigIntegerOrNull().orZero()
            if (productMinOrder > productStock) {
                val errorMessage = provider.getMinOrderExceedStockErrorMessage()
                errorMessage?.let { orderQuantityMessage = it }
                mIsOrderQuantityInputError.value = true
                return
            }
        }
        orderQuantityMessage = ""
        mIsOrderQuantityInputError.value = false
    }

    fun validatePreOrderDurationInput(timeUnit: Int, preOrderDurationInput: String) {
        if (preOrderDurationInput.isEmpty()) {
            val errorMessage = provider.getEmptyPreorderDurationErrorMessage()
            errorMessage?.let { preOrderDurationMessage = it }
            mIsPreOrderDurationInputError.value = true
            return
        }
        val preOrderDuration = preOrderDurationInput.toBigIntegerOrNull().orZero()
        if (preOrderDuration < MIN_PREORDER_DURATION.toBigInteger()) {
            val errorMessage = provider.getMinLimitPreorderDurationErrorMessage()
            errorMessage?.let { preOrderDurationMessage = it }
            mIsPreOrderDurationInputError.value = true
            return
        }
        val isDayUnit = timeUnit == UNIT_DAY
        if (isDayUnit && preOrderDuration > MAX_PREORDER_DAYS.toBigInteger()) {
            val errorMessage = provider.getMaxDaysLimitPreorderDuratioErrorMessage()
            errorMessage?.let { preOrderDurationMessage = it }
            mIsPreOrderDurationInputError.value = true
            return
        }
        val isWeekUnit = timeUnit == UNIT_WEEK
        if (isWeekUnit && preOrderDuration > MAX_PREORDER_WEEKS.toBigInteger()) {
            val errorMessage = provider.getMaxWeeksLimitPreorderDuratioErrorMessage()
            errorMessage?.let { preOrderDurationMessage = it }
            mIsPreOrderDurationInputError.value = true
            return
        }
        preOrderDurationMessage = ""
        mIsPreOrderDurationInputError.value = false
    }

    /**
     * This method purpose is to update the productPhotoPaths
     * @param imagePickerResult is the list of product photo paths that returned from the image picker (it will have different value if the user do addition, removal or edit any images that are previously added)
     * @param originalImageUrl is the list of product photo paths that returned from the image picker which contains all the original image path (it doesn't contain image path of any added or edited image)
     * @param editted is the list of image edit status any image added and edited will have true value
     **/
    fun updateProductPhotos(imagePickerResult: ArrayList<String>, originalImageUrl: ArrayList<String>, editted: ArrayList<Boolean>): DetailInputModel {
        val pictureList = productInputModel.detailInputModel.pictureList.filter {
            originalImageUrl.contains(it.urlOriginal)
        }.filterIndexed { index, _ -> !editted[index] }

        val imageUrlOrPathList = imagePickerResult.mapIndexed { index, urlOrPath ->
            if (editted[index]) urlOrPath else pictureList.find { it.urlOriginal == originalImageUrl[index] }?.urlThumbnail
                    ?: urlOrPath
        }.toMutableList()

        this.productPhotoPaths = imageUrlOrPathList

        return DetailInputModel().apply {
            this.pictureList = pictureList
            this.imageUrlOrPathList = imageUrlOrPathList
        }
    }

    fun getProductNameRecommendation(shopId: Int = 0, query: String) {
        launchCatchError(block = {
            val result = withContext(Dispatchers.IO) {
                getNameRecommendationUseCase.requestParams = GetNameRecommendationUseCase.createRequestParam(shopId, query)
                getNameRecommendationUseCase.executeOnBackground()
            }
            mProductNameRecommendations.value = Success(result)
        }, onError = {
            mProductNameRecommendations.value = Fail(it)
        })
    }

    fun getCategoryRecommendation(productNameInput: String) {
        launchCatchError(block = {
            productCategoryRecommendationLiveData.value = Success(withContext(Dispatchers.IO) {
                getCategoryRecommendationUseCase.params = GetCategoryRecommendationUseCase.createRequestParams(productNameInput)
                getCategoryRecommendationUseCase.executeOnBackground()
            })
        }, onError = {
            productCategoryRecommendationLiveData.value = Fail(it)
        })
    }
}