package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.product.detail.usecase.GetProductAllDataUseCase
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class DynamicProductDetailViewModel @Inject constructor(@Named("Main")
                                                        private val dispatcher: CoroutineDispatcher,
                                                        private val stickyLoginUseCase: StickyLoginUseCase,
                                                        private val getProductAllDataUseCase: GetProductAllDataUseCase,
                                                        private val getPdpLayoutUseCase: GetPdpLayoutUseCase) : BaseViewModel(dispatcher) {

    val productInfoP1 = MutableLiveData<Result<ProductInfoP1>>()
    val productSnapshotDataModel = MutableLiveData<Result<ProductSnapshotDataModel>>()
    val productLayout = MutableLiveData<Result<ProductDetailLayout>>()
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
            productLayout.value = Success(pdpLayout)

            productSnapshotDataModel.value = Success(getPdpData(14285907).convertToSnapshotData())
        }) {
            productLayout.value = Fail(it)
        }


    }
    private suspend fun getPdpData(productId:Int): ProductInfoP1 {
        getProductAllDataUseCase.productId = productId
        val pdpData = Success(getProductAllDataUseCase.executeOnBackground()).data
        return ProductInfoP1((pdpData as Success).data.data ?: ProductInfo())
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