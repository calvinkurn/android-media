package com.tokopedia.productcard_compact.similarproduct.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.productcard_compact.common.helper.LocalAddressHelper
import com.tokopedia.productcard_compact.common.viewmodel.BaseCartViewModel
import com.tokopedia.productcard_compact.similarproduct.domain.model.ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem
import com.tokopedia.productcard_compact.similarproduct.domain.usecase.GetSimilarProductUseCase
import com.tokopedia.productcard_compact.similarproduct.presentation.mapper.ProductCardCompactSimilarProductMapper.updateDeletedProductQuantity
import com.tokopedia.productcard_compact.similarproduct.presentation.mapper.ProductCardCompactSimilarProductMapper.updateProductQuantity
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductCardCompactSimilarProductViewModel @Inject constructor(
    private val getSimilarProductUseCase: GetSimilarProductUseCase,
    private val userSession: UserSessionInterface,
    private val localAddressHelper: LocalAddressHelper,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    dispatchers: CoroutineDispatchers
) : BaseCartViewModel(
    addToCartUseCase,
    updateCartUseCase,
    deleteCartUseCase,
    getMiniCartUseCase,
    localAddressHelper,
    userSession,
    dispatchers
) {
    private companion object {
        const val USER_CITY_ID = "user_cityId"
        const val USER_ADDRESS_ID = "user_addressId"
        const val USER_DISTRICT_ID = "user_districtId"
        const val USER_LAT = "user_lat"
        const val USER_LONG = "user_long"
        const val USER_POST_CODE = "user_postCode"
        const val USER_WAREHOUSE_IDs = "warehouse_ids"
    }

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

    init {
        miniCartSource = MiniCartSource.TokonowHome
    }

    override fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.onSuccessGetMiniCartData(miniCartData)
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
        val chooseAddressData = localAddressHelper.getChooseAddressData()
        warehouseId = chooseAddressData.warehouse_id

        if (chooseAddressData.city_id.isNotEmpty())
            tokonowQueryParam[USER_CITY_ID] = chooseAddressData.city_id
        if (chooseAddressData.address_id.isNotEmpty())
            tokonowQueryParam[USER_ADDRESS_ID] = chooseAddressData.address_id
        if (chooseAddressData.district_id.isNotEmpty())
            tokonowQueryParam[USER_DISTRICT_ID] = chooseAddressData.district_id
        if (chooseAddressData.lat.isNotEmpty())
            tokonowQueryParam[USER_LAT] = chooseAddressData.lat
        if (chooseAddressData.long.isNotEmpty())
            tokonowQueryParam[USER_LONG] = chooseAddressData.long
        if (chooseAddressData.postal_code.isNotEmpty())
            tokonowQueryParam[USER_POST_CODE] = chooseAddressData.postal_code
        if (chooseAddressData.warehouse_id.isNotEmpty())
            tokonowQueryParam[USER_WAREHOUSE_IDs] = chooseAddressData.warehouse_id

        return tokonowQueryParam
    }

    fun onViewCreated(productList: List<ProductCardCompactSimilarProductUiModel>) {
        layoutItemList.addAll(productList)

        miniCartData?.let {
            updateProductQuantity(it)
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
