package com.tokopedia.statistic.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created By @ilhamsuaib on 11/02/21
 */

@Parcelize
data class StatisticPageUiModel(
        val pageTitle: String = "",
        val pageSource: String = "",
        val tickerPageName: String = "",
        val tag: String = "",
        val actionMenu: List<ActionMenuUiModel> = emptyList(),
        val dateFilters: List<DateFilterItem> = emptyList(),
        val exclusiveIdentifierDateFilterDesc: String = ""
) : Parcelable