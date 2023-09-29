package com.tokopedia.home.beranda.data.newatf.mission

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.helper.LazyLoadDataMapper
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.widget.mission.MissionWidgetMapper.getAsChannelConfig
import com.tokopedia.home_component.widget.mission.MissionWidgetMapper.getAsHomeComponentHeader
import com.tokopedia.unifycomponents.CardUnify2

object MissionWidgetMapper {

    fun HomeMissionWidgetData.GetHomeMissionWidget.asVisitable(
        index: Int,
        atfData: AtfData,
    ): Visitable<*> {
        return if(atfData.atfStatus == AtfKey.STATUS_ERROR) {
            MissionWidgetListDataModel(
                id = atfData.atfMetadata.id.toString(),
                name = atfData.atfMetadata.name,
                verticalPosition = index,
                status = MissionWidgetListDataModel.STATUS_ERROR,
                showShimmering = atfData.atfMetadata.isShimmer,
                source = MissionWidgetListDataModel.SOURCE_ATF,
            )
        } else {
            MissionWidgetListDataModel(
                id = atfData.atfMetadata.id.toString(),
                name = atfData.atfMetadata.name,
                missionWidgetList = LazyLoadDataMapper.mapMissionWidgetData(missions),
                header = header.getAsHomeComponentHeader(),
                config = config.getAsChannelConfig(),
                verticalPosition = index,
                status = MissionWidgetListDataModel.STATUS_SUCCESS,
                showShimmering = atfData.atfMetadata.isShimmer,
                source = MissionWidgetListDataModel.SOURCE_ATF,
            )
        }
    }
}
