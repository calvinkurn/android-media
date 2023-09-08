package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.MultiComponentItemUiModel

data class MultiComponentDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    val tabs: List<MultiComponentTab>
): BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return false
        // TODO("check logic from configuration")
    }
}

data class MultiComponentTab(
    val id: String,
    val title: String,
    val components: List<MultiComponentData>,
    var isSelected: Boolean,
    var isLoaded: Boolean,
    var isError: Boolean,
)

data class MultiComponentData(
    val componentType: String,
    val dataKey: String,
    val configuration: String,
    val metricParam: String,
    val data: List<MultiComponentItemUiModel>?
)
