package com.tokopedia.inbox.fake

import com.tokopedia.inbox.common.AndroidFileUtil
import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifOrderListUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterCacheManager
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterDetailUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterFilterV2UseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.recom.FakeGetRecommendationUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.topads.FakeTopAdsRepository
import com.tokopedia.inbox.test.R
import com.tokopedia.notifcenter.data.entity.filter.NotifcenterFilterResponse
import com.tokopedia.notifcenter.data.entity.orderlist.NotifOrderListResponse
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.topads.sdk.domain.model.TopAdsmageViewResponse
import javax.inject.Inject

class InboxNotifcenterFakeDependency {

    @Inject
    lateinit var userSession: FakeUserSession

    @Inject
    lateinit var cacheManager: FakeNotifcenterCacheManager

    @Inject
    lateinit var notifOrderListUseCase: FakeNotifOrderListUseCase

    @Inject
    lateinit var notifcenterDetailUseCase: FakeNotifcenterDetailUseCase

    @Inject
    lateinit var notifcenterFilterUseCase: FakeNotifcenterFilterV2UseCase

    @Inject
    lateinit var topAdsRepository: FakeTopAdsRepository

    @Inject
    lateinit var getRecommendationUseCase: FakeGetRecommendationUseCase

    fun init() {
        initResponse()
    }

    private fun initResponse() {
        getRecommendationUseCase.response = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_recom, RecommendationEntity::class.java
        )
        topAdsRepository.response = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_tdn, TopAdsmageViewResponse::class.java
        )
        notifcenterFilterUseCase.response = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_filter_v2, NotifcenterFilterResponse::class.java
        )
        notifOrderListUseCase.response = AndroidFileUtil.parseRaw(
            R.raw.notifcenter_notif_order_list, NotifOrderListResponse::class.java
        )
    }

}