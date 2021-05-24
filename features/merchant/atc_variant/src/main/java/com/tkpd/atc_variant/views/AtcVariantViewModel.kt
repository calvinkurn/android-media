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
import com.tokopedia.product.detail.common.AtcVariantMapper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
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
        private val addWishListUseCase: AddWishListUseCase
) : ViewModel() {

    //This livedata is only for access variant,cartRedirection, and warehouse locally in viewmodel
    private val aggregatorData = MutableLiveData<ProductVariantAggregatorUiData>()

    private val _initialData = MutableLiveData<Result<List<AtcVariantVisitable>>>()
    val initialData: LiveData<Result<List<AtcVariantVisitable>>>
        get() = _initialData

    private val _buttonData = MutableLiveData<Result<PartialButtonDataModel>>()
    val buttonData: LiveData<Result<PartialButtonDataModel>>
        get() = _buttonData

    private val _variantActivityResult = MutableLiveData<ProductVariantResult>()
    val variantActivityResult: LiveData<ProductVariantResult>
        get() = _variantActivityResult

    private val _addToCartLiveData = MutableLiveData<Result<AddToCartDataModel>>()
    val addToCartLiveData: LiveData<Result<AddToCartDataModel>>
        get() = _addToCartLiveData

    private val _addWishlistResult = MutableLiveData<Result<Boolean>>()
    val addWishlistResult: LiveData<Result<Boolean>>
        get() = _addWishlistResult

    fun onVariantClicked(selectedOptionKey: String,
                         selectedOptionId: String,
                         variantImage: String, // only use when user click partially to update the image
                         variantLevel: Int) {
        viewModelScope.launchCatchError(dispatcher.io, block = {

            val selectedVariantIds = updateSelectedOptionIds(selectedOptionKey, selectedOptionId)

            //Run variant logic to determine selected , empty , flash sale, etc
            val processedVariant = AtcVariantMapper.processVariant(getVariantData(),
                    selectedVariantIds,
                    variantLevel
            )

            val selectedVariantChild = getVariantData()?.getChildByOptionId(selectedVariantIds?.values?.toList()
                    ?: listOf())
            val cartData = AtcCommonMapper.mapToCartRedirectionData(selectedVariantChild, aggregatorData.value?.cardRedirection)

            val isPartiallySelected = AtcVariantMapper.isPartiallySelectedOptionId(selectedVariantIds)
            val selectedWarehouse = getSelectedWarehouse(selectedVariantChild?.productId ?: "")

            //We update visitable to re-render selected variant and header
            val list = AtcCommonMapper.updateVisitable(
                    oldList = (_initialData.value as Success).data,
                    processedVariant = processedVariant,
                    isPartiallySelected = isPartiallySelected,
                    selectedVariantIds = selectedVariantIds,
                    variantImage = variantImage,
                    allChildEmpty = getVariantData()?.getBuyableVariantCount() == 0,
                    selectedVariantChild = selectedVariantChild,
                    selectedProductFulfillment = selectedWarehouse?.isFulfillment ?: false)

            _initialData.postValue(list.asSuccess())

            if (!isPartiallySelected) {
                // if user only select 1 of 2 variant, no need to update the button
                // this validation only be execute when user clicked variant and fully clicked 2 of 2 variant or 1 of 1
                _buttonData.postValue(cartData.asSuccess())
                updateActivityResult(
                        listVariant = processedVariant,
                        selectedProductId = selectedVariantChild?.productId ?: "",
                        mapOfSelectedVariantOption = selectedVariantIds)
            }
        }) {

        }
    }

    fun getVariantData(): ProductVariant? {
        return aggregatorData.value?.variantData
    }

    fun updateActivityResult(listVariant: List<VariantCategory>? = null,
                             selectedProductId: String? = null,
                             mapOfSelectedVariantOption: MutableMap<String, String>? = null,
                             atcSuccessMessage: String? = null,
                             shouldRefreshPreviousPage: Boolean? = null,
                             requestCode: Int? = null) {

        _variantActivityResult.run {
            postValue(AtcCommonMapper.updateActivityResultData(
                    recentData = value,
                    listVariant = listVariant,
                    parentProductId = getVariantData()?.parentId,
                    selectedProductId = selectedProductId,
                    mapOfSelectedVariantOption = mapOfSelectedVariantOption,
                    atcMessage = atcSuccessMessage,
                    requestCode = requestCode,
                    shouldRefreshPreviousPage = shouldRefreshPreviousPage))
        }
    }

    /**
     * Get current selected variant option id
     * if user already choose 2, the result will be sometng like this (warna, merah), (ukuran,L)
     * if user only choose 1 level of 2, the result will be like (warna,merah), (ukuran,0)
     */
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

            aggregatorData.postValue(aggregatorResult)

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

    fun addWishlist(productId: String, userId: String, btnTextAfterAction: String) {
        addWishListUseCase.createObservable(productId,
                userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
//                if (!(errorMessage.isNullOrEmpty() || productId.isNullOrEmpty())) {
////                    val extras = mapOf("wishlist_status" to ADD_WISHLIST).toString()
////                    ProductDetailLogger.logMessage(errorMessage, WISHLIST_ERROR_TYPE, productId, deviceId, extras)
//                }
                _addWishlistResult.postValue(Throwable(errorMessage ?: "").asFail())
            }

            override fun onSuccessAddWishlist(productId: String?) {
                updateActivityResult(shouldRefreshPreviousPage = true)
                updateButtonAndWishlistLocally(productId ?: "", btnTextAfterAction)
                _addWishlistResult.postValue(true.asSuccess())
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                // no op
            }
        })
    }

    private fun updateButtonAndWishlistLocally(productId: String, btnText: String) {
        updateCartRedirectionData(productId, btnText)
        //update wishlist in child locally
        val selectedChild = getVariantData()?.getChildByProductId(productId)
        selectedChild?.isWishlist = true

        val generateCartRedir = AtcCommonMapper.mapToCartRedirectionData(getVariantData()?.getChildByProductId(productId), aggregatorData.value?.cardRedirection)
        _buttonData.postValue(generateCartRedir.asSuccess())
    }

    private fun updateCartRedirectionData(productId: String, btnText: String) {
        val cartType = ProductDetailCommonConstant.KEY_CART_TYPE_CHECK_WISHLIST
        val btnColor = ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GRAY

        //update cart redir localy
        aggregatorData.value?.cardRedirection?.let {
            it[productId]?.availableButtons = listOf(it[productId]?.availableButtons?.firstOrNull()?.copy(
                    cartType = cartType,
                    color = btnColor,
                    text = btnText
            ) ?: return@let)
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
        val selectedChild = getVariantData()?.getChildByOptionId(getSelectedOptionIds()?.values?.toList()
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

    /**
     * Update selection by key eg:
     *  - Before update (warna, 0), (ukuran, 0)
     *  - After update (warna, merah), (ukuran, 0)
     */
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

    private fun getSelectedWarehouse(productId: String): WarehouseInfo? {
        return aggregatorData.value?.nearestWarehouse?.get(productId)
    }
}