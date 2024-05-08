package com.tokopedia.homenav.mainnav.view.datamodel.buyagain

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.homenav.mainnav.view.widget.BuyAgainModel
import com.tokopedia.kotlin.model.ImpressHolder

data class BuyAgainUiModel(
    val data: List<BuyAgainModel>
) : MainNavVisitable, ImpressHolder() {

    override fun id(): Any = "buyAgainList"

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean =
        visitable is BuyAgainUiModel &&
            visitable.data == data

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}
