package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.usecase.GetShopFollowingUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetShopFollowingUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): GetShopFollowingUseCase(repository, dispatcher) {

    var response: ShopFollowingPojo = ShopFollowingPojo()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var errorMessage = ""
        set(value) {
            if(value.isNotEmpty()) {
                repository.createErrorMapResult(response::class.java, value)
            }
            field = value
        }
}