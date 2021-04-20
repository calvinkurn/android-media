package com.tokopedia.promotionstarget.cm.dialog

import android.app.Activity
import android.content.DialogInterface
import com.tokopedia.notifications.inApp.DialogHandlerContract
import com.tokopedia.notifications.inApp.ruleEngine.interfaces.DataConsumer
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.notifications.inApp.viewEngine.CmInAppListener
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import java.lang.ref.WeakReference

class GratifPopupCallbackProvider(val dataConsumer: DataConsumer) {

    fun getCallbackTypePush(gratificationPresenter: GratificationPresenter, tempWeakActivity: WeakReference<Activity>): GratificationPresenter.AbstractGratifPopupCallback {
        return object : GratificationPresenter.AbstractGratifPopupCallback() {
            override fun onIgnored(reason: Int) {
                super.onIgnored(reason)
                tempWeakActivity.get()?.let {
                    gratificationPresenter.dialogVisibilityContract?.onDialogDismiss(it)
                }
            }
        }
    }

    fun getCallbackTypeOrganic(dialogHandlerContract: DialogHandlerContract?,
                               data: CMInApp,
                               cmInAppListener: CmInAppListener): GratificationPresenter.AbstractGratifPopupCallback {
        return object : GratificationPresenter.AbstractGratifPopupCallback() {
            override fun onShow(dialog: DialogInterface) {
                dialogHandlerContract?.dataConsumed(data)
            }

            override fun onDismiss(dialog: DialogInterface, @NotificationEntryType notificationEntryType: Int) {
                super.onDismiss(dialog, notificationEntryType)
                if(notificationEntryType == NotificationEntryType.ORGANIC){
                    cmInAppListener.onCMinAppDismiss(data)
                    dataConsumer.interactedWithView(data.id)
                }
            }

            override fun onIgnored(reason: Int) {
                //Do nothing
            }

            override fun onExeption(ex: Exception) {
                dialogHandlerContract?.cmInflateException(data)
            }
        }
    }
}