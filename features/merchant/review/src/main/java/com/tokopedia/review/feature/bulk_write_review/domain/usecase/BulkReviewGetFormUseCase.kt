package com.tokopedia.review.feature.bulk_write_review.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormRequestState
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BulkReviewGetFormUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: GraphqlRepository
) : FlowUseCase<Unit, BulkReviewGetFormRequestState>(dispatchers.io) {

    companion object {
        private const val ERROR_MESSAGE_REVIEW_ITEMS_IS_EMPTY = "productrevGetBulkForm.list is null or empty"
        private const val ERROR_MESSAGE_RESPONSE_IS_NULL = "productrevGetBulkForm is null"
    }

    override fun graphqlQuery(): String {
        return """
            query GetBulkReviewForm {
                productrevGetBulkForm {
                    list {
                        inboxID
                        reputationID
                        orderID
                        product {
                          productID
                          productName
                          productImageURL
                          productVariant {
                            variantID
                            variantName
                          }
                        }
                        timestamp {
                          createTimeFormatted
                        }
                    }
                }
            }
        """.trimIndent()
    }

    override suspend fun execute(
        params: Unit
    ) = flow {
        emit(BulkReviewGetFormRequestState.Requesting())
        sendRequest(params).productrevGetBulkForm?.let { productrevGetBulkForm ->
            if (productrevGetBulkForm.reviewForm.isNullOrEmpty()) {
                emit(
                    BulkReviewGetFormRequestState.Complete.Error(
                        IllegalStateException(ERROR_MESSAGE_REVIEW_ITEMS_IS_EMPTY)
                    )
                )
            } else {
                emit(BulkReviewGetFormRequestState.Complete.Success(productrevGetBulkForm))
            }
        } ?: emit(
            BulkReviewGetFormRequestState.Complete.Error(
                IllegalStateException(ERROR_MESSAGE_RESPONSE_IS_NULL)
            )
        )
    }.catch {
        emit(BulkReviewGetFormRequestState.Complete.Error(it))
    }

    private suspend fun sendRequest(params: Unit): BulkReviewGetFormResponse.Data {
//        return repository.request(graphqlQuery(), params)
        delay(5000L)
        return Gson().fromJson(
            """
            {"data":{"productrevGetBulkForm":{"list":[{"inboxID":"1234567891","reputationID":"1234567891","product":{"productID":"1234567891","productName":"Nike Air Force 1 Low White","productImageURL":"https://sneakernews.com/wp-content/uploads/2022/09/nike-air-force-1-low-white-grey-dv0788-100-8.jpg","productVariant":{"variantID":"1234567891","variantName":"White Red"}},"timestamp":{"createTimeFormatted":"7 Agu"}},{"inboxID":"1234567892","reputationID":"1234567892","product":{"productID":"1234567892","productName":"Strawberry korea","productImageURL":"https://api-lotte.hollacode.com/api/static/inspirasi/1648097038884-perbedaan-stroberi-korea-dan-stroberi-lokal.jpg","productVariant":null},"timestamp":{"createTimeFormatted":"Hari ini"}},{"inboxID":"1234567893","reputationID":"1234567893","product":{"productID":"1234567893","productName":"Cimory yogurt squeeze","productImageURL":"https://images.tokopedia.net/img/cache/500-square/VqbcmM/2021/10/20/1856df27-3efc-431b-875f-90dcc00d1b83.jpg","productVariant":{"variantID":"1234567893","variantName":"Plain"}},"timestamp":{"createTimeFormatted":"6 Jan"}},{"inboxID":"1234567894","reputationID":"1234567894","product":{"productID":"1234567894","productName":"Anggur manis kayak kamu","productImageURL":"https://cdn1-production-images-kly.akamaized.net/xVKi8bO3tCEWrksvOijPcVt1USQ=/640x640/smart/filters:quality(75):strip_icc():format(jpeg)/kly-media-production/medias/3544985/original/096481000_1629359516-239501595_4281020378649751_5471979207942431497_n.jpg","productVariant":null},"timestamp":{"createTimeFormatted":"6 Jul"}},{"inboxID":"1234567895","reputationID":"1234567895","product":{"productID":"1234567895","productName":"Stroberi mangga apel","productImageURL":"https://d1vbn70lmn1nqe.cloudfront.net/prod/wp-content/uploads/2022/02/16083053/jenis-jenis-buah-durian-dan-manfaatnya-bagi-kesehatan-halodoc.jpg","productVariant":{"variantID":"1234567893","variantName":"Sori ga lepel"}},"timestamp":{"createTimeFormatted":"30 Jul"}},{"inboxID":"1234567896","reputationID":"1234567896","product":{"productID":"1234567896","productName":"Bocah gemblung","productImageURL":"https://upload.wikimedia.org/wikipedia/en/thumb/3/33/Patrick_Star.svg/1200px-Patrick_Star.svg.png","productVariant":{"variantID":"1234567896","variantName":"Pink"}},"timestamp":{"createTimeFormatted":"30 Jul"}},{"inboxID":"1234567897","reputationID":"1234567897","product":{"productID":"1234567897","productName":"Tumbler Starbuck","productImageURL":"https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/3/5/24fcca29-65f5-4cee-b860-b94c2e946b2c.jpg","productVariant":{"variantID":"1234567897","variantName":"Transparan"}},"timestamp":{"createTimeFormatted":"30 Jul"}},{"inboxID":"1234567898","reputationID":"1234567898","product":{"productID":"1234567898","productName":"Nasi lemak","productImageURL":"https://awsimages.detik.net.id/community/media/visual/2021/06/11/resep-nasi-lemak-malaysia_43.jpeg?w=700&q=90","productVariant":{"variantID":"1234567898","variantName":"Ndude"}},"timestamp":{"createTimeFormatted":"30 Jul"}},{"inboxID":"1234567899","reputationID":"1234567899","product":{"productID":"1234567899","productName":"Palu thor","productImageURL":"https://images.tokopedia.net/img/cache/500-square/product-1/2019/5/18/787530/787530_6597e33b-4c84-4e7a-aed0-7391a6e6bc11_960_960.jpg","productVariant":{"variantID":"1234567899","variantName":"Ndude"}},"timestamp":{"createTimeFormatted":"30 Jul"}}]}}}
            """.trimIndent(),
            BulkReviewGetFormResponse::class.java
        ).data!!
    }
}
