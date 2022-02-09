package com.tokopedia.sellerhomecommon.presentation.view.adapter.listener

import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

interface DateFilterListener {

    fun onItemDateRangeClick(model: DateFilterItem)

    fun onApplyDateFilter()

    fun showDateTimePickerBottomSheet(bottomSheet: BottomSheetUnify, tag: String)

    fun showDateFilterBottomSheet()

    fun dismissDateFilterBottomSheet()
}