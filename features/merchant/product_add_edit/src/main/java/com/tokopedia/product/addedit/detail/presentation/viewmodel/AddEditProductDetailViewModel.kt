package com.tokopedia.product.addedit.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.DOUBLE_ZERO
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.GET_COMMISSION_ENGINE_REGULAR_MERCHANT
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.SERVICE_FEE_LIMIT
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.TEMP_IMAGE_EXTENSION
import com.tokopedia.product.addedit.common.constant.ProductStatus
import com.tokopedia.product.addedit.common.util.AddEditProductErrorHandler
import com.tokopedia.product.addedit.common.util.ResourceProvider
import com.tokopedia.product.addedit.detail.domain.model.*
import com.tokopedia.product.addedit.detail.domain.usecase.*
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.DEBOUNCE_DELAY_MILLIS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_MIN_ORDER_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PREORDER_DAYS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_PREORDER_WEEKS
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_SPECIFICATION_COUNTER
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MAX_WHOLESALE_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_MIN_ORDER_QUANTITY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PREORDER_DURATION
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_PRICE_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_DAY
import com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.Companion.UNIT_WEEK
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PriceSuggestion
import com.tokopedia.product.addedit.detail.presentation.model.SimilarProduct
import com.tokopedia.product.addedit.detail.presentation.model.TitleValidationModel
import com.tokopedia.product.addedit.preview.domain.usecase.ValidateProductNameUseCase
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import com.tokopedia.product.addedit.specification.presentation.constant.AddEditProductSpecificationConstants.SIGNAL_STATUS_VARIANT
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.shop.common.domain.interactor.GetMaxStockThresholdUseCase
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import javax.inject.Inject

@FlowPreview
class AddEditProductDetailViewModel @Inject constructor(
    val provider: ResourceProvider,
    private val dispatchers: CoroutineDispatchers,
    private val getNameRecommendationUseCase: GetNameRecommendationUseCase,
    private val getCategoryRecommendationUseCase: GetCategoryRecommendationUseCase,
    private val validateProductUseCase: ValidateProductUseCase,
    private val validateProductNameUseCase: ValidateProductNameUseCase,
    private val getShopEtalaseUseCase: GetShopEtalaseUseCase,
    private val annotationCategoryUseCase: AnnotationCategoryUseCase,
    private val getAddProductPriceSuggestionUseCase: GetAddProductPriceSuggestionUseCase,
    private val getEditProductPriceSuggestionUseCase: PriceSuggestionSuggestedPriceGetUseCase,
    private val getProductTitleValidationUseCase: GetProductTitleValidationUseCase,
    private val getMaxStockThresholdUseCase: GetMaxStockThresholdUseCase,
    private val getShopInfoUseCase: GetShopInfoUseCase,
    private val getDefaultCommissionRulesUseCase: GetDefaultCommissionRulesUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main) {

    var isEditing = false
    var isAdding = false
    var isDrafting = false

    var isReloadingShowCase = false
    var isFirstMoved = false

    var isAddingWholeSale = false
    var isAddingValidationWholeSale = false
    var isSavingPriceAdjustment = false
    var isPriceSuggestionRangeEmpty = false
    var isFreeOfServiceFee = false

    var shopType = 0

    private var isMultiLocationShop = false

    private var minimumStockCount = MIN_PRODUCT_STOCK_LIMIT
    private var stockAllocationDefaultMessage = ""
    private var priceAllocationDefaultMessage = ""

    var productInputModel = ProductInputModel()
    val hasVariants get() = productInputModel.variantInputModel.selections.isNotEmpty()
    val hasTransaction get() = productInputModel.itemSold > 0

    var productPhotoPaths: MutableList<String> = mutableListOf()
    private val mIsProductPhotoError = MutableLiveData<Boolean>()

    private val mProductNameInputLiveData = MutableLiveData<String>()
    private val mIsProductNameInputError = MutableLiveData<Boolean>()
    val isProductNameInputError: LiveData<Boolean>
        get() = mIsProductNameInputError
    var productNameMessage: String = ""
    var productNameValidationResult: TitleValidationModel = TitleValidationModel()

    private val mProductNameRecommendations = MutableLiveData<Result<List<String>>>()
    val productNameRecommendations: LiveData<Result<List<String>>>
        get() = mProductNameRecommendations

    private val mIsProductPriceInputError = MutableLiveData<Boolean>()
    val isProductPriceInputError: LiveData<Boolean>
        get() = mIsProductPriceInputError
    var productPriceMessage: String = ""

    var isWholeSalePriceActivated = MutableLiveData(false)
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

    var isPreOrderActivated = MutableLiveData(false)
    private val mIsPreOrderDurationInputError = MutableLiveData<Boolean>()
    val isPreOrderDurationInputError: LiveData<Boolean>
        get() = mIsPreOrderDurationInputError
    var preOrderDurationMessage: String = ""

    private val mIsProductSkuInputError = MutableLiveData<Boolean>()
    val isProductSkuInputError: LiveData<Boolean>
        get() = mIsProductSkuInputError
    var productSkuMessage: String = ""

    private var _productNameValidationFromNetwork = MutableLiveData<Result<String>>()
    val productNameValidationFromNetwork : LiveData<Result<String>>
        get() = _productNameValidationFromNetwork

    val productCategoryRecommendationLiveData = MutableLiveData<Result<List<ListItemUnify>>>()

    var productShowCases: MutableList<ShowcaseItemPicker> = mutableListOf()
    private val mShopShowCases = MutableLiveData<Result<List<ShopEtalaseModel>>>()
    val shopShowCases: LiveData<Result<List<ShopEtalaseModel>>>
        get() = mShopShowCases

    private val mShopInfo = MutableLiveData<GetShopInfoResponse>()
    val shopInfo: LiveData<GetShopInfoResponse>
        get() = mShopInfo
    private val mShopInfoError = MutableLiveData<Throwable>()
    val shopInfoError: LiveData<Throwable>
        get() = mShopInfoError

    private val mCommissionInfo = MutableLiveData<GetDefaultCommissionRulesResponse>()
    val commissionInfo: LiveData<GetDefaultCommissionRulesResponse>
        get() = mCommissionInfo
    private val mCommissionInfoError = MutableLiveData<Throwable>()
    val commissionInfoError: LiveData<Throwable>
        get() = mCommissionInfoError

    private val mAnnotationCategoryData = MutableLiveData<Result<List<AnnotationCategoryData>>>()
    val annotationCategoryData: LiveData<Result<List<AnnotationCategoryData>>>
        get() = mAnnotationCategoryData
    private val mSelectedSpecificationList = MutableLiveData<List<SpecificationInputModel>>()
    val selectedSpecificationList: LiveData<List<SpecificationInputModel>>
        get() = mSelectedSpecificationList
    private val mSpecificationText = MutableLiveData<String>()
    val specificationText: LiveData<String>
        get() = mSpecificationText
    private val mHasRequiredSpecification = MutableLiveData<Boolean>()
    val hasRequiredSpecification: LiveData<Boolean>
        get() = mHasRequiredSpecification

    private val mProductPriceRecommendation = MutableLiveData<PriceSuggestionSuggestedPriceGet>()
    val productPriceRecommendation: LiveData<PriceSuggestionSuggestedPriceGet>
        get() = mProductPriceRecommendation
    private val mProductPriceRecommendationError = MutableLiveData<Throwable>()
    val productPriceRecommendationError: LiveData<Throwable>
        get() = mProductPriceRecommendationError

    private val mAddProductPriceSuggestion = MutableLiveData<PriceSuggestionByKeyword>()
    val addProductPriceSuggestion: LiveData<PriceSuggestionByKeyword>
        get() = mAddProductPriceSuggestion
    private val mAddProductPriceSuggestionError = MutableLiveData<Throwable>()
    val addProductPriceSuggestionError: LiveData<Throwable>
        get() = mAddProductPriceSuggestionError

    private val mMaxStockThreshold = MutableLiveData<String>()
    val maxStockThreshold: LiveData<String>
        get() = mMaxStockThreshold

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
        addSource(mIsProductSkuInputError) {
            this.value = isInputValid()
        }
    }
    val isInputValid: LiveData<Boolean>
        get() = mIsInputValid

    init {
        launch {
            mProductNameInputLiveData.asFlow()
                .debounce(DEBOUNCE_DELAY_MILLIS)
                .distinctUntilChanged()
                .collect {
                    validateProductNameInput(it)
                }
        }
    }

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

        // by default the product sku is allowed to empty
        val isProductSkuError = mIsProductSkuInputError.value ?: false

        return (!isProductPhotoError && !isProductNameError &&
                !isProductPriceError && !isProductStockError &&
                !isOrderQuantityError && !isProductWholeSaleError &&
                !isPreOrderDurationError && !isProductSkuError)
    }

    fun validateProductPhotoInput(productPhotoCount: Int) {
        mIsProductPhotoError.value = productPhotoCount.isZero()
    }

    fun setProductNameInput(string: String) {
        mProductNameInputLiveData.value = string
    }

    fun getMaxStockThreshold(shopId: String) {
        launchCatchError(block = {
            mMaxStockThreshold.value = getMaxStockThresholdUseCase.execute(shopId).getIMSMeta.data.maxStockThreshold
        }, onError = {
            mMaxStockThreshold.value = null
        })
    }

    fun validateProductNameInput(productNameInput: String) {
        if (productNameInput.isEmpty()) {
            // show product error when product name is empty
            val errorMessage = provider.getEmptyProductNameErrorMessage()
            errorMessage?.let { productNameMessage = it }
            mIsProductNameInputError.value = true
        } else {
            // show product name tips
            val productNameTips = provider.getProductNameTips()
            productNameTips?.let { productNameMessage = it }
            mIsProductNameInputError.value = false

            if (productNameInput.trim() != productInputModel.detailInputModel.currentProductName) {
                // remote product name validation
                launchCatchError(block = {
                    productNameValidationResult = withContext(dispatchers.io) {
                        getProductTitleValidationUseCase.setParam(productNameInput)
                        getProductTitleValidationUseCase.getDataModelOnBackground()
                    }

                    productNameMessage = when {
                        productNameValidationResult.isBlacklistKeyword -> {
                            provider.getTitleValidationErrorBlacklisted()
                        }
                        productNameValidationResult.isTypoDetected -> {
                            provider.getTitleValidationErrorTypo()
                        }
                        productNameValidationResult.isNegativeKeyword -> {
                            provider.getTitleValidationErrorNegative()
                        }
                        else -> {
                            ""
                        }
                    }
                    mIsProductNameInputError.value = productNameValidationResult.isBlacklistKeyword
                }, onError = {
                    // log error
                    AddEditProductErrorHandler.logExceptionToCrashlytics(it)
                })
            }
        }
    }

    fun validateProductPriceInput(productPriceInput: String) {
        if (productPriceInput.isEmpty()) {
            val errorMessage = provider.getEmptyProductPriceErrorMessage()
            errorMessage.let { productPriceMessage = it }
            mIsProductPriceInputError.value = true
            return
        }
        val productPrice: BigInteger = productPriceInput.toBigIntegerOrNull().orZero()
        if (productPrice < MIN_PRODUCT_PRICE_LIMIT.toBigInteger()) {
            val errorMessage = provider.getMinLimitProductPriceErrorMessage()
            errorMessage.let { productPriceMessage = it }
            mIsProductPriceInputError.value = true
            return
        }

        productPriceMessage = priceAllocationDefaultMessage
        mIsProductPriceInputError.value = false
    }

    fun validateProductWholeSaleQuantityInput(wholeSaleQuantityInput: String, minOrderInput: String, previousInput: String): String {
        if (wholeSaleQuantityInput.isEmpty()) {
            provider.getEmptyWholeSaleQuantityErrorMessage()?.let { return it }
        }
        val wholeSaleQuantity = wholeSaleQuantityInput.toBigIntegerOrNull().orZero()
        if (wholeSaleQuantity == 0.toBigInteger()) {
            provider.getZeroWholeSaleQuantityErrorMessage()?.let { return it }
        } else if (wholeSaleQuantity >= MAX_WHOLESALE_QUANTITY.toBigInteger()) {
            return provider.getWholeSaleMaxErrorMessage(MAX_WHOLESALE_QUANTITY)
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
        if (hasVariants) {
            productStockMessage = ""
            mIsProductStockInputError.value = false
            return
        }

        if (productStockInput.isEmpty()) {
            val errorMessage = provider.getEmptyProductStockErrorMessage()
            errorMessage.let { productStockMessage = it }
            mIsProductStockInputError.value = true
            return
        }

        val productStock = productStockInput.toBigIntegerOrNull().orZero()
        val maxStock = mMaxStockThreshold.value
        val isMaxStockNotNull = maxStock != null
        val isCurrentStockLessThanMinStock = productStock < minimumStockCount.toBigInteger()
        val isCurrentStockMoreThanMaxStock = productStock > maxStock?.toBigIntegerOrNull().orZero()

        if (isCurrentStockLessThanMinStock) {
            val errorMessage = provider.getEmptyProductStockErrorMessage()
            errorMessage.let { productStockMessage = it }
            mIsProductStockInputError.value = true
            return
        }

        if (isMaxStockNotNull && isCurrentStockMoreThanMaxStock) {
            val errorMessage = provider.getMaxLimitProductStockErrorMessage(maxStock)
            productStockMessage = errorMessage
            mIsProductStockInputError.value = true
            return
        }

        productStockMessage = stockAllocationDefaultMessage
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
        if (productMinOrder > MAX_MIN_ORDER_QUANTITY.toBigInteger()) {
            val errorMessage = provider.getMinOrderExceedLimitQuantityErrorMessage()
            errorMessage?.let { orderQuantityMessage = it }
            mIsOrderQuantityInputError.value = true
            return
        }
        if (!hasVariants && productStockInput.isNotEmpty()) {
            val productStock = productStockInput.toBigIntegerOrNull().orZero()
            if (productMinOrder > productStock) {
                // It is possible for admin in multi location shop to edit product stock to 0
                if (productStock == 0.toBigInteger() && isMultiLocationShop && isEditing) {
                    return
                }
                val errorMessage = provider.getMinOrderExceedStockErrorMessage()
                errorMessage?.let { orderQuantityMessage = it }
                mIsOrderQuantityInputError.value = true
                return
            }
        }

        orderQuantityMessage = ""
        mIsOrderQuantityInputError.value = false
    }

    fun validateProductSkuInput(productSkuInput: String) {
        // remote product sku validation
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                validateProductUseCase.setParamsProductSku(productSkuInput)
                validateProductUseCase.executeOnBackground()
            }
            val validationMessage = response.productValidateV3.data.productSku
                    .joinToString("\n")
            productSkuMessage = validationMessage
            mIsProductSkuInputError.value = validationMessage.isNotEmpty()
        }, onError = {
            // log error
            AddEditProductErrorHandler.logExceptionToCrashlytics(it)
        })
    }

    fun validateProductNameInputFromNetwork(productName: String) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                validateProductNameUseCase.setParamsProductName(
                    productInputModel.productId.toString(), productName)
                validateProductNameUseCase.executeOnBackground()
            }
            val validationMessage = response.productValidateV3.data.validationResults.joinToString("\n")
            _productNameValidationFromNetwork.value = Success(validationMessage)
        }, onError = {
            _productNameValidationFromNetwork.value = Fail(it)
        })
    }

    fun setProductNameInputFromNetwork(value: Result<String>?) {
        _productNameValidationFromNetwork.value = value
    }

    fun setIsProductNameInputError(value: Boolean) {
        mIsProductNameInputError.value = value
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
    fun updateProductPhotos(imagePickerResult: MutableList<String>, originalImageUrl: MutableList<String>, editted: MutableList<Boolean>): DetailInputModel {
        val cleanResult = ArrayList(cleanProductPhotoUrl(imagePickerResult, originalImageUrl))
        val pictureList = productInputModel.detailInputModel.pictureList.filter {
            cleanResult.contains(it.urlOriginal)
        }

        val imageUrlOrPathList = cleanResult.mapIndexed { index, urlOrPath ->
            if (editted[index]) urlOrPath else pictureList.find { it.urlOriginal == cleanResult[index] }?.urlThumbnail
                    ?: urlOrPath
        }

        this.productPhotoPaths = imageUrlOrPathList.toMutableList()

        return DetailInputModel().apply {
            this.pictureList = pictureList
            this.imageUrlOrPathList = imageUrlOrPathList
        }
    }

    fun updateProductShowCases(selectedShowcaseList: ArrayList<ShowcaseItemPicker>) {
        productShowCases = selectedShowcaseList
    }

    fun getMaxProductPhotos(): Int {
        return if (userSession.isShopOfficialStore) {
            AddEditProductDetailConstants.MAX_PRODUCT_PHOTOS_OS
        } else {
            AddEditProductDetailConstants.MAX_PRODUCT_PHOTOS
        }
    }

    fun getProductNameRecommendation(shopId: Int = 0, query: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
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
            productCategoryRecommendationLiveData.value = Success(withContext(dispatchers.io) {
                getCategoryRecommendationUseCase.params = GetCategoryRecommendationUseCase.createRequestParams(productNameInput)
                getCategoryRecommendationUseCase.executeOnBackground()
            })
        }, onError = {
            productCategoryRecommendationLiveData.value = Fail(it)
        })
    }

    fun getShopShowCasesUseCase() {
        launchCatchError(block = {
            mShopShowCases.value = Success(withContext(dispatchers.io) {
                val response = getShopEtalaseUseCase.executeOnBackground()
                response.shopShowcases.result
            })
        }, onError = {
            mShopShowCases.value = Fail(it)
        })
    }

    fun getShopInfo(shopId: Int) {
        launchCatchError(block = {
            mShopInfo.value = withContext(dispatchers.io) {
                getShopInfoUseCase.setParam(shopId)
                val response = getShopInfoUseCase.executeOnBackground()
                response
            }
        }, onError = {
            mShopInfoError.value = it
        })
    }

    fun getCommissionInfo(categoryId: Int) {
        launchCatchError(block = {
            mCommissionInfo.value = withContext(dispatchers.io) {
                getDefaultCommissionRulesUseCase.setParam(categoryId)
                val response = getDefaultCommissionRulesUseCase.executeOnBackground()
                response
            }
        }, onError = {
            mCommissionInfoError.value = it
        })
    }

    /**
     * Modify stock related values if admin/owner has multi location shops
     */
    fun setupMultiLocationShopValues() {
        isMultiLocationShop = getIsMultiLocation()
        if (isMultiLocationShop) {
            setupMultiLocationPriceAllocationMessage()
            setupMultiLocationStockAllocationMessage()
            setupMultiLocationDefaultMinimumStock()
        } else {
            priceAllocationDefaultMessage = provider.getPriceTipsMessage()
            productPriceMessage = provider.getPriceTipsMessage()
            stockAllocationDefaultMessage = ""
            productStockMessage = ""
            minimumStockCount = MIN_PRODUCT_STOCK_LIMIT
        }
    }

    private fun setupMultiLocationPriceAllocationMessage() {
        getMultiLocationPriceAllocationMessage().let {
            priceAllocationDefaultMessage = it
            productPriceMessage = it
        }
    }

    private fun setupMultiLocationStockAllocationMessage() {
        getMultiLocationStockAllocationMessage().let {
            stockAllocationDefaultMessage = it
            productStockMessage = it
        }
    }

    private fun setupMultiLocationDefaultMinimumStock() {
        if (isEditing) {
            minimumStockCount = 0
        }
    }

    private fun getMultiLocationPriceAllocationMessage(): String =
        when {
            isEditing -> provider.getEditProductPriceMultiLocationMessage()
            isAdding -> provider.getAddProductPriceMultiLocationMessage()
            else -> ""
        }

    private fun getMultiLocationStockAllocationMessage(): String =
            when {
                isEditing -> provider.getEditProductStockMultiLocationMessage().orEmpty()
                isAdding -> provider.getAddProductStockMultiLocationMessage().orEmpty()
                else -> ""
            }

    private fun getIsMultiLocation(): Boolean =
            userSession.run {
                isMultiLocationShop && (isShopAdmin || isShopOwner)
            }


    fun getAnnotationCategory(categoryId: String, productId: String) {
        launchCatchError(block = {
            mAnnotationCategoryData.value = Success(withContext(dispatchers.io) {
                delay(DEBOUNCE_DELAY_MILLIS)
                annotationCategoryUseCase.setParamsCategoryId(categoryId)
                annotationCategoryUseCase.setParamsProductId(productId)
                val response = annotationCategoryUseCase.executeOnBackground()
                response.drogonAnnotationCategoryV2.data
            })
        }, onError = {
            mAnnotationCategoryData.value = Fail(it)
        })
    }

    fun updateSpecificationByAnnotationCategory(annotationCategoryList: List<AnnotationCategoryData>) {
        val selectedSpecificationList = mutableListOf<SpecificationInputModel>()
        annotationCategoryList.forEach {
            val selectedValue = it.data.firstOrNull { value -> value.selected }
            selectedValue?.apply {
                val specificationInputModel = SpecificationInputModel(id, name, it.variant)
                selectedSpecificationList.add(specificationInputModel)
            }
        }

        updateSelectedSpecification(selectedSpecificationList)
    }

    fun updateHasRequiredSpecification(annotationCategoryList: List<AnnotationCategoryData>) {
        mHasRequiredSpecification.value = annotationCategoryList.any {
            it.variant == SIGNAL_STATUS_VARIANT
        }
    }

    fun updateSelectedSpecification(selectedSpecificationList: List<SpecificationInputModel>) {
        mSelectedSpecificationList.value = selectedSpecificationList
        updateSpecificationText(selectedSpecificationList)
    }

    fun updateSpecificationText(selectedSpecificationList: List<SpecificationInputModel>) {
        val specificationNames = selectedSpecificationList.map { it.data }
        mSpecificationText.value = if (specificationNames.isEmpty()) {
            provider.getProductSpecificationTips()
        } else {
            val result = specificationNames.take(MAX_SPECIFICATION_COUNTER).joinToString(", ")
            if (specificationNames.size > MAX_SPECIFICATION_COUNTER) {
                result + provider.getProductSpecificationCounter(specificationNames.size - MAX_SPECIFICATION_COUNTER)
            } else {
                result
            }
        }
    }

    fun validateSelectedSpecificationList(): Boolean {
        return !hasRequiredSpecification.value.orFalse() || mSelectedSpecificationList.value.orEmpty().any {
            it.specificationVariant == SIGNAL_STATUS_VARIANT
        }
    }

    fun getProductPriceRecommendation() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                getEditProductPriceSuggestionUseCase.setParamsProductId(productInputModel.productId)
                getEditProductPriceSuggestionUseCase.executeOnBackground()
            }
            mProductPriceRecommendation.value = response.priceSuggestionSuggestedPriceGet
        }, onError = {
            mProductPriceRecommendationError.value = it
        })
    }

    fun getAddProductPriceSuggestion(keyword: String, categoryL3: String) {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                getAddProductPriceSuggestionUseCase.setPriceSuggestionParams(keyword, categoryL3)
                getAddProductPriceSuggestionUseCase.executeOnBackground()
            }
            mAddProductPriceSuggestion.value = response.priceSuggestionByKeyword
        }, onError = {
            mAddProductPriceSuggestionError.value = it
        })
    }

    fun removeKeywords(text: String, removedWords: List<String>): String {
        var result = text
        removedWords.forEach {
            result = result.replace(it, "")
        }
        return result.trim().replace("\\s+".toRegex(), " ")
    }

    fun mapAddPriceSuggestionToPriceSuggestionUiModel(priceSuggestion: PriceSuggestionByKeyword): PriceSuggestion {
        return PriceSuggestion(
                suggestedPrice = priceSuggestion.summary?.suggestedPrice,
                suggestedPriceMin = priceSuggestion.summary?.suggestedPriceMin,
                suggestedPriceMax = priceSuggestion.summary?.suggestedPriceMax,
                similarProducts = priceSuggestion.suggestions?.map {
                    SimilarProduct(
                            productId = it.productId,
                            displayPrice = it.displayPrice,
                            imageURL = it.imageURL,
                            title = it.title,
                            totalSold = it.totalSold,
                            rating = it.rating
                    )
                } ?: listOf()
        )
    }

    fun mapEditPriceSuggestionToPriceSuggestionUiModel(priceSuggestion: PriceSuggestionSuggestedPriceGet): PriceSuggestion {
        return PriceSuggestion(
                suggestedPrice = priceSuggestion.suggestedPrice,
                suggestedPriceMin = priceSuggestion.suggestedPriceMin,
                suggestedPriceMax = priceSuggestion.suggestedPriceMax,
                similarProducts = priceSuggestion.productRecommendation.map {
                    SimilarProduct(
                            productId = it.productID,
                            displayPrice = it.price,
                            imageURL = it.imageURL,
                            title = it.title,
                            totalSold = it.sold,
                            rating = it.rating
                    )
                }
        )
    }

    fun getProductPriceSuggestionRange(isEditing: Boolean): Pair<Double, Double> {
        return if (isEditing) {
            val minPrice = productPriceRecommendation.value?.suggestedPriceMin.orZero()
            val maxPrice = productPriceRecommendation.value?.suggestedPriceMax.orZero()
            minPrice to maxPrice
        } else {
            val summary = addProductPriceSuggestion.value?.summary
            val minPrice = summary?.suggestedPriceMin.orZero()
            val maxPrice = summary?.suggestedPriceMax.orZero()
            minPrice to maxPrice
        }
    }

    fun isProductPriceCompetitive(priceInput: Double, priceSuggestionRange: Pair<Double, Double>, isError: Boolean = false): Boolean {
        if (isError) return false
        val minPrice = priceSuggestionRange.first
        val maxPrice = priceSuggestionRange.second
        return priceInput <= minPrice ||  priceInput in minPrice..maxPrice
    }

    fun isPriceSuggestionLayoutVisible(isRangeEmpty: Boolean, productStatus: Int, isNew: Boolean, hasVariant: Boolean): Boolean {
        val isActive = productStatus == ProductStatus.STATUS_ACTIVE
        return !isRangeEmpty && isActive && isNew && !hasVariant
    }

    fun isPriceSuggestionRangeIsEmpty(minLimit: Double, maxLimit: Double): Boolean {
        return minLimit == DOUBLE_ZERO && maxLimit == DOUBLE_ZERO
    }

    fun isFreeOfServiceFee(totalTxSuccess: Int, shopType: Int): Boolean {
        // RM for shop service is 0 while in commission engine 999
        return shopType == GET_COMMISSION_ENGINE_REGULAR_MERCHANT && totalTxSuccess <= SERVICE_FEE_LIMIT
    }

    fun getCommissionRate(commissionRules: List<CommissionRule>, shopType: Int): Double {
        return commissionRules.firstOrNull { it.shopType == shopType }?.commissionRate.orZero()
    }

    /**
     * This method purpose is to cleanse imagePickerResult from cache url
     * If we input web url link to imagePicker usually imagePicker will return a temporary URL with "*.0" extension in imagePickerResult array
     * Therefore, we should cleanse URL by changing temporary URL to original web url
     * @param imagePickerResult is the list of product photo paths that returned from imagePicker (it will have different value if the user do addition, removal or edit any images that are previously added)
     * @param originalImageUrl is the list of original product photo paths that input to imagePicker (it doesn't contain image path of any added or edited image)
     **/
    private fun cleanProductPhotoUrl(imagePickerResult: MutableList<String>,
                                     originalImageUrl: MutableList<String>): List<String> {
        return imagePickerResult.mapIndexed { index, input ->
            if (input.endsWith(TEMP_IMAGE_EXTENSION)) {
                originalImageUrl[index]
            } else {
                imagePickerResult[index]
            }
        }
    }
}
