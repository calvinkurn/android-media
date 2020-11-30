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
import org.spekframework.spek2.Spek
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

class GratificationDialogHandlerTest : Spek({

    fun getDialogHandler(): GratificationDialogHandler {
        val gratificationPresenter: GratificationPresenter = mockk()
        val mapOfGratificationJobs: ConcurrentHashMap<Int, Job> = ConcurrentHashMap()
        val mapOfPendingInApp: ConcurrentHashMap<Int, PendingData> = ConcurrentHashMap()
        val broadcastScreenNames = arrayListOf<String>()
        val activityProvider: ActivityProvider = mockk()
        val firebaseRemote: FirebaseRemoteConfigImpl = mockk()
        return GratificationDialogHandler(gratificationPresenter, mapOfGratificationJobs, mapOfPendingInApp, broadcastScreenNames, activityProvider, firebaseRemote)
    }

    //===============showPushDialog========================

    test("show push dialog") {
        val dialogHandler = getDialogHandler()
        val gratificationId = "1"
        val screenName = javaClass.name
        val activity: Activity = mockk()
        every {
            dialogHandler.gratificationPresenter.showGratificationInApp(any(), gratificationId, NotificationEntryType.PUSH,
                    any(), screenName)
        } returns mockk()

        dialogHandler.showPushDialog(activity, gratificationId, screenName)
        verifyOrder {
            dialogHandler.gratificationPresenter.showGratificationInApp(any(), gratificationId, NotificationEntryType.PUSH,
                    any(), screenName)
        }
    }

    //===============showOrganicDialog=========================

    group("show organic dialog") {
        val dialogHandler = getDialogHandler()
        val screenName = javaClass.name
        val weakActivity: WeakReference<Activity> = mockk()

        val gratificationId = "1"
        val inAppId = 10L
        val gratificationPopupCallback: GratificationPresenter.GratifPopupCallback = mockk()
        every { gratificationPopupCallback.onExeption(any()) } just runs

        every {
            dialogHandler.gratificationPresenter.showGratificationInApp(weakActivity, gratificationId, NotificationEntryType.ORGANIC, gratificationPopupCallback, screenName)
        } returns mockk()

        test("gratification id is present") {
            val customValues: JSONObject = JSONObject()
            customValues.put("gratificationId", gratificationId)
            dialogHandler.showOrganicDialog(weakActivity, customValues.toString(), gratificationPopupCallback, screenName, inAppId)
            verifyOrder {
                dialogHandler.gratificationPresenter.showGratificationInApp(weakActivity, gratificationId, NotificationEntryType.ORGANIC, gratificationPopupCallback, screenName)
            }

        }
        test("when gratification id is not present then handle exception") {
            val customValues: JSONObject = JSONObject()
            dialogHandler.showOrganicDialog(weakActivity, customValues.toString(), gratificationPopupCallback, screenName, inAppId)
            verifyOrder {
                gratificationPopupCallback.onExeption(any())
            }
            verify(exactly = 0) {
                dialogHandler.gratificationPresenter.showGratificationInApp(weakActivity, gratificationId, NotificationEntryType.ORGANIC, gratificationPopupCallback, screenName)
            }
        }
    }

    //=============handleInAppPopup==============================

    group("handle in app pop up") {
        val dialogHandler = getDialogHandler()
        val cmData: CMInApp = mockk()
        val entityHashCode = 1
        val screenName = javaClass.name

        mockkStatic(CMInAppManager::class)
        every { CMInAppManager.getInstance().onCMInAppInflateException(cmData) } just runs


        test("when gratification is disabled") {
            every { dialogHandler.remoteConfigImpl?.getBoolean(IS_GRATIF_DISABLED, false) ?: false } returns true
            verify { dialogHandler.cmInflateException(cmData) }

            dialogHandler.handleInAppPopup(cmData, entityHashCode, screenName)
        }

        group("when gratification is active") {

            val currentActivity: Activity = mockk()
            every { dialogHandler.activityProvider.getActivity()?.get() } returns currentActivity
//            every { dialogHandler.handleShowOrganic(currentActivity, cmData, entityHashCode, screenName) } just runs

            group("when broadcastScreen has currentScreen name and their hashcode are also same") {
                dialogHandler.broadcastScreenNames.add(javaClass.name)
                every { currentActivity.hashCode() } returns entityHashCode

                test("has pending data") {
                    dialogHandler.mapOfPendingInApp[entityHashCode] = PendingData(true, cmData)

                    verifyOrder {
                        dialogHandler.mapOfPendingInApp.remove(entityHashCode)
                        dialogHandler.handleShowOrganic(currentActivity, cmData, entityHashCode, screenName)
                    }
                }

                test("does not have any pending data") {
                    verifyOrder {
                        dialogHandler.mapOfPendingInApp.contains(entityHashCode)
                    }
                }
            }

            test("when broadcastScreen does not have currentScreen name or hashcode are different") {
                verify { dialogHandler.handleShowOrganic(currentActivity, cmData, entityHashCode, screenName) }
            }
            dialogHandler.handleInAppPopup(cmData, entityHashCode, screenName)
        }
    }
})