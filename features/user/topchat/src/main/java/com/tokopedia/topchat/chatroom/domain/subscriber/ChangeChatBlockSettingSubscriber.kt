package com.tokopedia.topchat.chatroom.domain.subscriber

import com.tokopedia.chat_common.data.BlockedStatus
import com.tokopedia.chat_common.util.handleError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topchat.chatroom.domain.pojo.ChangeChatBlockPojo
import rx.Subscriber
import java.io.IOException

/**
 * @author by nisie on 14/01/19.
 */
class ChangeChatBlockSettingSubscriber(val onErrorChangeChatSetting: (Throwable) -> Unit,
                                       val onSuccessChangeChatSetting: (BlockedStatus) -> Unit)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, ChangeChatBlockPojo::class.java,
                routingOnNext(graphqlResponse), onErrorChangeChatSetting)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        onErrorChangeChatSetting(e)
    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<ChangeChatBlockPojo>(ChangeChatBlockPojo::class.java)

            if (pojo.chatToggleBlockChat.success) {
                onSuccessChangeChatSetting(BlockedStatus(
                        pojo.chatToggleBlockChat.blockStatus.isBlocked,
                        pojo.chatToggleBlockChat.blockStatus.isPromoBlocked,
                        pojo.chatToggleBlockChat.blockStatus.blockedUntil
                ))
            } else {
                onError(IOException())
            }

        }
    }


}
