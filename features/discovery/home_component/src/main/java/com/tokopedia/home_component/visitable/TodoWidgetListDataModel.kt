package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelConfig
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
    val config: ChannelConfig = ChannelConfig(),
    val widgetParam: String = "",
    val verticalPosition: Int = 0,
    val status: Int = STATUS_LOADING,
    val showShimmering: Boolean = true,
) : HomeComponentVisitable, ImpressHolder() {

    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_ERROR = 1
        const val STATUS_SUCCESS = 2
        const val PAYLOAD_IS_REFRESH = "isRefresh"
    }

    fun isShowTodoWidget() : Boolean {
        return when(status) {
            STATUS_SUCCESS -> todoWidgetList.isNotEmpty()
            STATUS_LOADING -> showShimmering
            else -> true
        }
    }

    override fun visitableId(): String {
        return id
    }

    override fun equalsWith(b: Any?): Boolean {
        return this == b
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        val bundle = Bundle()
        if (b is TodoWidgetListDataModel
            && b.status == STATUS_LOADING
            && this.status != STATUS_LOADING) {
            bundle.putBoolean(PAYLOAD_IS_REFRESH, true)
        }
        return bundle
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
