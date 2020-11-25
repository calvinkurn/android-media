package com.tokopedia.vouchercreation.common.view

import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel

interface VoucherCommonTypeFactory {

    fun type(voucherTextFieldUiModel: VoucherTextFieldUiModel): Int
    fun type(promotionExpenseEstimationUiModel: PromotionExpenseEstimationUiModel): Int

}