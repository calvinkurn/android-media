package com.tokopedia.vouchercreation.create.view.typefactory.vouchertype

import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.PromotionTypeTickerUiModel

interface PromotionTypeItemTypeFactory {

    fun type(promotionTypeTickerUiModel: PromotionTypeTickerUiModel): Int
}