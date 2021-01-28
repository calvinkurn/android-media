package com.tokopedia.homenav.mainnav.data.mapper

import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity

/**
 * Created by Lukas on 23/10/20.
 */

fun List<DynamicHomeIconEntity.Category>.toVisitable(): List<HomeNavVisitable> = map {
    HomeNavMenuDataModel(sectionId = MainNavConst.Section.BU_ICON, id = it.id, srcImage = it.imageUrl, itemTitle = it.name, applink = it.applink, submenus = it.categoryRows.toVisitables())
}

fun List<DynamicHomeIconEntity.CategoryRow>.toVisitables(): List<HomeNavMenuDataModel> = map {
    HomeNavMenuDataModel(sectionId = MainNavConst.Section.BU_ICON, id = it.id, srcImage = it.imageUrl, itemTitle = it.name, applink =  it.applink)
}