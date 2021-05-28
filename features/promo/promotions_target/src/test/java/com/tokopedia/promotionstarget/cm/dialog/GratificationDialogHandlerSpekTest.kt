package com.tokopedia.promotionstarget.cm.dialog

import android.app.Activity
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.promotionstarget.cm.ActivityProvider
import com.tokopedia.promotionstarget.cm.broadcast.PendingData
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import io.mockk.*
import kotlinx.coroutines.Job
import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.spekframework.spek2.Spek
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

class GratificationDialogHandlerSpekTest: Spek( {

    var entityHashCode = 1
    val inAppId = 10L
    var screenName = javaClass.name
    val gratificationId = "10"

    fun getDialogHandler(): GratificationDialogHandler {
        val gratificationPresenter: GratificationPresenter = mockk()
        val mapOfGratificationJobs: ConcurrentHashMap<Int, Job> = ConcurrentHashMap()
        val mapOfPendingInApp: ConcurrentHashMap<Int, PendingData> = ConcurrentHashMap()
        val broadcastScreenNames = arrayListOf<String>()
        val activityProvider: ActivityProvider = mockk()
        val firebaseRemote: FirebaseRemoteConfigImpl = mockk()
        return GratificationDialogHandler(gratificationPresenter, mapOfGratificationJobs, mapOfPendingInApp, broadcastScreenNames, activityProvider, firebaseRemote, mockk(), mockk())
    }

    //===============showPushDialog========================


    test("show push dialog") {
        val dialogHandler = getDialogHandler()
        val activity: Activity = mockk()
        every {
            dialogHandler.gratificationPresenter.showGratificationInApp(weakActivity = any(), gratificationId = gratificationId, notificationEntryType = NotificationEntryType.PUSH,
                    gratifPopupCallback = any(), screenName = screenName)
        } returns mockk()

        dialogHandler.showPushDialog(activity, gratificationId, screenName)
        verify(exactly = 1) {
            dialogHandler.gratificationPresenter.showGratificationInApp(weakActivity = any(), gratificationId = gratificationId, notificationEntryType = NotificationEntryType.PUSH,
                    gratifPopupCallback = any(), screenName = screenName)
        }
    }

    //===============showOrganicDialog=========================
    //temporary commented because always timeout when want to merge auto PR master 3.126 to release
    /*
    test("show organic dialog when gratification id is present") {
        val dialogHandler = getDialogHandler()
        val weakActivity: WeakReference<Activity> = mockk()

        val gratificationPopupCallback: GratificationPresenter.GratifPopupCallback = mockk()

        every {
            dialogHandler.gratificationPresenter.showGratificationInApp(weakActivity = weakActivity, gratificationId = gratificationId, notificationEntryType = NotificationEntryType.ORGANIC, gratifPopupCallback = gratificationPopupCallback, screenName = screenName, inAppId = inAppId)
        } returns mockk()

        val customValues: JSONObject = JSONObject()
        customValues.put("gratificationId", gratificationId)
        dialogHandler.showOrganicDialog(currentActivity = weakActivity, customValues = customValues.toString(), gratifPopupCallback = gratificationPopupCallback, screenName = screenName, inAppId = inAppId)
        verify(exactly = 1) {
            dialogHandler.gratificationPresenter.showGratificationInApp(weakActivity = weakActivity, gratificationId = gratificationId, notificationEntryType = NotificationEntryType.ORGANIC, gratifPopupCallback = gratificationPopupCallback, screenName = screenName, inAppId = inAppId)
        }
    }
     */


    test("do not show organic dialog when gratification id is not present") {
        val dialogHandler = getDialogHandler()
        val weakActivity: WeakReference<Activity> = mockk()
        val gratificationPopupCallback: GratificationPresenter.GratifPopupCallback = mockk()

        every { gratificationPopupCallback.onExeption(any()) } just runs
        val customValues: JSONObject = JSONObject()
        dialogHandler.showOrganicDialog(currentActivity = weakActivity, customValues = customValues.toString(), gratifPopupCallback = gratificationPopupCallback, screenName = screenName, inAppId = inAppId)
        verify(exactly = 1) {
            gratificationPopupCallback.onExeption(any())
        }

        verify(exactly = 0) {
            dialogHandler.gratificationPresenter.showGratificationInApp(weakActivity = weakActivity, gratificationId = gratificationId, notificationEntryType = NotificationEntryType.ORGANIC, gratifPopupCallback = gratificationPopupCallback, screenName = screenName, inAppId = inAppId)
        }
    }

    //=============handleInAppPopup==============================

    test("handle in app pop up when gratification is disabled") {
        val dialogHandler = getDialogHandler()
        val cmData: CMInApp = mockk()

        mockkStatic(CMInAppManager::class)
        every { CMInAppManager.getInstance().onCMInAppInflateException(cmData) } just runs
        every { dialogHandler.remoteConfigImpl?.getBoolean(IS_GRATIF_DISABLED, false) ?: false } returns true

        dialogHandler.handleInAppPopup(cmData, entityHashCode, screenName)
        verify { dialogHandler.cmInflateException(cmData) }
    }

    //==========handle in app pop up when gratification is active====================

    test("when broadcastScreen has currentScreen name and their hashcode are also same has pending data") {
        val dialogHandler = getDialogHandler()
        val currentActivity: Activity = mockk()
        val weakActivity:WeakReference<Activity?> = WeakReference(currentActivity)
        val cmData: CMInApp = CMInApp()

        val customValues: JSONObject = JSONObject()
        customValues.put("gratificationId", gratificationId)
        cmData.customValues = customValues.toString()
        cmData.id = inAppId

        screenName = currentActivity.javaClass.name
        entityHashCode = currentActivity.hashCode()

        every { dialogHandler.activityProvider.getActivity() } returns weakActivity

        dialogHandler.broadcastScreenNames.add(screenName)

        every { dialogHandler.gratificationPresenter.showGratificationInApp(
                weakActivity = any(),
                gratificationId = gratificationId,
                notificationEntryType = NotificationEntryType.ORGANIC,
                gratifPopupCallback = any(),
                screenName = screenName, inAppId = cmData.id) } returns mockk()

        dialogHandler.mapOfPendingInApp[entityHashCode] = PendingData(true, cmData)

        dialogHandler.handleInAppPopup(cmData, entityHashCode, screenName)

        Assert.assertEquals(dialogHandler.mapOfPendingInApp.size == 0, true)

        verify(exactly = 1) {
             dialogHandler.gratificationPresenter.showGratificationInApp(
                    weakActivity = any(),
                    gratificationId = gratificationId,
                    notificationEntryType = NotificationEntryType.ORGANIC,
                    gratifPopupCallback = any(),
                    screenName = screenName, inAppId = cmData.id) }
    }

    test("when broadcastScreen has currentScreen name and their hashcode are also same and does not have any pending data") {
        val dialogHandler = getDialogHandler()
        val cmData: CMInApp = CMInApp()
        val customValues: JSONObject = JSONObject()
        customValues.put("gratificationId", gratificationId)
        cmData.customValues = customValues.toString()
        cmData.id = inAppId

        //setup
        val currentActivity: Activity = spyk()
        val weakActivity:WeakReference<Activity?> = WeakReference(currentActivity)
        every { dialogHandler.activityProvider.getActivity() } returns weakActivity

        screenName = currentActivity.javaClass.name

        entityHashCode = currentActivity.hashCode()
        dialogHandler.broadcastScreenNames.add(screenName)

        //call
        dialogHandler.handleInAppPopup(cmData, entityHashCode, screenName)

        Assert.assertEquals(dialogHandler.mapOfPendingInApp[entityHashCode]!=null, true)

        verify(exactly = 0) {
            dialogHandler.gratificationPresenter.showGratificationInApp(
                    weakActivity = any(),
                    gratificationId = any(),
                    notificationEntryType = any(),
                    gratifPopupCallback = any(),
                    screenName = any(), inAppId = any()) }
    }


    test("when broadcastScreen does not have currentScreen name or hashcode are different") {
        val dialogHandler = getDialogHandler()
        val currentActivity: Activity = mockk()
        val weakActivity:WeakReference<Activity?> = WeakReference(currentActivity)
        val cmData: CMInApp = CMInApp()

        val customValues: JSONObject = JSONObject()
        customValues.put("gratificationId", gratificationId)
        cmData.customValues = customValues.toString()
        cmData.id = inAppId

        every { dialogHandler.activityProvider.getActivity() } returns weakActivity

        every { dialogHandler.gratificationPresenter.showGratificationInApp(
                weakActivity = any(),
                gratificationId = gratificationId,
                notificationEntryType = NotificationEntryType.ORGANIC,
                gratifPopupCallback = any(),
                screenName = screenName, inAppId = cmData.id) } returns mockk()

        dialogHandler.handleInAppPopup(cmData, entityHashCode, screenName)

        verify(exactly = 1) {
            dialogHandler.gratificationPresenter.showGratificationInApp(
                    weakActivity = any(),
                    gratificationId = gratificationId,
                    notificationEntryType = NotificationEntryType.ORGANIC,
                    gratifPopupCallback = any(),
                    screenName = screenName, inAppId = cmData.id) }
    }
})