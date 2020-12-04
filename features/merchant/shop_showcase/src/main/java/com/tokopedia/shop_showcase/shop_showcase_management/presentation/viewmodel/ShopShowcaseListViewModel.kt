package com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopShowcaseListSellerResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.tokopedia.shop_showcase.shop_showcase_management.domain.*

class ShopShowcaseListViewModel @Inject constructor(
        private val getShopShowcaseListBuyerUseCase: GetShopEtalaseByShopUseCase,
        private val getShopEtalaseUseCase: GetShopEtalaseUseCase,
        private val deleteShopShowcaseUseCase: DeleteShopShowcaseUseCase,
        private val reorderShopShowcaseUseCase: ReorderShopShowcaseListUseCase,
        private val getShopShowcaseTotalProductUseCase: GetShopShowcaseTotalProductUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val _getListBuyerShopShowcaseResponse = MutableLiveData<Result<List<ShopEtalaseModel>>>()
    val getListBuyerShopShowcaseResponse: LiveData<Result<List<ShopEtalaseModel>>>
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


    fun getShopShowcaseListAsBuyer(shopId: String, isOwner: Boolean, hideShowCaseGroup: Boolean) {
        launchCatchError(block = {
            val showcaseList = withContext(dispatchers.io) {
                clearGetShowcaseCache()
                val requestParam = if (isOwner) {
                    GetShopEtalaseByShopUseCase.createRequestParams(
                            shopId = shopId,
                            hideNoCount = GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.HIDE_NO_COUNT_VALUE,
                            hideShowCaseGroup = hideShowCaseGroup,
                            isOwner = GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.IS_OWNER_VALUE
                    )
                } else {
                    GetShopEtalaseByShopUseCase.createRequestParams(
                            shopId = shopId,
                            hideNoCount = GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.HIDE_NO_COUNT_VALUE,
                            hideShowCaseGroup = hideShowCaseGroup,
                            isOwner = GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.IS_OWNER_VALUE
                    )
                }

                getShopShowcaseListBuyerUseCase.createObservable(requestParam)
                        .toBlocking().first()
            }

            _getListBuyerShopShowcaseResponse.postValue(Success(showcaseList))
        }) {
            _getListBuyerShopShowcaseResponse.value = Fail(it)
        }
    }

    fun getShopShowcaseListAsSeller() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getShopEtalaseUseCase.params = GetShopEtalaseUseCase.createRequestParams(
                        // set withDefault to true for managing etalase purpose
                        withDefault = true
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

    fun removeSingleShopShowcase(showcaseId: String) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
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
            withContext(dispatchers.io) {
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
            withContext(dispatchers.io) {
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

    fun clearGetShowcaseCache() {
        getShopShowcaseListBuyerUseCase.clearCache()
    }

}