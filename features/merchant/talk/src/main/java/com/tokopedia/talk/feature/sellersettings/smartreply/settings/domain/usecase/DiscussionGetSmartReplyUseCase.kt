package com.tokopedia.talk.feature.sellersettings.smartreply.settings.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.smartreply.settings.data.DiscussionGetSmartReplyResponseWrapper
import javax.inject.Inject

class DiscussionGetSmartReplyUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionGetSmartReplyResponseWrapper>(graphqlRepository) {

    companion object {
        private const val TALK_SMART_REPLY_QUERY_CLASS_NAME = "TalkSmartReply"
        private const val query =
                """
                    {
                      discussionGetSmartReply {
                        isSmartReplyOn
                        totalQuestion
                        totalAnsweredBySmartReply
                        replySpeed
                        messageReady
                        messageNotReady
                      }
                    }
                """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_SMART_REPLY_QUERY_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkSmartReply.GQL_QUERY)
        setTypeClass(DiscussionGetSmartReplyResponseWrapper::class.java)
    }

}