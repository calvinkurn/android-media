package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.common.data.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class SmartReplyQuestionUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<ChatSmartReplyQuestionResponse>
) {

    fun getSrwList(
            msgId: String,
            productIds: String,
            addressId: String,
            districtId: String,
            postalCode: String,
            latLon: String
    ) = flow {
        emit(Resource.loading(null))
        val param = generateParam(msgId, productIds, addressId, districtId, postalCode, latLon)
        val response = gqlUseCase.apply {
            setGraphqlQuery(query)
            setRequestParams(param)
            setTypeClass(ChatSmartReplyQuestionResponse::class.java)
        }.executeOnBackground()
        emit(Resource.success(response))
    }

    private fun generateParam(
        msgId: String,
        productIds: String,
        addressId: String,
        districtId: String,
        postCode: String,
        latLon: String
    ): Map<String, Any?> {
        return mapOf(
            paramMsgId to msgId,
            productIDs to productIds,
            addressID to addressId.toLongOrZero(),
            districtID to districtId.toLongOrZero(),
            postalCode to postCode,
            latlon to latLon,
        )
    }

    private val query = """
        query chatSmartReplyQuestion(
            $$paramMsgId: String!,
            $$productIDs: String,
            $$addressID: Int,
            $$districtID: Int,
            $$postalCode: String,
            $$latlon: String
        ){
          chatSmartReplyQuestion(
            $paramMsgId: $$paramMsgId,
            $productIDs: $$productIDs,
            $addressID: $$addressID,
            $districtID: $$districtID,
            $postalCode: $$postalCode,
            $latlon: $$latlon
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

    companion object {
        private const val paramMsgId = "msgID"
        private const val productIDs = "productIDs"
        private const val addressID = "addressID"
        private const val districtID = "districtID"
        private const val postalCode = "postalCode"
        private const val latlon = "latlon"
    }
}