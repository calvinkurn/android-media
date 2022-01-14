package com.tokopedia.promotionstarget.cm.dialog

import android.app.Activity
import android.content.DialogInterface
import com.tokopedia.notifications.inApp.external.IExternalInAppCallback
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import java.lang.ref.WeakReference

class GratifPopupCallbackProvider(val iExternalInAppCallback: IExternalInAppCallback?) {

    fun getCallbackTypePush(gratificationPresenter: GratificationPresenter, tempWeakActivity: WeakReference<Activity>): GratificationPresenter.AbstractGratifPopupCallback {
        return object : GratificationPresenter.AbstractGratifPopupCallback() {
            override fun onIgnored(reason: Int) {
                super.onIgnored(reason)
                tempWeakActivity.get()?.let {
                    iExternalInAppCallback?.onInAppViewDismiss(it)
                }
            }
        }
    }

    fun getCallbackTypeOrganic(data: CMInApp): GratificationPresenter.AbstractGratifPopupCallback {
        return object : GratificationPresenter.AbstractGratifPopupCallback() {
            override fun onShow(dialog: DialogInterface) {
                iExternalInAppCallback?.onInAppDataConsumed(data)
            }

            override fun onDismiss(dialog: DialogInterface, @NotificationEntryType notificationEntryType: Int) {
                super.onDismiss(dialog, notificationEntryType)
                if(notificationEntryType == NotificationEntryType.ORGANIC){
                    iExternalInAppCallback?.onInAppViewDismiss(data)
                    iExternalInAppCallback?.onUserInteractedWithInAppView(data)
                }
            }

            override fun onIgnored(reason: Int) {
                //Do nothing
            }

            override fun onExeption(ex: Exception) {
                iExternalInAppCallback?.onCMInAppInflateException(data)
            }
        }
    }
}