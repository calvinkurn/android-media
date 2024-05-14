package com.tokopedia.homenav.mainnav.view.datamodel.buyagain

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class ShimmerBuyAgainUiModel(
    val id: Int = 456
) : MainNavVisitable, ImpressHolder() {

    override fun id() = id

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean {
        return visitable is ShimmerBuyAgainUiModel && id == visitable.id
    }

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}
