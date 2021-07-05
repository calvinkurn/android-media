package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import androidx.annotation.RawRes
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
            return alterDefaultResponse {
                it.getAsJsonObject(notifcenter_detail_v3)
                    .getAsJsonArray(list).removeAll { true }
            }
        }

    val newListOnlyHasNextTrue: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(notifcenter_detail_v3).apply {
                    getAsJsonArray(list).removeAll { true }
                    getAsJsonObject(new_paging).addProperty(has_next, true)
                }
            }
        }

    val newListOnlyHasNextFalse: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(notifcenter_detail_v3).apply {
                    getAsJsonArray(list).removeAll { true }
                    getAsJsonObject(new_paging).addProperty(has_next, false)
                }
            }
        }

    val noTrackHistoryWidget: NotifcenterDetailResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_detail_v3_no_track_history_widget,
            NotifcenterDetailResponse::class.java
        )

    val noTrackHistoryWidgetMsg: NotifcenterDetailResponse
        get() {
            return alterResponseOf(R.raw.notifcenter_detail_v3_no_track_history_widget) {
                it.getAsJsonObject(notifcenter_detail_v3)
                    .getAsJsonArray(new_list).get(0).asJsonObject
                    .getAsJsonObject(widget)
                    .addProperty(message, "")
            }
        }

    private val notifcenter_detail_v3 = "notifcenter_detail_v3"
    private val new_list = "new_list"
    private val new_paging = "new_paging"
    private val has_next = "has_next"
    private val list = "list"
    private val widget = "widget"
    private val message = "message"

    init {
        response = response
    }

    fun initialize() {
        this.response = defaultResponse
    }

    private fun alterDefaultResponse(
        altercation: (JsonObject) -> Unit
    ): NotifcenterDetailResponse {
        return alterResponseOf(R.raw.notifcenter_detail_v3, altercation)
    }

    private fun alterResponseOf(
        @RawRes
        rawRes: Int,
        altercation: (JsonObject) -> Unit
    ): NotifcenterDetailResponse {
        val responseObj: JsonObject = AndroidFileUtil.parseRaw(
            rawRes, JsonObject::class.java
        )
        altercation(responseObj)
        return CommonUtil.fromJson(
            responseObj.toString(), NotifcenterDetailResponse::class.java
        )
    }

}