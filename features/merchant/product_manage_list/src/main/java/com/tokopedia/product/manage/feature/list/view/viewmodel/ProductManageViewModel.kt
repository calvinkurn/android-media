package com.tokopedia.product.manage.feature.list.view.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.domain.interactor.SetCashbackUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.list.view.mapper.ProductMapper.mapToViewModels
import com.tokopedia.product.manage.feature.list.view.model.EditPriceResult
import com.tokopedia.product.manage.feature.list.view.model.GetPopUpResult
import com.tokopedia.product.manage.feature.list.view.model.ShopInfoResult
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import com.tokopedia.product.manage.feature.list.view.model.SetCashBackResult
import com.tokopedia.product.manage.feature.list.view.model.SetFeaturedProductResult
import com.tokopedia.product.manage.feature.list.view.model.ViewState
import com.tokopedia.product.manage.feature.list.view.model.ViewState.*
import com.tokopedia.product.manage.oldlist.data.ConfirmationProductData
import com.tokopedia.product.manage.oldlist.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductEditPriceParam
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductUpdateV3Param
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductUpdateV3Response
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ProductUpdateV3SuccessFailedResponse
import com.tokopedia.product.manage.oldlist.domain.BulkUpdateProductUseCase
import com.tokopedia.product.manage.oldlist.domain.EditFeaturedProductUseCase
import com.tokopedia.product.manage.oldlist.domain.EditPriceUseCase
import com.tokopedia.product.manage.oldlist.domain.PopupManagerAddProductUseCase
import com.tokopedia.shop.common.data.source.cloud.model.productlist.Product
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
import kotlinx.coroutines.withContext
import rx.Subscriber

class ProductManageViewModel(
    private val editPriceProductUseCase: EditPriceUseCase,
    private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase,
    private val userSessionInterface: UserSessionInterface,
    private val topAdsGetShopDepositGraphQLUseCase: TopAdsGetShopDepositGraphQLUseCase,
    private val setCashbackUseCase: SetCashbackUseCase,
    private val popupManagerAddProductUseCase: PopupManagerAddProductUseCase,
    private val getProductListUseCase: GQLGetProductListUseCase,
    private val bulkUpdateProductUseCase: BulkUpdateProductUseCase,
    private val editFeaturedProductUseCase: EditFeaturedProductUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher
): BaseViewModel(mainDispatcher) {

    val viewState : LiveData<ViewState>
        get() = _viewState
    val productListResult : LiveData<Result<List<ProductViewModel>>>
        get() = _productListResult
    val shopInfoResult : LiveData<Result<ShopInfoResult>>
        get() = _shopInfoResult
    val updateProductResult : LiveData<Result<ProductUpdateV3SuccessFailedResponse>>
        get() = _updateProductResult
    val deleteProductResult : LiveData<Result<ProductUpdateV3SuccessFailedResponse>>
        get() = _deleteProductResult
    val editPriceResult : LiveData<Result<EditPriceResult>>
        get() = _editPriceResult
    val setCashBackResult : LiveData<Result<SetCashBackResult>>
        get() = _setCashBackResult
    val getFreeClaimResult : LiveData<Result<DataDeposit>>
        get() = _getFreeClaimResult
    val getPopUpResult : LiveData<Result<GetPopUpResult>>
        get() = _getPopUpResult
    val setFeaturedProductResult : LiveData<Result<SetFeaturedProductResult>>
        get() = _setFeaturedProductResult

    private val _viewState = MutableLiveData<ViewState>()
    private val _productListResult = MutableLiveData<Result<List<ProductViewModel>>>()
    private val _shopInfoResult = MutableLiveData<Result<ShopInfoResult>>()
    private val _updateProductResult = MutableLiveData<Result<ProductUpdateV3SuccessFailedResponse>>()
    private val _deleteProductResult = MutableLiveData<Result<ProductUpdateV3SuccessFailedResponse>>()
    private val _editPriceResult = MutableLiveData<Result<EditPriceResult>>()
    private val _setCashBackResult = MutableLiveData<Result<SetCashBackResult>>()
    private val _getFreeClaimResult = MutableLiveData<Result<DataDeposit>>()
    private val _getPopUpResult = MutableLiveData<Result<GetPopUpResult>>()
    private val _setFeaturedProductResult = MutableLiveData<Result<SetFeaturedProductResult>>()

    fun isIdlePowerMerchant(): Boolean = userSessionInterface.isPowerMerchantIdle
    fun isPowerMerchant(): Boolean = userSessionInterface.isGoldMerchant

    fun getGoldMerchantStatus() {
        launchCatchError(block = {
            val status = withContext(ioDispatcher) {
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
            val productList = withContext(ioDispatcher) {
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

    fun editPrice(productId: String, price: String) {
        showProgressDialog()

        val param = ProductEditPriceParam()
        param.price = price.toFloatOrNull() ?: 0F
        param.productId = productId
        param.shop.shopId = userSessionInterface.shopId

        editPriceProductUseCase.execute(EditPriceUseCase.createRequestParams(param),
            object : Subscriber<ProductUpdateV3Response>() {
                override fun onNext(data: ProductUpdateV3Response) {
                    hideProgressDialog()
                    if (data.productUpdateV3Data.isSuccess) {
                        _editPriceResult.value = Success(EditPriceResult(productId, price))
                    } else {
                        _editPriceResult.value = Fail(EditPriceResult(productId, price, NetworkErrorException()))
                    }
                }

                override fun onCompleted() {

                }

                override fun onError(e: Throwable) {
                    _editPriceResult.value = Fail(EditPriceResult(productId, price, NetworkErrorException()))
                }
            })
    }

    fun setCashback(productId: String, cashback: Int) {
        showProgressDialog()
        val requestParams = SetCashbackUseCase.createRequestParams(productId, cashback)
        setCashbackUseCase.execute(requestParams, object : Subscriber<Boolean>() {
            override fun onNext(isSuccess: Boolean) {
                hideProgressDialog()
                if (isSuccess) {
                    _setCashBackResult.value = Success(SetCashBackResult(productId, cashback))
                } else {
                    _setCashBackResult.value = Fail(SetCashBackResult(productId, cashback, NetworkErrorException()))
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                hideProgressDialog()
                _setCashBackResult.value = Fail(SetCashBackResult(productId, cashback, NetworkErrorException()))
            }

        })
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

    fun deleteSingleProduct(productIds: String) {
        showProgressDialog()

        val singleDeleteParam = singleDeleteProductMapper(productIds)
        val requestParams = BulkUpdateProductUseCase.createRequestParams(singleDeleteParam)

        bulkUpdateProductUseCase.execute(requestParams,
            object : Subscriber<ProductUpdateV3SuccessFailedResponse>() {
                override fun onNext(listResponse: ProductUpdateV3SuccessFailedResponse) {
                    hideProgressDialog()
                    _deleteProductResult.value = Success(listResponse)
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable) {
                    _deleteProductResult.value = Fail(e)
                }

            })
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
     * Map for delete single product, from option menu product
     */
    private fun singleDeleteProductMapper(productId: String): List<ProductUpdateV3Param> {
        val param = ProductUpdateV3Param()
        param.productId = productId
        param.shop.shopId = userSessionInterface.shopId
        param.productStatus = "DELETED"
        return arrayListOf(param)
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
        editPriceProductUseCase.unsubscribe()
        gqlGetShopInfoUseCase.cancelJobs()
        topAdsGetShopDepositGraphQLUseCase.unsubscribe()
        setCashbackUseCase.unsubscribe()
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