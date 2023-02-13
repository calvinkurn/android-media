package com.tokopedia.content.common.producttag.view.uimodel


/**
 * Created By : Jonathan Darwin on May 27, 2022
 */
data class SearchHeaderUiModel(
    val totalData: Int,
    val totalDataText: String,
    val responseCode: Int,
    val keywordProcess: String,
    val componentId: String,
)