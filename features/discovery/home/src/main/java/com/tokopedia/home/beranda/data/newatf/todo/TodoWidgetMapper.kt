package com.tokopedia.home.beranda.data.newatf.todo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.helper.LazyLoadDataMapper
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.home_component.widget.todo.TodoWidgetMapper.getAsChannelConfig
import com.tokopedia.home_component.widget.todo.TodoWidgetMapper.getAsHomeComponentHeader
import javax.inject.Inject

/**
 * Created by Frenzel
 */
class TodoWidgetMapper @Inject constructor() {

    fun asVisitable(
        data: HomeTodoWidgetData.GetHomeTodoWidget,
        index: Int,
        atfData: AtfData,
    ): Visitable<*>? {
        return if(atfData.isCache) {
            TodoWidgetListDataModel(
                status = TodoWidgetListDataModel.STATUS_LOADING,
                showShimmering = atfData.atfMetadata.isShimmer,
                source = TodoWidgetListDataModel.SOURCE_ATF,
            )
        } else if(atfData.atfStatus == AtfKey.STATUS_ERROR) {
            TodoWidgetListDataModel(
                id = atfData.atfMetadata.id.toString(),
                widgetParam = atfData.atfMetadata.param,
                verticalPosition = index,
                status = TodoWidgetListDataModel.STATUS_ERROR,
                showShimmering = atfData.atfMetadata.isShimmer,
                source = TodoWidgetListDataModel.SOURCE_ATF,
            )
        } else if(data.todos.isEmpty()) {
            null
        } else {
            TodoWidgetListDataModel(
                id = atfData.atfMetadata.id.toString(),
                todoWidgetList = LazyLoadDataMapper.mapTodoWidgetData(data.todos),
                header = data.header.getAsHomeComponentHeader(),
                config = data.config.getAsChannelConfig(),
                widgetParam = atfData.atfMetadata.param,
                verticalPosition = index,
                status = TodoWidgetListDataModel.STATUS_SUCCESS,
                showShimmering = atfData.atfMetadata.isShimmer,
                source = TodoWidgetListDataModel.SOURCE_ATF,
            )
        }
    }
}
