package com.tokopedia.product.detail.view.viewholder.gwp.event

import com.tokopedia.product.detail.view.componentization.ComponentEvent
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 30/11/23"
 * Project name: android-tokopedia-core
 **/

sealed interface GWPEvent : ComponentEvent {

    data class OnClickComponent(val data: GWPWidgetUiModel) : GWPEvent

    data class OnClickProduct(val data: GWPWidgetUiModel.Card.Product) : GWPEvent

    data class OnClickShowMore(val data: GWPWidgetUiModel.Card.LoadMore) : GWPEvent

    data class OnCardImpress(val card: GWPWidgetUiModel.Card) : GWPEvent
}
