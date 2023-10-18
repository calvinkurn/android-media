package com.tokopedia.tokomember_seller_dashboard.model.mapper

import com.tokopedia.tokomember_common_widget.util.ProgramActionType
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmCouponCreateRequest
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.TmMerchantCouponUnifyRequest
import com.tokopedia.tokomember_seller_dashboard.model.TmSingleCouponData
import com.tokopedia.tokomember_seller_dashboard.util.CASHBACK_IDR
import com.tokopedia.tokomember_seller_dashboard.util.CASHBACK_PERCENTAGE
import com.tokopedia.tokomember_seller_dashboard.util.SOURCE_MULTIPLE_COUPON_CREATE
import com.tokopedia.tokomember_seller_dashboard.util.SOURCE_MULTIPLE_COUPON_EXTEND
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

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
        token: String,
        maximumBenefit: Int,
        programActionType: Int
    ): TmMerchantCouponUnifyRequest {
        // Mutation Mapper

        val voucherList = arrayListOf<TmCouponCreateRequest>()
        val voucherOne = TmCouponCreateRequest(
            targetBuyer = 3,
            image = tmCouponPremiumUploadId,
            couponType = couponPremiumData?.typeCoupon,
            minPurchase = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.minTransaki ?: ""),
            minimumTierLevel = 1,
            benefitPercent = couponPremiumData?.cashBackPercentage,
            quota = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.quota ?: ""),
            imagePortrait = tmCouponPremiumUploadId,
            imageSquare = tmCouponPremiumUploadId,
            isPublic = 1,
            hourStart = tmStartTimeUnix,
            dateStart = tmStartDateUnix,
            hourEnd = tmEndTimeUnix,
            dateEnd = tmEndDateUnix,
            benefitType = couponPremiumData?.typeCashback
        )
        when (couponPremiumData?.typeCashback) {
            CASHBACK_IDR -> {
                voucherOne.apply {
                    benefitIdr = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback ?: "")
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback ?: "")
                }
            }
            CASHBACK_PERCENTAGE -> {
                voucherOne.apply {
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback ?: "")
                    benefitIdr = 0
                }
            }
        }
        voucherList.add(0, voucherOne)
        val voucherTwo = TmCouponCreateRequest(
            targetBuyer = 3,
            image = tmCouponVipUploadId,
            couponType = couponVip?.typeCoupon,
            minPurchase = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData?.minTransaki ?: ""),
            minimumTierLevel = 2,
            benefitPercent = couponVip?.cashBackPercentage,
            quota = CurrencyFormatHelper.convertRupiahToInt(couponVip?.quota ?: ""),
            imagePortrait = tmCouponVipUploadId,
            imageSquare = tmCouponVipUploadId,
            isPublic = 1,
            hourStart = tmStartTimeUnix,
            dateStart = tmStartDateUnix,
            hourEnd = tmEndTimeUnix,
            dateEnd = tmEndDateUnix,
            benefitType = couponVip?.typeCashback
        )

        when (couponVip?.typeCashback) {
            CASHBACK_IDR -> {
                voucherTwo.apply {
                    benefitIdr = CurrencyFormatHelper.convertRupiahToInt(couponVip.maxCashback ?: "")
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponVip.maxCashback ?: "")
                }
            }
            CASHBACK_PERCENTAGE -> {
                voucherTwo.apply {
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponVip.maxCashback ?: "")
                    benefitIdr = 0
                }
            }
        }
        voucherList.add(1, voucherTwo)

        var source = SOURCE_MULTIPLE_COUPON_CREATE

        when (programActionType) {
            ProgramActionType.EXTEND -> {
                source = SOURCE_MULTIPLE_COUPON_EXTEND
            }
        }

        return TmMerchantCouponUnifyRequest(
            token = token,
            source = source,
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
        token: String,
        maximumBenefit: Int,
        tierLevel: Int
    ): TmCouponCreateRequest {
        // Mutation Mapper

        val voucherList = arrayListOf<TmCouponCreateRequest>()
        val tmCouponCreateRequest = TmCouponCreateRequest(
            targetBuyer = 3,
            image = tmCouponPremiumUploadId,
            couponType = couponPremiumData?.typeCoupon,
            minPurchase = couponPremiumData?.minTransaki?.let {
                CurrencyFormatHelper.convertRupiahToInt(
                    it
                )
            },
            minimumTierLevel = tierLevel,
            benefitPercent = couponPremiumData?.cashBackPercentage,
            quota = couponPremiumData?.quota?.let {
                CurrencyFormatHelper.convertRupiahToInt(
                    it
                )
            },
            imagePortrait = tmCouponPremiumUploadId,
            imageSquare = tmCouponPremiumUploadId,
            isPublic = 1,
            hourStart = startTime,
            dateStart = startDate,
            hourEnd = endTime,
            dateEnd = endDate,
            benefitType = couponPremiumData?.typeCashback
        )
        when (couponPremiumData?.typeCashback) {
            CASHBACK_IDR -> {
                tmCouponCreateRequest.apply {
                    benefitIdr = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback ?: "")
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback ?: "")
                }
            }
            CASHBACK_PERCENTAGE -> {
                tmCouponCreateRequest.apply {
                    benefitMax = CurrencyFormatHelper.convertRupiahToInt(couponPremiumData.maxCashback ?: "")
                    benefitIdr = 0
                }
            }
        }

        tmCouponCreateRequest.source = "android-sellerapp"
        tmCouponCreateRequest.token = token

        return tmCouponCreateRequest
    }
}
