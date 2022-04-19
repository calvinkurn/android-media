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

class FakeNotifcenterDetailUseCase(
    private val gqlUseCase: FakeGraphqlUseCase<NotifcenterDetailResponse>,
    mapper: NotifcenterDetailMapper,
    dispatchers: CoroutineDispatchers
) : NotifcenterDetailUseCase(gqlUseCase, mapper, dispatchers) {

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

    val fifteenNotifications: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3).apply {
                    val earlierSection = getAsJsonArray(LIST)
                    val notification = earlierSection.get(0)
                    for (i in 0 until 100) {
                        earlierSection.add(notification)
                    }
                }
            }
        }

    val emptyNotifications: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3).apply {
                    getAsJsonArray(LIST).removeAll { true }
                    getAsJsonArray(NEW_LIST).removeAll { true }
                }
            }
        }

    val newListOnly: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3)
                    .getAsJsonArray(LIST).removeAll { true }
            }
        }

    val earlierOnly: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3)
                    .getAsJsonArray(NEW_LIST).removeAll { true }
            }
        }

    val newListOnlyHasNextTrue: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3).apply {
                    getAsJsonArray(LIST).removeAll { true }
                    getAsJsonObject(NEW_PAGING).addProperty(HAS_NEXT, true)
                }
            }
        }

    val earlierOnlyHasNextTrue: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3).apply {
                    getAsJsonArray(NEW_LIST).removeAll { true }
                    getAsJsonObject(PAGING).addProperty(HAS_NEXT, true)
                }
            }
        }

    val newListOnlyHasNextFalse: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3).apply {
                    getAsJsonArray(LIST).removeAll { true }
                    getAsJsonObject(NEW_PAGING).addProperty(HAS_NEXT, false)
                }
            }
        }

    val earlierOnlyHasNextFalse: NotifcenterDetailResponse
        get() {
            return alterDefaultResponse {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3).apply {
                    getAsJsonArray(NEW_LIST).removeAll { true }
                    getAsJsonObject(PAGING).addProperty(HAS_NEXT, false)
                }
            }
        }

    val productOnly: NotifcenterDetailResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_detail_v3_product_only,
            NotifcenterDetailResponse::class.java
        )

    val noTrackHistoryWidget: NotifcenterDetailResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_detail_v3_no_track_history_widget,
            NotifcenterDetailResponse::class.java
        )

    val noTrackHistoryWidgetMsg: NotifcenterDetailResponse
        get() {
            return alterResponseOf(R.raw.notifcenter_detail_v3_no_track_history_widget) {
                it.getAsJsonObject(NOTIFCENTER_DETAIL_V3)
                    .getAsJsonArray(NEW_LIST).get(0).asJsonObject
                    .getAsJsonObject(WIDGET)
                    .addProperty(MESSAGE, "")
            }
        }

    val bannerWithinTwentyFourHours: NotifcenterDetailResponse
        get() {
            return alterResponseOf(R.raw.notifcenter_detail_v3_banner_only) {
                val newList = it.getAsJsonObject(NOTIFCENTER_DETAIL_V3).getAsJsonArray(NEW_LIST)
                for (i in 0 until newList.size()) {
                    newList.get(i).asJsonObject
                        .addProperty(EXPIRE_TIME_UNIX, getNewExpiredTime(THREE_HOURS))
                }
            }
        }

    val bannerOnly: NotifcenterDetailResponse
        get() {
            return alterResponseOf(R.raw.notifcenter_detail_v3_banner_only) {
                val newList = it.getAsJsonObject(NOTIFCENTER_DETAIL_V3).getAsJsonArray(NEW_LIST)
                for (i in 0 until newList.size()) {
                    newList.get(i).asJsonObject
                        .addProperty(EXPIRE_TIME_UNIX, getNewExpiredTime(THREE_DAYS))
                }
            }
        }

    private fun getNewExpiredTime(extendedTime: Long): Long {
        return (System.currentTimeMillis() / UNIX_DIVIDER) + extendedTime
    }

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

    fun setResponseWithDelay(
        delay: Long,
        response: NotifcenterDetailResponse
    ) {
        this.response = response
        gqlUseCase.delayMs = delay
    }

    companion object {
        private const val NOTIFCENTER_DETAIL_V3 = "notifcenter_detail_v3"
        private const val NEW_LIST = "new_list"
        private const val PAGING = "paging"
        private const val NEW_PAGING = "new_paging"
        private const val HAS_NEXT = "has_next"
        private const val LIST = "list"
        private const val WIDGET = "widget"
        private const val MESSAGE = "message"
        private const val EXPIRE_TIME_UNIX = "expire_time_unix"

        private const val THREE_HOURS = 10000L
        private const val THREE_DAYS = 240000L
        private const val UNIX_DIVIDER = 1000L
    }
}