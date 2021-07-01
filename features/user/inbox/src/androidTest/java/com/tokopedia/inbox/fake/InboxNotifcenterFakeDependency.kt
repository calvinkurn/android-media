package com.tokopedia.inbox.fake

import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifOrderListUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterCacheManager
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterDetailUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterFilterV2UseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.recom.FakeGetRecommendationUseCase
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.topads.FakeTopAdsRepository
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
        notifcenterDetailUseCase.initialize()
        getRecommendationUseCase.initialize()
        topAdsRepository.initialize()
        notifcenterFilterUseCase.initialize()
        notifOrderListUseCase.initialize()
    }

}