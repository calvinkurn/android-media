package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import java.io.Serializable

/**
 * @author by furqan on 22/09/2021
 */
class DigitalRecommendationUiModel : Visitable<BuyerOrderDetailTypeFactory>, Serializable {
    override fun type(typeFactory: BuyerOrderDetailTypeFactory): Int =
        typeFactory.type(this)
}
