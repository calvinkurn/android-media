package com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopShowcaseListSellerResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopShowcasePickerViewModel @Inject constructor(
        private val getShopEtalaseUseCase: GetShopEtalaseUseCase,
        private val getProductListUseCase: GetProductListUseCase,
        private val createShopShowcaseUseCase: CreateShopShowcaseUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _getListSellerShopShowcaseResponse = MutableLiveData<Result<ShopShowcaseListSellerResponse>>()
    val getListSellerShopShowcaseResponse: LiveData<Result<ShopShowcaseListSellerResponse>>
        get() = _getListSellerShopShowcaseResponse

    private val _shopTotalProduct = MutableLiveData<Result<Int>>()
    val shopTotalProduct: LiveData<Result<Int>>
        get() = _shopTotalProduct

    private val _createShopShowcase = MutableLiveData<Result<AddShopShowcaseResponse>>()
    val createShopShowcase: LiveData<Result<AddShopShowcaseResponse>> get() = _createShopShowcase

    fun getShopShowcaseListAsSeller() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getShopEtalaseUseCase.params = GetShopEtalaseUseCase.createRequestParams(
                        // set withDefault to false to avoid get default generated showcase
                        withDefault = false
                )
                val shopShowcaseData = getShopEtalaseUseCase.executeOnBackground()
                shopShowcaseData.let {
                    _getListSellerShopShowcaseResponse.postValue(Success(it))
                }
            }
        }) {
            _getListSellerShopShowcaseResponse.value = Fail(it)
        }
    }

    fun getTotalProducts(shopId: String, filter: GetProductListFilter) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getProductListUseCase.params = GetProductListUseCase.createRequestParams(shopId, filter)
                val productListResponse = getProductListUseCase.executeOnBackground()
                productListResponse.let { productList ->
                    _shopTotalProduct.postValue(Success(productList.size))
                }
            }
        }) {
            _shopTotalProduct.value = Fail(it)
        }
    }

    fun addShopShowcase(data: AddShopShowcaseParam) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                createShopShowcaseUseCase.params = CreateShopShowcaseUseCase.createRequestParams(data)
                _createShopShowcase.postValue(Success(createShopShowcaseUseCase.executeOnBackground()))
            }
        }, onError = {
            _createShopShowcase.value = Fail(it)
        })
    }

}