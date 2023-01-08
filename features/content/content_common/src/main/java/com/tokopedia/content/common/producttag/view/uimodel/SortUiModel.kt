package com.tokopedia.content.common.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 20, 2022
 */
data class SortUiModel(
    val text: String,
    val key: String,
    val value: String,
    val isSelected: Boolean,
) {

    fun isSame(sort: SortUiModel) = (key == sort.key && value == sort.value)
}