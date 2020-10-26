package com.tokopedia.homenav.base.viewmodel

import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class HomeNavMenuViewModel(
        val id: Int = 0,
        val srcImage: String = "",
        val itemTitle: String = "",
        val applink: String = "",
        val notifCount: String = "",
        val submenu: List<HomeNavMenuViewModel> = listOf()
): HomeNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean =
            visitable is HomeNavMenuViewModel &&
            srcImage == visitable.srcImage &&
            itemTitle == visitable.itemTitle &&
            applink == visitable.applink


    override fun type(factory: HomeNavTypeFactory): Int {
        return factory.type(this)
    }
}