package com.tokopedia.feedplus.browse.presentation.model

/**
 * Created by meyta.taliti on 31/08/23.
 */
data class FeedBrowseChipUiModel(
    val id: String,
    val label: String,
    val extraParams: Map<String, Any> = emptyMap(),
    val isSelected: Boolean
)
