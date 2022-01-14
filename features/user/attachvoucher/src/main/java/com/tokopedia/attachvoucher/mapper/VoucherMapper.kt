package com.tokopedia.attachvoucher.mapper

import android.annotation.SuppressLint
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.utils.time.RfcDateTimeParser
import javax.inject.Inject

class VoucherMapper @Inject constructor() {

    fun map(response: GetMerchantPromotionGetMVListResponse): List<VoucherUiModel> {
        val vouchers = arrayListOf<VoucherUiModel>()
        for (voucher in response.merchantPromotionGetMVList.data.vouchers) {
            val voucherType = MerchantVoucherType(
                    type = voucher.voucherType,
                    identifier = voucher.voucherType.toString()
            )
            val voucherAmount = MerchantVoucherAmount(
                    type = voucher.voucherDiscountType,
                    amount = voucher.voucherDiscountAmt.toFloat()
            )
            val voucherOwner = MerchantVoucherOwner()
            val voucherBanner = MerchantVoucherBanner(
                    desktopUrl = voucher.voucherImage,
                    mobileUrl = voucher.voucherImage
            )
            val voucherStatus = MerchantVoucherStatus()
            val voucherUiModel = VoucherUiModel(
                    voucherId = voucher.voucherId,
                    voucherName = voucher.voucherName,
                    voucherCode = voucher.voucherCode,
                    merchantVoucherType = voucherType,
                    merchantVoucherAmount = voucherAmount,
                    minimumSpend = voucher.voucherMinimumAmt,
                    merchantVoucherOwner = voucherOwner,
                    validThru = convertToUnix(voucher.voucherFinishTime),
                    tnc = voucher.tnc,
                    merchantVoucherBanner = voucherBanner,
                    merchantVoucherStatus = voucherStatus,
                    restrictedForLiquidProduct = false,
                    isPublic = voucher.isPublic,
                    remainingQuota = voucher.remainingQuota
            )
            vouchers.add(voucherUiModel)
        }
        return vouchers
    }

    // convert RFC3339 to unix
    @SuppressLint("SimpleDateFormat")
    private fun convertToUnix(voucherFinishTime: String): String {
        val unix = RfcDateTimeParser.parseDateString(voucherFinishTime).time / 1000
        return unix.toString()
    }

}