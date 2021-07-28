package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import androidx.annotation.RawRes
import com.google.gson.JsonObject
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.test.R
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.notifcenter.domain.NotifOrderListUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Named

class FakeNotifOrderListUseCase(
    @Named(QUERY_ORDER_LIST)
    query: String,
    private val gqlUseCase: FakeGraphqlUseCase<NotifOrderListResponse>,
    private val cacheManager: FakeNotifcenterCacheManager,
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

    val cacheResponse: NotifOrderListResponse
        get() = alterDefaultResponse {
            val list = it.getAsJsonObject(notifcenter_notifOrderList)
                .getAsJsonArray(list)
            list.get(0).asJsonObject.addProperty(text, "Cache Transaksi")
            list.get(1).asJsonObject.addProperty(text, "Cache All")
        }

    private var notifcenter_notifOrderList = "notifcenter_notifOrderList"
    private var list = "list"
    private var text = "text"

    init {
        response = response
    }

    fun initialize() {
        this.response = defaultResponse
    }

    fun setCache(cacheResponse: NotifOrderListResponse, @RoleType role: Int) {
        val cacheKey = getCacheKey(role)
        cacheManager.saveCache(cacheKey, cacheResponse)
    }

    fun setResponseWithDelay(delay: Long, response: NotifOrderListResponse) {
        this.response = response
        gqlUseCase.delayMs = delay
    }

    private fun alterDefaultResponse(
        altercation: (JsonObject) -> Unit
    ): NotifOrderListResponse {
        return alterResponseOf(R.raw.notifcenter_notif_order_list, altercation)
    }

    private fun alterResponseOf(
        @RawRes
        rawRes: Int,
        altercation: (JsonObject) -> Unit
    ): NotifOrderListResponse {
        val responseObj: JsonObject = AndroidFileUtil.parseRaw(
            rawRes, JsonObject::class.java
        )
        altercation(responseObj)
        return CommonUtil.fromJson(
            responseObj.toString(), NotifOrderListResponse::class.java
        )
    }

}