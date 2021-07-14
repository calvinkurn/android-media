package com.tkpd.atcvariant.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkpd.atcvariant.data.uidata.PartialButtonDataModel
import com.tkpd.atcvariant.data.uidata.VariantComponentDataModel
import com.tkpd.atcvariant.data.uidata.VariantShimmeringDataModel
import com.tkpd.atcvariant.usecase.GetAggregatorAndMiniCartUseCase
import com.tkpd.atcvariant.util.AtcCommonMapper
import com.tkpd.atcvariant.util.AtcCommonMapper.asFail
import com.tkpd.atcvariant.util.AtcCommonMapper.asSuccess
import com.tkpd.atcvariant.util.AtcCommonMapper.generateAvailableButtonIngatkanSaya
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable
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
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.AtcVariantMapper
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
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

    //This livedata is only for access variant, cartRedirection, and warehouse locally in viewmodel
    private var aggregatorData: ProductVariantAggregatorUiData? = null
    private var minicartData: MutableMap<String, MiniCartItem>? = null
    private var variantActivityResult: ProductVariantResult = ProductVariantResult()
    private var localQuantityData: MutableMap<String, Int> = mutableMapOf()

    private val _initialData = MutableLiveData<Result<List<AtcVariantVisitable>>>()
    val initialData: LiveData<Result<List<AtcVariantVisitable>>>
        get() = _initialData

    private val _buttonData = MutableLiveData<Result<PartialButtonDataModel>>()
    val buttonData: LiveData<Result<PartialButtonDataModel>>
        get() = _buttonData

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

    fun getActivityResultData() : ProductVariantResult = variantActivityResult

    //updated with the previous page data as well
    fun getVariantAggregatorData(): ProductVariantAggregatorUiData? {
        return aggregatorData
    }

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
            val shouldShowDeleteButton = selectedMiniCart != null
            val cartData = AtcCommonMapper.mapToCartRedirectionData(selectedVariantChild, aggregatorData?.cardRedirection, isShopOwner, selectedMiniCart != null, aggregatorData?.alternateCopy)

            val isPartiallySelected = AtcVariantMapper.isPartiallySelectedOptionId(selectedVariantIds)
            val selectedWarehouse = getSelectedWarehouse(selectedVariantChild?.productId ?: "")
            val selectedQuantity = getSelectedQuantity(selectedVariantChild?.productId ?: "")

            //We update visitable to re-render selected variant and header
            val list = AtcCommonMapper.updateVisitable(
                    oldList = (_initialData.value as Success).data,
                    processedVariant = processedVariant,
                    isPartiallySelected = isPartiallySelected,
                    selectedVariantIds = selectedVariantIds,
                    selectedVariantChild = selectedVariantChild,
                    variantImage = variantImage,
                    selectedProductFulfillment = selectedWarehouse?.isFulfillment ?: false,
                    isTokoNow = isTokoNow,
                    selectedQuantity = selectedQuantity,
                    shouldShowDeleteButton = shouldShowDeleteButton)

            _initialData.postValue(list.asSuccess())
            _titleVariantName.postValue(AtcVariantMapper.getSelectedVariantName(processedVariant, selectedVariantChild))

            if (!isPartiallySelected) {
                // if user only select 1 of 2 variant, no need to update the button
                // this validation only be execute when user clicked variant and fully clicked 2 of 2 variant or 1 of 1
                _buttonData.postValue(cartData.asSuccess())
                updateActivityResult(
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

    fun updateActivityResult(selectedProductId: String? = null,
                             mapOfSelectedVariantOption: MutableMap<String, String>? = null,
                             atcSuccessMessage: String? = null,
                             shouldRefreshPreviousPage: Boolean? = null,
                             requestCode: Int? = null) {
        variantActivityResult = AtcCommonMapper.updateActivityResultData(
                recentData = variantActivityResult,
                selectedProductId = selectedProductId,
                parentProductId = getVariantData()?.parentId,
                mapOfSelectedVariantOption = mapOfSelectedVariantOption,
                atcMessage = atcSuccessMessage,
                shouldRefreshPreviousPage = shouldRefreshPreviousPage,
                requestCode = requestCode)
    }

    fun getSelectedQuantity(productId: String): Int {
        return localQuantityData[productId] ?: 0
    }

    fun updateQuantity(quantity: Int, productId: String) {
        localQuantityData[productId] = quantity
    }

    fun decideInitialValue(aggregatorParams: ProductVariantBottomSheetParams, isLoggedIn: Boolean) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            _initialData.postValue(listOf(VariantShimmeringDataModel(99L)).asSuccess())
            isShopOwner = aggregatorParams.isShopOwner

            getAggregatorAndMiniCartData(aggregatorParams, isLoggedIn)

            //Get selected child by product id, if product parent auto select first child
            //If parent just update the header and ignore the variant selection
            val pairIsParentAndChild = aggregatorData?.variantData?.autoSelectChildIfGivenIdIsParent(aggregatorParams.productId)
            val isParent = pairIsParentAndChild?.first ?: false
            val selectedChild = pairIsParentAndChild?.second

            //Get cart redirection , and warehouse by selected product id to render button and toko cabang
            val selectedMiniCart = minicartData?.get(selectedChild?.productId ?: "")
            val cartData = AtcCommonMapper.mapToCartRedirectionData(selectedChild, aggregatorData?.cardRedirection, isShopOwner, selectedMiniCart != null, aggregatorData?.alternateCopy)
            val selectedWarehouse = getSelectedWarehouse(selectedChild?.productId ?: "")

            //generate variant component and data, initial render need to determine selected option
            val initialSelectedOptionIds = AtcCommonMapper.determineSelectedOptionIds(isParent, aggregatorData?.variantData, selectedChild)
            val processedVariant = AtcVariantMapper.processVariant(aggregatorData?.variantData, initialSelectedOptionIds)

            assignLocalQuantityWithMiniCartQuantity(minicartData?.values?.toList())
            val selectedQuantity = getSelectedQuantity(selectedChild?.productId ?: "")
            val shouldShowDeleteButton = minicartData?.get(selectedChild?.productId ?: "") != null

            //Generate visitables
            val visitables = AtcCommonMapper.mapToVisitable(
                    selectedChild = selectedChild,
                    isTokoNow = aggregatorParams.isTokoNow,
                    initialSelectedVariant = initialSelectedOptionIds,
                    processedVariant = processedVariant,
                    selectedProductFulfillment = selectedWarehouse?.isFulfillment ?: false,
                    totalStock = aggregatorData?.variantData?.totalStockChilds ?: 0,
                    selectedQuantity = selectedQuantity,
                    shouldShowDeleteButton = shouldShowDeleteButton)

            if (visitables != null) {
                _titleVariantName.postValue(AtcVariantMapper.getSelectedVariantName(processedVariant, selectedChild))
                _initialData.postValue(visitables.asSuccess())
                updateActivityResult(
                        selectedProductId = selectedChild?.productId ?: "",
                        mapOfSelectedVariantOption = initialSelectedOptionIds)
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

    private suspend fun getAggregatorAndMiniCartData(aggregatorParams: ProductVariantBottomSheetParams, isLoggedIn: Boolean) {
        /**
         * If data completely provided from previous page, use that
         * if not call GQL
         */
        if (aggregatorParams.variantAggregator.isAggregatorEmpty() || (aggregatorParams.isTokoNow && aggregatorParams.miniCartData == null && isLoggedIn)) {
            val result = aggregatorMiniCartUseCase.executeOnBackground(
                    productId = aggregatorParams.productId,
                    source = aggregatorParams.pageSource,
                    isTokoNow = aggregatorParams.isTokoNow,
                    warehouseId = aggregatorParams.whId,
                    pdpSession = aggregatorParams.pdpSession,
                    shopId = aggregatorParams.shopId,
                    isLoggedIn = isLoggedIn
            )
            aggregatorData = result.variantAggregator
            minicartData = result.miniCartData?.toMutableMap()
            if (aggregatorParams.pageSource == AtcVariantHelper.PDP_PAGESOURCE) {
                updateActivityResult(shouldRefreshPreviousPage = true)
            }
        } else {
            aggregatorData = aggregatorParams.variantAggregator
            minicartData = aggregatorParams.miniCartData?.toMutableMap()
        }
    }

    fun addWishlist(productId: String, userId: String) {
        addWishListUseCase.createObservable(productId,
                userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                _addWishlistResult.postValue(Throwable(errorMessage ?: "").asFail())
            }

            override fun onSuccessAddWishlist(productId: String?) {
                updateActivityResult(shouldRefreshPreviousPage = true)
                updateButtonAndWishlistLocally(productId ?: "")
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

    private fun updateButtonAndWishlistLocally(productId: String) {
        updateRemindMeCartRedirection(productId)
        //update wishlist in child locally
        val selectedChild = getVariantData()?.getChildByProductId(productId)
        selectedChild?.isWishlist = true

        val generateCartRedir = AtcCommonMapper.mapToCartRedirectionData(getVariantData()?.getChildByProductId(productId), aggregatorData?.cardRedirection, isShopOwner, false, aggregatorData?.alternateCopy)
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

        val generateCartRedir = AtcCommonMapper.mapToCartRedirectionData(getVariantData()?.getChildByProductId(productId), aggregatorData?.cardRedirection, isShopOwner, true, aggregatorData?.alternateCopy)
        _buttonData.postValue(generateCartRedir.asSuccess())
    }

    private fun updateRemindMeCartRedirection(productId: String) {
        val availableButtonIngatkanSaya = generateAvailableButtonIngatkanSaya(aggregatorData?.alternateCopy, aggregatorData?.cardRedirection?.get(productId))
        //update cart redir localy
        aggregatorData?.cardRedirection?.let {
            it[productId]?.availableButtons = availableButtonIngatkanSaya ?: return@let
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
        val updatedQuantity = localQuantityData[selectedChild?.productId ?: ""]
                ?: selectedChild?.getFinalMinOrder() ?: 1

        if (selectedMiniCart != null && isTokoNow) {
            getUpdateCartUseCase(selectedMiniCart, updatedQuantity, isTokoNow)
        } else {
            val atcRequestParam = AtcCommonMapper.generateAtcData(
                    actionButtonCart = actionButton,
                    selectedChild = selectedChild,
                    selectedWarehouse = selectedWarehouse,
                    shopIdInt = shopIdInt,
                    trackerAttributionPdp = trackerAttributionPdp,
                    trackerListNamePdp = trackerListNamePdp,
                    categoryName = categoryName,
                    shippingMinPrice = shippingMinPrice,
                    userId = userId,
                    isTokoNow = isTokoNow,
                    selectedStock = updatedQuantity)
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
            updateCartUseCase.setParams(
                    miniCartItemList = listOf(copyOfMiniCartItem),
                    source = UpdateCartUseCase.VALUE_SOURCE_PDP_UPDATE_QTY_NOTES
            )
            val result = withContext(dispatcher.io) {
                updateCartUseCase.executeOnBackground()
            }

            if (result.error.isEmpty()) {
                updateMiniCartAndButtonData(productId = copyOfMiniCartItem.productId, isTokoNow = isTokoNow, quantity = copyOfMiniCartItem.quantity, notes = copyOfMiniCartItem.notes)
                _updateCartLiveData.postValue(result.data.message.asSuccess())
            } else {
                _updateCartLiveData.postValue(MessageErrorException(result.error.firstOrNull()
                        ?: "").asFail())
            }
        })
        {
            _updateCartLiveData.postValue(it.cause?.asFail() ?: it.asFail())
        }
    }

    private fun updateQuantityEditorDeleteButtonAfterAtc(isTokoNow: Boolean) {
        if (isTokoNow) {
            val updatedList = AtcCommonMapper.updateDeleteButtonQtyEditor((_initialData.value as Success).data)
            _initialData.postValue(updatedList.asSuccess())
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
            updateQuantityEditorDeleteButtonAfterAtc(isTokoNow)
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

    fun getSelectedWarehouse(productId: String): WarehouseInfo? {
        return aggregatorData?.nearestWarehouse?.get(productId)
    }

    private fun getSelectedMiniCartItem(productId: String): MiniCartItem? {
        return minicartData?.get(productId)
    }
}