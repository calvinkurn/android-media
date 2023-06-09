package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.LayoutInflater
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.customview.ShopCampaignTabWidgetHeaderView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.databinding.ItemShopCampaignPlayWidgetBinding
import com.tokopedia.shop.databinding.ShopCampaignPlayTitleCustomViewBinding
import com.tokopedia.shop.home.view.listener.ShopHomePlayWidgetListener
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignCarouselPlayWidgetViewHolder(
    private val playWidgetViewHolder: PlayWidgetViewHolder,
    private val shopPlayWidgetListener: ShopHomePlayWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<CarouselPlayWidgetUiModel>(playWidgetViewHolder.itemView) {

    companion object{
        val LAYOUT = R.layout.item_shop_campaign_play_widget
        private val TV_PLAY_WIDGET_TITLE_ID =  com.tokopedia.play.widget.R.id.tv_play_widget_title
        private val TV_PLAY_WIDGET_ACTION_ID =  com.tokopedia.play.widget.R.id.tv_play_widget_action
    }

    private val viewBinding: ItemShopCampaignPlayWidgetBinding? by viewBinding()
    private var playWidgetView: PlayWidgetView? = viewBinding?.playWidgetView


    override fun bind(element: CarouselPlayWidgetUiModel) {
        setPlayWidget(element)
        setWidgetImpressionListener(element)
    }

    private fun setPlayWidget(element: CarouselPlayWidgetUiModel) {
        playWidgetView?.setCustomHeader(getCustomHeaderView())
        playWidgetViewHolder.bind(element.playWidgetState, this)
    }

    private fun getCustomHeaderView(): ShopCampaignTabWidgetHeaderView {
        val customHeader = ShopCampaignPlayTitleCustomViewBinding.inflate(
            LayoutInflater.from(itemView.context),
            playWidgetView,
            false
        )
        customHeader.headerView.setTitleId(TV_PLAY_WIDGET_TITLE_ID)
        customHeader.headerView.setCtaTextId(TV_PLAY_WIDGET_ACTION_ID)
        customHeader.headerView.showTitle()
        customHeader.headerView.configColorMode(shopCampaignInterface.isCampaignTabDarkMode())
        return customHeader.headerView
    }

    private fun setWidgetImpressionListener(model: CarouselPlayWidgetUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            shopPlayWidgetListener.onPlayWidgetImpression(model, bindingAdapterPosition)
        }
    }

}
