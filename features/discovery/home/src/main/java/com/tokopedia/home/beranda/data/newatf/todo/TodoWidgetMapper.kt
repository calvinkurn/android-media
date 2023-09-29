package com.tokopedia.home.beranda.data.newatf.todo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.helper.LazyLoadDataMapper
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.model.DynamicIconComponent
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.home_component.widget.mission.MissionWidgetMapper.getAsChannelConfig
import com.tokopedia.home_component.widget.mission.MissionWidgetMapper.getAsHomeComponentHeader
import com.tokopedia.home_component.widget.todo.TodoWidgetMapper.getAsChannelConfig
import com.tokopedia.home_component.widget.todo.TodoWidgetMapper.getAsHomeComponentHeader
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by Frenzel
 */
object TodoWidgetMapper {

    fun HomeTodoWidgetData.GetHomeTodoWidget.asVisitable(
        index: Int,
        atfData: AtfData,
    ): Visitable<*> {
        return if(atfData.atfStatus == AtfKey.STATUS_ERROR) {
            TodoWidgetListDataModel(
                id = atfData.atfMetadata.id.toString(),
                widgetParam = atfData.atfMetadata.param,
                verticalPosition = index,
                status = TodoWidgetListDataModel.STATUS_ERROR,
                showShimmering = atfData.atfMetadata.isShimmer,
                source = TodoWidgetListDataModel.SOURCE_ATF,
            )
        } else {
            TodoWidgetListDataModel(
                id = atfData.atfMetadata.id.toString(),
                todoWidgetList = LazyLoadDataMapper.mapTodoWidgetData(todos),
                header = header.getAsHomeComponentHeader(),
                config = config.getAsChannelConfig(),
                widgetParam = atfData.atfMetadata.param,
                verticalPosition = index,
                status = TodoWidgetListDataModel.STATUS_SUCCESS,
                showShimmering = atfData.atfMetadata.isShimmer,
                source = TodoWidgetListDataModel.SOURCE_ATF,
            )
        }
    }
}
