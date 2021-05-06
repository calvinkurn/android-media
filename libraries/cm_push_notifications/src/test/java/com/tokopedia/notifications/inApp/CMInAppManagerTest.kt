package com.tokopedia.notifications.inApp

import android.app.Activity
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.notifications.inApp.ruleEngine.RulesManager
import com.tokopedia.notifications.inApp.ruleEngine.repository.RepositoryManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.viewEngine.CmInAppBundleConvertor
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.NullPointerException

class CMInAppManagerTest {

    private val remoteMessage: RemoteMessage = mockk(relaxed = true)
    private val cmInAppManager:CMInAppManager = spyk(CMInAppManager.getInstance())
    private val cmActivityLifecycleHandler :CmActivityLifecycleHandler = spyk(CmActivityLifecycleHandler(cmInAppManager, PushIntentHandler(), cmInAppManager, cmInAppManager))

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    /*@Test
    fun handlePushPayloadTestWithoutNullCMInApp() {

        mockkStatic(CmInAppBundleConvertor::class)
        mockkStatic(RepositoryManager::class)
        val cmInApp = CMInApp()
        every { CmInAppBundleConvertor.getCmInApp(any()) } returns cmInApp
        every { RepositoryManager.getInstance().storageProvider.putDataToStore(cmInApp) }

        cmInAppManager.handlePushPayload(remoteMessage)

        verify { RepositoryManager.getInstance().storageProvider.putDataToStore(cmInApp) }
    }*/

    @Test
    fun handlePushPayloadTestWithNullCMInApp() {

        mockkStatic(CmInAppBundleConvertor::class)
        mockkStatic(RepositoryManager::class)
        val cmInApp = CMInApp()
        every { CmInAppBundleConvertor.getCmInApp(any()) } returns null

        cmInAppManager.handlePushPayload(remoteMessage)

        verify(inverse = true) { RepositoryManager.getInstance().storageProvider.putDataToStore(cmInApp) }
    }

    @Test
    fun handlePushPayloadTestWithException() {

        mockkStatic(CmInAppBundleConvertor::class)
        mockkStatic(RepositoryManager::class)
        val cmInApp = CMInApp()
        every { CmInAppBundleConvertor.getCmInApp(any()) } throws NullPointerException()

        cmInAppManager.handlePushPayload(remoteMessage)

        verify(inverse = true) { RepositoryManager.getInstance().storageProvider.putDataToStore(cmInApp) }
    }

    @Test
    fun onActivityStartedInternal() {

        val activity: Activity = mockk()
        mockkStatic(RulesManager::class)
        every { RulesManager.getInstance() } returns mockk(relaxed = true)
        every { RulesManager.getInstance().checkValidity(any(), any(), any(), any(), any()) } returns Unit
        every { activity.application } returns mockk(relaxed = true)

        cmActivityLifecycleHandler.onActivityStartedInternal(activity)

        verify { RulesManager.getInstance().checkValidity(any(), any(), any(), any(), any()) }
    }

}