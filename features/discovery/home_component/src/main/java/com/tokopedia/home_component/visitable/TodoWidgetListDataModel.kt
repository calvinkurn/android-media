package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by frenzel
 */
data class TodoWidgetListDataModel(
    val id: String = "",
    val todoWidgetList: List<TodoWidgetDataModel> = listOf(),
    val header: ChannelHeader = ChannelHeader(),
    val widgetParam: String = "",
    val verticalPosition: Int = 0,
    val status: Int = STATUS_LOADING,
) : HomeComponentVisitable, ImpressHolder() {

    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_ERROR = 1
        const val STATUS_SUCCESS = 2
    }

    fun isShowTodoWidget() : Boolean {
        return if (status == STATUS_SUCCESS)
            todoWidgetList.isNotEmpty()
        else
            true
    }

    override fun visitableId(): String {
        return id
    }

    override fun equalsWith(b: Any?): Boolean {
        return this === b
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle.EMPTY
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }

}
