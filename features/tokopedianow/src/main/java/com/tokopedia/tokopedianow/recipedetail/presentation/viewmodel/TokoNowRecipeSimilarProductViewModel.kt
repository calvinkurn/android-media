package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

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
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeSimilarProductMapper.updateDeletedProductQuantity
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeSimilarProductMapper.updateProductQuantity
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowRecipeSimilarProductViewModel @Inject constructor(
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addressData: TokoNowLocalAddress,
    userSession: UserSessionInterface,
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

    val visitableItems: LiveData<List<Visitable<*>>>
        get() = _visitableItems

    private val _visitableItems = MutableLiveData<List<Visitable<*>>>()

    private val layoutItemList = mutableListOf<Visitable<*>>()

    override fun setMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.setMiniCartData(miniCartData)
        updateProductQuantity(miniCartData)
    }

    fun onViewCreated(productList: List<RecipeProductUiModel>) {
        layoutItemList.addAll(productList)

        miniCartData?.let {
            setMiniCartData(it)
        }

        _visitableItems.postValue(layoutItemList)
    }

    private fun updateProductQuantity(miniCartData: MiniCartSimplifiedData) {
        launchCatchError(block = {
            layoutItemList.updateProductQuantity(miniCartData)
            layoutItemList.updateDeletedProductQuantity(miniCartData)
            _visitableItems.postValue(layoutItemList)
        }) {}
    }
}