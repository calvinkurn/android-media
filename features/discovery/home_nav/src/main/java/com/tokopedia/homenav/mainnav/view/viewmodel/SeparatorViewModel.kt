package com.tokopedia.homenav.mainnav.view.viewmodel

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class SeparatorViewModel(
        val isSeparator: Boolean = true
): HomeNavVisitable, ImpressHolder() {
    override fun id(): Any = ""

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean = id() == visitable.id()

    override fun type(factory: HomeNavTypeFactory): Int {
        return (factory as MainNavTypeFactory).type(this)
    }
}