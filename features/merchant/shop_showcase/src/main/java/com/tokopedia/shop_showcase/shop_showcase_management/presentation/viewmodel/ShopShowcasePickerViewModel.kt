package com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop_showcase.common.ShopShowcaseDispatchProvider
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.GetShopProductsResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListBuyer.ShopShowcaseListBuyerResponse
import com.tokopedia.shop_showcase.shop_showcase_management.domain.GetShopShowcaseListBuyerUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.domain.GetShopShowcaseTotalProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopShowcasePickerViewModel @Inject constructor(
        private val getShopShowcaseListBuyerUseCase: GetShopShowcaseListBuyerUseCase,
        private val getShopShowcaseTotalProductUseCase: GetShopShowcaseTotalProductUseCase,
        private val dispatchers: ShopShowcaseDispatchProvider
): BaseViewModel(dispatchers.ui()) {

    private val _getListBuyerShopShowcaseResponse = MutableLiveData<Result<ShopShowcaseListBuyerResponse>>()
    val getListBuyerShopShowcaseResponse: LiveData<Result<ShopShowcaseListBuyerResponse>>
        get() = _getListBuyerShopShowcaseResponse

    private val _getShopProductResponse = MutableLiveData<Result<GetShopProductsResponse>>()
    val getShopProductResponse: LiveData<Result<GetShopProductsResponse>>
        get() = _getShopProductResponse

    fun getShopShowcaseListAsBuyer(shopId: String, isOwner: Boolean) {
        launchCatchError(block = {
            withContext(dispatchers.io()) {
                getShopShowcaseListBuyerUseCase.params = GetShopShowcaseListBuyerUseCase
                        .createRequestParam(shopId, isOwner)
                val shopShowcaseData = getShopShowcaseListBuyerUseCase.executeOnBackground()
                shopShowcaseData.let {
                    _getListBuyerShopShowcaseResponse.postValue(Success(it))
                }
            }
        }) {
            _getListBuyerShopShowcaseResponse.value = Fail(it)
        }
    }

    fun getTotalProduct(shopId: String, page: Int, perPage: Int,
                        sortId: Int, etalase: String, search: String) {
        launchCatchError(block = {
            withContext(dispatchers.io()) {
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

}