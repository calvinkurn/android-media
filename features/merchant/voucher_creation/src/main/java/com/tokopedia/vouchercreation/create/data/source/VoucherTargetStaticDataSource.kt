package com.tokopedia.vouchercreation.create.data.source

import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetItemUiModel

object VoucherTargetStaticDataSource {

    fun getVoucherTargetItemUiModelList() : List<VoucherTargetItemUiModel> =
            listOf(
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardType.PUBLIC,
                            isEnabled = true,
                            isHavePromoCard = false),
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardType.PRIVATE,
                            isEnabled = false,
                            isHavePromoCard = false
                    ))
}