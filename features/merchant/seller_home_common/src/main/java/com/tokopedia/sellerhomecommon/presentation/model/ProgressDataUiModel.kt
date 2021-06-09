package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class ProgressDataUiModel(
        val valueTxt: String = "",
        val maxValueTxt: String = "",
        val value: Int = 0,
        val maxValue: Int = 0,
        val colorState: ShopScorePMWidget.State = ShopScorePMWidget.State.Good,
        val subtitle: String = "",
        override var dataKey: String = "",
        override var error: String = "",
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false
) : BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return value == 0
    }
}