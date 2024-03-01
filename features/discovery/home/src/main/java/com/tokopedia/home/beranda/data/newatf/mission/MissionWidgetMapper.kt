package com.tokopedia.home.beranda.data.newatf.mission

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.helper.LazyLoadDataMapper
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.widget.mission.MissionWidgetMapper.getAsChannelConfig
import com.tokopedia.home_component.widget.mission.MissionWidgetMapper.getAsHomeComponentHeader
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class MissionWidgetMapper @Inject constructor() {

    fun asVisitable(
        data: HomeMissionWidgetData.GetHomeMissionWidget,
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
                widgetParam = atfData.atfMetadata.param,
            )
        } else {
            MissionWidgetListDataModel(
                id = atfData.atfMetadata.id.toString(),
                name = atfData.atfMetadata.name,
                missionWidgetList = LazyLoadDataMapper.mapMissionWidgetData(data.missions, atfData.isCache, data.appLog),
                header = data.header.getAsHomeComponentHeader(),
                config = data.config.getAsChannelConfig(),
                verticalPosition = index,
                status = MissionWidgetListDataModel.STATUS_SUCCESS,
                showShimmering = atfData.atfMetadata.isShimmer,
                source = MissionWidgetListDataModel.SOURCE_ATF,
                type = getMissionWidgetType(atfData.atfMetadata.component),
                widgetParam = atfData.atfMetadata.param,
            )
        }
    }

    companion object {
        fun getMissionWidgetType(component: String): MissionWidgetListDataModel.Type {
            return if(component == AtfKey.TYPE_MISSION_V2)
                MissionWidgetListDataModel.Type.CLEAR
            else
                MissionWidgetListDataModel.Type.CARD
        }
    }
}
