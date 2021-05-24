package com.tkpd.atc_variant.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Yehezkiel on 24/05/21
 */
class GetAggregatorAndQuantityUseCase @Inject constructor(val dispatcher: CoroutineDispatchers,
                                                          val aggregatorUseCase: GetProductVariantAggregatorUseCase) : UseCase<ProductVariantAggregatorUiData>() {

    override suspend fun executeOnBackground(): ProductVariantAggregatorUiData {

    }

    private suspend fun executeAggregator(): ProductVariantAggregatorUiData = withContext(dispatcher.io) {
        async {
            aggregatorUseCase.executeOnBackground(mapOf())
        }
    }.await()

    private fun executeQuantity(): Deferred<ProductVariantAggregatorUiData> {

    }
}