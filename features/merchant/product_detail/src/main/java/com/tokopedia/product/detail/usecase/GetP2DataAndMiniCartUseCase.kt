package com.tokopedia.product.detail.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.util.OnErrorLog
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Yehezkiel on 03/06/21
 */
class GetP2DataAndMiniCartUseCase @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                      private val getProductInfoP2DataUseCase: GetProductInfoP2DataUseCase,
                                                      private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase) : UseCase<ProductInfoP2UiData>(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + SupervisorJob()

    private var requestParamsP2Data: RequestParams = RequestParams.create()
    private var isTokoNow: Boolean = false
    private var shopId: String = ""
    private var forceRefresh: Boolean = false

    fun clearCacheP2Data() {
        getProductInfoP2DataUseCase.clearCache()
    }

    suspend fun executeOnBackground(requestParams: RequestParams, isTokoNow: Boolean, shopId: String, forceRefresh: Boolean,
                                    setErrorLogListener: OnErrorLog): ProductInfoP2UiData {
        this.requestParamsP2Data = requestParams
        this.isTokoNow = isTokoNow
        this.shopId = shopId
        this.forceRefresh = forceRefresh
        getProductInfoP2DataUseCase.setErrorLogListener {
            setErrorLogListener.invoke(it)
        }
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductInfoP2UiData {
        val request: MutableList<Deferred<Any?>> = mutableListOf(executeP2Data())
        if (isTokoNow) {
            request.add(executeMiniCart())
        }

        val result = request.awaitAll()

        val p2Data = result.firstOrNull() as? ProductInfoP2UiData ?: ProductInfoP2UiData()

        val miniCartData = (result.getOrNull(1) as? MiniCartSimplifiedData)?.miniCartItems?.associateBy({
            it.productId
        }) {
            it
        }

        p2Data.miniCart = miniCartData
        return p2Data
    }


    private fun executeMiniCart(): Deferred<MiniCartSimplifiedData?> {
        return asyncCatchError(dispatcher.io, block = {
            getMiniCartListSimplifiedUseCase.setParams(listOf(shopId))
            getMiniCartListSimplifiedUseCase.executeOnBackground()
        }, onError = {
            null
        })
    }

    private fun executeP2Data(): Deferred<ProductInfoP2UiData> {
        return async(dispatcher.io) {
            getProductInfoP2DataUseCase.executeOnBackground(requestParamsP2Data, forceRefresh)
        }
    }
}