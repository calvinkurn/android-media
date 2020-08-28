package com.tokopedia.talk.feature.inbox.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.inbox.data.DiscussionInboxResponseWrapper
import javax.inject.Inject

class TalkInboxListUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<DiscussionInboxResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_INBOX_TYPE = "type"
        const val PARAM_FILTER = "filter"
        const val PARAM_PAGE = "page"
        const val PARAM_LIMIT = "limit"

        private val query by lazy {
            """
                query discussionInbox(${'$'}type: String!, ${'$'}filter: String!, ${'$'}page: Int!, ${'$'}limit: Int!) {
                  discussionInbox(type: ${'$'}type, filter: ${'$'}filter, page: ${'$'}page, limit: ${'$'}limit)
                  userName
                  shopID
                  shopName
                  inboxType
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
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(DiscussionInboxResponseWrapper::class.java)
    }

    fun setRequestParam(type: String, filter: String, page: Int) {

    }
}