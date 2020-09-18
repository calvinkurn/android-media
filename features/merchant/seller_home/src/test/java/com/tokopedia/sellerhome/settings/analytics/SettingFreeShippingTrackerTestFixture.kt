package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before

open class SettingFreeShippingTrackerTestFixture {

    private lateinit var analytics: Analytics
    private lateinit var userSession: UserSessionInterface
    protected lateinit var tracker: SettingFreeShippingTracker

    @Before
    fun setUp() {
        analytics = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        tracker = SettingFreeShippingTracker(analytics, userSession)
    }

    protected fun onGetIsGoldMerchant_thenReturn(isGoldMerchant: Boolean) {
        every { userSession.isGoldMerchant } returns isGoldMerchant
    }

    protected fun onGetUserId_thenReturn(userId: String) {
        every { userSession.userId } returns userId
    }

    protected fun onGetShopId_thenReturn(shopId: String) {
        every { userSession.shopId } returns shopId
    }

    protected fun verifySendGeneralEvent(event: Map<String, Any>) {
        verify { analytics.sendGeneralEvent(event) }
    }
}