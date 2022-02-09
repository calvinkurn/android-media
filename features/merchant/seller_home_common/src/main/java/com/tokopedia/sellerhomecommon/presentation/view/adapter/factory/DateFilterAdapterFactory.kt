package com.tokopedia.sellerhomecommon.presentation.view.adapter.factory

import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

interface DateFilterAdapterFactory {

    fun type(item: DateFilterItem.Click): Int

    fun type(item: DateFilterItem.Pick): Int

    fun type(item: DateFilterItem.ApplyButton): Int

    fun type(divider: DateFilterItem.Divider): Int

    fun type(item: DateFilterItem.MonthPickerItem): Int
}