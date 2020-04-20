package com.tokopedia.vouchercreation.create.data.source

import com.tokopedia.vouchercreation.create.view.customview.VoucherTargetCardItem
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetItemUiModel

object VoucherTargetStaticDataSource {

    fun getVoucherTargetItemUiModelList() : List<VoucherTargetItemUiModel> =
            listOf(
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardItem.TARGET_PUBLIC_TYPE,
                            isEnabled = true,
                            isHavePromoCard = true,
                            promoCode = "KOKUMIGRATIS100"),
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardItem.TARGET_SPECIAL_TYPE,
                            isEnabled = false,
                            isHavePromoCard = false
                    ))
}