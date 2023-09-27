package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.model.ImpressHolder

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
) {
    val impressHolder = ImpressHolder()
}

data class MultiComponentData(
    val componentType: String,
    val dataKey: String,
    val data: BaseWidgetUiModel<*>?
) {
    fun isError(): Boolean {
        return data == null || data.data?.error?.isNotEmpty().orFalse()
    }
}
