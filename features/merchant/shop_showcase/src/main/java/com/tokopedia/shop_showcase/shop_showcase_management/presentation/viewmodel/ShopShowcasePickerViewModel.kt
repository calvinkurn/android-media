package com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop_showcase.common.ShopShowcaseDispatchProvider
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseResponse
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.GetShopProductsResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListBuyer.ShopShowcaseListBuyerResponse
//import com.tokopedia.shop_showcase.shop_showcase_management.domain.GetShopShowcaseListBuyerUseCase
import com.tokopedia.shop_showcase.shop_showcase_management.domain.GetShopShowcaseTotalProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopShowcasePickerViewModel @Inject constructor(
//        private val getShopShowcaseListBuyerUseCase: GetShopShowcaseListBuyerUseCase,
        private val getShopShowcaseListBuyerUseCase: GetShopEtalaseByShopUseCase,
        private val getShopShowcaseTotalProductUseCase: GetShopShowcaseTotalProductUseCase,
        private val createShopShowcaseUseCase: CreateShopShowcaseUseCase,
        private val dispatchers: ShopShowcaseDispatchProvider
): BaseViewModel(dispatchers.ui()) {


    private val _getListBuyerShopShowcaseResponse = MutableLiveData<Result<List<ShopEtalaseModel>>>()
    val getListBuyerShopShowcaseResponse: LiveData<Result<List<ShopEtalaseModel>>>
        get() = _getListBuyerShopShowcaseResponse
//    private val _getListBuyerShopShowcaseResponse = MutableLiveData<Result<ShopShowcaseListBuyerResponse>>()
//    val getListBuyerShopShowcaseResponse: LiveData<Result<ShopShowcaseListBuyerResponse>>
//        get() = _getListBuyerShopShowcaseResponse

    private val _getShopProductResponse = MutableLiveData<Result<GetShopProductsResponse>>()
    val getShopProductResponse: LiveData<Result<GetShopProductsResponse>>
        get() = _getShopProductResponse

    private val _createShopShowcase = MutableLiveData<Result<AddShopShowcaseResponse>>()
    val createShopShowcase: LiveData<Result<AddShopShowcaseResponse>> get() = _createShopShowcase

    fun getShopShowcaseListAsBuyer(shopId: String, isOwner: Boolean) {
        launchCatchError(block = {
            val showcaseList = withContext(dispatchers.io()) {
                clearGetShowcaseCache()
                val requestParam = GetShopEtalaseByShopUseCase.createRequestParams(
                        shopId = shopId,
                        hideNoCount = GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.HIDE_NO_COUNT_VALUE,
                        hideShowCaseGroup = GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.HIDE_SHOWCASE_GROUP_VALUE,
                        isOwner = isOwner
                )

                getShopShowcaseListBuyerUseCase.createObservable(requestParam)
                        .toBlocking().first()
            }

            _getListBuyerShopShowcaseResponse.postValue(Success(showcaseList))

//            withContext(dispatchers.io()) {
//                getShopShowcaseListBuyerUseCase.params = GetShopShowcaseListBuyerUseCase
//                        .createRequestParam(shopId, isOwner)
//                val shopShowcaseData = getShopShowcaseListBuyerUseCase.executeOnBackground()
//                shopShowcaseData.let {
//                    _getListBuyerShopShowcaseResponse.postValue(Success(it))
//                }
//            }
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

    fun addShopShowcase(data: AddShopShowcaseParam) {
        launchCatchError(block = {
            withContext(dispatchers.io()) {
                createShopShowcaseUseCase.params = CreateShopShowcaseUseCase.createRequestParams(data)
                _createShopShowcase.postValue(Success(createShopShowcaseUseCase.executeOnBackground()))
            }
        }, onError = {
            _createShopShowcase.value = Fail(it)
        })
    }

    fun clearGetShowcaseCache() {
        getShopShowcaseListBuyerUseCase.clearCache()
    }

}