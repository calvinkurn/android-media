package com.tokopedia.product.detail.view.viewholder.gwp.event

import com.tokopedia.product.detail.view.componentization.ComponentEvent
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 30/11/23"
 * Project name: android-tokopedia-core
 **/

sealed interface GWPComponentEvent : ComponentEvent {
    data class OnClickTracking(val data: GWPWidgetUiModel) : GWPComponentEvent
}
