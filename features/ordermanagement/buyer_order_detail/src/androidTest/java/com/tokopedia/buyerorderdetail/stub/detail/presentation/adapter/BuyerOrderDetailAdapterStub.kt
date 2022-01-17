package com.tokopedia.buyerorderdetail.stub.detail.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.BuyerOrderDetailAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.model.PGRecommendationWidgetUiModel

class BuyerOrderDetailAdapterStub(
    typeFactory: BuyerOrderDetailTypeFactory
) : BuyerOrderDetailAdapter(typeFactory) {
    override fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setUpPhysicalRecommendationSection(
        pgRecommendationWidgetUiModel: PGRecommendationWidgetUiModel
    ) {}

    override fun MutableList<Visitable<BuyerOrderDetailTypeFactory>>.setupDigitalRecommendationSection() {}
}