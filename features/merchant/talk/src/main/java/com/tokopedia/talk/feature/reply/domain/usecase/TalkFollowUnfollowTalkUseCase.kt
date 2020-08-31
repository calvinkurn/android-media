package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.follow.TalkFollowUnfollowTalkResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkFollowUnfollowTalkUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TalkFollowUnfollowTalkResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        private val query by lazy {
            val talkId = "\$talk_id"
            """
                mutation talkFollowUnfollowTalk($talkId: Int) {
                  talkFollowUnfollowTalk(talk_id: $talkId) {
                    status
                    messageError
                    data {
                      isSuccess
                      talkID
                    }
                    messageErrorOriginal
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(TalkFollowUnfollowTalkResponseWrapper::class.java)
    }

    fun setParams(talkId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_TALK_ID, talkId)
        setRequestParams(requestParams.parameters)
    }
}