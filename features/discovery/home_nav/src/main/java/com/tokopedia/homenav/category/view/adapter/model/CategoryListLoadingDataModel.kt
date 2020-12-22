package com.tokopedia.homenav.category.view.adapter.model

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.category.view.adapter.typefactory.CategoryListTypeFactory

/**
 * Created by Lukas on 22/10/20.
 */
class CategoryListLoadingDataModel : HomeNavVisitable{
    override fun id(): Any = CategoryListLoadingDataModel::class.java.simpleName

    override fun isContentTheSame(visitable: HomeNavVisitable): Boolean = visitable.id() == id()

    override fun type(factory: HomeNavTypeFactory): Int = (factory as CategoryListTypeFactory).type(this)
}