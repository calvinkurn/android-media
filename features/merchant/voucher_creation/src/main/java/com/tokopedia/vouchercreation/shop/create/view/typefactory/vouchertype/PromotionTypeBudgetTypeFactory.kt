package com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertype

import com.tokopedia.vouchercreation.shop.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.widget.PromotionTypeInputUiModel

interface PromotionTypeBudgetTypeFactory : CreateVoucherTypeFactory {
    fun type(promotionTypeInputUiModel: PromotionTypeInputUiModel): Int
}