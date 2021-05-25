package com.tokopedia.power_merchant.subscribe.view.model

/**
 * Created By @ilhamsuaib on 25/02/21
 */

data class PmFeatureUiModel(
        val imageResId: Int,
        val title: String,
        val description: String,
        val clickableText: String = "",
        val clickableUrl: String = "",
        val boldTextList: List<String> = emptyList()
)