package com.tokopedia.homenav.base.datamodel

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable

/**
 * Created by Lukas on 21/10/20.
 */

data class HomeNavTitleDataModel (
        val identifier: Int = 0,
        val title: String = "",
        val applink: String = ""
) : HomeNavVisitable {
    override fun id(): Any = title

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean =
            visitable is HomeNavTitleDataModel && visitable.title == title && visitable.applink == applink

    override fun type(factory: HomeNavTypeFactory): Int = factory.type(this)

}