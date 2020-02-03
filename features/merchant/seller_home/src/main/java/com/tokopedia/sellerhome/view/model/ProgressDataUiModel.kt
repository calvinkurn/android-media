package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget

data class ProgressDataUiModel(
        val barTitle: String,
        val valueTxt: String,
        val maxValueTxt: String,
        val value: Int,
        val maxValue: Int,
        val colorState: ShopScorePMWidget.State,
        val subtitle: String
)