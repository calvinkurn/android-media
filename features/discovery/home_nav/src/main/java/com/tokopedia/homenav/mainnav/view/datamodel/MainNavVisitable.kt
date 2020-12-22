package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory

/**
 * Created by Lukas on 20/10/20.
 */

interface MainNavVisitable : Visitable<MainNavTypeFactory>{
    fun id(): Any
    fun isContentTheSame(visitable: MainNavVisitable): Boolean
    override fun type(factory: MainNavTypeFactory): Int
}