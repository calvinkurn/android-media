package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import com.tokopedia.sellerhomecommon.presentation.model.BaseDismissibleWidgetUiModel

/**
 * Created by @ilhamsuaib on 08/09/22.
 */

interface WidgetDismissalListener {

    fun setOnWidgetCancelDismissal(element: BaseDismissibleWidgetUiModel<*>) {}
}