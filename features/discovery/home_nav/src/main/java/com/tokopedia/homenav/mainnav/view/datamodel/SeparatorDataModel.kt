package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class SeparatorDataModel(
        val isSeparator: Boolean = true,
        val sectionId: Int? = null
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = sectionId.toString()

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean = id() == visitable.id()

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}