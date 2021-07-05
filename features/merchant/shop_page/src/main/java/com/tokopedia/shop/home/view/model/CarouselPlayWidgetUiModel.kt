package com.tokopedia.shop.home.view.model

import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.shop.home.util.Event
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory


/**
 * Created by mzennis on 13/10/20.
 */
data class CarouselPlayWidgetUiModel(
        override val widgetId: String,
        override val layoutOrder: Int,
        override val name: String,
        override val type: String,
        override val header: BaseShopHomeWidgetUiModel.Header,
        val actionEvent: Event<Action> = Event(Action.Refresh),
        val widgetUiModel: PlayWidgetUiModel = PlayWidgetUiModel.Placeholder
) : BaseShopHomeWidgetUiModel {

    sealed class Action {

        object Refresh : Action()
        data class Delete(val channelId: String) : Action()
        data class DeleteFailed(val channelId: String, val reason: Throwable) : Action()
    }

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}