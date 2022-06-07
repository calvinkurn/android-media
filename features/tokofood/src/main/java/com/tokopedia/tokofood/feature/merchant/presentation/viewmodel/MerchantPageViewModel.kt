package com.tokopedia.tokofood.feature.merchant.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.CurrencyFormatter
import com.tokopedia.tokofood.common.util.ResourceProvider
import com.tokopedia.tokofood.feature.merchant.domain.model.response.*
import com.tokopedia.tokofood.feature.merchant.domain.usecase.GetMerchantDataUseCase
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CarouselDataType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType.MULTIPLE_SELECTION
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType.SINGLE_SELECTION
import com.tokopedia.tokofood.feature.merchant.presentation.mapper.TokoFoodMerchantUiModelMapper
import com.tokopedia.tokofood.feature.merchant.presentation.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MerchantPageViewModel @Inject constructor(
        private val resourceProvider: ResourceProvider,
        private val dispatchers: CoroutineDispatchers,
        private val getMerchantDataUseCase: GetMerchantDataUseCase
) : BaseViewModel(dispatchers.main) {

    private val getMerchantDataResultLiveData = SingleLiveEvent<Result<GetMerchantDataResponse>>()
    val getMerchantDataResult: SingleLiveEvent<Result<GetMerchantDataResponse>> get() = getMerchantDataResultLiveData

    // map of productId to card positions info <dataset,adapter>
    val productMap: HashMap<String, Pair<Int, Int>> = hashMapOf()

    var productListItems: MutableList<ProductListItem> = mutableListOf()

    var selectedProducts: List<CheckoutTokoFoodProduct> = listOf()

    fun getDataSetPosition(cardPositions: Pair<Int, Int>): Int {
        return cardPositions.first
    }

    fun getAdapterPosition(cardPositions: Pair<Int, Int>): Int {
        return cardPositions.second
    }

    var filterList = listOf<TokoFoodCategoryFilter>()

    var merchantData: TokoFoodMerchantProfile? = null

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
            merchantData = result.tokofoodGetMerchantData.merchantProfile
            getMerchantDataResultLiveData.value = Success(result)
        }, onError = {
            getMerchantDataResultLiveData.value = Fail(it)
        })
    }

    fun updateFilterList(filterList: List<TokoFoodCategoryFilter>) {
        this.filterList = filterList
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

    fun mapOpsHourDetailsToMerchantOpsHours(opsHourDetails: List<TokoFoodMerchantOpsHour>): List<MerchantOpsHour> {
        return opsHourDetails.mapIndexed { index, opsHourDetail ->
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            // response data from be always start from monday with index 0
            // monday index from be = 0 ; Calendar.MONDAY = 2
            var day = index + 2
            // sunday index from be = 6 ; Calendar.SUNDAY = 1
            if (index == opsHourDetails.lastIndex) {
                day -= 5
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
            categories: List<TokoFoodCategoryCatalog>): List<ProductListItem> {
        val productListItems = mutableListOf<ProductListItem>()
        categories.forEach { category ->
            val categoryHeader = ProductListItem(
                    listItemType = ProductListItemType.CATEGORY_HEADER,
                    productCategory = CategoryUiModel(
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
                options = mapOptionDetailsToOptionUiModels(variant.maxQty, variant.options)
        )
    }

    private fun mapOptionDetailsToOptionUiModels(maxQty: Int, optionDetails: List<TokoFoodCatalogVariantOptionDetail>): List<OptionUiModel> {
        return optionDetails.map { optionDetail ->
            OptionUiModel(
                    isSelected = false,
                    id = optionDetail.id,
                    name = optionDetail.name,
                    price = optionDetail.price,
                    priceFmt = optionDetail.priceFmt,
                    selectionControlType = if (maxQty > Int.ONE) MULTIPLE_SELECTION else SINGLE_SELECTION
            )
        }
    }

    fun applyProductSelection(productListItems: List<ProductListItem>, selectedProducts: List<CheckoutTokoFoodProduct>): List<ProductListItem> {
        val mutableProductListItems = productListItems.toMutableList()
        val selectedProductMap = selectedProducts.groupBy { it.productId }
        selectedProductMap.forEach { entry ->
            if (entry.value.first().variants.isNotEmpty()) {
                val selectedProductListItem = mutableProductListItems.firstOrNull() { productListItem ->
                    productListItem.productUiModel.id == entry.key
                }
                selectedProductListItem?.productUiModel?.apply {
                    isAtc = true
                    customOrderDetails = mapTokoFoodProductsToCustomOrderDetails(entry.value)
                }
            } else {
                // NON-VARIANT PRODUCT ORDER
                val selectedProduct = entry.value.first()
                val selectedProductListItem = mutableProductListItems.firstOrNull() { productListItem ->
                    productListItem.productUiModel.id == entry.key
                }
                selectedProductListItem?.productUiModel?.apply {
                    isAtc = true
                    cartId = selectedProduct.cartId
                    orderQty = selectedProduct.quantity
                    orderNote = selectedProduct.notes
                }
            }
        }
        return mutableProductListItems.toList()
    }

    private fun mapTokoFoodProductsToCustomOrderDetails(tokoFoodProducts: List<CheckoutTokoFoodProduct>): MutableList<CustomOrderDetail> {
        return tokoFoodProducts.map { product ->
            val subtotal = calculateSubtotalPrice(product)
            CustomOrderDetail(
                    cartId = product.cartId,
                    subTotal = subtotal,
                    subTotalFmt = CurrencyFormatter.formatToRupiahFormat(subtotal),
                    qty = product.quantity,
                    customListItems = mapTokoFoodVariantsToCustomListItems(
                            variants = product.variants,
                            orderNote = product.notes
                    )
            )
        }.toMutableList()
    }

    private fun calculateSubtotalPrice(product: CheckoutTokoFoodProduct): Double {
        val quantity = product.quantity
        val baseProductPrice = product.price
        var subTotalPrice = baseProductPrice
        product.variants.forEach { variant ->
            val selectedOptions = variant.options.filter { it.isSelected }
            val subTotalOptionPrice = selectedOptions.sumOf { it.price }
            subTotalPrice += subTotalOptionPrice
        }
        return subTotalPrice * quantity
    }

    private fun mapTokoFoodVariantsToCustomListItems(variants: List<CheckoutTokoFoodProductVariant>, orderNote: String): List<CustomListItem> {
        val customListItems = mutableListOf<CustomListItem>()
        val addOns = variants.map { variant ->
            val options = variant.options.map { option ->
                OptionUiModel(
                        isSelected = option.isSelected,
                        id = option.optionId,
                        name = option.name,
                        price = option.price,
                        priceFmt = option.priceFmt,
                        selectionControlType = if (variant.rules.selectionRules.maxQuantity > Int.ONE) MULTIPLE_SELECTION else SINGLE_SELECTION
                )
            }
            CustomListItem(
                    listItemType = CustomListItemType.PRODUCT_ADD_ON,
                    addOnUiModel = AddOnUiModel(
                            id = variant.variantId,
                            name = variant.name,
                            isRequired = variant.rules.selectionRules.isRequired,
                            isSelected = variant.options.count { it.isSelected } != Int.ZERO,
                            maxQty = variant.rules.selectionRules.maxQuantity,
                            minQty = variant.rules.selectionRules.minQuantity,
                            options = options,
                            selectedAddOns = options.filter { it.isSelected }.map { it.name }
                    )
            )
        }
        val noteInput = CustomListItem(listItemType = CustomListItemType.ORDER_NOTE_INPUT, orderNote = orderNote, addOnUiModel = null)
        customListItems.addAll(addOns)
        customListItems.add(noteInput)
        return customListItems.toList()
    }

    fun mapProductUiModelToAtcRequestParam(shopId: String, productUiModel: ProductUiModel): UpdateParam {
        return TokoFoodMerchantUiModelMapper.mapProductUiModelToAtcRequestParam(
                cartId = productUiModel.cartId,
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

    fun mapCartTokoFoodToCustomOrderDetail(cartTokoFood: CartTokoFood, productUiModel: ProductUiModel): CustomOrderDetail {
        return TokoFoodMerchantUiModelMapper.mapCartTokoFoodToCustomOrderDetails(
                cartTokoFood = cartTokoFood,
                productUiModel = productUiModel
        )
    }

    fun isTickerDetailEmpty(tickerData: TokoFoodTickerDetail): Boolean {
        return tickerData.title.isBlank() && tickerData.subtitle.isBlank()
    }
}