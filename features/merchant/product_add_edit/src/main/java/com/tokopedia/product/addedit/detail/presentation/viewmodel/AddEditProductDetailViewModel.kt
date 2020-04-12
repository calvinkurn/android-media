package com.tokopedia.product.addedit.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.domain.usecase.GetCategoryRecommendationUseCase
import com.tokopedia.product.addedit.detail.domain.usecase.GetNameRecommendationUseCase
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.draft.domain.usecase.SaveProductDraftUseCase
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditProductDetailViewModel @Inject constructor(
        val provider: ResourceProvider, dispatcher: CoroutineDispatcher,
        private val getNameRecommendationUseCase: GetNameRecommendationUseCase,
        private val getCategoryRecommendationUseCase: GetCategoryRecommendationUseCase,
        private val saveProductDraftUseCase: SaveProductDraftUseCase
) : BaseViewModel(dispatcher) {

    var isEditing = false

    var isDrafting = false

    var productInputModel = ProductInputModel()

    var detailInputModel = DetailInputModel()

    var hasVariants = false

    var productPhotoPaths: MutableList<String> = mutableListOf()

    private val mIsProductPhotoError = MutableLiveData<Boolean>()

    var isProductNameChanged = false
    private val mIsProductNameInputError = MutableLiveData<Boolean>()
    val isProductNameInputError: LiveData<Boolean>
        get() = mIsProductNameInputError
    var productNameMessage: String = ""

    var isProductRecommendationSelected = false
    private val mProductNameRecommendations = MutableLiveData<Result<List<String>>>()
    val productNameRecommendations: LiveData<Result<List<String>>>
        get() = mProductNameRecommendations

    private val mIsProductPriceInputError = MutableLiveData<Boolean>()
    val isProductPriceInputError: LiveData<Boolean>
        get() = mIsProductPriceInputError
    var productPriceMessage: String = ""

    var isWholeSalePriceActivated = MutableLiveData<Boolean>(false)
    var wholeSaleErrorCounter = MutableLiveData(0)

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

    private val saveProductDraftResultMutableLiveData = MutableLiveData<Result<Long>>()
    val saveProductDraftResultLiveData: LiveData<Result<Long>>
        get() = saveProductDraftResultMutableLiveData

    private val getProductDraftResultMutableLiveData = MutableLiveData<Result<ProductDraft>>()
    val getProductDraftResultLiveData: LiveData<Result<ProductDraft>>
        get() = getProductDraftResultMutableLiveData

    var isEditMode: Boolean = false

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
        addSource(wholeSaleErrorCounter) {
            this.value = isInputValid()
        }
        addSource(mIsProductStockInputError) {
            this.value = isInputValid()
        }
        addSource(mIsOrderQuantityInputError) {
            this.value = isInputValid()
        }
        addSource(mIsPreOrderDurationInputError) {
            this.value = isInputValid()
        }
    }
    val isInputValid: LiveData<Boolean>
        get() = mIsInputValid

    val productCategoryRecommendationLiveData = MutableLiveData<Result<List<ListItemUnify>>>()

    private val minProductPriceLimit = 100
    private val maxProductPriceLimit = 500000000
    private val minWholeSaleQuantity = 2
    private val maxWholeSalePriceLimit = 500000000
    private val minProductStockLimit = 1
    private val maxProductStockLimit = 999999
    private val minOrderQuantity = 1
    private val minPreOrderDuration = 1
    private val maxPreOrderDays = 90
    private val maxPreOrderWeeks = 13

    private fun isInputValid(): Boolean {

        val isProductPhotoError = mIsProductPhotoError.value ?: false
        val isProductNameError = mIsProductNameInputError.value ?: false
        val isProductPriceError = mIsProductPriceInputError.value ?: false
        val isProductStockError = mIsProductStockInputError.value ?: false
        val isOrderQuantityError = mIsOrderQuantityInputError.value ?: false

        // if not activated; wholesale error is not countable
        val isWholeSaleActivated = isWholeSalePriceActivated.value ?: false
        val isProductWholeSaleError = isWholeSaleActivated && wholeSaleErrorCounter.value?.let { it > 0 } ?: false

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
        if (isProductNameExist(productNameInput)) {
            val errorMessage = provider.getProductNameExistErrorMessage()
            errorMessage?.let { productNameMessage = it }
            mIsProductNameInputError.value = true
            return
        }
        if (isProductNameBanned(productNameInput)) {
            val errorMessage = provider.getProductNameBannedErrorMessage()
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
        val productPrice = productPriceInput.toBigInteger()
        if (productPrice < minProductPriceLimit.toBigInteger()) {
            val errorMessage = provider.getMinLimitProductPriceErrorMessage()
            errorMessage?.let { productPriceMessage = it }
            mIsProductPriceInputError.value = true
            return
        }
        if (productPrice > maxProductPriceLimit.toBigInteger()) {
            val errorMessage = provider.getMaxLimitProductPriceErrorMessage()
            errorMessage?.let { productPriceMessage = it }
            mIsProductPriceInputError.value = true
            return
        }
        productPriceMessage = ""
        mIsProductPriceInputError.value = false
    }

    fun validateProductWholeSaleQuantityInput(wholeSaleQuantityInput: String): String {
        if (wholeSaleQuantityInput.isEmpty()) {
            provider.getEmptyWholeSaleQuantityErrorMessage()?.let { return it }
        }
        val wholeSaleQuantity = wholeSaleQuantityInput.toLong()
        if (wholeSaleQuantity == 0L) {
            provider.getZeroWholeSaleQuantityErrorMessage()?.let { return it }
        }
        if (wholeSaleQuantity < minWholeSaleQuantity) {
            provider.getMinLimitWholeSaleQuantityErrorMessage()?.let { return it }
        }
        return ""
    }

    fun validateProductWholeSalePriceInput(wholeSalePriceInput: String, productPriceInput: String): String {
        if (wholeSalePriceInput.isEmpty()) {
            provider.getEmptyWholeSalePriceErrorMessage()?.let { return it }
        }
        val wholeSalePrice = wholeSalePriceInput.toBigDecimal()
        if (wholeSalePrice == 0.toBigDecimal()) {
            provider.getZeroWholeSalePriceErrorMessage()?.let { return it }
        }
        if (wholeSalePrice > maxWholeSalePriceLimit.toBigDecimal()) {
            provider.getMaxLimitWholeSalePriceErrorMessage()?.let { return it }
        }
        if (productPriceInput.isNotEmpty()) {
            val productPrice = productPriceInput.toLong()
            if (wholeSalePrice > productPrice.toBigDecimal()) {
                provider.getWholeSalePriceTooExpensiveErrorMessage()?.let { return it }
            }
        }
        return ""
    }

    fun validateProductStockInput(productStockInput: String) {
        if (productStockInput.isEmpty()) {
            val errorMessage = provider.getEmptyProductStockErrorMessage()
            errorMessage?.let { productStockMessage = it }
            mIsProductStockInputError.value = true
            return
        }
        val productStock = productStockInput.toBigInteger()
        if (productStock < minProductStockLimit.toBigInteger()) {
            val errorMessage = provider.getMinLimitProductStockErrorMessage()
            errorMessage?.let { productStockMessage = it }
            mIsProductStockInputError.value = true
            return
        }
        if (productStock > maxProductStockLimit.toBigInteger()) {
            val errorMessage = provider.getMaxLimitProductStockErrorMessage()
            errorMessage?.let { productStockMessage = it }
            mIsProductStockInputError.value = true
            return
        }
        productStockMessage = ""
        mIsProductStockInputError.value = false
    }

    fun validateProductMinOrderInput(productStockInput: String, orderQuantityInput: String) {
        if (orderQuantityInput.isEmpty()) {
            val errorMessage = provider.getEmptyOrderQuantityErrorMessage()
            errorMessage?.let { orderQuantityMessage = it }
            mIsOrderQuantityInputError.value = true
            return
        }
        val productMinOrder = orderQuantityInput.toBigInteger()
        if (productMinOrder < minOrderQuantity.toBigInteger()) {
            val errorMessage = provider.getMinLimitOrderQuantityErrorMessage()
            errorMessage?.let { orderQuantityMessage = it }
            mIsOrderQuantityInputError.value = true
            return
        }
        if (productStockInput.isNotEmpty()) {
            val productStock = productStockInput.toBigInteger()
            if (productMinOrder > productStock) {
                val errorMessage = provider.getMaxLimitOrderQuantityErrorMessage()
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
        val preOrderDuration = preOrderDurationInput.toBigInteger()
        if (preOrderDuration < minPreOrderDuration.toBigInteger()) {
            val errorMessage = provider.getMinLimitPreorderDurationErrorMessage()
            errorMessage?.let { preOrderDurationMessage = it }
            mIsPreOrderDurationInputError.value = true
            return
        }
        val isDayUnit = timeUnit == UNIT_DAY
        if (isDayUnit && preOrderDuration > maxPreOrderDays.toBigInteger()) {
            val errorMessage = provider.getMaxDaysLimitPreorderDuratioErrorMessage()
            errorMessage?.let { preOrderDurationMessage = it }
            mIsPreOrderDurationInputError.value = true
            return
        }
        val isWeekUnit = timeUnit == UNIT_WEEK
        if (isWeekUnit && preOrderDuration > maxPreOrderWeeks.toBigInteger()) {
            val errorMessage = provider.getMaxWeeksLimitPreorderDuratioErrorMessage()
            errorMessage?.let { preOrderDurationMessage = it }
            mIsPreOrderDurationInputError.value = true
            return
        }
        preOrderDurationMessage = ""
        mIsPreOrderDurationInputError.value = false
    }

    private fun isProductNameExist(productNameInput: String): Boolean {
        // TODO: replace the validation with API check
        return false
    }

    private fun isProductNameBanned(productNameInput: String): Boolean {
        // TODO: replace the validation with API check
        return false
    }

    fun updateProductPhotos(imagePickerResult: ArrayList<String>, originalImageUrl: ArrayList<String>, editted: ArrayList<Boolean>) {
        val pictureList = productInputModel.detailInputModel.pictureList.filter {
            originalImageUrl.contains(it.urlOriginal)
        }.filterIndexed { index, _ -> !editted[index] }

        val imageUrlOrPathList = imagePickerResult.mapIndexed { index, urlOrPath ->
            if (editted[index]) urlOrPath else pictureList.find { it.urlOriginal == originalImageUrl[index] }?.urlThumbnail ?: urlOrPath
        }.toMutableList()

        this.detailInputModel = productInputModel.detailInputModel.apply {
            this.pictureList = pictureList
            this.imageUrlOrPathList = imageUrlOrPathList
        }

        this.productPhotoPaths = imageUrlOrPathList
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

    fun saveProductDraft(productDraft: ProductDraft, productId: Long, isUploading: Boolean) {
        launchCatchError(block = {
            saveProductDraftUseCase.params = SaveProductDraftUseCase.createRequestParams(productDraft, productId, isUploading)
            saveProductDraftResultMutableLiveData.value = withContext(Dispatchers.IO) {
                saveProductDraftUseCase.executeOnBackground()
            }.let { Success(it) }
        }, onError = {
            saveProductDraftResultMutableLiveData.value = Fail(it)
        })
    }
}