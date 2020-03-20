package com.tokopedia.product.manage.feature.list.view.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.list.view.mapper.ProductMapper.mapToViewModels
import com.tokopedia.product.manage.feature.list.view.model.GetPopUpResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.*
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.list.view.model.ShopInfoResult
import com.tokopedia.product.manage.feature.list.view.model.ViewState
import com.tokopedia.product.manage.feature.list.view.model.ViewState.HideProgressDialog
import com.tokopedia.product.manage.feature.list.view.model.ViewState.RefreshList
import com.tokopedia.product.manage.feature.list.view.model.ViewState.ShowProgressDialog
import com.tokopedia.product.manage.feature.multiedit.data.param.MenuParam
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.product.manage.feature.multiedit.data.param.ProductParam
import com.tokopedia.product.manage.feature.multiedit.data.param.ShopParam
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.oldlist.domain.EditFeaturedProductUseCase
import com.tokopedia.product.manage.oldlist.domain.PopupManagerAddProductUseCase
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    private val editFeaturedProductUseCase: EditFeaturedProductUseCase,
    private val editStockUseCase: EditStockUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val multiEditProductUseCase: MultiEditProductUseCase,
    mainDispatcher: CoroutineDispatcher
): BaseViewModel(mainDispatcher) {

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

    fun isIdlePowerMerchant(): Boolean = userSessionInterface.isPowerMerchantIdle
    fun isPowerMerchant(): Boolean = userSessionInterface.isGoldMerchant

    fun getGoldMerchantStatus() {
        launchCatchError(block = {
            val status = withContext(Dispatchers.IO) {
                val shopId: List<Int> = listOf(userSessionInterface.shopId.toInt())
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

            val response = withContext(Dispatchers.IO) {
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

            val response = withContext(Dispatchers.IO) {
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
        isRefresh: Boolean = false
    ) {
        launchCatchError(block = {
            val productList = withContext(Dispatchers.IO) {
                val requestParams = GQLGetProductListUseCase.createRequestParams(shopId, filterOptions, sortOption)
                val getProductList = getProductListUseCase.execute(requestParams)
                val productListResponse = getProductList.productList
                productListResponse?.data
            }

            if(isRefresh) refreshList()
            showProductList(productList)
        }, onError = {
            _productListResult.value = Fail(it)
        })
    }

    fun getFeaturedProductCount(shopId: String) {
        launchCatchError(block = {
            val productListFeaturedOnly = withContext(Dispatchers.IO) {
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
        editPriceUseCase.params = EditPriceUseCase.createRequestParams(userSessionInterface.shopId, productId, price.toFloatOrZero())
        launchCatchError(block = {
            val result = withContext(Dispatchers.IO) {
                editPriceUseCase.executeOnBackground()
            }
            if (result.productUpdateV3Data.isSuccess) {
                _editPriceResult.postValue(Success(EditPriceResult(productName, productId, price)))
            } else {
                _editPriceResult.postValue(Fail(EditPriceResult(productName, productId, price, NetworkErrorException())))
            }
        }) {
            _editPriceResult.postValue(Fail(EditPriceResult(productName, productId, price, NetworkErrorException())))
        }
        hideProgressDialog()
    }

    fun editStock(productId: String, stock: Int, productName: String, status: ProductStatus) {
        showProgressDialog()
        editStockUseCase.params = EditStockUseCase.createRequestParams(userSessionInterface.shopId, productId, stock, status)
        launchCatchError(block =  {
            val result = withContext(Dispatchers.IO) {
                editStockUseCase.executeOnBackground()
            }
            if (result.productUpdateV3Data.isSuccess) {
                _editStockResult.postValue(Success(EditStockResult(productName, productId, stock, status)))
            } else {
                _editStockResult.postValue(Fail(EditStockResult(productName, productId, stock, status, NetworkErrorException())))
            }
        }) {
            _editStockResult.postValue(Fail(EditStockResult(productName, productId, stock, status, NetworkErrorException())))
        }
        hideProgressDialog()
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
        deleteProductUseCase.params = DeleteProductUseCase.createParams(userSessionInterface.shopId, productId)
        launchCatchError( block = {
            val result = withContext(Dispatchers.IO) {
                deleteProductUseCase.executeOnBackground()
            }
            if(result.productUpdateV3Data.isSuccess) {
                _deleteProductResult.postValue(Success(DeleteProductResult(productName, productId)))
            } else {
                _deleteProductResult.postValue(Fail(DeleteProductResult(productName, productId, NetworkErrorException())))
            }
        }) {
            _deleteProductResult.postValue(Fail(DeleteProductResult(productName, productId, NetworkErrorException())))
        }
        hideProgressDialog()
    }

    fun setFeaturedProduct(productId: String, status: Int) {
        val requestParams = EditFeaturedProductUseCase.createRequestParams(productId.toInt(), status)

        editFeaturedProductUseCase.execute(requestParams,
            object : Subscriber<Unit>() {
                override fun onNext(unit: Unit) {
                    _setFeaturedProductResult.value = Success(SetFeaturedProductResult(productId, status))
                }

                override fun onCompleted() {
                    //No OP
                }

                override fun onError(throwable: Throwable) {
                    _setFeaturedProductResult.value = Fail(throwable)
                }
            })

    }

    fun setSelectedFilterAndSort(selectedFilter: FilterOptionWrapper) {
        _selectedFilterAndSort.value = selectedFilter
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
        editFeaturedProductUseCase.unsubscribe()
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