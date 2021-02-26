package com.tokopedia.product.manage.feature.list.view.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.common.feature.list.data.model.TopAdsInfo
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductListMetaUseCase
import com.tokopedia.product.manage.common.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.common.feature.quickedit.stock.domain.EditStatusUseCase
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapResultToUpdateParam
import com.tokopedia.product.manage.common.feature.variant.domain.EditProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.domain.GetProductVariantUseCase
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.product.manage.common.feature.variant.presentation.data.GetVariantResult
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper.Companion.countSelectedFilter
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.common.feature.list.domain.usecase.GetProductManageAccessUseCase
import com.tokopedia.product.manage.common.feature.list.view.mapper.ProductManageAccessMapper
import com.tokopedia.product.manage.feature.list.domain.PopupManagerAddProductUseCase
import com.tokopedia.product.manage.feature.list.domain.SetFeaturedProductUseCase
import com.tokopedia.product.manage.feature.list.view.mapper.ProductMapper.mapToFilterTabResult
import com.tokopedia.product.manage.feature.list.view.mapper.ProductMapper.mapToUiModels
import com.tokopedia.product.manage.feature.list.view.model.*
import com.tokopedia.product.manage.feature.list.view.model.DeleteProductDialogType.*
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.list.view.model.ViewState.*
import com.tokopedia.product.manage.feature.multiedit.data.param.MenuParam
import com.tokopedia.product.manage.feature.multiedit.data.param.ProductParam
import com.tokopedia.product.manage.feature.multiedit.data.param.ShopParam
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.shop.common.data.model.ProductStock
import com.tokopedia.shop.common.data.source.cloud.query.param.option.ExtraInfo
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption
import com.tokopedia.shop.common.data.source.cloud.query.param.option.SortOption
import com.tokopedia.shop.common.domain.interactor.GQLGetProductListUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetShopInfoTopAdsUseCase
import com.tokopedia.shop.common.domain.interactor.GetAdminInfoShopLocationUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateProductStockWarehouseUseCase
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
    private val getShopInfoTopAdsUseCase: GetShopInfoTopAdsUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val topAdsGetShopDepositGraphQLUseCase: TopAdsGetShopDepositGraphQLUseCase,
    private val popupManagerAddProductUseCase: PopupManagerAddProductUseCase,
    private val getProductListUseCase: GQLGetProductListUseCase,
    private val setFeaturedProductUseCase: SetFeaturedProductUseCase,
    private val editStatusUseCase: EditStatusUseCase,
    private val editStockUseCase: UpdateProductStockWarehouseUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val multiEditProductUseCase: MultiEditProductUseCase,
    private val getProductListMetaUseCase: GetProductListMetaUseCase,
    private val getProductManageAccessUseCase: GetProductManageAccessUseCase,
    private val editProductVariantUseCase: EditProductVariantUseCase,
    private val getProductVariantUseCase: GetProductVariantUseCase,
    private val getAdminInfoShopLocationUseCase: GetAdminInfoShopLocationUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    companion object {
        // Currently update data on server is not realtime.
        // Client need to add request delay in order to receive updated data.
        const val REQUEST_DELAY = 1000L
    }

    val viewState: LiveData<ViewState>
        get() = _viewState
    val showStockTicker: LiveData<Boolean>
        get() = _showStockTicker
    val showAddProductOptionsMenu: LiveData<Boolean>
        get() = _showAddProductOptionsMenu
    val showEtalaseOptionsMenu: LiveData<Boolean>
        get() = _showEtalaseOptionsMenu
    val refreshList: LiveData<Boolean>
        get() = _refreshList
    val getProductVariantsResult: LiveData<Result<GetVariantResult>>
        get() = _getProductVariantsResult
    val productListResult: LiveData<Result<List<ProductUiModel>>>
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
    val onClickPromoTopAds: LiveData<TopAdsPage>
        get() = _onClickPromoTopAds
    val productManageAccess: LiveData<Result<ProductManageAccess>>
        get() = _productManageAccess
    val deleteProductDialog: LiveData<DeleteProductDialogType>
        get() = _deleteProductDialog
    val topAdsInfo: LiveData<TopAdsInfo>
        get() = _topAdsInfo

    private val _viewState = MutableLiveData<ViewState>()
    private val _showStockTicker = MutableLiveData<Boolean>()
    private val _refreshList = MutableLiveData<Boolean>()
    private val _showAddProductOptionsMenu = MutableLiveData<Boolean>()
    private val _showEtalaseOptionsMenu = MutableLiveData<Boolean>()
    private val _getProductVariantsResult = MutableLiveData<Result<GetVariantResult>>()
    private val _productListResult = MutableLiveData<Result<List<ProductUiModel>>>()
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
    private val _topAdsInfo = MutableLiveData<TopAdsInfo>()
    private val _onClickPromoTopAds = MutableLiveData<TopAdsPage>()
    private val _productManageAccess = MutableLiveData<Result<ProductManageAccess>>()
    private val _deleteProductDialog = MutableLiveData<DeleteProductDialogType>()

    private var access: ProductManageAccess? = null
    private var getProductListJob: Job? = null
    private var getFilterTabJob: Job? = null
    private var warehouseId: String = ""
    private var totalProductCount = 0

    fun isPowerMerchant(): Boolean = userSessionInterface.isGoldMerchant

    fun getGoldMerchantStatus() {
        launchCatchError(block = {
            val status = withContext(dispatchers.io) {
                val shopId: List<Int> = listOf(userSessionInterface.shopId.toIntOrZero())
                gqlGetShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(shopId)

                val shopInfo = gqlGetShopInfoUseCase.executeOnBackground()
                val shopDomain = shopInfo.shopCore.domain
                val isGoldMerchant = shopInfo.goldOS.isGold == 1
                val isOfficialStore = shopInfo.goldOS.isOfficial == 1

                ShopInfoResult(shopDomain, isGoldMerchant, isOfficialStore)
            }
            _shopInfoResult.value = Success(status)
        }) {
            _shopInfoResult.value = Fail(it)
        }
    }

    fun getTopAdsInfo() {
        launchCatchError(block = {
            _topAdsInfo.value = withContext(dispatchers.io) {
                val shopId = userSessionInterface.shopId.toIntOrZero()
                val requestParams = GetShopInfoTopAdsUseCase.createRequestParams(shopId)
                val topAdsInfo = getShopInfoTopAdsUseCase.execute(requestParams)

                TopAdsInfo(topAdsInfo.isTopAds(), topAdsInfo.isAutoAds())
            }
        }) {
            _topAdsInfo.value = TopAdsInfo(isTopAds = false, isAutoAds = false)
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
                val warehouseId = getWarehouseId(shopId)
                val extraInfo = listOf(ExtraInfo.TOPADS, ExtraInfo.RBAC)
                val requestParams = GQLGetProductListUseCase.createRequestParams(shopId, warehouseId, filterOptions, sortOption, extraInfo)
                val getProductList = getProductListUseCase.execute(requestParams)
                val productListResponse = getProductList.productList
                productListResponse?.data
            }
            _refreshList.value = isRefresh

            showStockTicker()
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

    fun getProductVariants(productId: String) {
        showLoadingDialog()
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val warehouseId = getWarehouseId(userSessionInterface.shopId)
                val requestParams = GetProductVariantUseCase.createRequestParams(productId, false, warehouseId)
                val response = getProductVariantUseCase.execute(requestParams)

                val variant = response.getProductV3
                ProductManageVariantMapper.mapToVariantsResult(variant, getAccess())
            }

            if (result.variants.isNotEmpty()) {
                _getProductVariantsResult.value = Success(result)
            }

            hideLoadingDialog()
        }, onError = {
            _getProductVariantsResult.value = Fail(it)
            hideLoadingDialog()
        })
    }

    fun getFiltersTab(withDelay: Boolean = false) {
        getFilterTabJob?.cancel()

        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                if (withDelay) {
                    delay(REQUEST_DELAY)
                }
                getProductListMetaUseCase.setParams(userSessionInterface.shopId)
                getProductListMetaUseCase.executeOnBackground()
                    .productListMetaWrapper
                    .productListMetaData
                    .tabs
            }

            _productFiltersTab.apply {
                val data = mapToFilterTabResult(response)
                totalProductCount = data.totalProductCount
                value = Success(data)
            }
        }, onError = {
            if(it is CancellationException) {
                return@launchCatchError
            }
            _productFiltersTab.value = Fail(it)
        }).let { getFilterTabJob = it }
    }

    fun getProductManageAccess() {
        launchCatchError(block = {
            access = withContext(dispatchers.io) {
                if(userSessionInterface.isShopOwner) {
                    ProductManageAccessMapper.mapProductManageOwnerAccess()
                } else {
                    val shopId = userSessionInterface.shopId
                    val response = getProductManageAccessUseCase.execute(shopId)
                    ProductManageAccessMapper.mapToProductManageAccess(response)
                }
            }
            access?.let { _productManageAccess.value = Success(it) }
        }) {
            _productManageAccess.value = Fail(it)
        }
    }

    fun getFeaturedProductCount(shopId: String) {
        launchCatchError(block = {
            val productListFeaturedOnly = withContext(dispatchers.io) {
                val warehouseId = getWarehouseId(shopId)
                val requestParams = GQLGetProductListUseCase.createRequestParams(
                    shopId,
                    warehouseId,
                    listOf(FilterOption.FilterByCondition.FeaturedOnly),
                    null
                )
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
                    _editPriceResult.postValue(Fail(EditPriceResult(productName, productId, price, NetworkErrorException(com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc.toString()))))
                }
            }
        }) {
            _editPriceResult.postValue(Fail(EditPriceResult(productName, productId, price, NetworkErrorException(com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc.toString()))))
        }
        hideProgressDialog()
    }

    fun editStock(productId: String, productName: String, stock: Int? = null, status: ProductStatus? = null) {
        showProgressDialog()
        launchCatchError(block = {
            var result: Result<EditStockResult>? = null

            status?.let {
                result = editProductStatus(productId, productName, stock, it)
            }

            stock?.let {
                result = editProductStock(productId, productName, it, status)
            }

            _editStockResult.postValue(result)
        }) {
            val message = com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc.toString()
            val result = EditStockResult(productName, productId, stock, status, NetworkErrorException(message))
            _editStockResult.postValue(Fail(result))
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
            var data: Result<EditVariantResult>? = null

            if(result.editStatus) {
                data = editVariantStatus(result)
            }

            if(result.editStock) {
                data = editVariantStock(result)
            }

            _editVariantStockResult.value = data
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
                    _deleteProductResult.postValue(Fail(DeleteProductResult(productName, productId, NetworkErrorException(com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc.toString()))))
                }
            }
        }) {
            _deleteProductResult.postValue(Fail(DeleteProductResult(productName, productId, NetworkErrorException(com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc.toString()))))
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

    fun showHideOptionsMenu() {
        val showAddMenu = getAccess().addProduct
        val showEtalaseMenu = getAccess().etalaseList
        _showAddProductOptionsMenu.value = showAddMenu
        _showEtalaseOptionsMenu.value = showEtalaseMenu
    }

    fun setFilterOptionWrapper(filterOptionWrapper: FilterOptionWrapper) {
        showProgressDialog()
        _selectedFilterAndSort.value = filterOptionWrapper
    }

    fun setSelectedFilter(selectedFilter: List<FilterOption>) {
        val selectedFilterAndSort = _selectedFilterAndSort.value

        _selectedFilterAndSort.value = if (selectedFilterAndSort != null) {
            val list = arrayListOf<Boolean>()
            list.addAll(selectedFilterAndSort.filterShownState)
            list[list.size - 1] = true

            var selectedFilterCount = countSelectedFilter(selectedFilter)
            selectedFilterAndSort.sortOption?.let { selectedFilterCount++ }

            selectedFilterAndSort.copy(
                filterOptions = selectedFilter,
                filterShownState = list,
                selectedFilterCount = selectedFilterCount
            )
        } else {
            FilterOptionWrapper(null, selectedFilter, listOf(true, true, false, false))
        }
    }

    fun resetSelectedFilter() {
        _selectedFilterAndSort.value = FilterOptionWrapper(
            filterShownState = listOf(true, true, false, false)
        )
    }

    fun getTotalProductCount(): Int = totalProductCount

    fun toggleMultiSelect() {
        val multiSelectEnabled = _toggleMultiSelect.value == true
        _toggleMultiSelect.value = !multiSelectEnabled
    }

    fun onPromoTopAdsClicked(productId: String) {
        val topAdsInfo = _topAdsInfo.value

        if(topAdsInfo != null) {
            val shopHasTopAds = topAdsInfo.isTopAds
            val shopHasAutoAds = topAdsInfo.isAutoAds

            _onClickPromoTopAds.value = when {
                shopHasAutoAds -> TopAdsPage.AutoAds(productId)
                shopHasTopAds -> TopAdsPage.ManualAds(productId)
                else -> TopAdsPage.OnBoarding(productId)
            }
        } else {
            _onClickPromoTopAds.value = TopAdsPage.OnBoarding(productId)
        }
    }

    fun onDeleteSingleProduct(productName: String, productId: String) {
        val isMultiLocationShop = userSessionInterface.isMultiLocationShop
        _deleteProductDialog.value = SingleProduct(productId, productName, isMultiLocationShop)
    }

    fun onDeleteMultipleProducts() {
        val isMultiLocationShop = userSessionInterface.isMultiLocationShop
        _deleteProductDialog.value = MultipleProduct(isMultiLocationShop)
    }

    fun detachView() {
        gqlGetShopInfoUseCase.cancelJobs()
        topAdsGetShopDepositGraphQLUseCase.unsubscribe()
        popupManagerAddProductUseCase.unsubscribe()
        getProductListUseCase.cancelJobs()
        setFeaturedProductUseCase.cancelJobs()
    }

    fun hideStockTicker() {
        _showStockTicker.value = false
    }

    private suspend fun editProductStatus(
        productId: String,
        productName: String,
        stock: Int?,
        status: ProductStatus
    ): Result<EditStockResult> {
        return withContext(dispatchers.io) {
            editStatusUseCase.setParams(userSessionInterface.shopId, productId, status)
            val response = editStatusUseCase.executeOnBackground()

            val productUpdateV3Data = response.productUpdateV3Data
            val errorMessage = productUpdateV3Data.header.errorMessage

            when {
                productUpdateV3Data.isSuccess -> {
                    Success(EditStockResult(productName, productId, stock, status))
                }
                errorMessage.isNotEmpty() -> {
                    val error = Throwable(message = errorMessage.last())
                    Fail(EditStockResult(productName, productId, stock, status, error))
                }
                else -> {
                    val message = com.tokopedia.product.manage.common.R.string.product_stock_reminder_toaster_failed_desc.toString()
                    Fail(EditStockResult(productName, productId, stock, status, NetworkErrorException(message)))
                }
            }
        }
    }

    private suspend fun editProductStock(
        productId: String,
        productName: String,
        stock: Int,
        status: ProductStatus?
    ): Result<EditStockResult> {
        return withContext(dispatchers.io) {
            val warehouseId = getWarehouseId(userSessionInterface.shopId)
            val requestParams = UpdateProductStockWarehouseUseCase.createRequestParams(
                userSessionInterface.shopId, productId, warehouseId, stock.toString()
            )
            val response = editStockUseCase.execute(requestParams)
            val productStatus = response.getProductStatus() ?: status
            Success(EditStockResult(productName, productId, stock, productStatus))
        }
    }

    private suspend fun editVariantStatus(result: EditVariantResult): Result<EditVariantResult> {
        return withContext(dispatchers.io) {
            val shopId = userSessionInterface.shopId
            val variantInputParam = mapResultToUpdateParam(shopId, result)
            val requestParams = EditProductVariantUseCase.createRequestParams(variantInputParam)
            val response = editProductVariantUseCase.execute(requestParams).productUpdateV3Data

            when {
                response.isSuccess -> Success(result)
                response.header.errorMessage.isNotEmpty() -> {
                    val message = response.header.errorMessage.lastOrNull().orEmpty()
                    Fail(MessageErrorException(message))
                }
                else -> {
                    val message = com.tokopedia.product.manage.common.R.string
                        .product_stock_reminder_toaster_failed_desc.toString()
                    Fail(MessageErrorException(message))
                }
            }
        }
    }

    private suspend fun editVariantStock(result: EditVariantResult): Result<EditVariantResult> {
        return withContext(dispatchers.io) {
            val warehouseId = getWarehouseId(userSessionInterface.shopId)
            val productList = result.variants.map { ProductStock(it.id, it.stock.toString()) }
            val requestParams = UpdateProductStockWarehouseUseCase.createRequestParams(
                userSessionInterface.shopId, warehouseId, productList
            )
            editStockUseCase.execute(requestParams)
            Success(result)
        }
    }

    private fun showProductList(products: List<Product>?) {
        val isMultiSelectActive = _toggleMultiSelect.value == true
        val productList = mapToUiModels(products, getAccess(), isMultiSelectActive)
        _productListResult.value = Success(productList)
    }

    private fun showStockTicker() {
        val isTickerVisible = _showStockTicker.value == true
        val isInitialLoad = _productListResult.value == null
        val isMultiLocationShop = userSessionInterface.isMultiLocationShop
        val shouldShow = isInitialLoad && isMultiLocationShop || isTickerVisible
        _showStockTicker.value = shouldShow
    }

    private fun getAccess(): ProductManageAccess {
        return access ?: ProductManageAccessMapper.mapDefaultProductManageAccess()
    }

    private suspend fun getWarehouseId(shopId: String): String {
        return if(warehouseId.isEmpty()) {
            val shopLocation = getAdminInfoShopLocationUseCase.execute(shopId.toIntOrZero())
            warehouseId = shopLocation.firstOrNull { it.isMainLocation() }?.locationId.toString()
            warehouseId
        } else {
            warehouseId
        }
    }

    private fun setProductListFeaturedOnly(productsSize: Int) {
        _productListFeaturedOnlyResult.value = Success(productsSize)
    }

    private fun showProgressDialog() {
        _viewState.value = ShowProgressDialog
    }

    private fun hideProgressDialog() {
        _viewState.value = HideProgressDialog
    }

    private fun showLoadingDialog() {
        _viewState.value = ShowLoadingDialog
    }

    private fun hideLoadingDialog() {
        _viewState.value = HideLoadingDialog
    }
}