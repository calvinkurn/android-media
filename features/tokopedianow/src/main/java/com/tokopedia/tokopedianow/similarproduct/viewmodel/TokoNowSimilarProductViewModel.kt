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
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeSimilarProductMapper.updateDeletedProductQuantity
import com.tokopedia.tokopedianow.recipedetail.presentation.mapper.RecipeSimilarProductMapper.updateProductQuantity
import com.tokopedia.tokopedianow.similarproduct.domain.model.RecommendationItem
import com.tokopedia.tokopedianow.similarproduct.domain.usecase.GetSimilarProductUseCase
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowSimilarProductViewModel @Inject constructor(
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    private val getSimilarProductUseCase: GetSimilarProductUseCase,
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

    val similarProductList: LiveData<List<RecommendationItem?>>
        get() = _similarProductList

    private val _similarProductList = MutableLiveData<List<RecommendationItem?>>()

    private val layoutItemList = mutableListOf<Visitable<*>>()

    override fun setMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.setMiniCartData(miniCartData)
        updateProductQuantity(miniCartData)
    }

    fun onViewCreated(productList: List<SimilarProductUiModel>) {
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
    fun getSimilarProductList(userId: Int, productIds: String){
        launchCatchError( block = {
            val response = getSimilarProductUseCase.execute(userId, productIds)
            _similarProductList.postValue(response.productRecommendationWidgetSingle?.data?.recommendation)
        }, onError = {
        })

    }
}
