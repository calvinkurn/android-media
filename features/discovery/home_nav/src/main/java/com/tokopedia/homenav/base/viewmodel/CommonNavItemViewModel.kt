package com.tokopedia.homenav.base.viewmodel

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class CommonNavItemViewModel(
        val id: Int = 0,
        val srcImage: String = "",
        val itemTitle: String = "",
        val applink: String = ""
): HomeNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isDifferent(visitable: HomeNavVisitable): Boolean =
            visitable is CommonNavItemViewModel &&
            srcImage == visitable.srcImage &&
            itemTitle == visitable.itemTitle &&
            applink == visitable.applink


    override fun type(factory: HomeNavTypeFactory): Int {
        return (factory as MainNavTypeFactory).type(this)
    }
}