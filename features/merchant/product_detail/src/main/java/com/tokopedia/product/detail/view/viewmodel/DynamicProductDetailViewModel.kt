package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductInfoP1
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP1UseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2ShopUseCase
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Named

class DynamicProductDetailViewModel @Inject constructor(@Named("Main")
                                                        private val dispatcher: CoroutineDispatcher,
                                                        private val stickyLoginUseCase: StickyLoginUseCase,
                                                        private val getProductInfoP1UseCase: GetProductInfoP1UseCase,
                                                        private val getPdpLayoutUseCase: GetPdpLayoutUseCase,
                                                        private val getProductInfoP2ShopUseCase: GetProductInfoP2ShopUseCase) : BaseViewModel(dispatcher) {

    val productInfoP1 = MutableLiveData<Result<ProductInfoP1>>()
    val productSnapshotDataModel = MutableLiveData<Result<ProductSnapshotDataModel>>()
    val productLayout = MutableLiveData<Result<ProductDetailLayout>>()
    val p2ShopDataResp = MutableLiveData<ProductInfoP2ShopData>()

    private var productInfo = ProductInfo()

    fun getStickyLoginContent(onSuccess: (StickyLoginTickerPojo.TickerDetail) -> Unit, onError: ((Throwable) -> Unit)?) {
        stickyLoginUseCase.setParams(StickyLoginConstant.Page.PDP)
        stickyLoginUseCase.execute(
                onSuccess = {
                    if (it.response.tickers.isNotEmpty()) {
                        for (tickerDetail in it.response.tickers) {
                            if (tickerDetail.layout == StickyLoginConstant.LAYOUT_FLOATING) {
                                onSuccess.invoke(tickerDetail)
                                return@execute
                            }
                        }
                        onError?.invoke(Throwable(""))
                    } else {
                        onError?.invoke(Throwable(""))
                    }
                },
                onError = {
                    onError?.invoke(it)
                }
        )
    }

    fun getProductP1() {
        launchCatchError(block = {
            val pdpLayout = getPdpLayout("77777")
            val productInfo = getPdpData(14285907)
            productLayout.value = Success(pdpLayout)
            productSnapshotDataModel.value = Success(productInfo.convertToSnapshotData())

            val p2ShopDeferred = getProductInfoP2ShopAsync(productInfo.productInfo.basic.shopID,
                    productInfo.productInfo.basic.id.toString(),
                    "", false)

            p2ShopDataResp.value = p2ShopDeferred.await()

        }) {
            productLayout.value = Fail(it)
        }
    }

    private fun getProductInfoP2ShopAsync(shopId: Int, productId: String,
                                          warehouseId: String,
                                          forceRefresh: Boolean = false): Deferred<ProductInfoP2ShopData> {
        return async {
            getProductInfoP2ShopUseCase.createRequestParams(shopId, productId, warehouseId, forceRefresh)
            getProductInfoP2ShopUseCase.executeOnBackground()
        }

    }

    private suspend fun getPdpData(productId: Int): ProductInfoP1 {
        getProductInfoP1UseCase.params = GetProductInfoP1UseCase.createParams(productId, "", "")
        val pdpData = getProductInfoP1UseCase.executeOnBackground().data
        return ProductInfoP1(pdpData ?: ProductInfo())
    }

    private suspend fun getPdpLayout(productId: String): ProductDetailLayout {
        getPdpLayoutUseCase.requestParams = GetPdpLayoutUseCase.createParams(productId)
        getPdpLayoutUseCase.isFromCacheFirst = false
        return getPdpLayoutUseCase.executeOnBackground()
    }

    fun getImageUriPaths(): ArrayList<String> {
        return ArrayList(productInfo.run {
            media.map {
                if (it.type == "image") {
                    it.urlOriginal
                } else {
                    it.urlThumbnail
                }
            }
        })
    }

    private fun ProductInfoP1.convertToSnapshotData(): ProductSnapshotDataModel {
        return ProductSnapshotDataModel(productInfo.media, productInfo)
    }


}