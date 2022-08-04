package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.broadcaster.domain.model.PinnedProductException
import com.tokopedia.play.broadcaster.domain.model.SetPinnedProduct
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * @author by astidhiyaa on 13/07/22
 */
@GqlQuery(SetPinnedProductUseCase.QUERY_NAME, SetPinnedProductUseCase.QUERY)
class SetPinnedProductUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
) : RetryableGraphqlUseCase<SetPinnedProduct>(graphqlRepository) {

    init {
        setGraphqlQuery(SetPinnedProductUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )
        setTypeClass(SetPinnedProduct::class.java)
    }

    private var coolDownTimerJob: Job? = null
    private val scope = CoroutineScope(dispatcher.default)
    private var productInfo: ProductUiModel? = null

    private fun addCoolDown() {
        cancelTimerJob()
        coolDownTimerJob = scope.launch {
            delay(COOL_DOWN_TIMER)
        }
    }

    private fun isTimerActive(): Boolean = coolDownTimerJob?.isActive ?: false
    private fun isCurrentPinned(): Boolean = productInfo?.pinStatus?.isPinned ?: false


    override suspend fun executeOnBackground(): SetPinnedProduct {
        return if (!isTimerActive() || isCurrentPinned()) {
            handlingCoolDown(response = super.executeOnBackground())
        } else {
            throw PinnedProductException()
        }
    }

    private fun handlingCoolDown(response: SetPinnedProduct): SetPinnedProduct =
        if (response.data.success && !isCurrentPinned()) {
            addCoolDown()
            response
        } else if(!response.data.success) {
            val action = if (isCurrentPinned()) "lepas" else "pasang"
            throw PinnedProductException("Gagal $action pin di produk. Coba lagi, ya.")
        } else {
            response
        }

    fun createParam(channelId: String, product: ProductUiModel): Map<String, Any> {
        productInfo = product

        val productId = if(product.pinStatus.isPinned) "0" else product.id
        return mapOf(
            PARAM_CHANNEL_ID to channelId.toLongOrZero(),
            PARAM_PRODUCT_ID to productId.toLongOrZero(),
        )
    }

    fun cancelTimerJob(){
        coolDownTimerJob?.cancel()
    }

    companion object {
        private const val COOL_DOWN_TIMER = 5000L
        private const val PARAM_CHANNEL_ID = "channelID"
        private const val PARAM_PRODUCT_ID = "productID"
        const val QUERY_NAME = "SetPinnedProductUseCaseQuery"
        const val QUERY = """
            mutation broPinProduct(${"$${PARAM_CHANNEL_ID}"}: Int64!, ${"$${PARAM_PRODUCT_ID}"}: Int64) {
                broadcasterSetPinnedProductTag(
                $PARAM_CHANNEL_ID: ${"$${PARAM_CHANNEL_ID}"},
                $PARAM_PRODUCT_ID: ${"$${PARAM_PRODUCT_ID}"}
                ){
                  success
                }
            }
            """
    }
}