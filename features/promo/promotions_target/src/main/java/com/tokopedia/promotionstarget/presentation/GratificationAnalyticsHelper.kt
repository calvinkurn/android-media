package com.tokopedia.promotionstarget.presentation

import com.tokopedia.promotionstarget.data.coupon.CouponStatusType
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.notification.GratifNotification
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType

object GratificationAnalyticsHelper {

    fun handleMainCtaClick(userId: String, entryPoint: Int,
                           gratifNotification: GratifNotification?,
                           couponDetailResponse: TokopointsCouponDetailResponse?,
                           screenName: String
    ) {
        if (gratifNotification == null || couponDetailResponse == null) return

        GratificationAnalytics.userClickMainCtaPush(userId,
                entryPoint,
                getPopupType(gratifNotification, couponDetailResponse),
                gratifNotification.promoCode,
                gratifNotification.eventID,
                screenName,
                getActionForDisplay(gratifNotification, couponDetailResponse)
        )
    }

    fun handleDismiss(userId: String, entryPoint: Int,
                      gratifNotification: GratifNotification?,
                      couponDetailResponse: TokopointsCouponDetailResponse?,
                      screenName: String) {
        if (gratifNotification == null || couponDetailResponse == null) return

        GratificationAnalytics.userDismissPopup(userId,
                entryPoint,
                getPopupType(gratifNotification, couponDetailResponse),
                gratifNotification.promoCode,
                gratifNotification.eventID,
                screenName
        )
    }

    fun handleClickSecondaryCtaPush(userId: String, entryPoint: Int,
                                    gratifNotification: GratifNotification?,
                                    couponDetailResponse: TokopointsCouponDetailResponse?,
                                    screenName: String){
        if (gratifNotification == null || couponDetailResponse == null) return

        GratificationAnalytics.userClickSecondaryCtaPush(userId,
                entryPoint,
                getPopupType(gratifNotification, couponDetailResponse),
                gratifNotification.promoCode,
                gratifNotification.eventID,
                screenName
        )
    }

    private fun getPopupType(gratifNotification: GratifNotification, couponDetailResponse: TokopointsCouponDetailResponse): String {
        return "${gratifNotification.notificationStatus}x${couponDetailResponse.coupon?.couponStatus}"
    }

    private fun getActionForDisplay(gratifNotification: GratifNotification, couponDetailResponse: TokopointsCouponDetailResponse): String {
        //regular
        if (gratifNotification.notificationStatus == NotificationStatusType.ACTIVE_PUSH_NOTIF && couponDetailResponse.coupon?.couponStatus == CouponStatusType.ACTIVE) {
            return GratifActions.CLICK_YUK_BELANJA_HEMAT
        }

        //used
        if (couponDetailResponse.coupon?.couponStatus == CouponStatusType.USED) {
            return GratifActions.CLICK_LANJUT_BERBELANJA
        }

        //expired
        if (couponDetailResponse.coupon?.couponStatus == CouponStatusType.EXPIRED) {
            return GratifActions.CLICK_LANJUT_BERBELANJA
        }

        //seen
        if (gratifNotification.notificationStatus == NotificationStatusType.SEEN) {
            return GratifActions.CLICK_YUK_BELANJA_HEMAT
        }

        return ""

    }
}