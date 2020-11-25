package com.tokopedia.notifications.inApp.viewEngine

import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.notifications.inApp.CMInAppManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CmInAppBundleConvertorTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockkStatic(CmInAppBundleConvertor::class)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
    }

    @Test
    fun getCMInAppWithoutNotificationId() {

        val remoteMessage: RemoteMessage = mockk()
        val map = HashMap<String, String>()

        every { remoteMessage.data } returns map

        Assert.assertEquals(null, CmInAppBundleConvertor.getCmInApp(remoteMessage))
    }

    @Test
    fun getCMInAppWithoutScreen() {
        val remoteMessage: RemoteMessage = mockk()
        val map = HashMap<String, String>()
        map["notificationId"] = "12313"

        every { remoteMessage.data } returns map

        Assert.assertEquals(null, CmInAppBundleConvertor.getCmInApp(remoteMessage))
    }

    @Test
    fun getCMInAppWithoutUI() {
        val remoteMessage: RemoteMessage = mockk()
        val map = HashMap<String, String>()
        map["notificationId"] = "12313"
        map["s"] = "12313"

        every { remoteMessage.data } returns map

        Assert.assertEquals(null, CmInAppBundleConvertor.getCmInApp(remoteMessage))

    }

    /*@Test
    fun getCMInAppWithRequiredParams() {
        val remoteMessage: RemoteMessage = mockk()
        val map = HashMap<String, String>()
        map["notificationId"] = "12313"
        map["s"] = "12313"
        map["ui"] = "{}"
        every { remoteMessage.data } returns map
        every { CMInAppManager.getInstance() } returns spyk()
        every { CMInAppManager.getInstance().getCmInAppEndTimeInterval() } returns 60480000

        Assert.assertNotEquals(null, CmInAppBundleConvertor.getCmInApp(remoteMessage))

    }*/
}