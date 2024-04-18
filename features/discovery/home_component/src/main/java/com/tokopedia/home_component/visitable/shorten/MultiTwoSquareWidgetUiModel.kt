package com.tokopedia.home_component.visitable.shorten

import com.tokopedia.analytics.performance.perf.performanceTracing.components.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.kotlin.model.ImpressHolder

data class MultiTwoSquareWidgetUiModel(
    val id: String = "",
    val showShimmering: Boolean = true,
    val mission: MissionWidgetUiModel? = null,
    val thumbnail: ThumbnailWidgetUiModel? = null,
    val status: Status = Status.Loading
) : HomeComponentVisitable,
    ImpressHolder(),
    LoadableComponent by BlocksLoadableComponent(
        { status != Status.Loading },
        BLOCK_NAME
    ) {

    override fun visitableId() = id
    override fun equalsWith(b: Any?) = b == this
    override fun getChangePayloadFrom(b: Any?) = null
    override fun type(typeFactory: HomeComponentTypeFactory) = typeFactory.type(this)

    sealed class Status {
        object Loading : Status()
        object Success : Status()
        object Error : Status()
    }

    sealed class Type(val value: Int) {
        object Mission : Type(1)
        object Thumbnail : Type(2)
    }

    companion object {
        private const val BLOCK_NAME = "MultiTwoSquareWidgetUiModel"
    }
}
