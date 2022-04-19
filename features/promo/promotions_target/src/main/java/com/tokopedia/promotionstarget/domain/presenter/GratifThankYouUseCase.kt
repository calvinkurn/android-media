package com.tokopedia.promotionstarget.domain.presenter

import com.tokopedia.promotionstarget.data.coupon.TokopointsCouponDetailResponse
import com.tokopedia.promotionstarget.data.di.components.DaggerCmGratificationPresenterComponent
import com.tokopedia.promotionstarget.data.notification.GratifNotification
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.usecase.NotificationUseCase
import com.tokopedia.promotionstarget.domain.usecase.TokopointsCouponDetailUseCase
import javax.inject.Inject

class GratifThankYouUseCase @Inject constructor() {

    @Inject
    lateinit var notificationUseCase: NotificationUseCase

    @Inject
    lateinit var tpCouponDetailUseCase: TokopointsCouponDetailUseCase

    init {
        DaggerCmGratificationPresenterComponent.builder()
                .build()
                .inject(this)
    }

    suspend fun getResponse(notificationID: Int, paymentID: Long): Pair<GratifNotification, TokopointsCouponDetailResponse>? {
        val map = notificationUseCase.getQueryParams(notificationID, NotificationEntryType.ORGANIC, paymentID)
        val notifResponse = notificationUseCase.getResponse(map)
        val reason = notifResponse.response?.resultStatus?.code
        if (reason == GratifResultStatus.SUCCESS) {
            val code = notifResponse.response.promoCode
            if (!code.isNullOrEmpty()) {
                val couponDetail = tpCouponDetailUseCase.getResponse(tpCouponDetailUseCase.getQueryParams(code))
                val couponStatus = couponDetail?.coupon?.realCode ?: ""
                if (couponStatus.isNotEmpty()) {
                    return Pair(notifResponse.response, couponDetail)
                }
            }
        }
        return null
    }
}