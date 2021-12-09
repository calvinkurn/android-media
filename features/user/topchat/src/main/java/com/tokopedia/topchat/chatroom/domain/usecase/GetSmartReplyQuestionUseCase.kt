package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.param.SmartReplyQuestionParam
import com.tokopedia.topchat.chatroom.domain.pojo.param.SmartReplyQuestionParam.Companion.PARAM_ADDRESS_ID
import com.tokopedia.topchat.chatroom.domain.pojo.param.SmartReplyQuestionParam.Companion.PARAM_DISTRICT_ID
import com.tokopedia.topchat.chatroom.domain.pojo.param.SmartReplyQuestionParam.Companion.PARAM_LAT_LONG
import com.tokopedia.topchat.chatroom.domain.pojo.param.SmartReplyQuestionParam.Companion.PARAM_MSG_ID
import com.tokopedia.topchat.chatroom.domain.pojo.param.SmartReplyQuestionParam.Companion.PARAM_POSTAL_CODE
import com.tokopedia.topchat.chatroom.domain.pojo.param.SmartReplyQuestionParam.Companion.PARAM_PRODUCT_IDS
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.common.data.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

open class GetSmartReplyQuestionUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
): FlowUseCase<SmartReplyQuestionParam, Resource<ChatSmartReplyQuestionResponse>>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        query chatSmartReplyQuestion(
            $$PARAM_MSG_ID: String!,
            $$PARAM_PRODUCT_IDS: String,
            $$PARAM_ADDRESS_ID: Int,
            $$PARAM_DISTRICT_ID: Int,
            $$PARAM_POSTAL_CODE: String,
            $$PARAM_LAT_LONG: String
        ){
          chatSmartReplyQuestion(
            $PARAM_MSG_ID: $$PARAM_MSG_ID,
            $PARAM_PRODUCT_IDS: $$PARAM_PRODUCT_IDS,
            $PARAM_ADDRESS_ID: $$PARAM_ADDRESS_ID,
            $PARAM_DISTRICT_ID: $$PARAM_DISTRICT_ID,
            $PARAM_POSTAL_CODE: $$PARAM_POSTAL_CODE,
            $PARAM_LAT_LONG: $$PARAM_LAT_LONG
          ){
            isSuccess
            hasQuestion
            title
            list {
              content
              intent
            }
          }
        }
    """.trimIndent()

    override suspend fun execute(
        params: SmartReplyQuestionParam
    ): Flow<Resource<ChatSmartReplyQuestionResponse>> {
        return flow {
            val response = repository.request<SmartReplyQuestionParam,
                    ChatSmartReplyQuestionResponse>(graphqlQuery(), params)
            emit(Resource.success(response))
        }.onStart {
            emit(Resource.loading(null))
        }
    }

}