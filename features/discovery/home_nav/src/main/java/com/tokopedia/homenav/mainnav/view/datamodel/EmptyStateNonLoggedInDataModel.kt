package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

class EmptyStateNonLoggedInDataModel: MainNavVisitable, ImpressHolder() {
    override fun id(): Any = "Empty state non logged in"

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean = id() == visitable.id()

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}