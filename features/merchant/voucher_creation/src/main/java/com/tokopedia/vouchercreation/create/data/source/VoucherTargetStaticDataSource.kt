package com.tokopedia.vouchercreation.create.data.source

import com.tokopedia.vouchercreation.create.view.customview.VoucherTargetCardItemView
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetItemUiModel

object VoucherTargetStaticDataSource {

    fun getVoucherTargetItemUiModelList() : List<VoucherTargetItemUiModel> =
            listOf(
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardItemView.TARGET_PUBLIC_TYPE,
                            isEnabled = true,
                            isHavePromoCard = false),
                    VoucherTargetItemUiModel(
                            voucherTargetType = VoucherTargetCardItemView.TARGET_PRIVATE_TYPE,
                            isEnabled = false,
                            isHavePromoCard = false
                    ))
}