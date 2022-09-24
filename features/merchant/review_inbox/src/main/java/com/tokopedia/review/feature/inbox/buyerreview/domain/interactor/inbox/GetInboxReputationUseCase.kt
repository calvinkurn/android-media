package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox

import android.text.TextUtils
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.buyerreview.data.mapper.InboxReputationMapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationResponseWrapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 8/18/17.
 */
open class GetInboxReputationUseCase @Inject constructor(
    private val useCase: GraphqlUseCase,
    private val mapper: InboxReputationMapper
) : UseCase<InboxReputationResponseWrapper.Data.Response>() {

    companion object {
        const val PARAM_PER_PAGE: String = "perPage"
        const val PARAM_PAGE: String = "page"
        const val PARAM_ROLE: String = "role"
        const val DEFAULT_PER_PAGE: Int = 10
        const val DEFAULT_FIRST_PAGE: Int = 1
        const val PARAM_TIME_FILTER: String = "timeFilter"
        const val PARAM_STATUS: String = "status"
        const val PARAM_SCORE_FILTER: String = "scoreFilter"
        const val PARAM_REPUTATION_ID: String = "reputationId"

        private const val ROLE_BUYER: Int = 1
        private const val ROLE_SELLER: Int = 2
        const val DEFAULT_TIME_FILTER: Int = 1
        private const val PARAM_KEYWORD: String = "keyword"

        private const val QUERY_NAME = "GetInboxReputationList"
        private const val QUERY = """
            query $QUERY_NAME(
              ${'$'}page: Int!
              ${'$'}perPage: Int!
              ${'$'}role: Int!
              ${'$'}timeFilter: Int!
              ${'$'}status: Int!
              ${'$'}reputationId: String!
              ${'$'}keyword: String!
              ${'$'}scoreFilter: Int!
              ${'$'}readStatus: Int!
            ) {
              inboxReviewReputationListV2(
                page: ${'$'}page
                perPage: ${'$'}perPage
                role: ${'$'}role
                timeFilter: ${'$'}timeFilter
                status: ${'$'}status
                reputationId: ${'$'}reputationId
                keyword: ${'$'}keyword
                scoreFilter: ${'$'}scoreFilter
                readStatus: ${'$'}readStatus
              ) {
                reputationList {
                  shopIdStr
                  userIdStr
                  reputationIdStr
                  orderData {
                    invoiceRefNum
                    createTimeFmt
                  }
                  revieweeData {
                    name
                    roleId
                    picture
                    buyerBadge {
                      positive
                      neutral
                      negative
                      positivePercentage
                      noReputation
                    }
                    shopBadge {
                      tooltip
                      reputationScore
                      score
                      minBadgeScore
                      reputationBadgeUrl
                    }
                  }
                  reputationData {
                    revieweeScore
                    revieweeScoreStatus
                    showRevieweeScore
                    reviewerScore
                    reviewerScoreStatus
                    isEditable
                    isInserted
                    isLocked
                    isAutoScored
                    isCompleted
                    showLockingDeadline
                    lockingDeadlineDays
                    showBookmark
                    actionMessage
                  }
                }
                hasNext
              }
            }
        """

        fun getParam(
            page: Int, keyword: String, timeFilter: String,
            scoreFilter: String, tab: Int
        ): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE)
            params.putInt(PARAM_PAGE, page)
            params.putInt(PARAM_ROLE, getRole(tab))
            if (!TextUtils.isEmpty(keyword)) params.putString(PARAM_KEYWORD, keyword)
            params.putInt(
                PARAM_TIME_FILTER,
                if (!TextUtils.isEmpty(timeFilter)) {
                    timeFilter.toIntOrNull() ?: DEFAULT_TIME_FILTER
                } else {
                    DEFAULT_TIME_FILTER
                }
            )
            if (!TextUtils.isEmpty(scoreFilter)) params.putInt(PARAM_SCORE_FILTER, scoreFilter.toIntOrZero())
            params.putInt(PARAM_STATUS, tab)
            return params
        }

        fun getSpecificReputation(reputationId: String?, tab: Int): RequestParams {
            val params: RequestParams = RequestParams.create()
            params.putInt(PARAM_PER_PAGE, DEFAULT_PER_PAGE)
            params.putInt(PARAM_PAGE, DEFAULT_FIRST_PAGE)
            params.putInt(PARAM_ROLE, getRole(tab))
            params.putInt(PARAM_TIME_FILTER, DEFAULT_TIME_FILTER)
            params.putInt(PARAM_STATUS, tab)
            params.putString(PARAM_REPUTATION_ID, reputationId)
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

    override fun createObservable(requestParams: RequestParams): Observable<InboxReputationResponseWrapper.Data.Response> {
        val graphqlRequest = GraphqlRequest(QUERY, InboxReputationResponseWrapper.Data::class.java, requestParams.parameters)
        useCase.clearRequest()
        useCase.addRequest(graphqlRequest)
        return useCase.createObservable(requestParams).flatMap(mapper)
    }
}