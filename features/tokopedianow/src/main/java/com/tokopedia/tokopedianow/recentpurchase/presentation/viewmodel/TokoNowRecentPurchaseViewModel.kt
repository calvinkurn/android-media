package com.tokopedia.tokopedianow.recentpurchase.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.constant.ConstantValue
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addLayoutList
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addLoading
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.addProductGrid
import com.tokopedia.tokopedianow.recentpurchase.domain.mapper.RepurchaseLayoutMapper.removeLoading
import com.tokopedia.tokopedianow.recentpurchase.domain.usecase.GetRepurchaseProductListUseCase
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseLayoutUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductGridUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoNowRecentPurchaseViewModel @Inject constructor(
    private val getRepurchaseProductListUseCase: GetRepurchaseProductListUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase,
    private val userSession: UserSessionInterface,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io) {

    companion object {
        private const val INITIAL_PAGE = 1
    }

    val getLayout: LiveData<Result<RepurchaseLayoutUiModel>>
        get() = _getLayout
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    val miniCartRemove: LiveData<Result<Pair<String, String>>>
        get() = _miniCartRemove
    val productAddToCartQuantity: LiveData<Result<MiniCartSimplifiedData>>
        get() = _productAddToCartQuantity
    val chooseAddress: LiveData<Result<GetStateChosenAddressResponse>>
        get() = _chooseAddress

    private val _getLayout = MutableLiveData<Result<RepurchaseLayoutUiModel>>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCartRemove = MutableLiveData<Result<Pair<String,String>>>()
    private val _productAddToCartQuantity = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _chooseAddress = MutableLiveData<Result<GetStateChosenAddressResponse>>()

    private var miniCartSimplifiedData: MiniCartSimplifiedData? = null
    private var layoutList: MutableList<Visitable<*>> = mutableListOf()

    fun showLoading() {
        layoutList.clear()
        layoutList.addLoading()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            nextPage = INITIAL_PAGE,
            state = TokoNowLayoutState.LOADING
        )

        _getLayout.postValue(Success(layout))
    }

    fun getLayoutList() {
        layoutList.removeLoading()
        layoutList.addLayoutList()

        val layout = RepurchaseLayoutUiModel(
            layoutList = layoutList,
            nextPage = INITIAL_PAGE,
            state = TokoNowLayoutState.SHOW
        )

        _getLayout.postValue(Success(layout))
    }

    fun getLayoutData(warehouseID: Int, queryParam: String, totalScan: Int, page: Int) {
        launchCatchError(block = {
            layoutList.filter { it.isNotStaticLayout() }.forEach {
                when (it) {
                    is RepurchaseProductGridUiModel -> {
                        getProductListAsync(warehouseID, queryParam, totalScan, page).await()
                    }
                }

                val layout = RepurchaseLayoutUiModel(
                    layoutList = layoutList,
                    nextPage = INITIAL_PAGE,
                    state = TokoNowLayoutState.LOADED
                )

                _getLayout.postValue(Success(layout))
            }
        }) {

        }
    }

    private fun getProductListAsync(
        warehouseID: Int,
        queryParam: String,
        totalScan: Int,
        page: Int
    ): Deferred<Unit?> {
        return asyncCatchError(block = {
            val getProductListResponse = getRepurchaseProductListUseCase.execute(warehouseID,
                queryParam,
                totalScan,
                page
            ).productList

            layoutList.addProductGrid(getProductListResponse)
        }) {

        }
    }

    fun getMiniCart(shopId: List<String>, warehouseId: String?) {
        if(!shopId.isNullOrEmpty() && warehouseId.toLongOrZero() != 0L && userSession.isLoggedIn) {
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopId)
                getMiniCartUseCase.execute({
                    val isInitialLoad = true

                    if(isInitialLoad) {
                        setMiniCartAndProductQuantity(it)
                    } else {
                        setProductAddToCartQuantity(it)
                    }

                    _miniCart.postValue(Success(it))
                }, {
                    _miniCart.postValue(Fail(it))
                })
            }) {
                _miniCart.postValue(Fail(it))
            }
        }
    }

    fun getChooseAddress(source: String){
        getChooseAddressWarehouseLocUseCase.getStateChosenAddress( {
            _chooseAddress.postValue(Success(it))
        },{
            _chooseAddress.postValue(Fail(it))
        }, source)
    }

    fun getProductRecomOoc() {
        launchCatchError(block = {
            val recommendationWidgets = getRecommendationUseCase.getData(
                GetRecommendationRequestParam(
                    pageName = ConstantValue.PAGE_NAME_RECOMMENDATION_PARAM,
                    xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
                    xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
                )
            )
            if (!recommendationWidgets.first().recommendationItemList.isNullOrEmpty()) {
                // get recommendation data
            }
        }) { /* nothing to do */ }
    }

    fun setProductAddToCartQuantity(miniCart: MiniCartSimplifiedData) {
        launchCatchError(block = {
            setMiniCartAndProductQuantity(miniCart)
            _productAddToCartQuantity.postValue(Success(miniCart))
        }) {}
    }

    fun onCartItemUpdated(productId: String, quantity: Int, shopId: String) {
        val miniCartItem = getMiniCartItem(productId)

        when {
            miniCartItem == null -> addItemToCart(productId, shopId, quantity)
            quantity.isZero() -> removeItemFromCart(miniCartItem)
            else -> updateItemCart(miniCartItem, quantity)
        }
    }

    private fun getMiniCartItem(productId: String): MiniCartItem? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.firstOrNull { it.productId == productId }
    }

    private fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    private fun removeItemFromCart(miniCartItem: MiniCartItem) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
    }

    private fun updateItemCart(miniCartItem: MiniCartItem, quantity: Int) {
        miniCartItem.quantity = quantity
        val updateCartRequest = UpdateCartRequest(
            cartId = miniCartItem.cartId,
            quantity = miniCartItem.quantity,
            notes = miniCartItem.notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )
        updateCartUseCase.execute({
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun setMiniCartAndProductQuantity(miniCart: MiniCartSimplifiedData) {
        setMiniCartSimplifiedData(miniCart)
        updateProductQuantity(miniCart)
    }

    private fun setMiniCartSimplifiedData(miniCart: MiniCartSimplifiedData) {
        miniCartSimplifiedData = miniCart
    }

    private fun updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        //TO-DO: Update product quantity here
    }

    private fun Visitable<*>.isNotStaticLayout(): Boolean {
        return this::class.java != TokoNowChooseAddressWidgetUiModel::class.java
    }
}