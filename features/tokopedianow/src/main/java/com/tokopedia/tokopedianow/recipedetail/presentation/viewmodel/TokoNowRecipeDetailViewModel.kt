package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

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
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.common.util.CoroutineUtil.launchWithDelay
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipedetail.domain.usecase.GetRecipeUseCase
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.mapToMediaSlider
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.mapToRecipeInfo
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.mapToRecipeTab
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.removeLoadingItem
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.updateDeletedProductQuantity
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.updateProductQuantity
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BookmarkUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeDetailLoadingUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import javax.inject.Inject

class TokoNowRecipeDetailViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getAddressUseCase: GetChosenAddressWarehouseLocUseCase,
    private val addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    private val addressData: TokoNowLocalAddress,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val GET_ADDRESS_SOURCE = "tokonow"

        private const val INVALID_SHOP_ID = 0L
        private const val OOC_WAREHOUSE_ID = 0L
        private const val CHANGE_QUANTITY_DELAY = 500L
        private const val BOOKMARK_DELAY = 500L
    }

    val layoutList: LiveData<List<Visitable<*>>>
        get() = _layoutList
    val recipeInfo: LiveData<RecipeInfoUiModel>
        get() = _recipeInfo
    val addItemToCart: LiveData<Result<AddToCartDataModel>>
        get() = _addItemToCart
    val removeCartItem: LiveData<Result<Pair<String, String>>>
        get() = _removeCartItem
    val updateCartItem: LiveData<Result<UpdateCartV2Data>>
        get() = _updateCartItem
    val miniCart: LiveData<Result<MiniCartSimplifiedData>>
        get() = _miniCart
    val addBookmark: LiveData<BookmarkUiModel>
        get() = _addBookmark
    val removeBookmark: LiveData<BookmarkUiModel>
        get() = _removeBookmark
    val isBookmarked: LiveData<Boolean>
        get() = _isBookmarked

    private val _layoutList = MutableLiveData<List<Visitable<*>>>()
    private val _recipeInfo = MutableLiveData<RecipeInfoUiModel>()
    private val _addItemToCart = MutableLiveData<Result<AddToCartDataModel>>()
    private val _removeCartItem = MutableLiveData<Result<Pair<String, String>>>()
    private val _updateCartItem = MutableLiveData<Result<UpdateCartV2Data>>()
    private val _miniCart = MutableLiveData<Result<MiniCartSimplifiedData>>()
    private val _addBookmark = MutableLiveData<BookmarkUiModel>()
    private val _removeBookmark = MutableLiveData<BookmarkUiModel>()
    private val _isBookmarked = MutableLiveData<Boolean>()

    private var recipeId: String = ""
    private val layoutItemList = mutableListOf<Visitable<*>>()
    private var miniCartData: MiniCartSimplifiedData? = null

    private var changeQuantityJob: Job? = null
    private var getMiniCartJob: Job? = null
    private var bookmarkJob: Job? = null

    fun checkAddressData() {
        val shopId = addressData.getShopId()

        if (shopId == INVALID_SHOP_ID) {
            getAddress()
        } else {
            getRecipe()
        }
    }

    fun onQuantityChanged(productId: String, shopId: String, quantity: Int) {
        changeQuantityJob?.cancel()
        launchWithDelay(block = {
            val miniCartItem = getMiniCartItem(productId)
            val cartId = miniCartItem?.cartId.orEmpty()
            val notes = miniCartItem?.notes.orEmpty()

            when {
                miniCartItem == null -> addItemToCart(productId, shopId, quantity)
                quantity.isZero() -> deleteCartItem(productId, cartId)
                else -> updateCartItem(cartId, quantity, notes)
            }
        }, delay = CHANGE_QUANTITY_DELAY).let {
            changeQuantityJob = it
        }
    }

    fun addItemToCart(productId: String, shopId: String, quantity: Int) {
        val addToCartRequestParams = AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            _addItemToCart.postValue(Success(it))
        }, {
            _addItemToCart.postValue(Fail(it))
        })
    }

    fun deleteCartItem(productId: String, cartId: String) {
        deleteCartUseCase.setParams(cartIdList = listOf(cartId))
        deleteCartUseCase.execute({
            val message = it.data.message.joinToString(separator = ", ")
            val data = Pair(productId, message)
            _removeCartItem.postValue(Success(data))
        }, {
            _removeCartItem.postValue(Fail(it))
        })
    }

    fun getMiniCart() {
        val shopIds = listOf(getShopId())

        if(shouldGetMiniCart(shopIds)) {
            getMiniCartJob?.cancel()
            launchCatchError(block = {
                getMiniCartUseCase.setParams(shopIds, MiniCartSource.TokonowRecipe)
                getMiniCartUseCase.execute({
                    val showMiniCart = it.isShowMiniCartWidget
                    val outOfCoverage = addressData.isOutOfCoverage()
                    val data = it.copy(isShowMiniCartWidget = showMiniCart && !outOfCoverage)
                    setProductAddToCartQuantity(it)
                    _miniCart.postValue(Success(data))
                }, {
                    _miniCart.postValue(Fail(it))
                })
            }) {
                _miniCart.postValue(Fail(it))
            }.let {
                getMiniCartJob = it
            }
        }
    }

    fun setProductAddToCartQuantity(miniCart: MiniCartSimplifiedData) {
        launchCatchError(block = {
            setMiniCartAndProductQuantity(miniCart)
            _layoutList.postValue(layoutItemList)
        }) {}
    }

    fun getMiniCartItem(productId: String): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun onClickBookmarkBtn(addBookmark: Boolean) {
        bookmarkJob?.cancel()

        launchWithDelay(block = {
            val isRecipeBookmarked = _isBookmarked.value == true

            when {
                isRecipeBookmarked == addBookmark -> {
                    return@launchWithDelay
                }
                addBookmark -> addRecipeBookmark()
                else -> removeRecipeBookmark()
            }
        }, delay = BOOKMARK_DELAY).let {
            bookmarkJob = it
        }
    }

    fun addRecipeBookmark() {
        launchCatchError(block = {
            addRecipeBookmarkUseCase.execute(recipeId)

            val isBookmarked = true
            val data = BookmarkUiModel(
                recipeTitle = getRecipeTitle(),
                isSuccess = true
            )

            _isBookmarked.postValue(isBookmarked)
            _addBookmark.postValue(data)
        }) {
            val isBookmarked = false
            val data = BookmarkUiModel(
                recipeTitle = getRecipeTitle(),
                isSuccess = false
            )

            _isBookmarked.postValue(isBookmarked)
            _addBookmark.postValue(data)
        }
    }

    fun removeRecipeBookmark() {
        launchCatchError(block = {
            removeRecipeBookmarkUseCase.execute(recipeId)

            val isBookmarked = false
            val data = BookmarkUiModel(
                recipeTitle = getRecipeTitle(),
                isSuccess = true
            )

            _isBookmarked.postValue(isBookmarked)
            _removeBookmark.postValue(data)
        }) {
            val isBookmarked = true

            val data = BookmarkUiModel(
                recipeTitle = getRecipeTitle(),
                isSuccess = false
            )

            _isBookmarked.postValue(isBookmarked)
            _removeBookmark.postValue(data)
        }
    }

    fun refreshPage() {
        showLoading()
        updateAddressData()
        checkAddressData()
    }

    fun showLoading() {
        layoutItemList.clear()
        layoutItemList.add(RecipeDetailLoadingUiModel)
        _layoutList.postValue(layoutItemList)
    }

    fun updateAddressData() = addressData.updateLocalData()

    fun getShopId(): String = addressData.getShopId().toString()

    fun setRecipeId(recipeId: String) {
        this.recipeId = recipeId
    }

    private fun getRecipe() {
        launchCatchError(block = {
            val warehouseId = addressData.getWarehouseId().toString()
            val response = getRecipeUseCase.execute(recipeId, warehouseId)
            val bookmarked = response.isBookmarked
            recipeId = response.id

            val mediaSlider = mapToMediaSlider(response)
            val recipeInfo = mapToRecipeInfo(response)
            val recipeTab = mapToRecipeTab(response, addressData)

            layoutItemList.clear()
            layoutItemList.add(mediaSlider)
            layoutItemList.add(recipeInfo)
            layoutItemList.add(recipeTab)

            miniCartData?.let {
                updateProductQuantity(it)
            }

            _isBookmarked.postValue(bookmarked)
            _recipeInfo.postValue(recipeInfo)
            _layoutList.postValue(layoutItemList)
        }) {
            hideLoading()
            showError()
        }
    }

    private fun getAddress() {
        getAddressUseCase.getStateChosenAddress( {
            addressData.updateAddressData(it)
            checkAddressData()
        },{

        }, GET_ADDRESS_SOURCE)
    }

    private fun setMiniCartAndProductQuantity(miniCart: MiniCartSimplifiedData) {
        setMiniCartData(miniCart)
        updateProductQuantity(miniCart)
    }

    private fun updateCartItem(cartId: String, quantity: Int, notes: String) {
        val updateCartRequest = UpdateCartRequest(
            cartId = cartId,
            quantity = quantity,
            notes = notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES,
        )
        updateCartUseCase.execute({
            _updateCartItem.postValue(Success(it))
        }, {
            _updateCartItem.postValue(Fail(it))
        })
    }

    private fun updateProductQuantity(miniCart: MiniCartSimplifiedData) {
        layoutItemList.updateProductQuantity(miniCart)
        layoutItemList.updateDeletedProductQuantity(miniCart)
    }

    private fun setMiniCartData(miniCart: MiniCartSimplifiedData) {
        miniCartData = miniCart
    }

    private fun shouldGetMiniCart(shopId: List<String>): Boolean {
        val warehouseId = addressData.getWarehouseId()
        val outOfCoverage = warehouseId == OOC_WAREHOUSE_ID
        return !shopId.isNullOrEmpty() && !outOfCoverage && userSession.isLoggedIn
    }

    private fun getRecipeTitle() = _recipeInfo.value?.title.orEmpty()

    private fun hideLoading() {
        layoutItemList.removeLoadingItem()
        _layoutList.postValue(layoutItemList)
    }

    private fun showError() {
        layoutItemList.add(TokoNowServerErrorUiModel)
        _layoutList.postValue(layoutItemList)
    }
}