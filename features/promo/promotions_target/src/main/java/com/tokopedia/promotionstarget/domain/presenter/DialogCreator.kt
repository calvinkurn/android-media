package com.tokopedia.promotionstarget.domain.presenter

import android.app.Activity
import android.content.DialogInterface
import android.os.Debug
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.notification.GratifNotification
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.presentation.ui.dialog.CmGratificationDialog

class DialogCreator {

    fun createGratifDialog(activity: Activity,
                           gratifNotification: GratifNotification,
                           couponDetail: TokopointsCouponDetailResponse,
                           @NotificationEntryType notificationEntryType: Int,
                           gratifPopupCallback: GratificationPresenter.GratifPopupCallback? = null,
                           screenName: String,
                           closeCurrentActivity: Boolean,
                           inAppId: Long?): BottomSheetDialog? {
        return CmGratificationDialog().show(activity,
                gratifNotification,
                couponDetail,
                notificationEntryType,
                DialogInterface.OnShowListener { dialog ->
                    if (dialog != null) {
                        gratifPopupCallback?.onShow(dialog)
                    }
                    Debug.stopMethodTracing()
                }, screenName, closeCurrentActivity, inAppId)
    }
}