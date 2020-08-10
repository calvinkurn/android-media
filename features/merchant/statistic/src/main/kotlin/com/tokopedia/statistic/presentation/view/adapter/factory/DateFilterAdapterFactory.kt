package com.tokopedia.statistic.presentation.view.adapter.factory

import com.tokopedia.statistic.presentation.view.model.DateFilterItem

/**
 * Created By @ilhamsuaib on 15/06/20
 */

interface DateFilterAdapterFactory {

    fun type(item: DateFilterItem.Click): Int

    fun type(item: DateFilterItem.Pick): Int

    fun type(item: DateFilterItem.ApplyButton): Int

    fun type(divider: DateFilterItem.Divider): Int

    fun type(item: DateFilterItem.MonthPickerItem): Int
}