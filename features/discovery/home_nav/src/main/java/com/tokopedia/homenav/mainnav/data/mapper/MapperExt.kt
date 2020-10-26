package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity

/**
 * Created by Lukas on 23/10/20.
 */

fun List<DynamicHomeIconEntity.Category>.toVisitable(): List<HomeNavVisitable> = map {
    HomeNavMenuViewModel(it.id, it.imageUrl, it.name, it.applink, submenu = it.categoryRows.toVisitables())
}

fun List<DynamicHomeIconEntity.CategoryRow>.toVisitables(): List<HomeNavMenuViewModel> = map {
    HomeNavMenuViewModel(it.id, it.imageUrl, it.name, it.applink)
}