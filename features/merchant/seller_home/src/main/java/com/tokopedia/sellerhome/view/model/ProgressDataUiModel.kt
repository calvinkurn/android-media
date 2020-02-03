package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget

data class ProgressDataUiModel(
        val valueTxt: String = "",
        val maxValueTxt: String = "",
        val value: Int= 0,
        val maxValue: Int = 0,
        val state: ShopScorePMWidget.State = ShopScorePMWidget.State.RED,
        val subtitle: String = "",
        val error: String = ""
): BaseDataUiModel