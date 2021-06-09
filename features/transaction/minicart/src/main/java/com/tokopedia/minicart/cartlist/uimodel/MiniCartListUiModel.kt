package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.subpage.summarytransaction.MiniCartSummaryTransactionUiModel
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData

data class MiniCartListUiModel(
        var title: String = "",
        var maximumShippingWeight: Int = 0,
        var maximumShippingWeightErrorMessage: String = "",
        var miniCartWidgetUiModel: MiniCartWidgetData = MiniCartWidgetData(),
        var miniCartSummaryTransactionUiModel: MiniCartSummaryTransactionUiModel = MiniCartSummaryTransactionUiModel(),
        var visitables: MutableList<Visitable<*>> = mutableListOf(),
        var isFirstLoad: Boolean = false
) {

    fun getMiniCartProductUiModelList(): List<MiniCartProductUiModel> {
        val products = mutableListOf<MiniCartProductUiModel>()
        visitables.forEach {
            if (it is MiniCartProductUiModel) {
                products.add(it)
            }
        }
        return products
    }

    fun getMiniCartTickerWarningUiModel(): MiniCartTickerWarningUiModel? {
        loop@ for (visitable in visitables) {
            if (visitable is MiniCartTickerWarningUiModel) {
                return visitable
            }
        }

        return null
    }
}