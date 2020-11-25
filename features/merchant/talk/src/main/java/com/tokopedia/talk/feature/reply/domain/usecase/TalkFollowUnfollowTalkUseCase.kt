package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkFollowUnfollowTalkUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TalkFollowUnfollowTalkResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        private const val TALK_FOLLOW_UNFOLLOW_TALK_MUTATION_CLASS_NAME = "TalkFollowUnfollowTalk"
        private const val query =
            """
                mutation talkFollowUnfollowTalk(${'$'}talk_id: Int) {
                  talkFollowUnfollowTalk(talk_id: ${'$'}talk_id) {
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

    @GqlQuery(TALK_FOLLOW_UNFOLLOW_TALK_MUTATION_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkFollowUnfollowTalk.GQL_QUERY)
        setTypeClass(TalkFollowUnfollowTalkResponseWrapper::class.java)
    }

    fun setParams(talkId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_TALK_ID, talkId)
        setRequestParams(requestParams.parameters)
    }
}