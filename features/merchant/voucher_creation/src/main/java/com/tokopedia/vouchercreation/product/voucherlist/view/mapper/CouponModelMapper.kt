package com.tokopedia.vouchercreation.product.voucherlist.view.mapper

import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst.Companion.CASHBACK
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst.Companion.FREE_ONGKIR
import com.tokopedia.vouchercreation.product.voucherlist.view.bottomsheet.CouponFilterBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherTarget.Companion.PRIVATE
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherTarget.Companion.PUBLIC

object CouponModelMapper {
    fun mapToType(type: CouponFilterBottomSheet.FilterType): Int? {
        return when (type) {
            CouponFilterBottomSheet.FilterType.FREE_SHIPPING -> FREE_ONGKIR
            CouponFilterBottomSheet.FilterType.CASHBACK -> CASHBACK
            else -> null
        }
    }

    fun mapToTarget(target: CouponFilterBottomSheet.FilterTarget): String? {
        return when (target) {
            CouponFilterBottomSheet.FilterTarget.PUBLIC -> PUBLIC
            CouponFilterBottomSheet.FilterTarget.PRIVATE -> PRIVATE
            else -> null
        }
    }
}