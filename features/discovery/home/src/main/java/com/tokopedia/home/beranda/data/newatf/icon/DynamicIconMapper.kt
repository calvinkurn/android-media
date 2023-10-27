package com.tokopedia.home.beranda.data.newatf.icon

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.device.info.DeviceScreenInfo
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
class DynamicIconMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun asVisitable(
        data: DynamicHomeIcon,
        atfData: AtfData,
    ): Visitable<*> {
        return if(atfData.atfStatus == AtfKey.STATUS_ERROR) {
            ErrorStateIconModel()
        } else {
            val componentName = atfData.atfMetadata.component
            val iconType = if(componentName == AtfKey.TYPE_ICON_V2) DynamicIconComponentDataModel.Type.SMALL else DynamicIconComponentDataModel.Type.BIG
            val numOfRows = if(componentName == AtfKey.TYPE_ICON_V2) 2 else 1
            DynamicIconComponentDataModel(
                id = atfData.atfMetadata.id.toString(),
                dynamicIconComponent = DynamicIconComponent(getDynamicIconList(data)),
                numOfRows = numOfRows,
                type = iconType,
                isCache = atfData.isCache,
            )
        }
    }

    private fun getDynamicIconList(data: DynamicHomeIcon): List<DynamicIconComponent.DynamicIcon> {
        return if(DeviceScreenInfo.isTablet(context)) {
            val (even, odd) = data.dynamicIcon.withIndex().partition { it.index % 2 == 0 }
            val newList = (even + odd).map { it.value }
            mapToVisitable(newList)
        } else {
            mapToVisitable(data.dynamicIcon)
        }
    }

    private fun mapToVisitable(dynamicIcons: List<DynamicHomeIcon.DynamicIcon>): List<DynamicIconComponent.DynamicIcon> {
        return dynamicIcons.mapIndexed { idx, it ->
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
                withBackground = it.withBackground,
                position = idx,
            )
        }
    }
}
