package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class InitialShimmerTransactionDataModel(
        val id: Int = 125
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean {
        return visitable is InitialShimmerTransactionDataModel && id == visitable.id
    }

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}