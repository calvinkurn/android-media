package com.tokopedia.homenav.base.viewmodel

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable

/**
 * Created by Lukas on 21/10/20.
 */

data class HomeNavGlobalErrorViewModel (
        val throwable: Throwable
) : HomeNavVisitable {
    override fun id(): Any = this::class.java.name

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean =
            visitable is HomeNavGlobalErrorViewModel && visitable.throwable == throwable

    override fun type(factory: HomeNavTypeFactory): Int = factory.type(this)

}