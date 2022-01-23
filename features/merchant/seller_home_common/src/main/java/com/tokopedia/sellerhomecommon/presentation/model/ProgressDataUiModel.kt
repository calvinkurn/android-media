package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.presentation.view.customview.ShopScorePMWidget

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class ProgressDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    override val lastUpdated: Long = 0,
    val valueTxt: String = "",
    val maxValueTxt: String = "",
    val value: Int = 0,
    val maxValue: Int = 0,
    val colorState: ShopScorePMWidget.State = ShopScorePMWidget.State.Good,
    val subtitle: String = ""
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return value == 0
    }
}