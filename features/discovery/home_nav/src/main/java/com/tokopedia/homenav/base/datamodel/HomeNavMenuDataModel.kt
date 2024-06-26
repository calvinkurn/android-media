package com.tokopedia.homenav.base.datamodel

import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class HomeNavMenuDataModel(
        val sectionId: Int = 0,
        val id: Int = 0,
        val srcImage: String = "",
        var srcIconId: Int? = null,
        val itemTitle: String = "",
        val applink: String = "",
        var notifCount: String = "",
        val submenus: List<HomeNavMenuDataModel> = listOf(),
        val showCta: Boolean = false,
): HomeNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean =
            this == visitable


    override fun type(factory: HomeNavTypeFactory): Int {
        return factory.type(this)
    }
}
