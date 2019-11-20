package com.tokopedia.product.manage.list.view.presenter

import android.accounts.NetworkErrorException
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.core.base.domain.RequestParams
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel
import com.tokopedia.gm.common.domain.interactor.SetCashbackUseCase
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase
import com.tokopedia.product.manage.list.data.ConfirmationProductData
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType.Companion.ETALASE_DEFAULT
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType.Companion.STOCK_DELETED
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3Param
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3Response
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3SuccessFailedResponse
import com.tokopedia.product.manage.list.domain.BulkUpdateProductUseCase
import com.tokopedia.product.manage.list.domain.EditPriceProductUseCase
import com.tokopedia.product.manage.list.domain.PopupManagerAddProductUseCase
import com.tokopedia.product.manage.list.view.listener.ProductManageView
import com.tokopedia.product.manage.list.view.mapper.ProductListMapperView
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel
import com.tokopedia.seller.product.manage.constant.*
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductListResponse
import com.tokopedia.shop.common.domain.interactor.GetProductListUseCase
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

class ProductManagePresenterImpl @Inject constructor(
        private val getShopInfoUseCase: GetShopInfoUseCase,
        private val editPriceProductUseCase: EditPriceProductUseCase,
        private val userSessionInterface: UserSessionInterface,
        private val topAdsGetShopDepositGraphQLUseCase: TopAdsGetShopDepositGraphQLUseCase,
        private val setCashbackUseCase: SetCashbackUseCase,
        private val popupManagerAddProductUseCase: PopupManagerAddProductUseCase,
        private val getProductListUseCase: GetProductListUseCase,
        val productListMapperView: ProductListMapperView,
        private val bulkUpdateProductUseCase: BulkUpdateProductUseCase
) : BaseDaggerPresenter<ProductManageView>(), ProductManagePresenter {

    override fun isIdlePowerMerchant(): Boolean =  userSessionInterface.isPowerMerchantIdle
    override fun isPowerMerchant(): Boolean = userSessionInterface.isGoldMerchant

    override fun getGoldMerchantStatus() {
        getShopInfoUseCase.execute(RequestParams.EMPTY, object : Subscriber<ShopModel>() {
            override fun onNext(shopModel: ShopModel) {
                if (isViewAttached) {
                    view.onSuccessGetShopInfo(shopModel.info.isGoldMerchant, shopModel.info.isOfficialStore, shopModel.getInfo().shopDomain)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

        })
    }

    override fun bulkUpdateProduct(listUpdateResponse: MutableList<ConfirmationProductData>) {
        view.showLoadingProgress()
        bulkUpdateProductUseCase.execute(BulkUpdateProductUseCase.createRequestParams(mapToBulkUpdateParam(listUpdateResponse)), object : Subscriber<ProductUpdateV3SuccessFailedResponse>() {
            override fun onNext(listOfUpdateResponse: ProductUpdateV3SuccessFailedResponse) {
                view.onSuccessBulkUpdateProduct(listOfUpdateResponse)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                view.onErrorBulkUpdateProduct(e)
            }

        })
    }

    override fun getProductList(page: Int, keywordFilter: String, @CatalogProductOption catalogOption: String,
                                @ConditionProductOption conditionOption: String,
                                etalaseId: Int, @PictureStatusProductOption pictureOption: String,
                                @SortProductOption sortOption: String, categoryId: String) {
        val catalogOptionInt = Integer.parseInt(catalogOption)
        val conditionOptionInt = Integer.parseInt(conditionOption)
        val sortOptionInt = Integer.parseInt(sortOption)
        val pictureOptionInt = Integer.parseInt(pictureOption)
        val etalaseIdString = generateEtalaseIdFilter(etalaseId)
        val categoryIdInt = Integer.parseInt(categoryId)

        getProductListUseCase.execute(GetProductListUseCase.createRequestParams(userSessionInterface.shopId, page,
                keywordFilter, catalogOptionInt, conditionOptionInt, etalaseIdString,
                pictureOptionInt, sortOptionInt, categoryIdInt), object : Subscriber<ProductListResponse>() {

            override fun onNext(productListResponse: ProductListResponse) {
                if (productListResponse.getProductList.data.isEmpty()) {
                    view.onLoadListEmpty()
                    return
                }

                val productListManageModelView = productListMapperView.mapIntoViewModel(productListResponse)
                view.onSuccessGetProductList(productListManageModelView.productManageViewModels,
                        productListManageModelView.productManageViewModels.size,
                        productListManageModelView.isHasNextPage)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (isViewAttached) {
                    view.onLoadListEmpty()
                }
            }

        })
    }

    override fun editPrice(productId: String, price: String, currencyId: String, currencyText: String) {
        view.showLoadingProgress()
        editPriceProductUseCase.execute(EditPriceProductUseCase.createRequestParams(price, currencyId, productId),
                object : Subscriber<Boolean>() {
                    override fun onNext(isSuccessEditPrice: Boolean) {
                        view.hideLoadingProgress()
                        if (isSuccessEditPrice) {
                            view.onSuccessEditPrice(productId, price, currencyId, currencyText)
                        } else {
                            view.onErrorEditPrice(NetworkErrorException(), productId, price, currencyId, currencyText)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {
                            view.hideLoadingProgress()
                            view.onErrorEditPrice(e, productId, price, currencyId, currencyText)
                        }
                    }

                })
    }

    override fun setCashback(productId: String, cashback: Int) {
        view.showLoadingProgress()
        setCashbackUseCase.execute(SetCashbackUseCase.createRequestParams(productId, cashback), object : Subscriber<Boolean>() {
            override fun onNext(isSuccess: Boolean) {
                view.hideLoadingProgress()
                if (isSuccess) {
                    view.onSuccessSetCashback(productId, cashback)
                } else {
                    view.onErrorSetCashback(NetworkErrorException(), productId, cashback)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (isViewAttached) {
                    view.hideLoadingProgress()
                    view.onErrorSetCashback(e, productId, cashback)
                }
            }

        })
    }

    override fun getFreeClaim(graphqlQuery: String, shopId: String) {
        topAdsGetShopDepositGraphQLUseCase.execute(TopAdsGetShopDepositGraphQLUseCase.createRequestParams(graphqlQuery, shopId),
                object : Subscriber<DataDeposit>() {
                    override fun onNext(dataDeposit: DataDeposit) {
                        if (isViewAttached) {
                            view.onSuccessGetFreeClaim(dataDeposit)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {
                            view.onErrorGetFreeClaim(e)
                        }
                    }

                })

    }

    override fun getPopupsInfo(productId: String) {
        val shopId = getShopIdInteger()
        popupManagerAddProductUseCase.execute(PopupManagerAddProductUseCase.createRequestParams(shopId),
                object : Subscriber<Boolean>() {
                    override fun onNext(isSuccess: Boolean) {
                        view.onSuccessGetPopUp(isSuccess, productId)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (!isViewAttached) {
                            return
                        }
                        view.onErrorGetPopUp(e)
                    }

                })
    }

    override fun deleteSingleProduct(productIds: String) {
        view.showLoadingProgress()

        bulkUpdateProductUseCase.execute(BulkUpdateProductUseCase.createRequestParams(singleDeleteProductMapper(productIds)),
                object : Subscriber<ProductUpdateV3SuccessFailedResponse>() {
                    override fun onNext(listResponse: ProductUpdateV3SuccessFailedResponse) {
                        view.hideLoadingProgress()
                        if (listResponse.failedResponse.isNotEmpty()) {
                            view.onErrorMultipleDeleteProduct(NetworkErrorException(),
                                    listResponse)
                        } else {
                            view.onSuccessMultipleDeleteProduct()
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {
                            view.hideLoadingProgress()
                            view.onErrorMultipleDeleteProduct(e, ProductUpdateV3SuccessFailedResponse())
                        }
                    }

                })
    }

    private fun getShopIdInteger(): Int {
        return try {
            Integer.parseInt(userSessionInterface.shopId)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            0
        }
    }

    private fun generateEtalaseIdFilter(etalaseId: Int): String {
        return when (etalaseId) {
            ProductManageConstant.FILTER_ALL_PRODUK -> ProductManageConstant.FILTER_ALL_PRODUK_VALUE
            ProductManageConstant.FILTER_SOLD_PRODUK -> ProductManageConstant.FILTER_SOLD_PRODUK_VALUE
            ProductManageConstant.FILTER_EMPTY_STOK -> ProductManageConstant.FILTER_EMPTY_STOK_VALUE
            ProductManageConstant.FILTER_PENDING -> ProductManageConstant.FILTER_PENDING_VALUE
            ProductManageConstant.FILTER_FREE_RETURNS -> ProductManageConstant.FILTER_FREE_RETURNS_VALUE
            ProductManageConstant.FILTER_PREORDER -> ProductManageConstant.FILTER_PREORDER_VALUE
            ProductManageConstant.FILTER_ALL_SHOWCASE -> ProductManageConstant.FILTER_ALL_SHOWCASE_VALUE
            else -> etalaseId.toString()
        }
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

    override fun mapToProductConfirmationData(isActionDelete: Boolean, stockType: BulkBottomSheetType.StockType, etalaseType: BulkBottomSheetType.EtalaseType,
                                              productManageViewModels: List<ProductManageViewModel>): ArrayList<ConfirmationProductData> {

        val confirmationProductDataList: ArrayList<ConfirmationProductData> = arrayListOf()
        productManageViewModels.forEach {
            val confirmationProductData = ConfirmationProductData()
            confirmationProductData.productId = it.productId
            confirmationProductData.productName = it.productName
            confirmationProductData.productImgUrl = it.imageUrl
            confirmationProductData.productEtalaseName = etalaseType.etalaseValue
            confirmationProductData.isVariant = it.isProductVariant
            confirmationProductDataList.add(confirmationProductData)

            if (etalaseType.etalaseId == ETALASE_DEFAULT) {
                confirmationProductData.productEtalaseId = 0
            } else {
                confirmationProductData.productEtalaseId = etalaseType.etalaseId
            }

            if (isActionDelete) {
                confirmationProductData.statusStock = STOCK_DELETED
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
    override fun failedBulkDataMapper(failData: List<ProductUpdateV3Response>, confirmationProductDataList: List<ConfirmationProductData>)
            : MutableList<ConfirmationProductData> {
        return confirmationProductDataList.filter {
            failData.map { it.productUpdateV3Data.productId }.contains(it.productId)
        }.toMutableList()
    }
}
