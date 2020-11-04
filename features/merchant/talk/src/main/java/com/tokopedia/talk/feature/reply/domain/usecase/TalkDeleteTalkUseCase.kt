package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkDeleteTalkUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<TalkDeleteTalkResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        private const val TALK_DELETE_TALK_MUTATION_CLASS_NAME = "TalkDeleteTalk"
        private const val query =
            """
                mutation talkDeleteTalk(${'$'}talk_id: Int) {
                  talkDeleteTalk(talk_id:${'$'}talk_id) {
                    status
                    messageError
                    data {
                      isSuccess
                      talkID
                    }
                    messageErrorOriginal
                  }
                }
            """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_DELETE_TALK_MUTATION_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkDeleteTalk.GQL_QUERY)
        setTypeClass(TalkDeleteTalkResponseWrapper::class.java)
    }

    fun setParams(talkId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_TALK_ID, talkId)
        setRequestParams(requestParams.parameters)
    }

}
