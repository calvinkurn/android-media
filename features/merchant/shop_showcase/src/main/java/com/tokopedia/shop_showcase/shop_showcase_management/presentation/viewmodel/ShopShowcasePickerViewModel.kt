package com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopShowcaseListSellerResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.GetShopProductsResponse
import com.tokopedia.shop_showcase.shop_showcase_management.domain.GetShopShowcaseTotalProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopShowcasePickerViewModel @Inject constructor(
        private val getShopEtalaseUseCase: GetShopEtalaseUseCase,
        private val getShopShowcaseTotalProductUseCase: GetShopShowcaseTotalProductUseCase,
        private val createShopShowcaseUseCase: CreateShopShowcaseUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _getListSellerShopShowcaseResponse = MutableLiveData<Result<ShopShowcaseListSellerResponse>>()
    val getListSellerShopShowcaseResponse: LiveData<Result<ShopShowcaseListSellerResponse>>
        get() = _getListSellerShopShowcaseResponse

    private val _getShopProductResponse = MutableLiveData<Result<GetShopProductsResponse>>()
    val getShopProductResponse: LiveData<Result<GetShopProductsResponse>>
        get() = _getShopProductResponse

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

    fun getTotalProduct(shopId: String, page: Int, perPage: Int,
                        sortId: Int, etalase: String, search: String) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                val paramInput = mapOf(
                        "page" to page,
                        "fkeyword" to search,
                        "perPage" to perPage,
                        "fmenu" to etalase,
                        "sort" to sortId
                )

                getShopShowcaseTotalProductUseCase.params = GetShopShowcaseTotalProductUseCase.createRequestParam(shopId, paramInput)
                val shopProductData = getShopShowcaseTotalProductUseCase.executeOnBackground()
                shopProductData.let {
                    _getShopProductResponse.postValue(Success(it))
                }
            }
        }) {
            _getShopProductResponse.value = Fail(it)
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