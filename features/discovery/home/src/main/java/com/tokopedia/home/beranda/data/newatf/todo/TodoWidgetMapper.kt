package com.tokopedia.home.beranda.data.newatf.todo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon
import com.tokopedia.home.beranda.helper.LazyLoadDataMapper
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

object TodoWidgetMapper {

    fun HomeTodoWidgetData.GetHomeTodoWidget.asVisitable(
        metadata: AtfMetadata,
        index: Int,
    ): Visitable<*> {
        return TodoWidgetListDataModel(
            id = metadata.id.toString(),
            todoWidgetList = LazyLoadDataMapper.mapTodoWidgetData(todos),
            header = header.getAsHomeComponentHeader(),
            config = config.getAsChannelConfig(),
            widgetParam = metadata.param,
            verticalPosition = index,
            status = MissionWidgetListDataModel.STATUS_SUCCESS,
            showShimmering = metadata.isShimmer,
            source = MissionWidgetListDataModel.SOURCE_ATF,
        )
    }
}
