package com.tokopedia.talk.feature.sellersettings.smartreply.detail.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.data.DiscussionSetSmartReplySettingResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class DiscussionSetSmartReplySettingsUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<DiscussionSetSmartReplySettingResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_SWITCHED_ON = "switchedOn"
        private const val TALK_SMART_REPLY_SETTING_CLASS_NAME = "TalkSmartReplySettings"
        private const val query = """
            mutation discussionSetSmartReplySetting(${'$'}switchedOn: Boolean!) {
              discussionSetSmartReplySetting(switchedOn: ${'$'}switchedOn) {
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

    @GqlQuery(TALK_SMART_REPLY_SETTING_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkSmartReplySettings.GQL_QUERY)
        setTypeClass(DiscussionSetSmartReplySettingResponseWrapper::class.java)
    }

    fun setRequestParams(switchedOn: Boolean) {
        requestParams.putBoolean(PARAM_SWITCHED_ON, switchedOn)
        setRequestParams(requestParams.parameters)
    }
}