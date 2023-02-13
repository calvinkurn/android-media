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
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.mapProductsWithProductId
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.AtcVariantMapper
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailGallery
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.re.RestrictionData
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.common.usecase.ToggleFavoriteUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.launch
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
        private val addToCartOccUseCase: AddToCartOccMultiUseCase,
        private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
        private val updateCartUseCase: UpdateCartUseCase,
        private val deleteCartUseCase: DeleteCartUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    companion object {
        private const val INITIAL_POSITION_SHIMMERING = 99L
    }

    //This livedata is only for access variant, cartRedirection, and warehouse locally in viewmodel
    private var aggregatorData: ProductVariantAggregatorUiData? = null
    private var minicartData: MutableMap<String, MiniCartItem.MiniCartItemProduct>? = null
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

    private val _deleteCartLiveData = MutableLiveData<Result<String>>()
    val deleteCartLiveData: LiveData<Result<String>>
        get() = _deleteCartLiveData

    private val _restrictionData = MutableLiveData<Result<RestrictionData>>()
    val restrictionData: LiveData<Result<RestrictionData>>
        get() = _restrictionData

    private val _toggleFavoriteShop = MutableLiveData<Result<Boolean>>()
    val toggleFavoriteShop: LiveData<Result<Boolean>>
        get() = _toggleFavoriteShop

    private val _ratesLiveData = MutableLiveData<Result<P2RatesEstimate>>()
    val ratesLiveData: LiveData<Result<P2RatesEstimate>>
        get() = _ratesLiveData

    private val _stockCopy = MutableLiveData<String>()
    val stockCopy: LiveData<String>
        get() = _stockCopy

    private val _variantImagesData = MutableLiveData<ProductDetailGallery>()
    val variantImagesData: LiveData<ProductDetailGallery>
        get() = _variantImagesData

    private var isShopOwner: Boolean = false

    fun getActivityResultData(): ProductVariantResult = variantActivityResult

    //updated with the previous page data as well
    fun getVariantAggregatorData(): ProductVariantAggregatorUiData? {
        return aggregatorData
    }

    fun onVariantClicked(showQtyEditor: Boolean,
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
            val cartData = AtcCommonMapper.mapToCartRedirectionData(
                    selectedVariantChild,
                    aggregatorData?.cardRedirection,
                    isShopOwner,
                    selectedMiniCart != null,
                    aggregatorData?.alternateCopy
            )

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
                    showQtyEditor = showQtyEditor,
                    selectedQuantity = selectedQuantity,
                    shouldShowDeleteButton = shouldShowDeleteButton,
                    aggregatorUiData = aggregatorData)

            _initialData.postValue(list.asSuccess())

            if (!isPartiallySelected) {
                // if user only select 1 of 2 variant, no need to update the button
                // this validation only be execute when user clicked variant and fully clicked 2 of 2 variant or 1 of 1
                _buttonData.postValue(cartData.asSuccess())
                _stockCopy.postValue(selectedVariantChild?.stock?.stockCopy ?: "")

                //generate restriction data (shop followers or exclusive campaign)
                assignReData(aggregatorData?.reData, selectedVariantChild?.productId ?: "")
                assignRatesData(selectedVariantChild?.productId ?: "")

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

    fun updateActivityResult(
            selectedProductId: String? = null,
            mapOfSelectedVariantOption: MutableMap<String, String>? = null,
            atcSuccessMessage: String? = null,
            shouldRefreshPreviousPage: Boolean? = null,
            isFollowShop: Boolean? = null,
            requestCode: Int? = null,
            cartId: String? = null
    ) {
        variantActivityResult = AtcCommonMapper.updateActivityResultData(
                recentData = variantActivityResult,
                selectedProductId = selectedProductId,
                parentProductId = getVariantData()?.parentId,
                mapOfSelectedVariantOption = mapOfSelectedVariantOption,
                atcMessage = atcSuccessMessage,
                shouldRefreshPreviousPage = shouldRefreshPreviousPage,
                isFollowShop = isFollowShop,
                requestCode = requestCode,
                cartId = cartId
        )
    }

    fun getSelectedQuantity(productId: String): Int {
        return localQuantityData[productId] ?: 0
    }

    fun updateQuantity(quantity: Int, productId: String) {
        localQuantityData[productId] = quantity
    }

    fun decideInitialValue(aggregatorParams: ProductVariantBottomSheetParams, isLoggedIn: Boolean) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            _initialData.postValue(listOf(VariantShimmeringDataModel(INITIAL_POSITION_SHIMMERING)).asSuccess())
            isShopOwner = aggregatorParams.isShopOwner

            getAggregatorAndMiniCartData(aggregatorParams, isLoggedIn)

            //Get selected child by product id, if product parent auto select first child
            //If parent just update the header and ignore the variant selection
            val selectedChild = getVariantData()?.autoSelectIfParent(aggregatorParams.productId)

            //Get cart redirection , and warehouse by selected product id to render button and toko cabang
            val selectedMiniCart = minicartData?.get(selectedChild?.productId ?: "")
            val cartData = AtcCommonMapper.mapToCartRedirectionData(selectedChild, aggregatorData?.cardRedirection, isShopOwner, selectedMiniCart != null, aggregatorData?.alternateCopy)
            val selectedWarehouse = getSelectedWarehouse(selectedChild?.productId ?: "")

            //generate variant component and data, initial render need to determine selected option
            val initialSelectedOptionIds = AtcCommonMapper.determineSelectedOptionIds(getVariantData(), selectedChild)
            val processedVariant = AtcVariantMapper.processVariant(getVariantData(), initialSelectedOptionIds)

            assignLocalQuantityWithMiniCartQuantity(minicartData?.values?.toList())
            val selectedQuantity = getSelectedQuantity(selectedChild?.productId ?: "")
            val shouldShowDeleteButton = minicartData?.get(selectedChild?.productId ?: "") != null

            //Generate visitables
            val visitables = AtcCommonMapper.mapToVisitable(
                    selectedChild = selectedChild,
                    showQtyEditor = aggregatorParams.showQtyEditor,
                    initialSelectedVariant = initialSelectedOptionIds,
                    processedVariant = processedVariant,
                    selectedProductFulfillment = selectedWarehouse?.isFulfillment ?: false,
                    selectedQuantity = selectedQuantity,
                    shouldShowDeleteButton = shouldShowDeleteButton,
                    aggregatorUiData = aggregatorData)

            if (visitables != null) {
                _initialData.postValue(visitables.asSuccess())
                updateActivityResult(
                        selectedProductId = selectedChild?.productId ?: "",
                        mapOfSelectedVariantOption = initialSelectedOptionIds)
            } else {
                _initialData.postValue(Throwable().asFail())
            }

            assignReData(aggregatorData?.reData, selectedChild?.productId ?: "")
            assignRatesData(selectedChild?.productId ?: "")
            _buttonData.postValue(cartData.asSuccess())
            _stockCopy.postValue(selectedChild?.stock?.stockCopy ?: "")
        }) {
            _buttonData.postValue(it.asFail())
            _initialData.postValue(it.asFail())
        }
    }

    fun toggleFavorite(shopId: String) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            val requestParams = ToggleFavoriteUseCase.createParams(shopId, ToggleFavoriteUseCase.FOLLOW_ACTION)
            val favoriteData = toggleFavoriteUseCase.executeOnBackground(requestParams).followShop
            if (favoriteData?.isSuccess == true) {
                _toggleFavoriteShop.postValue(favoriteData.isSuccess.asSuccess())
            } else {
                _toggleFavoriteShop.postValue(Throwable(favoriteData?.message.orEmpty()).asFail())
            }
        }) {
            _toggleFavoriteShop.postValue(it.asFail())
        }
    }

    private fun assignRatesData(productId: String) {
        val data = getVariantAggregatorData() ?: return
        val rates = data.getP2RatesEstimateByProductId(productId)
        val shouldHide = data.shouldHideRatesBottomSheet(rates)
        if (rates == null || shouldHide) {
            _ratesLiveData.postValue(Throwable().asFail())
        } else {
            _ratesLiveData.postValue(rates.asSuccess())
        }
    }

    private fun assignReData(reResponse: RestrictionInfoResponse?, productId: String) {
        val restrictionData = reResponse?.getReByProductId(productId = productId)
        if (restrictionData == null) {
            _restrictionData.postValue(Throwable().asFail())
        } else {
            _restrictionData.postValue(restrictionData.asSuccess())
        }
    }

    private fun assignLocalQuantityWithMiniCartQuantity(miniCart: List<MiniCartItem.MiniCartItemProduct>?) {
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
        val shouldHitMiniCart = (aggregatorParams.isTokoNow || aggregatorParams.showQtyEditor)
                && aggregatorParams.miniCartData == null
                && isLoggedIn

        if (aggregatorParams.variantAggregator.isAggregatorEmpty() || shouldHitMiniCart) {
            val result = aggregatorMiniCartUseCase.executeOnBackground(
                    productId = aggregatorParams.productId,
                    source = aggregatorParams.pageSource,
                    isTokoNow = aggregatorParams.isTokoNow,
                    warehouseId = aggregatorParams.whId,
                    pdpSession = aggregatorParams.pdpSession,
                    shopId = aggregatorParams.shopId,
                    isLoggedIn = isLoggedIn,
                    extParams = aggregatorParams.extParams,
                    showQtyEditor = aggregatorParams.showQtyEditor
            )
            aggregatorData = result.variantAggregator
            minicartData = result.miniCartData?.mapProductsWithProductId()?.toMutableMap()
            if (aggregatorParams.pageSource == VariantPageSource.PDP_PAGESOURCE.source) {
                updateActivityResult(shouldRefreshPreviousPage = true)
            }
        } else {
            aggregatorData = aggregatorParams.variantAggregator
            minicartData = aggregatorParams.miniCartData?.toMutableMap()
        }
    }

    fun addWishlistV2(productId: String, userId: String, wishlistV2ActionListener: WishlistV2ActionListener) {
        viewModelScope.launch(dispatcher.main) {
            addToWishlistV2UseCase.setParams(productId, userId)
            val result = withContext(dispatcher.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                updateActivityResult(shouldRefreshPreviousPage = true)
                updateButtonAndWishlistLocally(productId)
                wishlistV2ActionListener.onSuccessAddWishlist(result.data, productId)
            } else if (result is Fail) {
                wishlistV2ActionListener.onErrorAddWishList(result.throwable, productId)
            }
        }
    }

    private fun updateButtonAndWishlistLocally(productId: String) {
        updateRemindMeCartRedirection(productId)
        //update wishlist in child locally
        val selectedChild = getVariantData()?.getChildByProductId(productId)
        selectedChild?.isWishlist = true

        val generateCartRedir = AtcCommonMapper.mapToCartRedirectionData(getVariantData()?.getChildByProductId(productId), aggregatorData?.cardRedirection, isShopOwner, false, aggregatorData?.alternateCopy)
        _buttonData.postValue(generateCartRedir.asSuccess())
    }

    private fun updateMiniCartAndButtonAfterDelete(productId: String) {
        minicartData?.remove(productId)

        //we dont want to use alternate tokonow, use cart redir button instead
        val generateCartRedir = AtcCommonMapper.mapToCartRedirectionData(getVariantData()?.getChildByProductId(productId), aggregatorData?.cardRedirection, isShopOwner, false, aggregatorData?.alternateCopy)
        _buttonData.postValue(generateCartRedir.asSuccess())
    }

    private fun updateMiniCartAndButtonData(productId: String,
                                            quantity: Int,
                                            showQtyEditor: Boolean,
                                            cartId: String = "",
                                            notes: String = "") {
        if (!showQtyEditor) return
        val selectedMiniCartData = minicartData?.get(productId)

        if (selectedMiniCartData == null) {
            minicartData?.set(productId, MiniCartItem.MiniCartItemProduct(
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

    fun deleteProductInCart(productId: String) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            val selectedMiniCart = getSelectedMiniCartItem(productId) ?: return@launchCatchError

            deleteCartUseCase.setParams(listOf(selectedMiniCart.cartId))
            val data = deleteCartUseCase.executeOnBackground()

            updateMiniCartAndButtonAfterDelete(productId)
            updateQuantityEditorDeleteButtonAfterAtc(showQtyEditor = true, value = false)
            val minOrder = getVariantData()?.getChildByProductId(productId)?.getFinalMinOrder() ?: 0
            updateQuantity(minOrder, productId)

            _deleteCartLiveData.postValue((data.data.message.firstOrNull() ?: "").asSuccess())
        }) {
            _deleteCartLiveData.postValue(it.asFail())
        }
    }

    fun hitAtc(actionButton: Int,
               shopIdInt: Int,
               categoryName: String,
               userId: String,
               shippingMinPrice: Double,
               trackerAttributionPdp: String,
               trackerListNamePdp: String,
               showQtyEditor: Boolean) {
        val selectedChild = getVariantData()?.getChildByOptionId(getSelectedOptionIds()?.values?.toList()
                ?: listOf())
        val selectedWarehouse = getSelectedWarehouse(selectedChild?.productId ?: "")
        val selectedMiniCart = getSelectedMiniCartItem(selectedChild?.productId ?: "")
        val updatedQuantity = localQuantityData[selectedChild?.productId ?: ""]
                ?: selectedChild?.getFinalMinOrder() ?: 1

        if (selectedMiniCart != null && showQtyEditor) {
            getUpdateCartUseCase(selectedMiniCart, updatedQuantity, showQtyEditor)
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
                    showQtyEditor = showQtyEditor,
                    selectedStock = updatedQuantity)
            addToCart(atcRequestParam, showQtyEditor)
        }
    }

    private fun addToCart(atcParams: Any, showQtyEditor: Boolean) {
        viewModelScope.launchCatchError(block = {
            val requestParams = RequestParams.create()
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, atcParams)

            when (atcParams) {
                is AddToCartRequestParams -> {
                    getAddToCartUseCase(requestParams, showQtyEditor)
                }
                is AddToCartOcsRequestParams -> {
                    getAddToCartOcsUseCase(requestParams)
                }
                is AddToCartOccMultiRequestParams -> {
                    getAddToCartOccUseCase(atcParams)
                }
            }
        }) {
            _addToCartLiveData.value = it.cause?.asFail() ?: it.asFail()
        }
    }

    private fun getUpdateCartUseCase(params: MiniCartItem.MiniCartItemProduct, updatedQuantity: Int, showQtyEditor: Boolean) {
        viewModelScope.launchCatchError(block = {
            val copyOfMiniCartItem = params.copy(quantity = updatedQuantity)
            val updateCartRequest = UpdateCartRequest(
                    cartId = copyOfMiniCartItem.cartId,
                    quantity = copyOfMiniCartItem.quantity,
                    notes = copyOfMiniCartItem.notes
            )
            updateCartUseCase.setParams(
                    updateCartRequestList = listOf(updateCartRequest),
                    source = UpdateCartUseCase.VALUE_SOURCE_PDP_UPDATE_QTY_NOTES
            )
            val result = withContext(dispatcher.io) {
                updateCartUseCase.executeOnBackground()
            }

            if (result.error.isEmpty()) {
                updateMiniCartAndButtonData(
                        productId = copyOfMiniCartItem.productId,
                        showQtyEditor = showQtyEditor,
                        quantity = copyOfMiniCartItem.quantity,
                        notes = copyOfMiniCartItem.notes)
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

    private fun updateQuantityEditorDeleteButtonAfterAtc(showQtyEditor: Boolean, value: Boolean) {
        if (showQtyEditor) {
            val updatedList = AtcCommonMapper.updateDeleteButtonQtyEditor((_initialData.value as Success).data, value)
            _initialData.postValue(updatedList.asSuccess())
        }
    }

    private suspend fun getAddToCartUseCase(requestParams: RequestParams, showQtyEditor: Boolean) {
        val result = withContext(dispatcher.io) {
            addToCartUseCase.createObservable(requestParams).toBlocking().single()
        }
        if (result.isDataError()) {
            val errorMessage = result.errorMessage.firstOrNull() ?: ""
            _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
        } else {
            updateQuantityEditorDeleteButtonAfterAtc(showQtyEditor, true)
            updateMiniCartAndButtonData(
                    productId = result.data.productId.toString(),
                    quantity = result.data.quantity,
                    showQtyEditor = showQtyEditor,
                    cartId = result.data.cartId,
                    notes = result.data.notes)
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


    private suspend fun getAddToCartOccUseCase(atcParams: AddToCartOccMultiRequestParams) {
        val result = withContext(dispatcher.io) {
            addToCartOccUseCase.setParams(atcParams).executeOnBackground().mapToAddToCartDataModel()
        }
        if (result.isStatusError()) {
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

    private fun getSelectedMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        return minicartData?.get(productId)
    }

    fun onVariantImageClicked(
            imageUrl: String,
            productId: String,
            userId: String,
            mainImageTag: String
    ) {
        val selectedChild = getVariantData()?.getChildByProductId(productId)
        val selectedOptionId = selectedChild?.optionIds?.firstOrNull()

        val variantAggregatorData = getVariantAggregatorData()

        val mainImage = variantAggregatorData?.simpleBasicInfo?.defaultMediaURL
        val defaultImage = if (mainImage?.isNotEmpty() == true)
            mainImage
        else imageUrl

        val variantGalleryItems = variantAggregatorData?.getVariantGalleryItems()

        val items = variantGalleryItems ?: emptyList()
        if (items.isEmpty() && defaultImage.isEmpty()) return

        val productDetailGalleryData = ProductDetailGallery(
                productId = productId,
                userId = userId,
                page = ProductDetailGallery.Page.VariantBottomSheet,
                defaultItem = ProductDetailGallery.Item(
                        "",
                        defaultImage,
                        tag = mainImageTag,
                        type = ProductDetailGallery.Item.Type.Image
                ),
                items = items,
                selectedId = selectedOptionId
        )

        _variantImagesData.postValue(productDetailGalleryData)
    }
}
