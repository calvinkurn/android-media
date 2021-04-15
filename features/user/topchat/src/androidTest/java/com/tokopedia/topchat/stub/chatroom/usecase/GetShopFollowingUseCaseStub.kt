package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.usecase.GetShopFollowingUseCase
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub

class GetShopFollowingUseCaseStub(
        private val gqlUseCase: GraphqlUseCaseStub<ShopFollowingPojo>,
        dispatchers: CoroutineDispatchers
) : GetShopFollowingUseCase(gqlUseCase, dispatchers) {

    var response: ShopFollowingPojo = ShopFollowingPojo()
        set(value) {
            gqlUseCase.response = value
            field = value
        }

    init {
        gqlUseCase.response = response
    }

}