package com.tokopedia.home.beranda.data.newatf.icon

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel

object DynamicIconMapper {

    fun DynamicHomeIcon.asVisitable(
        id: Int,
        index: Int,
        isCache: Boolean
    ): Visitable<*> {
        return DynamicIconComponentDataModel(
            id = id.toString(),
            dynamicIconComponent = DynamicIconComponent(
                dynamicIcon.map {
                    DynamicIconComponent.DynamicIcon(
                        id = it.id,
                        applink = it.applinks,
                        imageUrl = it.imageUrl,
                        name = it.name,
                        url = it.url,
                        businessUnitIdentifier = it.bu_identifier,
                        galaxyAttribution = it.galaxyAttribution,
                        persona = it.persona,
                        brandId = it.brandId,
                        categoryPersona = it.categoryPersona,
                        campaignCode = it.campaignCode,
                        withBackground = it.withBackground
                    )
                }
            ),
            isCache = isCache,
            type = type
        )
    }
}
