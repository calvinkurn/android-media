package com.tokopedia.vouchercreation.create.view.typefactory.vouchertype

import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.*

interface PromotionTypeItemTypeFactory {

    fun type(promotionTypeTickerUiModel: PromotionTypeTickerUiModel): Int
    fun type(promotionExpenseEstimationUiModel: PromotionExpenseEstimationUiModel): Int
    fun type(cashbackTypePickerUiModel: CashbackTypePickerUiModel): Int
    fun type(promotionTypeInputListUiModel: PromotionTypeInputListUiModel): Int
    fun type(unavailableTickerUiModel: UnavailableTickerUiModel): Int
    fun type(voucherTitleUiModel: VoucherTitleUiModel): Int
    fun type(recommendationTickerUiModel: RecommendationTickerUiModel): Int
}