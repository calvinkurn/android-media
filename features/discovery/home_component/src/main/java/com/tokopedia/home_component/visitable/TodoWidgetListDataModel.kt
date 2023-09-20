package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.LoadableComponent
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelConfig
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
) : HomeComponentVisitable, ImpressHolder(),
    LoadableComponent by BlocksLoadableComponent(
        { status != STATUS_LOADING },
        "TodoWidgetListDataModel"
    ) {

    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_ERROR = 1
        const val STATUS_SUCCESS = 2
        const val PAYLOAD_NO_SHIMMER = "noShimmer"
    }

    fun isShowWidget() : Boolean {
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
        bundle.putBoolean(PAYLOAD_NO_SHIMMER, isRefresh(b))
        return bundle
    }

    private fun isRefresh(b: Any?): Boolean {
        return b is TodoWidgetListDataModel
            && b.status == STATUS_LOADING
            && this.status != STATUS_LOADING
            && !b.showShimmering
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
