package com.tokopedia.product.manage.feature.list.view.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.cashback.data.SetCashbackResult
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase.Companion.CASHBACK_NUMBER_OF_PRODUCT_EXCEED_LIMIT_ERROR_CODE
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase.Companion.CASHBACK_SUCCESS_ERROR_CODE
import com.tokopedia.product.manage.feature.list.view.mapper.ProductMapper.mapToViewModels
import com.tokopedia.product.manage.feature.list.view.model.GetPopUpResult
import com.tokopedia.product.manage.feature.list.view.model.ShopInfoResult
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.list.view.model.ViewState
import com.tokopedia.product.manage.feature.list.view.model.ViewState.*
import com.tokopedia.product.manage.feature.quickedit.delete.data.model.DeleteProductResult
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.data.model.EditPriceResult
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.product.manage.feature.quickedit.stock.data.model.EditStockResult
import com.tokopedia.product.manage.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.oldlist.data.ConfirmationProductData
import com.tokopedia.product.manage.oldlist.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductUpdateV3Param
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductUpdateV3Response
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductUpdateV3SuccessFailedResponse
import com.tokopedia.product.manage.oldlist.domain.BulkUpdateProductUseCase
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
    private val setCashbackUseCase: SetCashbackUseCase,
    private val popupManagerAddProductUseCase: PopupManagerAddProductUseCase,
    private val getProductListUseCase: GQLGetProductListUseCase,
    private val bulkUpdateProductUseCase: BulkUpdateProductUseCase,
    private val editFeaturedProductUseCase: EditFeaturedProductUseCase,
    private val editStockUseCase: EditStockUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    mainDispatcher: CoroutineDispatcher
): BaseViewModel(mainDispatcher) {

    val viewState : LiveData<ViewState>
        get() = _viewState
    val productListResult : LiveData<Result<List<ProductViewModel>>>
        get() = _productListResult
    val productListFeaturedOnlyResult : LiveData<Result<Int>>
        get() = _productListFeaturedOnlyResult
    val shopInfoResult : LiveData<Result<ShopInfoResult>>
        get() = _shopInfoResult
    val updateProductResult : LiveData<Result<ProductUpdateV3SuccessFailedResponse>>
        get() = _updateProductResult
    val deleteProductResult : LiveData<Result<DeleteProductResult>>
        get() = _deleteProductResult
    val editPriceResult : LiveData<Result<EditPriceResult>>
        get() = _editPriceResult
    val editStockResult : LiveData<Result<EditStockResult>>
        get() = _editStockResult
    val setCashbackResult: LiveData<Result<SetCashbackResult>>
        get() = _setCashbackResult
    val getFreeClaimResult : LiveData<Result<DataDeposit>>
        get() = _getFreeClaimResult
    val getPopUpResult : LiveData<Result<GetPopUpResult>>
        get() = _getPopUpResult
    val setFeaturedProductResult : LiveData<Result<SetFeaturedProductResult>>
        get() = _setFeaturedProductResult

    private val _viewState = MutableLiveData<ViewState>()
    private val _productListResult = MutableLiveData<Result<List<ProductViewModel>>>()
    private val _productListFeaturedOnlyResult = MutableLiveData<Result<Int>>()
    private val _shopInfoResult = MutableLiveData<Result<ShopInfoResult>>()
    private val _updateProductResult = MutableLiveData<Result<ProductUpdateV3SuccessFailedResponse>>()
    private val _deleteProductResult = MutableLiveData<Result<DeleteProductResult>>()
    private val _editPriceResult = MutableLiveData<Result<EditPriceResult>>()
    private val _editStockResult = MutableLiveData<Result<EditStockResult>>()
    private val _setCashbackResult = MutableLiveData<Result<SetCashbackResult>>()
    private val _getFreeClaimResult = MutableLiveData<Result<DataDeposit>>()
    private val _getPopUpResult = MutableLiveData<Result<GetPopUpResult>>()
    private val _setFeaturedProductResult = MutableLiveData<Result<SetFeaturedProductResult>>()

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

    fun updateMultipleProducts(listUpdateResponse: MutableList<ConfirmationProductData>) {
        showProgressDialog()

        val updateParam = mapToBulkUpdateParam(listUpdateResponse)
        val requestParams = BulkUpdateProductUseCase.createRequestParams(updateParam)

        bulkUpdateProductUseCase.execute(requestParams, object : Subscriber<ProductUpdateV3SuccessFailedResponse>() {
            override fun onNext(listOfUpdateResponse: ProductUpdateV3SuccessFailedResponse) {
                _updateProductResult.value = Success(listOfUpdateResponse)
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                _updateProductResult.value = Fail(e)
            }
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

            refreshList(isRefresh)
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

    fun setCashback(productId: String, productName: String, cashback: Int) {
        setCashbackUseCase.params = SetCashbackUseCase.createRequestParams(productId.toIntOrZero(), cashback, false)
        launchCatchError(block = {
            val result = setCashbackUseCase.executeOnBackground()
            when(result.goldSetProductCashback.header.errorCode) {
                CASHBACK_SUCCESS_ERROR_CODE -> _setCashbackResult.postValue(Success(SetCashbackResult(productId = productId, cashback = cashback, productName = productName)))
                CASHBACK_NUMBER_OF_PRODUCT_EXCEED_LIMIT_ERROR_CODE -> _setCashbackResult.postValue(Fail(SetCashbackResult(limitExceeded = true)))
                else -> _setCashbackResult.postValue(Fail(SetCashbackResult(productId = productId, productName = productName, cashback = cashback)))
            }
        }) {
            _setCashbackResult.postValue(Fail(SetCashbackResult()))
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

    fun mapToProductConfirmationData(isActionDelete: Boolean, stockType: BulkBottomSheetType.StockType, etalaseType: BulkBottomSheetType.EtalaseType,
                                              productManageViewModels: List<ProductViewModel>): ArrayList<ConfirmationProductData> {

        val confirmationProductDataList: ArrayList<ConfirmationProductData> = arrayListOf()
        productManageViewModels.forEach {
            val confirmationProductData = ConfirmationProductData()
            confirmationProductData.productId = it.id
            confirmationProductData.productName = it.title.orEmpty()
            confirmationProductData.productImgUrl = it.imageUrl.orEmpty()
            confirmationProductData.productEtalaseName = etalaseType.etalaseValue
            confirmationProductData.isVariant = it.isVariant()
            confirmationProductDataList.add(confirmationProductData)

            if (etalaseType.etalaseId == BulkBottomSheetType.ETALASE_DEFAULT) {
                confirmationProductData.productEtalaseId = 0
            } else {
                confirmationProductData.productEtalaseId = etalaseType.etalaseId
            }

            if (isActionDelete) {
                confirmationProductData.statusStock = BulkBottomSheetType.STOCK_DELETED
            } else {
                confirmationProductData.statusStock = stockType.stockStatus
            }

        }
        return confirmationProductDataList

    }

    /**
     * Filter the data based on failed data
     * This use for retry update only the failed data
     */
    fun failedBulkDataMapper(failData: List<ProductUpdateV3Response>, confirmationProductDataList: List<ConfirmationProductData>)
        : MutableList<ConfirmationProductData> {
        return confirmationProductDataList.filter {
            failData.map { response ->
                response.productUpdateV3Data.productId
            }.contains(it.productId)
        }.toMutableList()
    }

    fun detachView() {
        gqlGetShopInfoUseCase.cancelJobs()
        topAdsGetShopDepositGraphQLUseCase.unsubscribe()
        popupManagerAddProductUseCase.unsubscribe()
        getProductListUseCase.cancelJobs()
        bulkUpdateProductUseCase.unsubscribe()
        editFeaturedProductUseCase.unsubscribe()
    }

    private fun mapToBulkUpdateParam(confirmationData: List<ConfirmationProductData>): MutableList<ProductUpdateV3Param> {
        val listParam: MutableList<ProductUpdateV3Param> = arrayListOf()

        listParam.addAll(confirmationData.map {
            val response = ProductUpdateV3Param()
            response.productEtalase.etalaseId = it.productEtalaseId.toString()
            response.productEtalase.etalaseName = it.productEtalaseName
            response.productId = it.productId
            response.productStatus = it.getStatusProductParam()
            response.shop.shopId = userSessionInterface.shopId
            response
        })
        return listParam
    }

    private fun showProductList(products: List<Product>?) {
        val productList = mapToViewModels(products)
        _productListResult.value = Success(productList)
    }

    private fun setProductListFeaturedOnly(productsSize: Int){
        _productListFeaturedOnlyResult.value = Success(productsSize)
    }

    private fun refreshList(isRefresh: Boolean) {
        if (isRefresh) _viewState.value = RefreshList
    }

    private fun showProgressDialog() {
        _viewState.value = ShowProgressDialog
    }

    private fun hideProgressDialog() {
        _viewState.value = HideProgressDialog
    }
}