package com.tokopedia.notifcenter.stub.data.response

import com.tokopedia.notifcenter.data.entity.NotificationResponse
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.entity.notification.NotifcenterDetailResponse
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse

object GqlResponseStub {

    lateinit var notificationDetailResponse: ResponseStub<NotifcenterDetailResponse>
    lateinit var notificationFilterResponse: ResponseStub<NotifcenterFilterResponse>
    lateinit var notificationOrderListResponse: ResponseStub<NotifOrderListResponse>
    lateinit var notificationCounterResponse: ResponseStub<NotificationResponse>

    init {
        reset()
    }

    fun reset() {
        notificationDetailResponse = ResponseStub(
            filePath = "detail/notifcenter_detail_v3.json",
            type = NotifcenterDetailResponse::class.java,
            query = "notifcenter_detail_v3",
            isError = false
        )
        notificationFilterResponse = ResponseStub(
            filePath = "filter/notifcenter_filter_v2.json",
            type = NotifcenterFilterResponse::class.java,
            query = "notifcenter_filter_v2",
            isError = false
        )
        notificationOrderListResponse = ResponseStub(
            filePath = "filter/notifcenter_notif_order_list.json",
            type = NotifOrderListResponse::class.java,
            query = "notifcenter_notifOrderList",
            isError = false
        )
        notificationCounterResponse = ResponseStub(
            filePath = "counter/notifcenter_counter.json",
            type = NotificationResponse::class.java,
            query = "notifications",
            isError = false
        )
    }
}
