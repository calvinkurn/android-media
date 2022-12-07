package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.domain.model.RequestState
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

typealias BulkReviewGetFormRequestState = RequestState<BulkReviewGetFormResponse.Data.ProductRevGetBulkForm>

class BulkReviewGetFormUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : FlowUseCase<String, BulkReviewGetFormRequestState>(dispatchers.io) {

    companion object {
        private const val PARAM_USER_ID = "userID"
    }

    override fun graphqlQuery(): String {
        return """
            query GetBulkReviewForm(${'$'}$PARAM_USER_ID: String!) {
                productrevGetBulkForm(userID: ${'$'}$PARAM_USER_ID) {
                    list {
                        inboxID
                        reputationID
                        product {
                          productID
                          productName
                          productImageURL
                          productVariant {
                            variantID
                            variantName
                          }
                          timeStamp {
                            createTimeFormatted
                          }
                        }
                    }
                }
            }
        """.trimIndent()
    }

    override suspend fun execute(
        params: String
    ) = flow {
        emit(RequestState.Requesting)
        emit(RequestState.Complete.Success(sendRequest(params).productrevGetBulkForm!!))
    }.catch {
        emit(RequestState.Complete.Error(it))
    }

    private suspend fun sendRequest(
        params: String
    ): BulkReviewGetFormResponse.Data {
//        return repository.request(graphqlQuery(), createRequestParam(params))
        return Gson().fromJson("""
            {"data":{"productrevGetBulkForm":{"list":[{"inboxID":"1234567891","reputationID":"1234567891","product":{"productID":"1234567891","productName":"Nike Air Force 1 Low White","productImageURL":"https://sneakernews.com/wp-content/uploads/2022/09/nike-air-force-1-low-white-grey-dv0788-100-8.jpg","productVariant":{"variantID":"1234567891","variantName":"White Red"},"timeStamp":{"createTimeFormatted":"7 Agu"}}},{"inboxID":"1234567892","reputationID":"1234567892","product":{"productID":"1234567892","productName":"Strawberry korea","productImageURL":"https://api-lotte.hollacode.com/api/static/inspirasi/1648097038884-perbedaan-stroberi-korea-dan-stroberi-lokal.jpg","productVariant":null,"timeStamp":{"createTimeFormatted":"Hari ini"}}},{"inboxID":"1234567893","reputationID":"1234567893","product":{"productID":"1234567893","productName":"Cimory yogurt squeeze","productImageURL":"https://images.tokopedia.net/img/cache/500-square/VqbcmM/2021/10/20/1856df27-3efc-431b-875f-90dcc00d1b83.jpg","productVariant":{"variantID":"1234567893","variantName":"Plain"},"timeStamp":{"createTimeFormatted":"6 Jan"}}}]}}}
        """.trimIndent(), BulkReviewGetFormResponse::class.java).data!!
    }

    private fun createRequestParam(params: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(PARAM_USER_ID, params)
        }.parameters
    }
}
