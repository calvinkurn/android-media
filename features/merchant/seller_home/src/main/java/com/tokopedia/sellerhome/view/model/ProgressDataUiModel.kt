package com.tokopedia.sellerhome.view.model

import com.tokopedia.sellerhome.view.widget.ShopScorePMWidget

data class ProgressDataUiModel(
        val valueTxt: String = "",
        val maxValueTxt: String = "",
        val value: Int= 0,
        val maxValue: Int = 0,
        val colorState: ShopScorePMWidget.State = ShopScorePMWidget.State.GREEN,
        override var error: String = "",
        val subtitle: String =""
): BaseDataUiModel