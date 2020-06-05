package com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListBuyer.ShopShowcaseListBuyerResponse
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListSeller.ShopShowcaseListSellerResponse
import com.tokopedia.shop_showcase.shop_showcase_management.domain.*

class ShopShowcaseListViewModel @Inject constructor(
        private val getShopShowcaseListBuyerUseCase: GetShopShowcaseListBuyerUseCase,
        private val getShopShowcaseListSellerUseCase: GetShopShowcaseListSellerUseCase,
        private val deleteShopShowcaseUseCase: DeleteShopShowcaseUseCase,
        private val reorderShopShowcaseUseCase: ReorderShopShowcaseListUseCase,
        private val getShopShowcaseTotalProductUseCase: GetShopShowcaseTotalProductUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val _getListBuyerShopShowcaseResponse = MutableLiveData<Result<ShopShowcaseListBuyerResponse>>()
    val getListBuyerShopShowcaseResponse: LiveData<Result<ShopShowcaseListBuyerResponse>>
        get() = _getListBuyerShopShowcaseResponse

    private val _getListSellerShopShowcaseResponse = MutableLiveData<Result<ShopShowcaseListSellerResponse>>()
    val getListSellerShopShowcaseResponse: LiveData<Result<ShopShowcaseListSellerResponse>>
        get() = _getListSellerShopShowcaseResponse

    private val _deleteShopShowcaseResponse = MutableLiveData<Result<DeleteShopShowcaseResponse>>()
    val deleteShopShowcaseResponse: LiveData<Result<DeleteShopShowcaseResponse>>
        get() = _deleteShopShowcaseResponse

    private val _reoderShopShowcaseResponse =  MutableLiveData<Result<ReorderShopShowcaseResponse>>()
    val reoderShopShowcaseResponse: LiveData<Result<ReorderShopShowcaseResponse>>
        get() = _reoderShopShowcaseResponse

    private val _getShopProductResponse = MutableLiveData<Result<GetShopProductsResponse>>()
    val getShopProductResponse: LiveData<Result<GetShopProductsResponse>>
        get() = _getShopProductResponse


    fun getShopShowcaseListAsBuyer(shopId: String, isOwner: Boolean) {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
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

    fun getShopShowcaseListAsSeller() {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val shopShowcaseData = getShopShowcaseListSellerUseCase.executeOnBackground()
                shopShowcaseData.let {
                    _getListSellerShopShowcaseResponse.postValue(Success(it))
                }
            }
        }) {
            _getListSellerShopShowcaseResponse.value = Fail(it)
        }
    }

    fun removeSingleShopShowcase(showcaseId: String) {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                deleteShopShowcaseUseCase.params = DeleteShopShowcaseUseCase.createRequestParam(showcaseId)
                val removeSingleShowcaseData = deleteShopShowcaseUseCase.executeOnBackground()
                removeSingleShowcaseData.let {
                    _deleteShopShowcaseResponse.postValue(Success(it))
                }
            }
        }) {
            _deleteShopShowcaseResponse.value = Fail(it)
        }
    }

    fun reorderShopShowcaseList(ids: List<String>) {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                reorderShopShowcaseUseCase.params = ReorderShopShowcaseListUseCase.createRequestParam(ids)
                val reorderShowcaseData = reorderShopShowcaseUseCase.executeOnBackground()
                reorderShowcaseData.let {
                    _reoderShopShowcaseResponse.postValue(Success(it))
                }
            }
        }) {
            _reoderShopShowcaseResponse.value = Fail(it)
        }
    }

    fun getTotalProduct(shopId: String, page: Int, perPage: Int,
            sortId: Int, etalase: String, search: String) {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                var paramInput = mapOf(
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