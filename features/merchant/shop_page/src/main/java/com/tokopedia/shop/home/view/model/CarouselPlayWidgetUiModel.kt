package com.tokopedia.shop.home.view.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.shop.campaign.view.adapter.ShopCampaignTabAdapterTypeFactory
import com.tokopedia.shop.home.util.Event
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory

/**
 * Created by mzennis on 13/10/20.
 */
data class CarouselPlayWidgetUiModel(
    override val widgetId: String,
    override val layoutOrder: Int,
    override val name: String,
    override val type: String,
    override val header: BaseShopHomeWidgetUiModel.Header,
    override val isFestivity: Boolean = false,
    val actionEvent: Event<Action> = Event(Action.Refresh),
    val playWidgetState: PlayWidgetState = PlayWidgetState(isLoading = true)
) : BaseShopHomeWidgetUiModel() {
    val impressHolder = ImpressHolder()

    sealed class Action {

        object Refresh : Action()
        data class Delete(val channelId: String) : Action()
        data class DeleteFailed(val channelId: String, val reason: Throwable) : Action()
    }

    companion object{
        private const val LINK_TYPE_CAMPAIGN_ID = "campaign"
    }

    override fun type(typeFactory: ShopWidgetTypeFactory): Int {
        return when (typeFactory) {
            is ShopHomeAdapterTypeFactory -> {
                typeFactory.type(this)
            }

            is ShopCampaignTabAdapterTypeFactory -> {
                typeFactory.type(this)
            }

            else -> {
                Int.ZERO
            }
        }
    }

    fun getCampaignId(): String {
        return header.data.firstOrNull { it.linkType == LINK_TYPE_CAMPAIGN_ID }?.linkId?.toString().orEmpty()
    }
}
