package com.tokopedia.sellerhomecommon.presentation.model

data class MultiComponentDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    val tabs: List<MultiComponentTab> = listOf(),
    var selectedTabPosition: Int = 0
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return tabs.isEmpty()
    }
}

data class MultiComponentTab(
    val id: String,
    val title: String,
    val ticker: String,
    val components: List<MultiComponentData>,
    var isLoaded: Boolean,
    var isError: Boolean,
)

data class MultiComponentData(
    val componentType: String,
    val dataKey: String,
    val configuration: String,
    val metricParam: String,
    val data: BaseWidgetUiModel<*>?
)
