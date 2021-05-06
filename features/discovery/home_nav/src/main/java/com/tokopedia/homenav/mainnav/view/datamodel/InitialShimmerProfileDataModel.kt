package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class InitialShimmerProfileDataModel(
        val id: Int = 124
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean {
        return visitable is InitialShimmerProfileDataModel && id == visitable.id
    }

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}