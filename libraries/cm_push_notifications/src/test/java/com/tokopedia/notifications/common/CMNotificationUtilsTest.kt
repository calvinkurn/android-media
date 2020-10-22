package com.tokopedia.notifications.common

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class CMNotificationUtilsTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun `isTokenExpired fail case`() {
        mockkStatic(CMNotificationUtils::class)
        val cacheHandler = mockk<CMNotificationCacheHandler>()

        every { cacheHandler.getStringValue(CMConstant.FCM_TOKEN_CACHE_KEY) } returns "5646456sdfsdfsdf"
        every { cacheHandler.getStringValue(CMConstant.USERID_CACHE_KEY) } returns "1235645"
        every { cacheHandler.getStringValue(CMConstant.GADSID_CACHE_KEY) } returns "sdfsdfhsduifhuiasdfhasduifhasui"
        every { cacheHandler.getStringValue(CMConstant.APP_VERSION_CACHE_KEY) } returns "3.91"

        assertFalse(CMNotificationUtils.isTokenExpired(cacheHandler, "5646456sdfsdfsdf", "1235645"
        , "sdfsdfhsduifhuiasdfhasduifhasui", "3.91"))
    }

    @Test
    fun `isTokenExpired tokenUpdateRequired success case`() {
        mockkStatic(CMNotificationUtils::class)
        val cacheHandler = mockk<CMNotificationCacheHandler>()

        every { cacheHandler.getStringValue(CMConstant.FCM_TOKEN_CACHE_KEY) } returns "sdfsdfsdgfsdgr"

        assertTrue(CMNotificationUtils.isTokenExpired(cacheHandler, "5646456sdfsdfsdf", "1235645"
        , "sdfsdfhsduifhuiasdfhasduifhasui", "3.91"))
    }

    @Test
    fun `isTokenExpired mapTokenWithUserRequired success case`() {
        mockkStatic(CMNotificationUtils::class)
        val cacheHandler = mockk<CMNotificationCacheHandler>()

        every { cacheHandler.getStringValue(CMConstant.FCM_TOKEN_CACHE_KEY) } returns "5646456sdfsdfsdf"
        every { cacheHandler.getStringValue(CMConstant.USERID_CACHE_KEY) } returns "sdfsdfsdgfsdgr"

        assertTrue(CMNotificationUtils.isTokenExpired(cacheHandler, "5646456sdfsdfsdf", "1235645"
        , "sdfsdfhsduifhuiasdfhasduifhasui", "3.91"))
    }

    @Test
    fun `isTokenExpired mapTokenWithGAdsIdRequired success case`() {
        mockkStatic(CMNotificationUtils::class)
        val cacheHandler = mockk<CMNotificationCacheHandler>()

        every { cacheHandler.getStringValue(CMConstant.FCM_TOKEN_CACHE_KEY) } returns "5646456sdfsdfsdf"
        every { cacheHandler.getStringValue(CMConstant.USERID_CACHE_KEY) } returns "1235645"
        every { cacheHandler.getStringValue(CMConstant.GADSID_CACHE_KEY) } returns "5646456sdfsdfsdf"

        assertTrue(CMNotificationUtils.isTokenExpired(cacheHandler, "5646456sdfsdfsdf", "1235645"
        , "sdfsdfhsduifhuiasdfhasduifhasui", "3.91"))
    }

    @Test
    fun `isTokenExpired mapTokenWithAppVersionRequired success case`() {
        mockkStatic(CMNotificationUtils::class)
        val cacheHandler = mockk<CMNotificationCacheHandler>()

        every { cacheHandler.getStringValue(CMConstant.FCM_TOKEN_CACHE_KEY) } returns "5646456sdfsdfsdf"
        every { cacheHandler.getStringValue(CMConstant.USERID_CACHE_KEY) } returns "1235645"
        every { cacheHandler.getStringValue(CMConstant.GADSID_CACHE_KEY) } returns "sdfsdfhsduifhuiasdfhasduifhasui"
        every { cacheHandler.getStringValue(CMConstant.APP_VERSION_CACHE_KEY) } returns ""

        assertTrue(CMNotificationUtils.isTokenExpired(cacheHandler, "5646456sdfsdfsdf", "1235645"
        , "sdfsdfhsduifhuiasdfhasduifhasui", "3.91"))
    }
}