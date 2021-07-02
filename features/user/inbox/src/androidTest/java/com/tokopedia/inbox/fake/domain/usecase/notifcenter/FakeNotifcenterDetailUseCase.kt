package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.fake.common.FakeGraphqlUseCase
import com.tokopedia.inbox.test.R
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.mapper.NotifcenterDetailMapper
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import javax.inject.Named

class FakeNotifcenterDetailUseCase(
    @Named(QUERY_NOTIFCENTER_DETAIL_V3)
    query: String,
    private val gqlUseCase: FakeGraphqlUseCase<NotifcenterDetailResponse>,
    mapper: NotifcenterDetailMapper,
    dispatchers: CoroutineDispatchers
) : NotifcenterDetailUseCase(query, gqlUseCase, mapper, dispatchers) {

    var response = NotifcenterDetailResponse()
        set(value) {
            field = value
            gqlUseCase.response = value
        }

    val defaultResponse: NotifcenterDetailResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_detail_v3,
            NotifcenterDetailResponse::class.java
        )

    val newListOnly: NotifcenterDetailResponse
        get() {
            val responseObj: JsonObject = AndroidFileUtil.parseRaw(
                R.raw.notifcenter_detail_v3,
                JsonObject::class.java
            )
            responseObj.getAsJsonObject(notifcenter_detail_v3)
                .getAsJsonArray(list).removeAll { true }
            return CommonUtil.fromJson(
                responseObj.toString(), NotifcenterDetailResponse::class.java
            )
        }

    val noTrackHistoryWidget: NotifcenterDetailResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_detail_v3_no_track_history_widget,
            NotifcenterDetailResponse::class.java
        )

    val noTrackHistoryWidgetMsg: NotifcenterDetailResponse
        get() {
            val responseObj: JsonObject = AndroidFileUtil.parseRaw(
                R.raw.notifcenter_detail_v3_no_track_history_widget,
                JsonObject::class.java
            )
            responseObj.getAsJsonObject(notifcenter_detail_v3)
                .getAsJsonArray(new_list).get(0).asJsonObject
                .getAsJsonObject(widget)
                .addProperty(message, "")
            return CommonUtil.fromJson(
                responseObj.toString(), NotifcenterDetailResponse::class.java
            )
        }

    private val notifcenter_detail_v3 = "notifcenter_detail_v3"
    private val new_list = "new_list"
    private val list = "list"
    private val widget = "widget"
    private val message = "message"

    init {
        response = response
    }

    fun initialize() {
        this.response = defaultResponse
    }


}