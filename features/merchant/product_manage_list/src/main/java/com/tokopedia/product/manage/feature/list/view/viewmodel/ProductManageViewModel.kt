package com.tokopedia.product.manage.feature.list.view.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.coroutine.CoroutineDispatchers
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper.Companion.countSelectedFilter
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.domain.GetProductListMetaUseCase
import com.tokopedia.product.manage.feature.list.domain.PopupManagerAddProductUseCase
import com.tokopedia.product.manage.feature.list.view.mapper.ProductMapper.mapToFilterTabResult
import com.tokopedia.product.manage.feature.list.domain.SetFeaturedProductUseCase
import com.tokopedia.product.manage.feature.list.view.mapper.ProductMapper.mapToViewModels
import com.tokopedia.product.manage.feature.list.view.model.*
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.*
import com.tokopedia.product.manage.feature.multiedit.data.param.MenuParam
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.ViewState.*
import com.tokopedia.product.manage.feature.multiedit.data.param.ProductParam
import com.tokopedia.product.manage.feature.multiedit.data.param.ShopParam
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.feature.quickedit.variant.data.mapper.ProductManageVariantMapper.mapResultToUpdateParam
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.feature.quickedit.variant.domain.EditProductVariantUseCase
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption
import com.tokopedia.shop.common.domain.interactor.GQLGetProductListUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ProductManageViewModel @Inject constructor(
    private val editPriceUseCase: EditPriceUseCase,
    private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val topAdsGetShopDepositGraphQLUseCase: TopAdsGetShopDepositGraphQLUseCase,
    private val popupManagerAddProductUseCase: PopupManagerAddProductUseCase,
    private val getProductListUseCase: GQLGetProductListUseCase,
    private val setFeaturedProductUseCase: SetFeaturedProductUseCase,
    private val editStockUseCase: EditStockUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val multiEditProductUseCase: MultiEditProductUseCase,
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val editProductVariantUseCase: EditProductVariantUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    companion object {
        // Currently update data on server is not realtime.
        // Client need to add request delay in order to receive updated data.
        private const val REQUEST_DELAY = 1000L
    }

    val viewState: LiveData<ViewState>
        get() = _viewState
    val productListResult: LiveData<Result<List<ProductViewModel>>>
        get() = _productListResult
    val productListFeaturedOnlyResult: LiveData<Result<Int>>
        get() = _productListFeaturedOnlyResult
    val shopInfoResult: LiveData<Result<ShopInfoResult>>
        get() = _shopInfoResult
    val deleteProductResult: LiveData<Result<DeleteProductResult>>
        get() = _deleteProductResult
    val editPriceResult: LiveData<Result<EditPriceResult>>
        get() = _editPriceResult
    val editStockResult: LiveData<Result<EditStockResult>>
        get() = _editStockResult
    val getFreeClaimResult: LiveData<Result<DataDeposit>>
        get() = _getFreeClaimResult
    val getPopUpResult: LiveData<Result<GetPopUpResult>>
        get() = _getPopUpResult
    val setFeaturedProductResult: LiveData<Result<SetFeaturedProductResult>>
        get() = _setFeaturedProductResult
    val toggleMultiSelect: LiveData<Boolean>
        get() = _toggleMultiSelect
    val multiEditProductResult: LiveData<Result<MultiEditResult>>
        get() = _multiEditProductResult
    val selectedFilterAndSort: LiveData<FilterOptionWrapper>
        get() = _selectedFilterAndSort
    val editVariantPriceResult: LiveData<Result<EditVariantResult>>
        get() = _editVariantPriceResult
    val editVariantStockResult: LiveData<Result<EditVariantResult>>
        get() = _editVariantStockResult
    val productFiltersTab: LiveData<Result<GetFilterTabResult>>
        get() = _productFiltersTab

    private val _viewState = MutableLiveData<ViewState>()
    private val _productListResult = MutableLiveData<Result<List<ProductViewModel>>>()
    private val _productListFeaturedOnlyResult = MutableLiveData<Result<Int>>()
    private val _shopInfoResult = MutableLiveData<Result<ShopInfoResult>>()
    private val _deleteProductResult = MutableLiveData<Result<DeleteProductResult>>()
    private val _editPriceResult = MutableLiveData<Result<EditPriceResult>>()
    private val _editStockResult = MutableLiveData<Result<EditStockResult>>()
    private val _getFreeClaimResult = MutableLiveData<Result<DataDeposit>>()
    private val _getPopUpResult = MutableLiveData<Result<GetPopUpResult>>()
    private val _setFeaturedProductResult = MutableLiveData<Result<SetFeaturedProductResult>>()
    private val _toggleMultiSelect = MutableLiveData<Boolean>()
    private val _multiEditProductResult = MutableLiveData<Result<MultiEditResult>>()
    private val _selectedFilterAndSort = MutableLiveData<FilterOptionWrapper>()
    private val _editVariantPriceResult = MutableLiveData<Result<EditVariantResult>>()
    private val _editVariantStockResult = MutableLiveData<Result<EditVariantResult>>()
    private val _productFiltersTab = MutableLiveData<Result<GetFilterTabResult>>()

    private var getProductListJob: Job? = null
    private var getFilterTabJob: Job? = null

    fun isPowerMerchant(): Boolean = userSessionInterface.isGoldMerchant

    fun getGoldMerchantStatus() {
        launchCatchError(block = {
            val status = withContext(dispatchers.io) {
                val shopId: List<Int> = listOf(userSessionInterface.shopId.toIntOrZero())
                gqlGetShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(shopId)

                val shopInfo = gqlGetShopInfoUseCase.executeOnBackground()
                val shopDomain = shopInfo.shopCore.domain
                val isGoldMerchant  = shopInfo.goldOS.isGold == 1
                val isOfficialStore= shopInfo.goldOS.isOfficial == 1

                ShopInfoResult(shopDomain, isGoldMerchant, isOfficialStore)
            }
            _shopInfoResult.value = Success(status)
        }) {
            _shopInfoResult.value = Fail(it)
        }
    }

    fun editProductsByStatus(productIds: List<String>, status: ProductStatus) {
        launchCatchError(block = {
            showProgressDialog()

            val response = withContext(dispatchers.io) {
                val shopParam = ShopParam(userSessionInterface.shopId)

                val params = productIds.map { productId ->
                    ProductParam(productId = productId, shop = shopParam, status = status)
                }

                val requestParams = MultiEditProductUseCase.createRequestParam(params)
                multiEditProductUseCase.execute(requestParams)
            }

            val success = response.results?.filter { it.isSuccess() }.orEmpty()
            val failed = response.results?.filter { !it.isSuccess() }.orEmpty()

            _multiEditProductResult.value = Success(EditByStatus(status, success, failed))
            hideProgressDialog()
        }, onError = {
            _multiEditProductResult.value = Fail(it)
            hideProgressDialog()
        })
    }

    fun editProductsEtalase(productIds: List<String>, menuId: String, menuName: String) {
        launchCatchError(block = {
            showProgressDialog()

            val response = withContext(dispatchers.io) {
                val shopParam = ShopParam(userSessionInterface.shopId)
                val menuParam = MenuParam(menuId, menuName)

                val params = productIds.map { productId ->
                    ProductParam(productId = productId, shop = shopParam, menu = menuParam)
                }

                val requestParams = MultiEditProductUseCase.createRequestParam(params)
                multiEditProductUseCase.execute(requestParams)
            }

            val success = response.results?.filter { it.isSuccess() }.orEmpty()
            val failed = response.results?.filter { !it.isSuccess() }.orEmpty()

            val result = EditByMenu(menuId, menuName, success, failed)
            _multiEditProductResult.value = Success(result)
            hideProgressDialog()
        }, onError = {
            _multiEditProductResult.value = Fail(it)
            hideProgressDialog()
        })
    }

    fun getProductList(
        shopId: String,
        filterOptions: List<FilterOption>? = null,
        sortOption: SortOption? = null,
        isRefresh: Boolean = false,
        withDelay: Boolean = false
    ) {
        getProductListJob?.cancel()

        launchCatchError(block = {
            val productList = withContext(dispatchers.io) {
                if(withDelay) { delay(REQUEST_DELAY) }
                val requestParams = GQLGetProductListUseCase.createRequestParams(shopId, filterOptions, sortOption)
                val getProductList = getProductListUseCase.execute(requestParams)
                val productListResponse = getProductList.productList
                productListResponse?.data
            }

            if(isRefresh) { refreshList() }
            showProductList(productList)
            hideProgressDialog()
        }, onError = {
            if(it is CancellationException) {
                return@launchCatchError
            }
            hideProgressDialog()
            _productListResult.value = Fail(it)
        }).let { getProductListJob = it }
    }

    fun getFiltersTab(withDelay: Boolean = false) {
        getFilterTabJob?.cancel()

        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                if(withDelay) { delay(REQUEST_DELAY) }
                getProductListMetaUseCase.setParams(userSessionInterface.shopId)
                getProductListMetaUseCase.executeOnBackground()
                    .productListMetaWrapper
                    .productListMetaData
                    .tabs
            }

            _productFiltersTab.apply { value = Success(mapToFilterTabResult(response)) }
        }, onError = {
            if(it is CancellationException) {
                return@launchCatchError
            }
            _productFiltersTab.value = Fail(it)
        }).let { getFilterTabJob = it }
    }

    fun getFeaturedProductCount(shopId: String) {
        launchCatchError(block = {
            val productListFeaturedOnly = withContext(dispatchers.io) {
                val requestParams = GQLGetProductListUseCase.createRequestParams(shopId, listOf(FilterOption.FilterByCondition.FeaturedOnly), null)
                val getProductList = getProductListUseCase.execute(requestParams)
                val productListSize = getProductList.productList?.data?.size
                productListSize
            }

            productListFeaturedOnly?.let { setProductListFeaturedOnly(it) }
        }, onError = {
            _productListFeaturedOnlyResult.value = Fail(it)
        })
    }

    fun editPrice(productId: String, price: String, productName: String) {
        showProgressDialog()
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                editPriceUseCase.setParams(userSessionInterface.shopId, productId, price.toFloatOrZero())
                editPriceUseCase.executeOnBackground()
            }
            when {
                result.productUpdateV3Data.isSuccess -> {
                    _editPriceResult.postValue(Success(EditPriceResult(productName, productId, price)))
                }
                result.productUpdateV3Data.header.errorMessage.isNotEmpty() -> {
                    _editPriceResult.postValue(Fail(EditPriceResult(productName, productId, price, Throwable(message = result.productUpdateV3Data.header.errorMessage.last()))))
                }
                else -> {
                    _editPriceResult.postValue(Fail(EditPriceResult(productName, productId, price, NetworkErrorException(R.string.product_stock_reminder_toaster_failed_desc.toString()))))
                }
            }
        }) {
            _editPriceResult.postValue(Fail(EditPriceResult(productName, productId, price, NetworkErrorException(R.string.product_stock_reminder_toaster_failed_desc.toString()))))
        }
        hideProgressDialog()
    }

    fun editStock(productId: String, stock: Int, productName: String, status: ProductStatus) {
        showProgressDialog()
        launchCatchError(block =  {
            val result = withContext(dispatchers.io) {
                editStockUseCase.setParams(userSessionInterface.shopId, productId, stock, status)
                editStockUseCase.executeOnBackground()
            }
            when {
                result.productUpdateV3Data.isSuccess -> {
                    _editStockResult.postValue(Success(EditStockResult(productName, productId, stock, status)))
                }
                result.productUpdateV3Data.header.errorMessage.isNotEmpty() -> {
                    _editStockResult.postValue(Fail(EditStockResult(productName, productId, stock, status, Throwable(message = result.productUpdateV3Data.header.errorMessage.last()))))
                }
                else -> {
                    _editStockResult.postValue(Fail(EditStockResult(productName, productId, stock, status, NetworkErrorException(R.string.product_stock_reminder_toaster_failed_desc.toString()))))
                }
            }
        }) {
            _editStockResult.postValue(Fail(EditStockResult(productName, productId, stock, status, NetworkErrorException(R.string.product_stock_reminder_toaster_failed_desc.toString()))))
        }
        hideProgressDialog()
    }

    fun editVariantsPrice(result: EditVariantResult) {
        showProgressDialog()
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                val shopId = userSessionInterface.shopId
                val variantInputParam = mapResultToUpdateParam(shopId, result)
                val requestParams = EditProductVariantUseCase.createRequestParams(variantInputParam)
                editProductVariantUseCase.execute(requestParams).productUpdateV3Data
            }

            if(response.isSuccess) {
                _editVariantPriceResult.value = Success(result)
            } else {
                val message = response.header.errorMessage.lastOrNull().orEmpty()
                _editVariantPriceResult.value = Fail(MessageErrorException(message))
            }
            hideProgressDialog()
        }) {
            _editVariantPriceResult.value = Fail(it)
            hideProgressDialog()
        }
    }

    fun editVariantsStock(result: EditVariantResult) {
        showProgressDialog()
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                val shopId = userSessionInterface.shopId
                val variantInputParam = mapResultToUpdateParam(shopId, result)
                val requestParams = EditProductVariantUseCase.createRequestParams(variantInputParam)
                editProductVariantUseCase.execute(requestParams).productUpdateV3Data
            }

            if(response.isSuccess) {
                _editVariantStockResult.value = Success(result)
            } else {
                val message = response.header.errorMessage.lastOrNull().orEmpty()
                _editVariantStockResult.value = Fail(MessageErrorException(message))
            }
            hideProgressDialog()
        }) {
            _editVariantStockResult.value = Fail(it)
            hideProgressDialog()
        }
    }

    fun getFreeClaim(graphqlQuery: String, shopId: String) {
        val requestParams = TopAdsGetShopDepositGraphQLUseCase.createRequestParams(graphqlQuery, shopId)
        topAdsGetShopDepositGraphQLUseCase.execute(requestParams,
            object : Subscriber<DataDeposit>() {
                override fun onNext(dataDeposit: DataDeposit) {
                    _getFreeClaimResult.value = Success(dataDeposit)
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    _getFreeClaimResult.value = Fail(e)
                }
            })
    }

    fun getPopupsInfo(productId: String) {
        val shopId = productId.toIntOrZero()
        popupManagerAddProductUseCase.execute(PopupManagerAddProductUseCase.createRequestParams(shopId),
            object : Subscriber<Boolean>() {
                override fun onNext(isSuccess: Boolean) {
                    _getPopUpResult.value = Success(GetPopUpResult(productId, isSuccess))
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    _getPopUpResult.value = Fail(e)
                }

            })
    }

    fun deleteSingleProduct(productName: String, productId: String) {
        showProgressDialog()
        launchCatchError( block = {
            val result = withContext(dispatchers.io) {
                deleteProductUseCase.setParams(userSessionInterface.shopId, productId)
                deleteProductUseCase.executeOnBackground()
            }
            when {
                result.productUpdateV3Data.isSuccess -> {
                    _deleteProductResult.postValue(Success(DeleteProductResult(productName, productId)))
                }
                result.productUpdateV3Data.header.errorMessage.isNotEmpty() -> {
                    _deleteProductResult.postValue(Fail(DeleteProductResult(productName, productId, Throwable(message = result.productUpdateV3Data.header.errorMessage.last()))))
                }
                else -> {
                    _deleteProductResult.postValue(Fail(DeleteProductResult(productName, productId, NetworkErrorException(R.string.product_stock_reminder_toaster_failed_desc.toString()))))
                }
            }
        }) {
            _deleteProductResult.postValue(Fail(DeleteProductResult(productName, productId, NetworkErrorException(R.string.product_stock_reminder_toaster_failed_desc.toString()))))
        }
        hideProgressDialog()
    }

    fun setFeaturedProduct(productId: String, status: Int) {
        launchCatchError( block = {
            setFeaturedProductUseCase.setParams(productId.toInt(), status)
            withContext(dispatchers.io) {
                setFeaturedProductUseCase.executeOnBackground()
            }
            _setFeaturedProductResult.postValue(Success(SetFeaturedProductResult(productId, status)))
        }, onError = { throwable ->
            _setFeaturedProductResult.postValue(Fail(throwable))
        })
    }

    fun setFilterOptionWrapper(filterOptionWrapper: FilterOptionWrapper) {
        showProgressDialog()
        _selectedFilterAndSort.value = filterOptionWrapper
    }

    fun setSelectedFilter(selectedFilter: List<FilterOption>?) {
        selectedFilter?.let {
            _selectedFilterAndSort.value = if (_selectedFilterAndSort.value != null) {
                _selectedFilterAndSort.value?.let { filters ->
                    val list = arrayListOf<Boolean>()
                    list.addAll(filters.filterShownState)
                    list[list.size - 1] = true

                    var selectedFilterCount = countSelectedFilter(selectedFilter)
                    _selectedFilterAndSort.value?.sortOption?.let { selectedFilterCount++ }

                    filters.copy(
                        filterOptions = selectedFilter,
                        filterShownState = list,
                        selectedFilterCount = selectedFilterCount
                    )
                }
            } else {
                FilterOptionWrapper(null, selectedFilter, listOf(true, true, false, false))
            }
        }
    }

    fun resetSelectedFilter() {
        _selectedFilterAndSort.value = FilterOptionWrapper(
            filterShownState = listOf(true, true, false, false)
        )
    }

    fun getTotalProductCount(): Int {
       return (_productFiltersTab.value as? Success<GetFilterTabResult>)
           ?.data?.totalProductCount.orZero()
    }

    fun toggleMultiSelect() {
        val multiSelectEnabled = _toggleMultiSelect.value == true
        _toggleMultiSelect.value = !multiSelectEnabled
    }

    fun detachView() {
        gqlGetShopInfoUseCase.cancelJobs()
        topAdsGetShopDepositGraphQLUseCase.unsubscribe()
        popupManagerAddProductUseCase.unsubscribe()
        getProductListUseCase.cancelJobs()
        setFeaturedProductUseCase.cancelJobs()
    }

    private fun showProductList(products: List<Product>?) {
        val isMultiSelectActive = _toggleMultiSelect.value == true
        val productList = mapToViewModels(products, isMultiSelectActive)
        _productListResult.value = Success(productList)
    }

    private fun setProductListFeaturedOnly(productsSize: Int){
        _productListFeaturedOnlyResult.value = Success(productsSize)
    }

    private fun refreshList() {
        _viewState.value = RefreshList
    }

    private fun showProgressDialog() {
        _viewState.value = ShowProgressDialog
    }

    private fun hideProgressDialog() {
        _viewState.value = HideProgressDialog
    }
}