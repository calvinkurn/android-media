package com.tokopedia.attachvoucher.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactory
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel

open class VoucherUiModel constructor(
        voucherId: Int,
        voucherName: String?,
        voucherCode: String?,
        merchantVoucherType: MerchantVoucherType?,
        merchantVoucherAmount: MerchantVoucherAmount?,
        minimumSpend: Int,
        merchantVoucherOwner: MerchantVoucherOwner,
        validThru: String,
        tnc: String?,
        merchantVoucherBanner: MerchantVoucherBanner?,
        merchantVoucherStatus: MerchantVoucherStatus?,
        restrictedForLiquidProduct: Boolean,
        val isPublic: Int = 1,
        val remainingQuota: Int = 0,
        val isLockToProduct: Int = 0
) : MerchantVoucherModel(
        voucherId,
        voucherName,
        voucherCode,
        merchantVoucherType,
        merchantVoucherAmount,
        minimumSpend,
        merchantVoucherOwner,
        validThru,
        tnc,
        merchantVoucherBanner,
        merchantVoucherStatus,
        restrictedForLiquidProduct
), Visitable<AttachVoucherTypeFactory> {

    val identifier: String get() = merchantVoucherType?.identifier ?: ""
    val availableAmount: String get() = merchantVoucherAmount?.amount?.toInt()?.toString() ?: ""
    val amountType: Int? get() = merchantVoucherAmount?.type
    val type: Int? get() = merchantVoucherType?.type

    private fun isPublic(): Boolean {
        return isPublic == 1
    }

    private fun isLockToProduct(): Boolean {
        return isLockToProduct == 1
    }

    fun getMerchantVoucherViewModel(): MerchantVoucherViewModel {
        return MerchantVoucherViewModel(this).apply {
            this.isPublic = isPublic()
            this.isLockToProduct = isLockToProduct()
        }
    }

    override fun type(typeFactory: AttachVoucherTypeFactory): Int {
        return typeFactory.type(this)
    }

}