package com.tokopedia.catalog.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.catalog.ui.model.CatalogProductAtcUiModel
import com.tokopedia.catalog.domain.GetProductListFromSearchUseCase
import com.tokopedia.catalog.domain.model.CatalogProductItem
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.oldcatalog.model.raw.ProductListResponse
import com.tokopedia.oldcatalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogQuickFilterUseCase
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class CatalogProductListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private var quickFilterUseCase: CatalogQuickFilterUseCase,
    private val dynamicFilterUseCase: CatalogDynamicFilterUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getNotificationUseCase: GetNotificationUseCase,
    private val getProductListUseCase: GetProductListFromSearchUseCase
) : BaseViewModel(dispatchers.main) {

    val quickFilterResult = MutableLiveData<Result<DynamicFilterModel>>()

    val quickFilterModel = MutableLiveData<DynamicFilterModel>()

    val quickFilterClicked = MutableLiveData<Boolean>()

    val dynamicFilterResult = MutableLiveData<Result<DynamicFilterModel>>()

    val dynamicFilterModel = MutableLiveData<DynamicFilterModel>()

    var quickFilterOptionList: MutableList<Option> = ArrayList()

    var filterController: FilterController? = FilterController()

    var searchParameter: SearchParameter = SearchParameter()

    val searchParametersMap = MutableLiveData<HashMap<String, String>>()

    val selectedSortIndicatorCount = MutableLiveData<Int>()

    val mDynamicFilterModel: LiveData<Result<DynamicFilterModel>>
        get() = dynamicFilterResult

    private val _errorsToaster = MutableLiveData<Throwable>()
    val errorsToaster: LiveData<Throwable>
        get() = _errorsToaster

    private val _textToaster = MutableLiveData<String>()
    val textToaster: LiveData<String>
        get() = _textToaster

    private val _totalCartItem = MutableLiveData<Int>()
    val totalCartItem: LiveData<Int>
        get() = _totalCartItem

    val productList: LiveData<Result<List<CatalogProductItem>>>
        get() = _productList

    private val _productList = MutableLiveData<Result<List<CatalogProductItem>>>()


    var comparisonCardIsAdded = false

    var pageCount = 0

    fun fetchQuickFilters(params: RequestParams) {

        quickFilterUseCase.execute(params, object : Subscriber<DynamicFilterModel>() {
            override fun onNext(t: DynamicFilterModel?) {
                quickFilterResult.value = Success(t as DynamicFilterModel)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                quickFilterResult.value = Fail(e)
            }
        })
    }

    fun fetchDynamicAttribute(params: RequestParams) {
        dynamicFilterUseCase.execute(params, object : Subscriber<DynamicFilterModel>() {
            override fun onNext(t: DynamicFilterModel?) {
                dynamicFilterResult.value = Success(t as DynamicFilterModel)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                dynamicFilterResult.value = (Fail(e))
            }
        })
    }

    fun fetchProductListing(params: RequestParams) {
        comparisonCardIsAdded = pageCount != 0
        launchCatchError(
            dispatchers.io,
            block = {
                val data = getProductListUseCase.execute(params).searchProduct?.data?.catalogProductItemList.orEmpty()
                _productList.postValue(Success(data))

            },
            onError = {
                _productList.postValue(Fail(it))
            }
        )
    }

    fun addProductToCart(atcUiModel: CatalogProductAtcUiModel) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = AddToCartRequestParams(
                    productId = atcUiModel.productId,
                    shopId = atcUiModel.shopId,
                    quantity = atcUiModel.quantity,
                    warehouseId = atcUiModel.warehouseId
                )
                addToCartUseCase.setParams(param)
                showAtcResult(addToCartUseCase.executeOnBackground())
            },
            onError = {
                _errorsToaster.postValue(it)
            }
        )
    }

    private fun showAtcResult(atcResult: AddToCartDataModel) {
        if (atcResult.isStatusError()) {
            _errorsToaster.postValue(MessageErrorException(atcResult.getAtcErrorMessage()))
        } else {
            _textToaster.postValue(atcResult.getAtcErrorMessage())
        }
    }

    fun refreshNotification() {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getNotificationUseCase.executeOnBackground()
                _totalCartItem.postValue(result.totalCart)
            },
            onError = {
                _errorsToaster.postValue(it)
            }
        )
    }
}
