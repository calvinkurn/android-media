package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ProductrevGetInboxDesktop
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
            QUERY,
            ProductrevGetInboxDesktop::class.java,
            requestParams.parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).flatMap(Mapper())
    }

    private inner class Mapper : Func1<GraphqlResponse, Observable<ReviewDomain>> {
        override fun call(response: GraphqlResponse): Observable<ReviewDomain> {
            val result = response.getData<ProductrevGetInboxDesktop>(
                ProductrevGetInboxDesktop::class.java
            ).reviewDomain
            return Observable.just(result)
        }
    }

    companion object {
        const val PARAM_REPUTATION_ID: String = "reputationID"
        const val PARAM_USER_ID: String = "user_id"

        private const val PARAM_ROLE: String = "role"
        private const val ROLE_BUYER: Int = 1
        private const val ROLE_SELLER: Int = 2
        private const val QUERY = """
            query productrevGetInboxDesktop(
              $$PARAM_REPUTATION_ID: String!, 
              $$PARAM_ROLE: Int!
            ) {
              productrevGetInboxDesktop(
                $PARAM_REPUTATION_ID: $$PARAM_REPUTATION_ID, 
                $PARAM_ROLE: $$PARAM_ROLE
              ) {
                reputationID
                orderID
                reviewsData {
                  feedbackID
                  hasReviewed
                  isSkipped
                  isEditable
                  productData {
                    productID
                    productName
                    productImageURL
                    productStatus
                  }
                  reviewDataInbox {
                    feedbackID
                    message
                    rating
                    createTime
                    updateTime
                    anonymity
                    imageAttachments {
                      attachmentID
                      imageThumbnailUrl
                      imageUrl
                    }
                    videoAttachments {
                      attachmentID
                      videoUrl
                    }
                    responseMessage
                    responseTime
                  }
                }
                userData {
                  userID
                  fullName
                }
                shopData {
                  shopID
                  name
                }
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
