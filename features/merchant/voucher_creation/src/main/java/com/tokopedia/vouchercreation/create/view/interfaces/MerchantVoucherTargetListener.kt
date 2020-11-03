package com.tokopedia.vouchercreation.create.view.interfaces

import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType

interface MerchantVoucherTargetListener: CreateMerchantVoucherListener {

    fun onSetVoucherName(@VoucherTargetType targetType: Int, voucherName: String, promoCode: String)

}