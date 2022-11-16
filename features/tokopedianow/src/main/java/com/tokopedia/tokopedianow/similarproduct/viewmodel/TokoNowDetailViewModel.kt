package com.tokopedia.tokopedianow.similarproduct.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.updateDeletedProductQuantity
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeDetailMapper.updateProductQuantity
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.BookmarkUiModel
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeDetailLoadingUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowDetailViewModel @Inject constructor(
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

    val layoutList: LiveData<List<Visitable<*>>>
        get() = _layoutList
    val removeBookmark: LiveData<BookmarkUiModel>
        get() = _removeBookmark
    val isBookmarked: LiveData<Boolean>
        get() = _isBookmarked

    private val _layoutList = MutableLiveData<List<Visitable<*>>>()
    private val _removeBookmark = MutableLiveData<BookmarkUiModel>()
    private val _isBookmarked = MutableLiveData<Boolean>()

    private val layoutItemList = mutableListOf<Visitable<*>>()

    override fun setMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.setMiniCartData(miniCartData)
        updateProductQuantity(miniCartData)
    }

    fun refreshPage() {
        showLoading()
    }

    fun showLoading() {
        layoutItemList.clear()
        layoutItemList.add(RecipeDetailLoadingUiModel)
        _layoutList.postValue(layoutItemList)
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
}
