package com.tokopedia.inbox.fake

import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.*
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

    @Inject
    lateinit var addWishlistUseCase: FakeNotifCenterAddWishlist

    fun init() {
        initResponse()
        initCacheManager()
    }

    private fun initResponse() {
        notifcenterDetailUseCase.initialize()
        getRecommendationUseCase.initialize()
        topAdsRepository.initialize()
        notifcenterFilterUseCase.initialize()
        notifOrderListUseCase.initialize()
    }

    private fun initCacheManager() {
        cacheManager.clearCache()
    }

    fun tearDown() {
        cacheManager.clearCache()
    }

}