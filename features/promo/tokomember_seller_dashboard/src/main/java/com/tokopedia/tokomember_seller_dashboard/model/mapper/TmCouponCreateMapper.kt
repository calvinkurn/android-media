package com.tokopedia.tokomember_seller_dashboard.model.mapper

import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponCreateRequest
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmMerchantCouponUnifyRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmSingleCouponData
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
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
        val voucherOne = TmCouponCreateRequest(
            targetBuyer = 3,
            image = tmCouponPremiumUploadId,
            couponType = couponPremiumData?.typeCoupon,
            minPurchase = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.minTransaki?:""),
            minimumTierLevel = 1,
            benefitPercent = couponPremiumData?.cashBackPercentage,
            quota = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.quota?:""),
            imagePortrait = imagePortrait,
            imageSquare = imageSquare,
            isPublic = 0,
            hourStart = tmStartTimeUnix,
            dateStart = tmStartDateUnix,
            hourEnd = tmEndTimeUnix,
            dateEnd = tmEndDateUnix,
            benefitType = couponPremiumData?.typeCashback
        )
        when (couponPremiumData?.typeCashback) {
            CASHBACK_IDR -> {
                voucherOne.apply {
                    benefitIdr = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback?:"")
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback?:"")
                }
            }
            CASHBACK_PERCENTAGE -> {
                voucherOne.apply {
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback?:"")
                    benefitIdr = 0
                }
            }
        }
        voucherList.add(0, voucherOne )
        val voucherTwo =  TmCouponCreateRequest(
            targetBuyer = 3,
            image = tmCouponVipUploadId,
            couponType = couponVip?.typeCoupon,
            minPurchase = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.minTransaki?:""),
            minimumTierLevel = 1,
            benefitPercent = couponVip?.cashBackPercentage,
            quota = CurrencyFormatHelper.convertRupiahToInt(couponVip?.quota?:""),
            imagePortrait = imagePortrait,
            imageSquare = imageSquare,
            isPublic = 0,
            hourStart = tmStartTimeUnix,
            dateStart = tmStartDateUnix,
            hourEnd = tmEndTimeUnix,
            dateEnd = tmEndDateUnix,
            benefitType = couponVip?.typeCashback
        )

        when (couponVip?.typeCashback) {
            CASHBACK_IDR -> {
                voucherTwo.apply {
                    benefitIdr = CurrencyFormatHelper.convertRupiahToInt(couponVip.maxCashback?:"")
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponVip.maxCashback?:"")
                }
            }
            CASHBACK_PERCENTAGE -> {
                voucherTwo.apply {
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponVip.maxCashback?:"")
                    benefitIdr = 0
                }
            }
        }
        voucherList.add(1, voucherTwo)

        return TmMerchantCouponUnifyRequest(
            token = token,
            source = TM_MULTIPLE_COUPON_SOURCE,
            status = "",
            vouchers = voucherList
        )
    }

    fun mapCreateDataSingle(
        couponPremiumData: TmSingleCouponData?,
        tmCouponPremiumUploadId: String,
        startDate: String?,
        endDate: String?,
        startTime: String?,
        endTime: String?,
        token:String,
        imageSquare:String,
        imagePortrait: String,
        maximumBenefit: Int
    ): TmMerchantCouponUnifyRequest {
        //Mutation Mapper

        val voucherList = arrayListOf<TmCouponCreateRequest>()
        voucherList.add(0, TmCouponCreateRequest(
            benefitIdr = couponPremiumData?.maxCashback?.let {
                CurrencyFormatHelper.convertRupiahToInt(
                    it
                )
            },
            benefitMax = couponPremiumData?.maxCashback?.let {
                CurrencyFormatHelper.convertRupiahToInt(
                    it
                )
            },
            targetBuyer = 3,
            image = tmCouponPremiumUploadId,
            couponType = couponPremiumData?.typeCoupon,
            minPurchase = couponPremiumData?.minTransaki?.let {
                CurrencyFormatHelper.convertRupiahToInt(
                    it
                )
            },
            minimumTierLevel = 1,
            benefitPercent = couponPremiumData?.cashBackPercentage,
            quota = couponPremiumData?.quota?.let {
                CurrencyFormatHelper.convertRupiahToInt(
                    it
                )
            },
            imagePortrait = imagePortrait,
            imageSquare = imageSquare,
            isPublic = 0,
            hourStart = startTime,
            dateStart = startDate,
            hourEnd = endTime,
            dateEnd = endDate,
            benefitType = couponPremiumData?.typeCashback
        ))

        return TmMerchantCouponUnifyRequest(
            token = token,
            source = TM_SINGLE_COUPON_SOURCE,
            status = "",
            vouchers = voucherList
        )
    }
}