package com.tokopedia.promotionstarget.domain.presenter

import android.app.Activity
import android.content.DialogInterface
import android.os.Debug
import android.util.Log
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
        val curTime = System.currentTimeMillis()
        val TAG = "GratifTag"
        Log.e(TAG, "gratification show dialog start time seconds:${curTime / 1000}")
        return CmGratificationDialog().show(activity,
                gratifNotification,
                couponDetail,
                notificationEntryType,
                DialogInterface.OnShowListener { dialog ->
                    if (dialog != null) {
                        gratifPopupCallback?.onShow(dialog)
                    }
                    Debug.stopMethodTracing()
                    val endTime = System.currentTimeMillis()
                    Log.e(TAG, "gratification show dialog finish time seconds:${endTime / 1000}")
                }, screenName, closeCurrentActivity, inAppId)
    }
}