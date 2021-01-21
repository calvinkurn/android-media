package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class InitialShimmerDataModel(
        val id: Int = 123
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean {
        return visitable is InitialShimmerDataModel && id == visitable.id
    }

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}