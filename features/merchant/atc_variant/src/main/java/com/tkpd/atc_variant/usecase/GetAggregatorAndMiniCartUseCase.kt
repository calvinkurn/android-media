package com.tkpd.atc_variant.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.product.detail.common.data.model.aggregator.AggregatorMiniCartUiModel
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Yehezkiel on 24/05/21
 */
class GetAggregatorAndMiniCartUseCase @Inject constructor(val dispatcher: CoroutineDispatchers,
                                                          private val aggregatorUseCase: GetProductVariantAggregatorUseCase,
                                                          private val miniCartUseCase: GetMiniCartListSimplifiedUseCase) : UseCase<AggregatorMiniCartUiModel>() {

    private var requestParamsAggregator: Map<String, Any?> = mapOf()
    private var shopIds: List<String> = listOf()
    private var isTokoNow: Boolean = false

    suspend fun executeOnBackground(requestParamsAggregator: Map<String, Any?>, shopId: String,
                                    isTokoNow: Boolean): AggregatorMiniCartUiModel {
        this.requestParamsAggregator = requestParamsAggregator
        this.shopIds = listOf(shopId)
        this.isTokoNow = isTokoNow
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
        val miniCartData = (result.getOrNull(1) as? MiniCartSimplifiedData)?.miniCartItems?.associateBy({
            it.productId
        }) {
            it
        }

        return AggregatorMiniCartUiModel(aggregatorData, miniCartData)
    }

    private suspend fun executeAggregator(): Deferred<ProductVariantAggregatorUiData> = withContext(dispatcher.io) {
        async {
            aggregatorUseCase.executeOnBackground(requestParamsAggregator)
        }
    }

    private suspend fun executeMiniCart(): Deferred<MiniCartSimplifiedData?> = withContext(dispatcher.io) {
        asyncCatchError(block = {
            miniCartUseCase.setParams(shopIds)
            miniCartUseCase.executeOnBackground()
        }, onError = {
            null
        })
    }
}