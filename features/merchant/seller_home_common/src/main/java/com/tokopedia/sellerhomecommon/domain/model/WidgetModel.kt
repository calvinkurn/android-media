package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created By @ilhamsuaib on 21/05/20
 */

data class WidgetModel(
    @SerializedName("ID")
    val id: Long?,
    @SerializedName("applink")
    val appLink: String?,
    @SerializedName("ctaText")
    val ctaText: String?,
    @SerializedName("gridSize")
    val gridSize: Int?,
    @SerializedName("maxData")
    val maxData: Int? = 0,
    @SerializedName("maxDisplay")
    val maxDisplay: Int? = 0,
    @SerializedName("dataKey")
    val dataKey: String?,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("tooltip")
    val tooltip: TooltipModel,
    @SerializedName("tag")
    val tag: String? = "",
    @SerializedName("title")
    val title: String?,
    @SerializedName("widgetType")
    val widgetType: String?,
    @SerializedName("showEmpty")
    val isShowEmpty: Boolean?,
    @SerializedName("postFilter")
    val postFilter: List<PostFilterModel>?,
    @SerializedName("emptyState")
    val emptyStateModel: WidgetEmptyStateModel,
    @SerializedName("comparePeriode")
    val isComparePeriodOnly: Boolean = false,
    @SerializedName("searchTableColumnFilter")
    val searchTableColumnFilters: List<SearchTableColumnFilterModel>?,
    @SerializedName("isDismissible")
    val isDismissible: Boolean = false,
    @SerializedName("dismissibleState")
    val dismissibleState: String = String.EMPTY,
    @SerializedName("useRealtime")
    val useRealtime: Boolean = false,
)
