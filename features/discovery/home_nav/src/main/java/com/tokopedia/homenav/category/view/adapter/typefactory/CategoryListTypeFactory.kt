package com.tokopedia.homenav.category.view.adapter.typefactory

import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.category.view.adapter.model.CategoryListLoadingViewModel

/**
 * Created by Lukas on 20/10/20.
 */

interface CategoryListTypeFactory : HomeNavTypeFactory {
    fun type(visitable: CategoryListLoadingViewModel): Int
}