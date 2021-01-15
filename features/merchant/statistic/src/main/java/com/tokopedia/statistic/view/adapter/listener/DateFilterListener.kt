package com.tokopedia.statistic.view.adapter.listener

import com.tokopedia.statistic.view.model.DateFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By @ilhamsuaib on 11/11/20
 */

interface DateFilterListener {

    fun onItemDateRangeClick(model: DateFilterItem)

    fun onApplyDateFilter()

    fun showDateTimePickerBottomSheet(bottomSheet: BottomSheetUnify, tag: String)

    fun showDateFilterBottomSheet()

    fun dismissDateFilterBottomSheet()
}