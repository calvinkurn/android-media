package com.tokopedia.sellerhomecommon.utils

import com.tokopedia.sellerhomecommon.presentation.model.BaseWidgetUiModel

/**
 * Created By @ilhamsuaib on 20/05/20
 */

object Utils {

    inline fun <reified T : BaseWidgetUiModel<*>> getWidgetDataKeys(widgets: List<BaseWidgetUiModel<*>>?): List<String> {
        return widgets.orEmpty().filterIsInstance<T>().map { it.dataKey }
    }

    fun fromHtmlWithoutExtraSpace(text: String): String {
        return text.replace(Regex("<p.*?>|</p.*?>|\\n*\$"), "")
    }
}