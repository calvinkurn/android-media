package com.tokopedia.universal_sharing.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductWrapperResponse
import com.tokopedia.universal_sharing.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class UniversalSharingPostPurchaseGetDetailProductUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers
) {

    private val productDetailFlow =
        MutableStateFlow<Result<UniversalSharingPostPurchaseProductResponse>?>(null)

    private fun graphqlQuery(): String = """
        query getProductV3(
          $$PRODUCT_ID: String!,
          $$OPTIONS: OptionV3!
        ) {
          getProductV3(
            productID:$$PRODUCT_ID, 
            options:$$OPTIONS, 
            extraInfo: { aggregate: true }
          ) {
            productID
            productName
            price
            stock
            status
            url
            pictures {
              urlOriginal
            }
            shop {
              name
            }
          }
        }
    """.trimIndent()

    fun observe() = productDetailFlow.asStateFlow()

    suspend fun getDetailProduct(productId: String) {
        withContext(dispatcher.io) {
            productDetailFlow.emit(Result.Loading)
            try {
                val params = generateParam(productId)
                val response = repository
                    .request<Map<String, Any>, UniversalSharingPostPurchaseProductWrapperResponse>(
                        graphqlQuery(),
                        params
                    )
                productDetailFlow.emit(Result.Success(response.product))
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                productDetailFlow.emit(Result.Error(throwable))
            }
        }
    }

    private fun generateParam(productId: String): Map<String, Any> {
        val optionMap = mutableMapOf(
            "basic" to true,
            "picture" to true
        )
        return mapOf(
            PRODUCT_ID to productId,
            OPTIONS to optionMap
        )
    }

    companion object {
        const val PRODUCT_ID = "productID"
        const val OPTIONS = "options"
    }
}
