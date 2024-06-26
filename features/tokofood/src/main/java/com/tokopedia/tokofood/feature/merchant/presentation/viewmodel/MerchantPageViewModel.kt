package com.tokopedia.tokofood.feature.merchant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodBusinessData
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodCart
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCart
import com.tokopedia.tokofood.common.presentation.mapper.CustomOrderDetailsMapper.mapTokoFoodProductsToCustomOrderDetails
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.ResourceProvider
import com.tokopedia.tokofood.feature.merchant.domain.model.response.GetMerchantDataResponse
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCatalogVariantDetail
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCatalogVariantOptionDetail
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCategoryCatalog
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCategoryFilter
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodGetMerchantData
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTickerDetail
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTopBanner
import com.tokopedia.tokofood.feature.merchant.domain.usecase.GetMerchantDataUseCase
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CarouselDataType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType.MULTIPLE_SELECTION
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType.SINGLE_SELECTION
import com.tokopedia.tokofood.feature.merchant.presentation.mapper.TokoFoodMerchantUiModelMapper
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CarouselData
import com.tokopedia.tokofood.feature.merchant.presentation.model.CategoryUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MerchantPageViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val dispatchers: CoroutineDispatchers,
    private val getMerchantDataUseCase: GetMerchantDataUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase
) : BaseViewModel(dispatchers.main) {

    private val getMerchantDataResultLiveData = SingleLiveEvent<Result<GetMerchantDataResponse>>()
    val getMerchantDataResult: SingleLiveEvent<Result<GetMerchantDataResponse>> get() = getMerchantDataResultLiveData
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>> get() = _chooseAddress
    private val _mvcLiveData = SingleLiveEvent<MvcData?>()
    val mvcLiveData: LiveData<MvcData?> get() = _mvcLiveData

    private val _quantityUpdateState = MutableSharedFlow<Boolean>()
    private val _currentProductsWithUpdatedQuantity: MutableMap<String, Int> = mutableMapOf()
    val updateQuantityParam = MutableSharedFlow<UpdateQuantityTokofoodParam>(Int.ONE)

    // map of productId to card positions info <dataset,adapter>
    val productMap: HashMap<String, Pair<Int, Int>> = hashMapOf()

    var sameProductList: List<String> = listOf()

    var productListItems: MutableList<ProductListItem> = mutableListOf()

    var selectedProducts: List<CartListCartGroupCart> = listOf()

    var isAddressManuallyUpdated = false

    var isProductDetailBottomSheetVisible = false

    fun getDataSetPosition(cardPositions: Pair<Int, Int>): Int {
        return cardPositions.first
    }

    fun getAdapterPosition(cardPositions: Pair<Int, Int>): Int {
        return cardPositions.second
    }

    var filterList = listOf<TokoFoodCategoryFilter>()

    var filterNameSelected = ""

    var isStickyBarVisible = false

    var merchantData: TokoFoodGetMerchantData? = null

    init {
        viewModelScope.launch {
            _quantityUpdateState
                .debounce(UPDATE_QUANTITY_DEBOUNCE)
                .flatMapConcat {
                    flow {
                        getCurrentQuantityUpdateParams().forEach {
                            emit(it)
                        }
                    }
                }
                .collect {
                    _currentProductsWithUpdatedQuantity.clear()
                    updateQuantityParam.emit(it)
                }
        }
    }

    fun getMerchantData(merchantId: String, latlong: String, timezone: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetMerchantDataUseCase.createRequestParams(
                    merchantId = merchantId,
                    latlong = latlong,
                    timezone = timezone
                )
                getMerchantDataUseCase.setRequestParams(params = params.parameters)
                getMerchantDataUseCase.executeOnBackground()
            }
            filterList = result.tokofoodGetMerchantData.filters
            merchantData = result.tokofoodGetMerchantData
            sameProductList = result.tokofoodGetMerchantData.getProductsWithSameId()
            getMerchantDataResultLiveData.value = Success(result)
            _mvcLiveData.value = getMvcData(result.tokofoodGetMerchantData.topBanner)
        }, onError = {
                getMerchantDataResultLiveData.value = Fail(it)
                _mvcLiveData.value = null
            })
    }

    fun getChooseAddress(source: String) {
        isAddressManuallyUpdated = true
        launchCatchError(block = {
            _chooseAddress.value = Success(getChooseAddressWarehouseLocUseCase(source))
        }) {
            _chooseAddress.value = Fail(it)
        }
    }

    fun mapMerchantProfileToCarouselData(merchantProfile: TokoFoodMerchantProfile): List<CarouselData> {
        // rating
        val ratingData = CarouselData(
            carouselDataType = CarouselDataType.RATING,
            title = merchantProfile.totalRatingFmt,
            information = merchantProfile.ratingFmt
        )
        // distance
        val distanceData = CarouselData(
            carouselDataType = CarouselDataType.DISTANCE,
            title = resourceProvider.getDistanceTitle() ?: "",
            information = merchantProfile.distanceFmt.content,
            isWarning = merchantProfile.distanceFmt.isWarning
        )
        // estimation
        val estimationData = CarouselData(
            carouselDataType = CarouselDataType.ETA,
            title = resourceProvider.getEstimationTitle() ?: "",
            information = merchantProfile.etaFmt.content,
            isWarning = merchantProfile.etaFmt.isWarning
        )
        // ops hours
        val opsHoursData = CarouselData(
            carouselDataType = CarouselDataType.OPS_HOUR,
            title = resourceProvider.getOpsHoursTitle() ?: "",
            information = merchantProfile.opsHourFmt.content,
            isWarning = merchantProfile.opsHourFmt.isWarning
        )
        return listOf(ratingData, distanceData, estimationData, opsHoursData)
    }

    fun mapOpsHourDetailsToMerchantOpsHours(today: Int, opsHourDetails: List<TokoFoodMerchantOpsHour>): List<MerchantOpsHour> {
        return opsHourDetails.mapIndexed { index, opsHourDetail ->
            // response data from be always start from monday with index 0
            var day = index
            if (index == opsHourDetails.lastIndex) {
                // sunday index from be = 6 ; Calendar.SUNDAY = 1
                day -= DAYS_DECREASE
            } else {
                // monday index from be = 0 ; Calendar.MONDAY = 2
                day += DAYS_INCREASE
            }
            MerchantOpsHour(
                initial = opsHourDetail.day.firstOrNull(),
                day = opsHourDetail.day,
                time = opsHourDetail.time,
                isWarning = opsHourDetail.isWarning,
                isToday = day == today
            )
        }
    }

    fun mapFoodCategoriesToProductListItems(
        isShopClosed: Boolean,
        categories: List<TokoFoodCategoryCatalog>
    ): List<ProductListItem> {
        val productListItems = mutableListOf<ProductListItem>()
        categories.forEach { category ->
            val categoryHeader = ProductListItem(
                listItemType = ProductListItemType.CATEGORY_HEADER,
                productCategory = CategoryUiModel(
                    id = category.id,
                    key = category.key,
                    title = category.categoryName
                )
            )
            val products = category.catalogs
            val productCards = products.map { product ->
                ProductListItem(
                    listItemType = ProductListItemType.PRODUCT_CARD,
                    productUiModel = ProductUiModel(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        imageURL = product.imageURL,
                        price = product.price,
                        priceFmt = product.priceFmt,
                        slashPrice = product.slashPrice,
                        slashPriceFmt = product.slashPriceFmt,
                        isOutOfStock = product.isOutOfStock,
                        isShopClosed = isShopClosed,
                        customListItems = mapProductVariantsToCustomListItems(product.variants)
                    )
                )
            }
            productListItems.add(categoryHeader)
            productListItems.addAll(productCards)
        }
        return productListItems
    }

    private fun mapProductVariantsToCustomListItems(variants: List<TokoFoodCatalogVariantDetail>): List<CustomListItem> {
        val customListItems = mutableListOf<CustomListItem>()
        // add on selections widget
        variants.forEach { variant ->
            customListItems.add(
                CustomListItem(
                    listItemType = CustomListItemType.PRODUCT_ADD_ON,
                    addOnUiModel = mapVariantToAddOnUiModel(variant)
                )
            )
        }
        if (customListItems.isNotEmpty()) {
            // order input widget
            customListItems.add(
                CustomListItem(
                    listItemType = CustomListItemType.ORDER_NOTE_INPUT,
                    addOnUiModel = null
                )
            )
        }
        return customListItems.toList()
    }

    private fun mapVariantToAddOnUiModel(variant: TokoFoodCatalogVariantDetail): AddOnUiModel {
        return AddOnUiModel(
            id = variant.id,
            name = variant.name,
            isRequired = variant.isRequired,
            maxQty = variant.maxQty,
            minQty = variant.minQty,
            options = mapOptionDetailsToOptionUiModels(variant.minQty, variant.maxQty, variant.options),
            outOfStockWording = resourceProvider.getOutOfStockWording()
        )
    }

    private fun mapOptionDetailsToOptionUiModels(minQty: Int, maxQty: Int, optionDetails: List<TokoFoodCatalogVariantOptionDetail>): List<OptionUiModel> {
        return optionDetails.map { optionDetail ->
            OptionUiModel(
                isSelected = false,
                id = optionDetail.id,
                status = optionDetail.status,
                name = optionDetail.name,
                price = optionDetail.price,
                priceFmt = optionDetail.priceFmt,
                selectionControlType = getSelectionControlType(minQty, maxQty)
            )
        }
    }

    private fun getSelectionControlType(minQty: Int, maxQty: Int): SelectionControlType {
        return when {
            maxQty > Int.ONE -> MULTIPLE_SELECTION
            minQty == Int.ZERO && maxQty == Int.ONE -> MULTIPLE_SELECTION
            else -> SINGLE_SELECTION
        }
    }

    fun applyProductSelection(productListItems: List<ProductListItem>, selectedProducts: List<CartListCartGroupCart>): List<ProductListItem> {
        val mutableProductListItems = productListItems.toMutableList()
        val selectedProductMap = selectedProducts.groupBy { it.productId }
        selectedProductMap.forEach { entry ->
            if (entry.value.first().customResponse.variants.isNotEmpty()) {
                val selectedProductListItems = mutableProductListItems.filter { productListItem ->
                    productListItem.productUiModel.id == entry.key
                }
                selectedProductListItems.forEach {
                    with(it.productUiModel) {
                        isAtc = true
                        customOrderDetails = mapTokoFoodProductsToCustomOrderDetails(entry.value)
                    }
                }
            } else {
                // NON-VARIANT PRODUCT ORDER
                val selectedProduct = entry.value.first()
                val selectedProductListItems = mutableProductListItems.filter { productListItem ->
                    productListItem.productUiModel.id == entry.key
                }
                selectedProductListItems.forEach {
                    with(it.productUiModel) {
                        isAtc = true
                        cartId = selectedProduct.cartId
                        orderQty = selectedProduct.quantity
                        orderNote = selectedProduct.customResponse.notes
                    }
                }
            }
        }
        return mutableProductListItems.toList()
    }

    fun getAppliedProductSelection(): List<ProductListItem>? {
        val successMerchantData = (getMerchantDataResultLiveData.value as? Success)?.data
        val tokofoodGetMerchantData = successMerchantData?.tokofoodGetMerchantData
        return tokofoodGetMerchantData?.let { merchantData ->
            val isShopClosed = merchantData.merchantProfile.opsHourFmt.isWarning
            val foodCategories = merchantData.categories
            val productListItems = mapFoodCategoriesToProductListItems(
                isShopClosed,
                foodCategories
            )
            applyProductSelection(productListItems, selectedProducts)
        }
    }

    fun mapProductUiModelToAtcRequestParam(shopId: String, productUiModel: ProductUiModel): UpdateParam {
        return TokoFoodMerchantUiModelMapper.mapProductUiModelToAtcRequestParam(
            shopId = shopId,
            productUiModels = listOf(productUiModel)
        )
    }

    fun mapCustomOrderDetailToAtcRequestParam(shopId: String, productId: String, customOrderDetail: CustomOrderDetail): UpdateParam {
        return TokoFoodMerchantUiModelMapper.mapCustomOrderDetailToAtcRequestParam(
            shopId = shopId,
            cartId = customOrderDetail.cartId,
            productId = productId,
            customOrderDetails = listOf(customOrderDetail)
        )
    }

    fun mapCartTokoFoodToCustomOrderDetail(cartTokoFood: CartListCartGroupCart, productUiModel: ProductUiModel): CustomOrderDetail? {
        if (!productUiModel.isCustomizable) return null
        resetMasterData(productUiModel.customListItems)
        return TokoFoodMerchantUiModelMapper.mapCartTokoFoodToCustomOrderDetail(
            cartTokoFood = cartTokoFood,
            productUiModel = productUiModel
        )
    }

    fun updateQuantity(
        cartId: String,
        quantity: Int
    ) {
        _currentProductsWithUpdatedQuantity[cartId]
        _currentProductsWithUpdatedQuantity[cartId] = quantity

        viewModelScope.launch {
            _quantityUpdateState.emit(true)
        }
    }

    private fun getCurrentQuantityUpdateParams(): List<UpdateQuantityTokofoodParam> {
        return _currentProductsWithUpdatedQuantity.entries.map {
            UpdateQuantityTokofoodParam(
                source = SOURCE,
                businessData = UpdateQuantityTokofoodBusinessData(
                    businessId = TokoFoodCartUtil.getBusinessId(),
                    carts = listOf(
                        UpdateQuantityTokofoodCart(
                            cartId = it.key,
                            quantity = it.value
                        )
                    )
                )
            )
        }
    }

    private fun resetMasterData(customListItems: List<CustomListItem>): List<CustomListItem> {
        customListItems.forEach {
            it.orderNote = String.EMPTY
            it.addOnUiModel?.run {
                isSelected = false
                options.forEach { optionUiModel ->
                    optionUiModel.isSelected = false
                }
                selectedAddOns = listOf()
            }
        }
        return customListItems
    }

    private fun getMvcData(topBanner: TokoFoodTopBanner): MvcData? {
        return topBanner.takeIf { it.isShown }?.let {
            MvcData(
                listOf(
                    AnimatedInfos(
                        title = it.title,
                        subTitle = it.subtitle,
                        iconURL = it.imageUrl
                    )
                )
            )
        }
    }

    fun isTickerDetailEmpty(tickerData: TokoFoodTickerDetail): Boolean {
        return tickerData.title.isBlank() && tickerData.subtitle.isBlank()
    }

    companion object {
        private const val DAYS_DECREASE = 5
        private const val DAYS_INCREASE = 2

        private const val UPDATE_QUANTITY_DEBOUNCE = 500L
        private const val SOURCE = "merchant_page"
    }
}
