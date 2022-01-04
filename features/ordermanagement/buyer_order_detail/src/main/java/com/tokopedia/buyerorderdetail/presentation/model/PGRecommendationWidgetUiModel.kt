package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory

class PGRecommendationWidgetUiModel(val pageName: String = "",
                                    val productIdList: List<String> = arrayListOf()) : Visitable<BuyerOrderDetailTypeFactory> {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int =
            typeFactory.type(this)
}
