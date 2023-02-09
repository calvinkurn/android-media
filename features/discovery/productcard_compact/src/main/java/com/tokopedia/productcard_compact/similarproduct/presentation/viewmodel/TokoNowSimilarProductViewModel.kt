package com.tokopedia.productcard_compact.similarproduct.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.productcard_compact.common.util.ChooseAddressWrapper
import com.tokopedia.productcard_compact.common.util.LocalAddress
import com.tokopedia.productcard_compact.similarproduct.domain.model.ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem
import com.tokopedia.productcard_compact.similarproduct.domain.usecase.GetSimilarProductUseCase
import com.tokopedia.productcard_compact.similarproduct.presentation.mapper.SimilarProductMapper.updateDeletedProductQuantity
import com.tokopedia.productcard_compact.similarproduct.presentation.mapper.SimilarProductMapper.updateProductQuantity
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.SimilarProductUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowSimilarProductViewModel @Inject constructor(
    private val getSimilarProductUseCase: GetSimilarProductUseCase,
    private val chooseAddressWrapper: ChooseAddressWrapper,
    private val userSession: UserSessionInterface,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addressData: LocalAddress,
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
    private val _visitableItems = MutableLiveData<List<Visitable<*>>>()
    private val _similarProductList = MutableLiveData<List<RecommendationItem?>>()
    private val layoutItemList = mutableListOf<Visitable<*>>()

    val visitableItems: LiveData<List<Visitable<*>>>
        get() = _visitableItems
    val similarProductList: LiveData<List<RecommendationItem?>>
        get() = _similarProductList

    val userId: String
        get() = userSession.userId
    val isLoggedIn: Boolean
        get() = userSession.isLoggedIn

    var warehouseId: String = ""
        private set

    override fun setMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.setMiniCartData(miniCartData)
        updateProductQuantity(miniCartData)
    }

    private fun updateProductQuantity(miniCartData: MiniCartSimplifiedData) {
        launchCatchError(block = {
            layoutItemList.updateProductQuantity(miniCartData)
            layoutItemList.updateDeletedProductQuantity(miniCartData)
            _visitableItems.postValue(layoutItemList)
        }) {}
    }

    private fun appendChooseAddressParams(): MutableMap<String, Any> {
        val tokonowQueryParam: MutableMap<String, Any> = mutableMapOf()
        val chooseAddressData = chooseAddressWrapper.getChooseAddressData()
        warehouseId = chooseAddressData.warehouse_id

        if (chooseAddressData.city_id.isNotEmpty())
            tokonowQueryParam[SearchApiConst.USER_CITY_ID] = chooseAddressData.city_id
        if (chooseAddressData.address_id.isNotEmpty())
            tokonowQueryParam[SearchApiConst.USER_ADDRESS_ID] = chooseAddressData.address_id
        if (chooseAddressData.district_id.isNotEmpty())
            tokonowQueryParam[SearchApiConst.USER_DISTRICT_ID] = chooseAddressData.district_id
        if (chooseAddressData.lat.isNotEmpty())
            tokonowQueryParam[SearchApiConst.USER_LAT] = chooseAddressData.lat
        if (chooseAddressData.long.isNotEmpty())
            tokonowQueryParam[SearchApiConst.USER_LONG] = chooseAddressData.long
        if (chooseAddressData.postal_code.isNotEmpty())
            tokonowQueryParam[SearchApiConst.USER_POST_CODE] = chooseAddressData.postal_code
        if (chooseAddressData.warehouse_id.isNotEmpty())
            tokonowQueryParam[SearchApiConst.USER_WAREHOUSE_IDs] = chooseAddressData.warehouse_id

        return tokonowQueryParam
    }

    fun onViewCreated(productList: List<SimilarProductUiModel>) {
        layoutItemList.addAll(productList)

        miniCartData?.let {
            setMiniCartData(it)
        }

        _visitableItems.postValue(layoutItemList)
    }

    fun getSimilarProductList(productIds: String){
        launchCatchError( block = {
            val response = getSimilarProductUseCase.execute(userId.toIntOrZero(), productIds, appendChooseAddressParams())
            _similarProductList.postValue(response.productRecommendationWidgetSingle?.data?.recommendation.orEmpty())
        }, onError = {
        })

    }
}
