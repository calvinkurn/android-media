package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.domain.NotifOrderListUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Named

class FakeNotifOrderListUseCase(
    @Named(QUERY_ORDER_LIST)
    query: String,
    private val gqlUseCase: FakeGraphqlUseCase<NotifOrderListResponse>,
    cacheManager: NotifcenterCacheManager,
    userSession: UserSessionInterface
) : NotifOrderListUseCase(
    query, gqlUseCase, cacheManager, userSession
) {

    var response = NotifOrderListResponse()
        set(value) {
            field = value
            gqlUseCase.response = value
        }

    init {
        response = response
    }

}