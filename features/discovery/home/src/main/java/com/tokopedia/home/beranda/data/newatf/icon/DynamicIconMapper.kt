package com.tokopedia.home.beranda.data.newatf.icon

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateIconModel
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class DynamicIconMapper @Inject constructor() {

    fun asVisitable(
        data: DynamicHomeIcon,
        atfData: AtfData,
    ): Visitable<*> {
        return if(atfData.atfStatus == AtfKey.STATUS_ERROR) {
            ErrorStateIconModel()
        } else {
            DynamicIconComponentDataModel(
                id = atfData.atfMetadata.id.toString(),
                dynamicIconComponent = DynamicIconComponent(
                    data.dynamicIcon.map {
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
                isCache = atfData.isCache,
                type = data.type
            )
        }
    }
}
