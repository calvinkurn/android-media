package com.tokopedia.inbox.fake.domain.usecase.notifcenter

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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

    val noTrackHistoryWidget: NotifcenterDetailResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_detail_v3_no_track_history_widget,
            NotifcenterDetailResponse::class.java
        )

    val defaultResponse: NotifcenterDetailResponse
        get() = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_detail_v3,
            NotifcenterDetailResponse::class.java
        )

    init {
        response = response
    }

    fun initialize() {
        this.response = defaultResponse
    }


}