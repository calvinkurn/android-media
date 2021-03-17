package com.tokopedia.promotionstarget.presentation

import com.tokopedia.promotionstarget.data.coupon.CouponStatusType
import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.notification.GratifNotification
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType
import com.tokopedia.promotionstarget.data.notification.PopupType
import java.security.MessageDigest
import kotlin.experimental.and

object GratificationAnalyticsHelper {

    fun handleMainCtaClick(userId: String, entryPoint: Int,
                           gratifNotification: GratifNotification?,
                           couponDetailResponse: TokopointsCouponDetailResponse?,
                           screenName: String,
                           autoApplyStatusCode: String?
    ) {
        if (gratifNotification == null || couponDetailResponse == null) return

        val label = createLabel(entryPoint,
                getPopupType(gratifNotification, couponDetailResponse),
                gratifNotification.promoCode,
                gratifNotification.eventID,
                autoApplyStatusCode,
                screenName)

        GratificationAnalytics.userClickMainCtaPush(userId,
                screenName,
                getAction(gratifNotification, couponDetailResponse),
                label)
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

    private fun getAction(gratifNotification: GratifNotification?, couponDetailResponse: TokopointsCouponDetailResponse?): String {
        val popupType = getPopupType(gratifNotification, couponDetailResponse)
        return when (popupType) {
            PopupType.ACTIVE, PopupType.SEEN -> GratifActions.CLICK_YUK_BELANJA_HEMAT
            PopupType.USED, PopupType.EXPIRED -> GratifActions.CLICK_LANJUT_BERBELANJA
            else -> ""
        }
    }

    @PopupType
    fun getPopupType(gratifNotification: GratifNotification?, couponDetailResponse: TokopointsCouponDetailResponse?): Int {
        gratifNotification?.notificationStatus?.let {
            if (it == NotificationStatusType.SEEN) {
                return PopupType.SEEN
            }
        }
        couponDetailResponse?.coupon?.couponStatus?.let {
            when (it) {
                CouponStatusType.ACTIVE -> return PopupType.ACTIVE
                CouponStatusType.EXPIRED -> return PopupType.EXPIRED
                CouponStatusType.USED -> return PopupType.USED
                else -> return PopupType.UNKNOWN
            }
        }
        return PopupType.UNKNOWN
    }

    fun handleClickSecondaryCta(userId: String, entryPoint: Int,
                                gratifNotification: GratifNotification?,
                                couponDetailResponse: TokopointsCouponDetailResponse?,
                                screenName: String) {
        if (gratifNotification == null || couponDetailResponse == null) return

        GratificationAnalytics.userClickSecondaryCtaPush(userId,
                entryPoint,
                getPopupType(gratifNotification, couponDetailResponse),
                gratifNotification.promoCode,
                gratifNotification.eventID,
                screenName
        )
    }

    private fun createLabel(entryPoint: Int,
                            popupType: Int,
                            baseCode: String?,
                            eventId: String?,
                            autoApplyStatus: String?,
                            screenName: String
    ): String {
        val screenNameMD5 = md5(screenName)
        if (autoApplyStatus.isNullOrEmpty()) {
            return "$entryPoint - $popupType - $baseCode - $eventId - $screenNameMD5"
        } else {
            return "$entryPoint - $popupType - $baseCode - $eventId - $autoApplyStatus - $screenNameMD5"
        }

    }

    fun md5(screenName: String): String? {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(screenName.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuilder()
            for (b in messageDigest) {
                hexString.append(String.format("%02x", b and 0xff.toByte()))
            }
            hexString.toString()
        } catch (e: Throwable) {
            e.printStackTrace()
            ""
        }
    }
}