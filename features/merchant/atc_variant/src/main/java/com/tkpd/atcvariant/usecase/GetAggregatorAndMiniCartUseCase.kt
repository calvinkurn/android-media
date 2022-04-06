package com.tkpd.atcvariant.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.product.detail.common.data.model.aggregator.AggregatorMiniCartUiModel
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Yehezkiel on 24/05/21
 */
class GetAggregatorAndMiniCartUseCase @Inject constructor(val dispatcher: CoroutineDispatchers,
                                                          private val aggregatorUseCase: GetProductVariantAggregatorUseCase,
                                                          private val miniCartUseCase: GetMiniCartListSimplifiedUseCase) : UseCase<AggregatorMiniCartUiModel>(), CoroutineScope {

    private var requestParamsAggregator: Map<String, Any?> = mapOf()
    private var shopIds: List<String> = listOf()
    private var isTokoNow: Boolean = false
    private var isLoggedIn: Boolean = false

    override val coroutineContext: CoroutineContext
        get() = dispatcher.main + SupervisorJob()

    suspend fun executeOnBackground(productId: String,
                                    source: String,
                                    warehouseId: String? = null,
                                    pdpSession: String? = null,
                                    shopId: String,
                                    isLoggedIn: Boolean,
                                    isTokoNow: Boolean,
                                    extParams: String): AggregatorMiniCartUiModel {
        this.requestParamsAggregator = aggregatorUseCase.createRequestParams(productId, source, isTokoNow, shopId, extParams, warehouseId, pdpSession)
        this.shopIds = listOf(shopId)
        this.isTokoNow = isTokoNow
        this.isLoggedIn = isLoggedIn
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): AggregatorMiniCartUiModel {
        val request: MutableList<Deferred<Any?>> = mutableListOf(executeAggregator())
        if (isTokoNow) {
            request.add(executeMiniCart())
        }

        val result = request.awaitAll()

        val aggregatorData = result.firstOrNull() as? ProductVariantAggregatorUiData
                ?: ProductVariantAggregatorUiData()
        val miniCartData = (result.getOrNull(1) as? MiniCartSimplifiedData)?.miniCartItems

        return AggregatorMiniCartUiModel(aggregatorData, miniCartData)
    }

    private fun executeAggregator(): Deferred<ProductVariantAggregatorUiData> {
        return async(dispatcher.io) {
            aggregatorUseCase.executeOnBackground(requestParamsAggregator)
        }
    }

    private fun executeMiniCart(): Deferred<MiniCartSimplifiedData?> {
        return asyncCatchError(dispatcher.io, block = {
            miniCartUseCase.setParams(shopIds)
            miniCartUseCase.executeOnBackground()
        }, onError = {
            null
        })
    }
}