package com.tokopedia.homenav.base.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

/**
 * Created by dhaba
 */
data class HomeNavExpandableDataModel (
        val id: Int = 0,
        var menus: List<Visitable<*>> = listOf(),
        var isExpanded : Boolean = false
): HomeNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean = false

    override fun type(factory: HomeNavTypeFactory): Int {
        return factory.type(this)
    }
}