package com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.data.DiscussionSetSmartReplyTemplateResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionSetSmartReplyTemplateUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionSetSmartReplyTemplateResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_READY = "templateReady"
        const val PARAM_NOT_READY = "templateNotReady"
        private const val TALK_SMART_REPLY_TEMPLATE_CLASS_NAME = "TalkSmartReplyTemplate"
        private const val query = """
            mutation discussionSetSmartReplyTemplate(${'$'}templateReady: String!, ${'$'}templateNotReady: String!) {
              discussionSetSmartReplyTemplate(templateReady: ${'$'}templateReady, templateNotReady: ${'$'}templateNotReady) {
                isSuccess
                reason
              }
            }
        """
    }

    private val requestParams = RequestParams.EMPTY

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_SMART_REPLY_TEMPLATE_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkSmartReplyTemplate.GQL_QUERY)
        setTypeClass(DiscussionSetSmartReplyTemplateResponseWrapper::class.java)
    }

    fun setParams(templateReady: String, templateNotReady: String) {
        requestParams.apply {
            putString(PARAM_READY, templateReady)
            putString(PARAM_NOT_READY, templateNotReady)
        }
        setRequestParams(requestParams.parameters)
    }
}