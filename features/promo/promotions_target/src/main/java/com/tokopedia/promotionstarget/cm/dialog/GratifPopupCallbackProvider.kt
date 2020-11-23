package com.tokopedia.promotionstarget.cm.dialog

import android.app.Activity
import android.content.DialogInterface
import com.tokopedia.notifications.inApp.DialogHandlerContract
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import com.tokopedia.promotionstarget.domain.presenter.GratifPopupIngoreType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import java.lang.ref.WeakReference

class GratifPopupCallbackProvider {

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

    fun getCallbackTypeOrganic(dialogHandlerContract: DialogHandlerContract?, data: CMInApp): GratificationPresenter.AbstractGratifPopupCallback {
        return object : GratificationPresenter.AbstractGratifPopupCallback() {
            override fun onShow(dialog: DialogInterface) {
                dialogHandlerContract?.dataConsumed(data)
            }

            override fun onIgnored(reason: Int) {
                if (reason != GratifPopupIngoreType.DIALOG_ALREADY_ACTIVE) {
                    dialogHandlerContract?.dataConsumed(data)
                }
            }

            override fun onExeption(ex: Exception) {
                dialogHandlerContract?.dataConsumed(data)
                dialogHandlerContract?.cmInflateException(data)
            }
        }
    }
}