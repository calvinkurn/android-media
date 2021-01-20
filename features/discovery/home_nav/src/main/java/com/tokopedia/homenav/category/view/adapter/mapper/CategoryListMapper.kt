package com.tokopedia.homenav.category.view.adapter.mapper

import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity

/**
 * Created by Lukas on 21/10/20.
 */


fun List<DynamicHomeIconEntity.CategoryRow>.toVisitable(): List<HomeNavVisitable>{
    return map {
        HomeNavMenuDataModel(id = it.id, itemTitle = it.name, srcImage = it.imageUrl, applink = it.applink)
    }
}