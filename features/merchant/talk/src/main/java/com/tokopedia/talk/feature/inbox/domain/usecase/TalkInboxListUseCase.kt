package com.tokopedia.talk.feature.inbox.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.inbox.data.DiscussionInboxResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkInboxListUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<DiscussionInboxResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_INBOX_TYPE = "type"
        const val PARAM_FILTER = "filter"
        const val PARAM_PAGE = "page"
        const val PARAM_LIMIT = "limit"
        private const val TALK_INBOX_LIST_QUERY_CLASS_NAME = "TalkInboxList"
        private const val query =
            """
                query discussionInbox(${'$'}type: String!, ${'$'}filter: String!, ${'$'}page: Int!, ${'$'}limit: Int!) {
                  discussionInbox(type: ${'$'}type, filter: ${'$'}filter, page: ${'$'}page, limit: ${'$'}limit) {
                    userName
                    shopID
                    shopName
                    inboxType
                    sellerUnread
                    buyerUnread
                    problemTotal
                    unrespondedTotal
                    hasNext
                    inbox {
                      inboxID
                      questionID
                      isMasked
                      content
                      lastReplyTime
                      totalAnswer
                      isUnread
                      answererThumbnail
                      productID
                      productName
                      productThumbnail
                      productURL
                      state {
                        isUnresponded
                        hasProblem
                      }
                    }
                  }
                }
            """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_INBOX_LIST_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkInboxList.GQL_QUERY)
        setTypeClass(DiscussionInboxResponseWrapper::class.java)
    }

    fun setRequestParam(type: String, filter: String, page: Int) {
        setRequestParams(
                RequestParams.create().apply {
                    putString(PARAM_INBOX_TYPE, type)
                    putString(PARAM_FILTER, filter)
                    putInt(PARAM_PAGE, page)
                    putInt(PARAM_LIMIT, TalkConstants.DEFAULT_ITEM_LIMIT)
                }.parameters
        )
    }
}