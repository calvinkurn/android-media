package com.tokopedia.statistic.presentation.view.adapter.factory

import com.tokopedia.statistic.presentation.model.DateRangeItem

/**
 * Created By @ilhamsuaib on 15/06/20
 */

interface DateRangeAdapterFactory {

    fun type(item: DateRangeItem.Click): Int

    fun type(item: DateRangeItem.Pick): Int

    fun type(item: DateRangeItem.ApplyButton): Int

    fun type(divider: DateRangeItem.Divider): Int
}