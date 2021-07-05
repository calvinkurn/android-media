package com.tokopedia.homenav.base.diffutil

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by Lukas on 20/10/20.
 */

interface HomeNavVisitable : Visitable<HomeNavTypeFactory>{
    fun id(): Any
    fun isContentTheSame(visitable: HomeNavVisitable): Boolean
    override fun type(factory: HomeNavTypeFactory): Int
}