package com.tokopedia.vouchercreation.create.view.typefactory.vouchertype

import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel

interface PromotionTypeBudgetTypeFactory : CreateVoucherTypeFactory {
    fun type(promotionTypeInputUiModel: PromotionTypeInputUiModel): Int
}