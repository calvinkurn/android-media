package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
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
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import javax.inject.Inject

class TokoNowRecipeDetailViewModel @Inject constructor(
    private val getRecipeUseCase: GetRecipeUseCase,
    private val getAddressUseCase: GetChosenAddressWarehouseLocUseCase,
    private val addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase,
    private val removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase,
    private val addressData: TokoNowLocalAddress,
    userSession: UserSessionInterface,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowViewModel(
    addToCartUseCase,
    updateCartUseCase,
    deleteCartUseCase,
    getMiniCartUseCase,
    addressData,
    userSession,
    dispatchers
) {

    companion object {
        private const val GET_ADDRESS_SOURCE = "tokonow"

        private const val INVALID_SHOP_ID = 0L
        private const val BOOKMARK_DELAY = 500L
    }

    val layoutList: LiveData<List<Visitable<*>>>
        get() = _layoutList
    val recipeInfo: LiveData<RecipeInfoUiModel>
        get() = _recipeInfo
    val addBookmark: LiveData<BookmarkUiModel>
        get() = _addBookmark
    val removeBookmark: LiveData<BookmarkUiModel>
        get() = _removeBookmark
    val isBookmarked: LiveData<Boolean>
        get() = _isBookmarked

    private val _layoutList = MutableLiveData<List<Visitable<*>>>()
    private val _recipeInfo = MutableLiveData<RecipeInfoUiModel>()
    private val _addBookmark = MutableLiveData<BookmarkUiModel>()
    private val _removeBookmark = MutableLiveData<BookmarkUiModel>()
    private val _isBookmarked = MutableLiveData<Boolean>()

    private var recipeId: String = ""
    private var slug: String = ""

    private val layoutItemList = mutableListOf<Visitable<*>>()

    private var bookmarkJob: Job? = null

    override fun setMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.setMiniCartData(miniCartData)
        updateProductQuantity(miniCartData)
    }

    fun checkAddressData() {
        val shopId = addressData.getShopId()

        if (shopId == INVALID_SHOP_ID) {
            getAddress()
        } else {
            getRecipe()
        }
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

    fun updateAddressData() {
        addressData.updateLocalData()
    }

    fun setRecipeData(recipeId: String, slug: String) {
        this.recipeId = recipeId
        this.slug = slug
    }

    private fun getRecipe() {
        launchCatchError(block = {
            val warehouseId = addressData.getWarehouseId().toString()
            val response = getRecipeUseCase.execute(recipeId, slug, warehouseId)
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
                setMiniCartData(it)
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
        getAddressUseCase.getStateChosenAddress({
            addressData.updateAddressData(it)
            checkAddressData()
        }, {
            hideLoading()
            showError()
        }, GET_ADDRESS_SOURCE)
    }

    private fun updateProductQuantity(miniCartData: MiniCartSimplifiedData) {
        launchCatchError(block = {
            layoutItemList.updateProductQuantity(miniCartData)
            layoutItemList.updateDeletedProductQuantity(miniCartData)
            _layoutList.postValue(layoutItemList)
        }) {
            // do nothing
        }
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