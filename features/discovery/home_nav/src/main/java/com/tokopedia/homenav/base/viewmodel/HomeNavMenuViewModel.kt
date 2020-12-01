package com.tokopedia.homenav.base.viewmodel

import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class HomeNavMenuViewModel(
        val trackerName: String = "",
        val sectionId: Int = 0,
        val id: Int = 0,
        val srcImage: String = "",
        val srcIconId: Int? = null,
        val itemTitle: String = "",
        val applink: String = "",
        var notifCount: String = "",
        val submenu: List<HomeNavMenuViewModel> = listOf()
): HomeNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean =
            visitable is HomeNavMenuViewModel &&
            srcImage == visitable.srcImage &&
            itemTitle == visitable.itemTitle &&
            applink == visitable.applink && notifCount == visitable.notifCount


    override fun type(factory: HomeNavTypeFactory): Int {
        return factory.type(this)
    }
}