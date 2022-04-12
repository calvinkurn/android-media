package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReviewReviewListV2
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 9/26/17.
 */
class GetReviewUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase
) : UseCase<ReviewDomain>() {

    override fun createObservable(requestParams: RequestParams): Observable<ReviewDomain> {
        val graphqlRequest = GraphqlRequest(
            QUERY, InboxReviewReviewListV2::class.java, requestParams.parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).flatMap(Mapper())
    }

    private inner class Mapper : Func1<GraphqlResponse, Observable<ReviewDomain>> {
        override fun call(response: GraphqlResponse): Observable<ReviewDomain> {
            val result = response.getData<InboxReviewReviewListV2>(
                InboxReviewReviewListV2::class.java
            ).reviewDomain
            return Observable.just(result)
        }
    }

    companion object {
        const val PARAM_REPUTATION_ID: String = "reputationId"
        const val PARAM_USER_ID: String = "user_id"

        private const val PARAM_ROLE: String = "role"
        private const val ROLE_BUYER: Int = 1
        private const val ROLE_SELLER: Int = 2
        private const val QUERY = """
            query inboxReviewReviewListV2(${'$'}reputationId: String!, ${'$'}role: Int!) {
              inboxReviewReviewListV2(reputationId: ${'$'}reputationId, role: ${'$'}role) {
                reputationIdStr
                reviewList {
                  inboxIdStr
                  reviewIdStr
                  hasReviewed
                  isSkipped
                  isEditable
                  product {
                    productIdStr
                    name
                    imageUrl
                    url
                    shopIdStr
                    status
                  }
                  reviewData {
                    reviewIdStr
                    reputationIdStr
                    message
                    rating
                    createTime {
                      dateTimeFmt
                    }
                    updateTime {
                      dateTimeFmt
                    }
                    anonymity
                    attachments {
                      attachmentId
                      attachmentIdStr
                      description
                      imageThumbnailUrl
                      imageUrl
                    }
                    videoAttachments {
                      attachmentId
                      videoUrl
                    }
                    response {
                      message
                      createTime {
                        dateTimeFmt
                      }
                    }
                  }
                }
                userData {
                  userIdStr
                  fullName
                }
                shopData {
                  shopIdStr
                  domain
                  shopName
                }
                invoiceRefNum
                invoiceTime
              }
            }
        """

        fun getParam(id: String?, tab: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putString(PARAM_REPUTATION_ID, id)
            params.putInt(PARAM_ROLE, getRole(tab))
            return params
        }

        fun getRole(tab: Int): Int {
            return when (tab) {
                ReviewInboxConstants.TAB_WAITING_REVIEW -> ROLE_BUYER
                ReviewInboxConstants.TAB_MY_REVIEW -> ROLE_BUYER
                ReviewInboxConstants.TAB_BUYER_REVIEW -> ROLE_SELLER
                else -> ROLE_BUYER
            }
        }
    }
}