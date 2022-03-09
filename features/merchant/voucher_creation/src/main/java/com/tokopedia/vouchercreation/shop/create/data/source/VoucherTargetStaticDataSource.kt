package com.tokopedia.vouchercreation.shop.create.data.source

import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.VoucherTargetItemUiModel

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