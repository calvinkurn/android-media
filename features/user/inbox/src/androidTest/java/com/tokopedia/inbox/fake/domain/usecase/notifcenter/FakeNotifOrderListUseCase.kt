package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.test.R
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

    val defaultResponse: NotifOrderListResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_notif_order_list, NotifOrderListResponse::class.java
        )

    init {
        response = response
    }

    fun initialize() {
        this.response = defaultResponse
    }

}