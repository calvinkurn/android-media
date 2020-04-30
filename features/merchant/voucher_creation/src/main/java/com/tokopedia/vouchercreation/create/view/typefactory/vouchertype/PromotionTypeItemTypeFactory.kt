package com.tokopedia.vouchercreation.create.view.typefactory.vouchertype

import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackTypePickerUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel

interface PromotionTypeItemTypeFactory {

    fun type(promotionTypeTickerUiModel: PromotionTypeTickerUiModel): Int
    fun type(promotionExpenseEstimationUiModel: PromotionExpenseEstimationUiModel): Int
    fun type(cashbackTypePickerUiModel: CashbackTypePickerUiModel): Int

}