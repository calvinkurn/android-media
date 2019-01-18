package com.tokopedia.topchat.chatroom.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.ChangeChatBlockPojo
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 09/01/19.
 */

class ChangeChatBlockSettingUseCase @Inject constructor(
        val resources: Resources,
        private val graphqlUseCase: GraphqlUseCase
) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.chatsettings)
        val graphqlRequest = GraphqlRequest(query,
                ChangeChatBlockPojo::class.java, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_MESSAGE_ID: String = "messageID"
        private val PARAM_BLOCK_TYPE: String = "blockType"
        private val PARAM_IS_BLOCKED: String = "isBlocked"

        const val BLOCK_TYPE_PERSONAL: String = "1"
        const val BLOCK_TYPE_PROMOTION: String = "2"

        fun generateParam(messageId: String, blockType: String, isBlocked: Boolean):
                Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_MESSAGE_ID] = messageId
            requestParams[PARAM_BLOCK_TYPE] = blockType
            requestParams[PARAM_IS_BLOCKED] = isBlocked
            return requestParams
        }

        fun getBlockType(opponentRole: String): String {
            return if (opponentRole.toLowerCase().contains(ChatRoomHeaderViewModel.Companion.ROLE_OFFICIAL)) {
                ChangeChatBlockSettingUseCase.BLOCK_TYPE_PROMOTION
            } else {
                ChangeChatBlockSettingUseCase.BLOCK_TYPE_PERSONAL
            }
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}