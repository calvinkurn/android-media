package com.tkpd.atc_variant.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkpd.atc_variant.data.uidata.PartialButtonDataModel
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.data.uidata.VariantShimmeringDataModel
import com.tkpd.atc_variant.usecase.GetProductVariantAggregatorUseCase
import com.tkpd.atc_variant.util.AtcCommonMapper
import com.tkpd.atc_variant.util.AtcCommonMapper.asFail
import com.tkpd.atc_variant.util.AtcCommonMapper.asSuccess
import com.tkpd.atc_variant.util.AtcVariantMapper
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Yehezkiel on 10/05/21
 */
class AtcVariantViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val aggregatorUseCase: GetProductVariantAggregatorUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val addToCartOcsUseCase: AddToCartOcsUseCase,
        private val addToCartOccUseCase: AddToCartOccUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        private val addWishListUseCase: AddWishListUseCase
) : ViewModel() {

    private val _initialData = MutableLiveData<Result<List<AtcVariantVisitable>>>()
    val initialData: LiveData<Result<List<AtcVariantVisitable>>>
        get() = _initialData

    private val _buttonData = MutableLiveData<Result<PartialButtonDataModel>>()
    val buttonData: LiveData<Result<PartialButtonDataModel>>
        get() = _buttonData

    private val _aggregatorData = MutableLiveData<ProductVariantAggregatorUiData>()
    val aggregatorData: LiveData<ProductVariantAggregatorUiData>
        get() = _aggregatorData

    private val _variantActivityResult = MutableLiveData<ProductVariantResult>()
    val variantActivityResult: LiveData<ProductVariantResult>
        get() = _variantActivityResult

    private val _addToCartLiveData = MutableLiveData<Result<AddToCartDataModel>>()
    val addToCartLiveData: LiveData<Result<AddToCartDataModel>>
        get() = _addToCartLiveData

    fun onVariantClicked(selectedOptionKey: String,
                         selectedOptionId: String,
                         variantImage: String, // only use when user click partially to update the image
                         variantLevel: Int) {
        viewModelScope.launchCatchError(dispatcher.io, block = {

            val selectedVariantIds = updateSelectedOptionIds(selectedOptionKey, selectedOptionId)

            //Run variant logic to determine selected , empty , etc
            val processedVariant = AtcVariantMapper.processVariant(_aggregatorData.value?.variantData,
                    selectedVariantIds,
                    variantLevel
            )

            val selectedVariantChild = _aggregatorData.value?.variantData?.getChildByOptionId(selectedVariantIds?.values?.toList()
                    ?: listOf())
            val cartData = AtcCommonMapper.mapToCartRedirectionData(selectedVariantChild, _aggregatorData.value?.cardRedirection)

            val isPartiallySelected = AtcVariantMapper.isPartiallySelectedOptionId(selectedVariantIds)
            val selectedWarehouse = getSelectedWarehouse(selectedVariantChild?.productId ?: "")

            //We update visitable to re-render selected variant and header
            val list = AtcCommonMapper.updateVisitable(
                    oldList = (_initialData.value as Success).data,
                    processedVariant = processedVariant,
                    isPartiallySelected = isPartiallySelected,
                    selectedVariantIds = selectedVariantIds,
                    variantImage = variantImage,
                    allChildEmpty = _aggregatorData.value?.variantData?.getBuyableVariantCount() == 0,
                    selectedVariantChild = selectedVariantChild,
                    selectedProductFulfillment = selectedWarehouse?.isFulfillment ?: false)

            _initialData.postValue(list.asSuccess())

            if (!isPartiallySelected) {
                // if user only select 1 of 2 variant, no need to update the button
                // this validation only be execute when user clicked variant
                _buttonData.postValue(cartData.asSuccess())
                _variantActivityResult.run {
                    postValue(AtcCommonMapper.updateActivityResultData(
                            recentData = value,
                            listVariant = processedVariant,
                            selectedProductId = selectedVariantChild?.productId ?: "",
                            mapOfSelectedVariantOption = selectedVariantIds))
                }
            }
        }) {}
    }

    private fun updateSelectedOptionIds(selectedOptionKey: String, selectedOptionId: String): MutableMap<String, String>? {
        val variantDataModel = (_initialData.value as Success).data.firstOrNull {
            it is VariantComponentDataModel
        } as? VariantComponentDataModel

        //Update selected variant id to existing options
        val selectedVariantIds = variantDataModel?.mapOfSelectedVariant?.toMutableMap()
        selectedVariantIds?.let { selectedIds ->
            selectedIds[selectedOptionKey] = selectedOptionId
        }
        return selectedVariantIds
    }

    fun getSelectedOptionIds(): MutableMap<String, String>? {
        val variantDataModel = (_initialData.value as Success).data.firstOrNull {
            it is VariantComponentDataModel
        } as? VariantComponentDataModel
        return variantDataModel?.mapOfSelectedVariant
    }

    fun decideInitialValue(aggregatorParams: ProductVariantBottomSheetParams) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            _initialData.postValue(listOf(VariantShimmeringDataModel(99L)).asSuccess())
            /**
             * If data completely provided from previous page, use that
             * if not call GQL
             */
            val aggregatorResult = if (aggregatorParams.variantAggregator.isAggregatorEmpty()) {
                aggregatorUseCase.executeOnBackground(GetProductVariantAggregatorUseCase.createRequestParams(aggregatorParams.productId,
                        aggregatorParams.pageSource, aggregatorParams.whId, aggregatorParams.pdpSession, UserLocationRequest()))
            } else {
                aggregatorParams.variantAggregator
            }

            _aggregatorData.postValue(aggregatorResult)

            //Get selected child by product id, if product parent auto select first child
            //If parent just update the header and ignore the variant selection
            val pairParentAndChild = aggregatorResult.variantData.getFirstChildIfParent(aggregatorParams.productId)
            val isParent = pairParentAndChild.first
            val selectedChild = pairParentAndChild.second

            //Get cart redirection , and warehouse by selected product id to render button and toko cabang
            val cartData = AtcCommonMapper.mapToCartRedirectionData(selectedChild, aggregatorResult.cardRedirection)
            val selectedWarehouse = getSelectedWarehouse(selectedChild?.productId ?: "")

            //Generate visitables
            val visitables = generateVisitable(selectedChild, selectedWarehouse?.isFulfillment
                    ?: false, aggregatorParams.isTokoNow, isParent, aggregatorResult.variantData)

            if (visitables != null) {
                _initialData.postValue(visitables.asSuccess())
            } else {
                _initialData.postValue(Throwable().asFail())
            }

            _buttonData.postValue(cartData.asSuccess())
        }) {
            _buttonData.postValue(it.asFail())
            _initialData.postValue(it.asFail())
        }
    }

    fun hitAtc(actionButton: Int,
               shopIdInt: Int,
               categoryName: String,
               userId: String,
               tradein: Boolean = false,
               shippingMinPrice: Int = 0,
               trackerAttributionPdp: String = "",
               trackerListNamePdp: String = "") {

        val selectedChild = _aggregatorData.value?.variantData?.getChildByOptionId(getSelectedOptionIds()?.values?.toList()
                ?: listOf())
        val selectedWarehouse = getSelectedWarehouse(selectedChild?.productId ?: "")

        when (actionButton) {
            ProductDetailCommonConstant.OCS_BUTTON -> {
                val addToCartOcsRequestParams = AddToCartOcsRequestParams().apply {
                    productId = selectedChild?.productId?.toLongOrZero() ?: 0L
                    shopId = shopIdInt
                    quantity = selectedChild?.getFinalMinOrder() ?: 0
                    notes = ""
                    customerId = userId.toIntOrZero()
                    warehouseId = selectedWarehouse?.id?.toIntOrZero() ?: 0
                    trackerAttribution = trackerAttributionPdp
                    trackerListName = trackerListNamePdp
                    isTradeIn = tradein
                    shippingPrice = shippingMinPrice
                    productName = selectedChild?.name ?: ""
                    category = categoryName
                    price = selectedChild?.finalPrice?.toString() ?: ""
                    this.userId = userId
                }
                addToCart(addToCartOcsRequestParams)
            }
            ProductDetailCommonConstant.OCC_BUTTON -> {
                val addToCartOccRequestParams = AddToCartOccRequestParams(
                        productId = selectedChild?.productId ?: "",
                        shopId = shopIdInt.toString(),
                        quantity = selectedChild?.getFinalMinOrder().toString()
                ).apply {
                    warehouseId = selectedWarehouse?.id ?: ""
                    attribution = trackerAttributionPdp
                    listTracker = trackerListNamePdp
                    productName = selectedChild?.name ?: ""
                    category = categoryName
                    price = selectedChild?.finalPrice?.toString() ?: ""
                    this.userId = userId
                }
                addToCart(addToCartOccRequestParams)
            }
            else -> {
                val addToCartRequestParams = AddToCartRequestParams().apply {
                    productId = selectedChild?.productId?.toLongOrZero() ?: 0L
                    shopId = shopIdInt
                    quantity = selectedChild?.getFinalMinOrder() ?: 0
                    notes = ""
                    attribution = trackerAttributionPdp
                    listTracker = trackerListNamePdp
                    warehouseId = selectedWarehouse?.id?.toIntOrZero() ?: 0
                    atcFromExternalSource = AddToCartRequestParams.ATC_FROM_PDP
                    productName = selectedChild?.name ?: ""
                    category = categoryName
                    price = selectedChild?.finalPrice?.toString() ?: ""
                    this.userId = userId
                }
                addToCart(addToCartRequestParams)
            }
        }
    }

    private fun getSelectedWarehouse(productId: String): WarehouseInfo? {
        return _aggregatorData.value?.nearestWarehouse?.get(productId)
    }

    private fun addToCart(atcParams: Any) {
        viewModelScope.launchCatchError(block = {
            val requestParams = RequestParams.create()
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, atcParams)

            when (atcParams) {
                is AddToCartRequestParams -> {
                    getAddToCartUseCase(requestParams)
                }
                is AddToCartOcsRequestParams -> {
                    getAddToCartOcsUseCase(requestParams)
                }
                is AddToCartOccRequestParams -> {
                    getAddToCartOccUseCase(requestParams)
                }
            }
        }) {
            _addToCartLiveData.value = it.cause?.asFail() ?: it.asFail()
        }
    }

    private suspend fun getAddToCartUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io) {
            val result = addToCartUseCase.createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                val errorMessage = result.errorMessage.firstOrNull() ?: ""
//                if (errorMessage.isNotBlank()) {
//                    ProductDetailLogger.logMessage(errorMessage, ATC_ERROR_TYPE, getDynamicProductInfoP1?.basic?.productID
//                            ?: "", deviceId)
//                }
                _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private suspend fun getAddToCartOcsUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io) {
            val result = addToCartOcsUseCase.createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                val errorMessage = result.errorMessage.firstOrNull() ?: ""

                _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private suspend fun getAddToCartOccUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io) {
            val result = addToCartOccUseCase.createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                val errorMessage = result.getAtcErrorMessage() ?: ""
//                if (errorMessage.isNotBlank()) {
//                    ProductDetailLogger.logMessage(errorMessage, ATC_ERROR_TYPE, getDynamicProductInfoP1?.basic?.productID
//                            ?: "", deviceId)
//                }
                _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private fun generateVisitable(selectedVariantChild: VariantChild?, isFulfillment: Boolean, isTokoNow: Boolean, isParent: Boolean, variantData: ProductVariant): List<AtcVariantVisitable>? {
        val selectedOptionIds = AtcCommonMapper.determineSelectedOptionIds(isParent, variantData, selectedVariantChild)
        return AtcCommonMapper.mapToVisitable(
                selectedChild = selectedVariantChild,
                isTokoNow = isTokoNow,
                initialSelectedVariant = selectedOptionIds,
                processedVariant = AtcVariantMapper.processVariant(variantData, selectedOptionIds),
                selectedProductFulfillment = isFulfillment,
                totalStock = variantData.totalStockChilds)
    }
}