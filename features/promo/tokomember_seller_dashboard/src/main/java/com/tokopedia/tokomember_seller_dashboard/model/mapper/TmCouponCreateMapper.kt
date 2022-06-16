package com.tokopedia.tokomember_seller_dashboard.model.mapper

import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponCreateRequest
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmMerchantCouponUnifyRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmSingleCouponData
import com.tokopedia.tokomember_seller_dashboard.util.ANDROID
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil
import java.util.*

object TmCouponCreateMapper {

    fun mapCreateData(
        couponPremiumData: TmSingleCouponData?,
        couponVip: TmSingleCouponData?,
        tmCouponPremiumUploadId: String,
        tmCouponVipUploadId: String,
        tmStartDateUnix: String,
        tmEndDateUnix: String,
        tmStartTimeUnix: String,
        tmEndTimeUnix: String,
        token:String,
        imageSquare:String,
        imagePortrait: String,
        maximumBenefit: Int
    ): TmMerchantCouponUnifyRequest {
        //Mutation Mapper

        val voucherList = arrayListOf<TmCouponCreateRequest>()
        voucherList.add(0, TmCouponCreateRequest(
            benefitIdr = maximumBenefit,
            benefitMax = couponPremiumData?.maxCashback.toIntSafely(),
            targetBuyer = 3,
            image = tmCouponPremiumUploadId,
            couponType = couponPremiumData?.typeCoupon,
            minPurchase = couponPremiumData?.minTransaki.toIntSafely(),
            minimumTierLevel = 1,
            benefitPercent = couponPremiumData?.cashBackPercentage,
            quota = couponPremiumData?.quota.toIntSafely(),
            imagePortrait = imagePortrait,
            imageSquare = imageSquare,
            isPublic = 0,
            hourStart = tmStartTimeUnix,
            dateStart = tmStartDateUnix,
            hourEnd = tmEndTimeUnix,
            dateEnd = tmEndDateUnix,
            benefitType = couponPremiumData?.typeCashback
        ))
        voucherList.add(1, TmCouponCreateRequest(
            benefitIdr = maximumBenefit,
            benefitMax = couponVip?.maxCashback.toIntSafely(),
            targetBuyer = 3,
            image = tmCouponVipUploadId,
            couponType = couponVip?.typeCoupon,
            minPurchase = couponVip?.minTransaki.toIntSafely(),
            minimumTierLevel = 1,
            benefitPercent = couponVip?.cashBackPercentage,
            quota = couponVip?.quota.toIntSafely(),
            imagePortrait = imagePortrait,
            imageSquare = imageSquare,
            isPublic = 0,
            hourStart = tmStartTimeUnix,
            dateStart = tmStartDateUnix,
            hourEnd = tmEndTimeUnix,
            dateEnd = tmEndDateUnix,
            benefitType = couponVip?.typeCashback
        ))

        return TmMerchantCouponUnifyRequest(
            token = token,
            source = ANDROID,
            status = "",
            voucher = voucherList
        )
    }

    fun mapCreateDataSingle(
        couponPremiumData: TmSingleCouponData?,
        tmCouponPremiumUploadId: String,
        tmStartDateUnix: Calendar?,
        tmEndDateUnix: Calendar?,
        tmStartTimeUnix: Calendar?,
        tmEndTimeUnix: Calendar?,
        token:String,
        imageSquare:String,
        imagePortrait: String,
        maximumBenefit: Int
    ): TmMerchantCouponUnifyRequest {
        //Mutation Mapper

        val voucherList = arrayListOf<TmCouponCreateRequest>()
        voucherList.add(0, TmCouponCreateRequest(
            benefitIdr = maximumBenefit,
            benefitMax = couponPremiumData?.maxCashback.toIntSafely(),
            targetBuyer = 3,
            image = tmCouponPremiumUploadId,
            couponType = couponPremiumData?.typeCoupon,
            minPurchase = couponPremiumData?.minTransaki.toIntSafely(),
            minimumTierLevel = 1,
            benefitPercent = couponPremiumData?.cashBackPercentage,
            quota = couponPremiumData?.quota.toIntSafely(),
            imagePortrait = imagePortrait,
            imageSquare = imageSquare,
            isPublic = 0,
            hourStart = tmStartTimeUnix?.let { TmDateUtil.getTimeFromUnix(it) },
            dateStart = tmStartDateUnix?.let { TmDateUtil.getDateFromUnix(it) },
            hourEnd = tmEndTimeUnix?.let { TmDateUtil.getTimeFromUnix(it) },
            dateEnd = tmEndDateUnix?.let { TmDateUtil.getDateFromUnix(it) },
            benefitType = couponPremiumData?.typeCashback
        ))

        return TmMerchantCouponUnifyRequest(
            token = token,
            source = ANDROID,
            status = "",
            voucher = voucherList
        )
    }
}