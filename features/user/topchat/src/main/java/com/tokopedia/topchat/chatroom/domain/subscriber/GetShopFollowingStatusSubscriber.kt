package com.tokopedia.topchat.chatroom.domain.subscriber

import com.tokopedia.chat_common.util.handleError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import rx.Subscriber

/**
 * @author by nisie on 16/01/19.
 */

class GetShopFollowingStatusSubscriber(val onErrorGetShopFollowingStatus: (Throwable) -> Unit,
                                       val onSuccess: (Boolean) -> Unit
) : Subscriber<GraphqlResponse>() {
    override fun onNext(graphqlResponse: GraphqlResponse) {
        handleError(graphqlResponse, ShopFollowingPojo::class.java,
                routingOnNext(graphqlResponse), onErrorGetShopFollowingStatus)
    }


    private fun routingOnNext(graphqlResponse: GraphqlResponse): (GraphqlResponse) -> Unit {
        return {
            val pojo = graphqlResponse.getData<ShopFollowingPojo>(ShopFollowingPojo::class.java)
            val ALREADY_FAVORITED = 1
            onSuccess(pojo.shopInfoById.result[0].favoriteData.alreadyFavorited == ALREADY_FAVORITED)
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        onErrorGetShopFollowingStatus(e)
    }

}
