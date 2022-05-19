package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.common.data.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

open class GetSmartReplyQuestionUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
): FlowUseCase<GetSmartReplyQuestionUseCase.Param, Resource<ChatSmartReplyQuestionResponse>>(dispatcher.io) {

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
        params: Param
    ): Flow<Resource<ChatSmartReplyQuestionResponse>> {
        return flow {
            val response = repository.request<Param,
                    ChatSmartReplyQuestionResponse>(graphqlQuery(), params)
            emit(Resource.success(response))
        }.onStart {
            emit(Resource.loading(null))
        }
    }

    data class Param (
        @SerializedName(PARAM_MSG_ID)
        var msgId: String,

        @SerializedName(PARAM_PRODUCT_IDS)
        var productIds: String,

        @SerializedName(PARAM_ADDRESS_ID)
        var addressId: Long,

        @SerializedName(PARAM_DISTRICT_ID)
        var districtId: Long,

        @SerializedName(PARAM_POSTAL_CODE)
        var postalCode: String,

        @SerializedName(PARAM_LAT_LONG)
        var latLon: String
    ): GqlParam

    companion object {
        const val PARAM_MSG_ID = "msgID"
        const val PARAM_PRODUCT_IDS = "productIDs"
        const val PARAM_ADDRESS_ID = "addressID"
        const val PARAM_DISTRICT_ID = "districtID"
        const val PARAM_POSTAL_CODE = "postalCode"
        const val PARAM_LAT_LONG = "latlon"
    }
}