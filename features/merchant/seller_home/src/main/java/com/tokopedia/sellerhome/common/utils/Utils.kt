package com.tokopedia.sellerhome.common.utils

import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel

/**
 * Created By @ilhamsuaib on 2020-02-22
 */
 
object Utils {

    inline fun <reified T : BaseWidgetUiModel<*>> getWidgetDataKeys(widgets: List<BaseWidgetUiModel<*>>?): List<String> {
        return widgets.orEmpty().filterIsInstance<T>().map { it.dataKey }
    }
}