package com.tokopedia.vouchercreation.product.create.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.vouchercreation.product.create.data.response.Voucher
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import javax.inject.Inject

class CouponDetailMapper @Inject constructor() {

    fun map(voucher: Voucher): CouponUiModel {
        val products = mutableListOf<Long>()

        voucher.productIds.forEach { parentProduct ->
            products.add(parentProduct.parentProductId)

            parentProduct.childProductId.forEach { variantProductId ->
                products.add(variantProductId)
            }
        }

        return CouponUiModel(
            id = voucher.voucherId.toIntOrZero(),
            name = voucher.voucherName,
            type = voucher.voucherType,
            typeFormatted = voucher.voucherTypeFormatted,
            image = voucher.voucherImage,
            imageSquare = voucher.imageSquare,
            imagePortrait = voucher.imagePortrait,
            status = voucher.voucherStatus,
            discountTypeFormatted = voucher.discountTypeFormatted,
            discountAmt = voucher.discountAmt,
            discountAmtFormatted = voucher.discountAmtFormatted,
            discountAmtMax = voucher.discountAmtMax,
            minimumAmt = voucher.voucherMinimumAmt,
            quota = voucher.voucherQuota,
            confirmedQuota = voucher.confirmedQuota,
            bookedQuota = voucher.bookedQuota,
            startTime = voucher.startTime,
            finishTime = voucher.finishTime,
            code = voucher.voucherCode,
            createdTime = voucher.createTime,
            updatedTime = voucher.updateTime,
            isPublic = voucher.isPublic == 1,
            tnc = voucher.tnc,
            productIds = products,
            products = voucher.productIds,
            galadrielVoucherId = voucher.galadrielVoucherId.toLongOrZero()
        )

    }

}