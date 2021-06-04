package com.tkpd.atc_variant.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkpd.atc_variant.data.uidata.PartialButtonDataModel
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.data.uidata.VariantShimmeringDataModel
import com.tkpd.atc_variant.usecase.GetAggregatorAndMiniCartUseCase
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
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.usecase.UpdateCartUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.AtcVariantMapper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
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
        private val aggregatorMiniCartUseCase: GetAggregatorAndMiniCartUseCase,
        private val addToCartUseCase: AddToCartUseCase,
        private val addToCartOcsUseCase: AddToCartOcsUseCase,
        private val addToCartOccUseCase: AddToCartOccUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val updateCartUseCase: UpdateCartUseCase
) : ViewModel() {

    //This livedata is only for access variant,cartRedirection, and warehouse locally in viewmodel
    private var aggregatorData: ProductVariantAggregatorUiData? = null
    private var minicartData: MutableMap<String, MiniCartItem>? = null
    private var localQuantityData: MutableMap<String, Int> = mutableMapOf()

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

    private val _updateCartLiveData = MutableLiveData<Result<String>>()
    val updateCartLiveData: LiveData<Result<String>>
        get() = _updateCartLiveData

    private val _addWishlistResult = MutableLiveData<Result<Boolean>>()
    val addWishlistResult: LiveData<Result<Boolean>>
        get() = _addWishlistResult

    private val _titleVariantName = MutableLiveData<String>()
    val titleVariantName: LiveData<String>
        get() = _titleVariantName

    private var isShopOwner: Boolean = false

    fun onVariantClicked(isTokoNow: Boolean,
                         selectedOptionKey: String,
                         selectedOptionId: String,
                         variantImage: String, // only use when user click partially to update the image
                         variantLevel: Int) {
        viewModelScope.launchCatchError(dispatcher.io, block = {

            val selectedVariantIds = updateSelectedOptionIdsVisitable(selectedOptionKey, selectedOptionId)

            //Run variant logic to determine selected , empty , flash sale, etc
            val processedVariant = AtcVariantMapper.processVariant(getVariantData(),
                    selectedVariantIds,
                    variantLevel
            )

            val selectedVariantChild = getVariantData()?.getChildByOptionId(selectedVariantIds?.values?.toList()
                    ?: listOf())
            val selectedMiniCart = minicartData?.get(selectedVariantChild?.productId ?: "")
            val cartData = AtcCommonMapper.mapToCartRedirectionData(selectedVariantChild, aggregatorData?.cardRedirection, isShopOwner, selectedMiniCart != null)

            val isPartiallySelected = AtcVariantMapper.isPartiallySelectedOptionId(selectedVariantIds)
            val selectedWarehouse = getSelectedWarehouse(selectedVariantChild?.productId ?: "")
            val selectedQuantity = localQuantityData[selectedVariantChild?.productId ?: ""] ?: 0

            //We update visitable to re-render selected variant and header
            val list = AtcCommonMapper.updateVisitable(
                    oldList = (_initialData.value as Success).data,
                    processedVariant = processedVariant,
                    isPartiallySelected = isPartiallySelected,
                    selectedVariantIds = selectedVariantIds,
                    allChildEmpty = getVariantData()?.getBuyableVariantCount() == 0,
                    selectedVariantChild = selectedVariantChild,
                    variantImage = variantImage,
                    selectedProductFulfillment = selectedWarehouse?.isFulfillment ?: false,
                    isTokoNow = isTokoNow,
                    selectedQuantity = selectedQuantity)

            _initialData.postValue(list.asSuccess())
            _titleVariantName.postValue(AtcVariantMapper.getSelectedVariantName(processedVariant, selectedVariantChild))

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
        return aggregatorData?.variantData
    }

    /**
     * Get current selected variant option id
     * if user already choose 2, the result will be sometng like this (warna, merah), (ukuran,L)
     * if user only choose 1 level of 2, the result will be like (warna,merah), (ukuran,0)
     */
    fun getSelectedOptionIds(): MutableMap<String, String>? {
        val variantDataModel = (_initialData.value as? Success)?.data?.firstOrNull {
            it is VariantComponentDataModel
        } as? VariantComponentDataModel
        return variantDataModel?.mapOfSelectedVariant
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

    fun updateQuantity(quantity: Int, productId: String) {
        localQuantityData[productId] = quantity
    }

    fun decideInitialValue(aggregatorParams: ProductVariantBottomSheetParams) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            _initialData.postValue(listOf(VariantShimmeringDataModel(99L)).asSuccess())
            isShopOwner = aggregatorParams.isShopOwner

            getAggregatorAndMiniCartData(aggregatorParams)

            //Get selected child by product id, if product parent auto select first child
            //If parent just update the header and ignore the variant selection
            val pairIsParentAndChild = aggregatorData?.variantData?.autoSelectChildIfGivenIdIsParent(aggregatorParams.productId)
            val isParent = pairIsParentAndChild?.first ?: false
            val selectedChild = pairIsParentAndChild?.second

            //Get cart redirection , and warehouse by selected product id to render button and toko cabang
            val selectedMiniCart = minicartData?.get(selectedChild?.productId ?: "")
            val cartData = AtcCommonMapper.mapToCartRedirectionData(selectedChild, aggregatorData?.cardRedirection, isShopOwner, selectedMiniCart != null)
            val selectedWarehouse = getSelectedWarehouse(selectedChild?.productId ?: "")

            //generate variant component and data, initial render need to determine selected option
            val initialSelectedOptionIds = AtcCommonMapper.determineSelectedOptionIds(isParent, aggregatorData?.variantData, selectedChild)
            val processedVariant = AtcVariantMapper.processVariant(aggregatorData?.variantData, initialSelectedOptionIds)

            assignLocalQuantityWithMiniCartQuantity(minicartData?.values?.toList())
            val selectedQuantity = localQuantityData[selectedChild?.productId ?: ""] ?: 0

            //Generate visitables
            val visitables = AtcCommonMapper.mapToVisitable(
                    selectedChild = selectedChild,
                    isTokoNow = aggregatorParams.isTokoNow,
                    initialSelectedVariant = initialSelectedOptionIds,
                    processedVariant = processedVariant,
                    selectedProductFulfillment = selectedWarehouse?.isFulfillment ?: false,
                    totalStock = aggregatorData?.variantData?.totalStockChilds ?: 0,
                    selectedQuantity = selectedQuantity)

            if (visitables != null) {
                _titleVariantName.postValue(AtcVariantMapper.getSelectedVariantName(processedVariant, selectedChild))
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

    private fun assignLocalQuantityWithMiniCartQuantity(miniCart: List<MiniCartItem>?) {
        if (miniCart == null) return
        miniCart.forEach {
            localQuantityData[it.productId] = it.quantity
        }
    }

    private suspend fun getAggregatorAndMiniCartData(aggregatorParams: ProductVariantBottomSheetParams) {
        /**
         * If data completely provided from previous page, use that
         * if not call GQL
         */
        if (aggregatorParams.variantAggregator.isAggregatorEmpty() || (aggregatorParams.isTokoNow && aggregatorParams.miniCartData == null)) {
            val result = aggregatorMiniCartUseCase.executeOnBackground(
                    aggregatorMiniCartUseCase.createAggregatorRequestParams(aggregatorParams.productId,
                            aggregatorParams.pageSource, aggregatorParams.whId, aggregatorParams.pdpSession),
                    aggregatorParams.shopId, aggregatorParams.isTokoNow
            )
            aggregatorData = result.variantAggregator
            minicartData = result.miniCartData?.toMutableMap()
        } else {
            aggregatorData = aggregatorParams.variantAggregator
            minicartData = aggregatorParams.miniCartData?.toMutableMap()
        }
    }

    fun addWishlist(productId: String, userId: String, btnTextAfterAction: String) {
        addWishListUseCase.createObservable(productId,
                userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
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
        updateRemindMeCartRedirection(productId, btnText)
        //update wishlist in child locally
        val selectedChild = getVariantData()?.getChildByProductId(productId)
        selectedChild?.isWishlist = true

        val generateCartRedir = AtcCommonMapper.mapToCartRedirectionData(getVariantData()?.getChildByProductId(productId), aggregatorData?.cardRedirection)
        _buttonData.postValue(generateCartRedir.asSuccess())
    }

    private fun updateMiniCartAndButtonData(productId: String, quantity: Int, isTokoNow: Boolean, cartId: String = "", notes: String = "") {
        if (!isTokoNow) return
        val selectedMiniCartData = minicartData?.get(productId)

        if (selectedMiniCartData == null) {
            minicartData?.set(productId, MiniCartItem(
                    cartId = cartId,
                    productId = productId,
                    quantity = quantity,
                    notes = notes
            ))
        } else {
            minicartData?.get(productId)?.quantity = quantity
        }

        val generateCartRedir = AtcCommonMapper.mapToCartRedirectionData(getVariantData()?.getChildByProductId(productId), aggregatorData?.cardRedirection, isShopOwner, true)
        _buttonData.postValue(generateCartRedir.asSuccess())
    }

    private fun updateRemindMeCartRedirection(productId: String, btnText: String) {
        val cartType = ProductDetailCommonConstant.KEY_CART_TYPE_CHECK_WISHLIST
        val btnColor = ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GRAY

        //update cart redir localy
        aggregatorData?.cardRedirection?.let {
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
               shippingMinPrice: Int,
               trackerAttributionPdp: String,
               trackerListNamePdp: String,
               isTokoNow: Boolean) {
        val selectedChild = getVariantData()?.getChildByOptionId(getSelectedOptionIds()?.values?.toList()
                ?: listOf())
        val selectedWarehouse = getSelectedWarehouse(selectedChild?.productId ?: "")
        val selectedMiniCart = getSelectedMiniCartItem(selectedChild?.productId ?: "")

        if (selectedMiniCart != null && isTokoNow) {
            val updatedQuantity = localQuantityData[selectedChild?.productId ?: ""]
                    ?: selectedChild?.getFinalMinOrder() ?: 1
            getUpdateCartUseCase(selectedMiniCart, updatedQuantity, isTokoNow)
        } else {
            val atcRequestParam = AtcCommonMapper.generateAtcData(actionButton, selectedChild, selectedWarehouse, shopIdInt, trackerAttributionPdp, trackerListNamePdp, categoryName, shippingMinPrice, userId)
            addToCart(atcRequestParam, isTokoNow)
        }
    }

    private fun addToCart(atcParams: Any, isTokoNow: Boolean) {
        viewModelScope.launchCatchError(block = {
            val requestParams = RequestParams.create()
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, atcParams)

            when (atcParams) {
                is AddToCartRequestParams -> {
                    getAddToCartUseCase(requestParams, isTokoNow)
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

    private fun getUpdateCartUseCase(params: MiniCartItem, updatedQuantity: Int, isTokoNow: Boolean) {
        viewModelScope.launchCatchError(block = {
            val copyOfMiniCartItem = params.copy(quantity = updatedQuantity)
            updateCartUseCase.setParams(listOf(copyOfMiniCartItem))
            val result = withContext(dispatcher.io) {
                updateCartUseCase.executeOnBackground()
            }

            if (result.error.isEmpty()) {
                updateMiniCartAndButtonData(productId = copyOfMiniCartItem.productId, isTokoNow = isTokoNow, quantity = copyOfMiniCartItem.quantity, notes = copyOfMiniCartItem.notes)
                _updateCartLiveData.postValue(result.data.message.asSuccess())
            } else {
                _updateCartLiveData.postValue(MessageErrorException(result.error.firstOrNull() ?: "").asFail())
            }
        })
        {
            _updateCartLiveData.postValue(it.cause?.asFail() ?: it.asFail())
        }
    }

    private suspend fun getAddToCartUseCase(requestParams: RequestParams, isTokoNow: Boolean) {
        val result = withContext(dispatcher.io) {
            addToCartUseCase.createObservable(requestParams).toBlocking().single()
        }
        if (result.isDataError()) {
            val errorMessage = result.errorMessage.firstOrNull() ?: ""
            _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
        } else {
            updateMiniCartAndButtonData(result.data.productId.toString(), result.data.quantity, isTokoNow, result.data.cartId, result.data.notes)
            _addToCartLiveData.postValue(result.asSuccess())
        }
    }

    private suspend fun getAddToCartOcsUseCase(requestParams: RequestParams) {
        val result = withContext(dispatcher.io) {
            addToCartOcsUseCase.createObservable(requestParams).toBlocking().single()
        }
        if (result.isDataError()) {
            val errorMessage = result.errorMessage.firstOrNull() ?: ""

            _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
        } else {
            _addToCartLiveData.postValue(result.asSuccess())
        }
    }


    private suspend fun getAddToCartOccUseCase(requestParams: RequestParams) {
        val result = withContext(dispatcher.io) {
            addToCartOccUseCase.createObservable(requestParams).toBlocking().single()
        }
        if (result.isDataError()) {
            val errorMessage = result.getAtcErrorMessage() ?: ""
            _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
        } else {
            _addToCartLiveData.postValue(result.asSuccess())
        }
    }

    /**
     * Update selection by key eg:
     *  - Before update (warna, 0), (ukuran, 0)
     *  - After update (warna, merah), (ukuran, 0)
     */
    private fun updateSelectedOptionIdsVisitable(selectedOptionKey: String, selectedOptionId: String): MutableMap<String, String>? {
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
        return aggregatorData?.nearestWarehouse?.get(productId)
    }

    private fun getSelectedMiniCartItem(productId: String): MiniCartItem? {
        return minicartData?.get(productId)
    }
}