package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.customview.ShopCampaignTabWidgetHeaderView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.listener.ShopCampaignPlayWidgetListener
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.ItemShopCampaignPlayWidgetBinding
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignCarouselPlayWidgetViewHolder(
    itemView: View,
    private val playWidgetViewHolder: PlayWidgetViewHolder,
    private val shopPlayWidgetListener: ShopCampaignPlayWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<CarouselPlayWidgetUiModel>(itemView) {

    companion object{
        val LAYOUT = R.layout.item_shop_campaign_play_widget
    }

    private val viewBinding: ItemShopCampaignPlayWidgetBinding? by viewBinding()
    private var headerView: ShopCampaignTabWidgetHeaderView? = viewBinding?.headerView
    private var playWidgetView: PlayWidgetView? = viewBinding?.playWidgetView

    override fun bind(element: CarouselPlayWidgetUiModel) {
        setHeader(element)
        setPlayWidget(element)
        setPlayWidgetItemAnalyticListener(element)
        setPlayWidgetImpressionListener(element)
    }

    private fun setPlayWidgetImpressionListener(element: CarouselPlayWidgetUiModel) {
        itemView.addOnImpressionListener(element.impressHolder) {
            shopPlayWidgetListener.onImpressionPlayWidget(
                element,
                ShopUtil.getActualPositionFromIndex(bindingAdapterPosition)
            )
        }
    }

    private fun setHeader(uiModel: CarouselPlayWidgetUiModel) {
        val title = uiModel.header.title
        if (title.isEmpty()) {
            headerView?.hide()
        } else {
            headerView?.show()
            headerView?.setTitle(title)
            headerView?.configColorMode(shopCampaignInterface.isCampaignTabDarkMode())
        }
    }

    private fun setPlayWidgetItemAnalyticListener(element: CarouselPlayWidgetUiModel) {
        playWidgetView?.setAnalyticListener(object : PlayWidgetAnalyticListener {
            override fun onImpressChannelCard(
                view: PlayWidgetMediumView,
                item: PlayWidgetChannelUiModel,
                config: PlayWidgetConfigUiModel,
                channelPositionInList: Int
            ) {
                shopPlayWidgetListener.onPlayWidgetItemImpression(
                    element,
                    item,
                    ShopUtil.getActualPositionFromIndex(channelPositionInList)
                )
            }

            override fun onClickChannelCard(
                view: PlayWidgetMediumView,
                item: PlayWidgetChannelUiModel,
                config: PlayWidgetConfigUiModel,
                channelPositionInList: Int
            ) {
                shopPlayWidgetListener.onPlayWidgetItemClick(
                    element,
                    item,
                    ShopUtil.getActualPositionFromIndex(channelPositionInList)
                )
            }
        })
    }

    private fun setPlayWidget(element: CarouselPlayWidgetUiModel) {
        playWidgetViewHolder.bind(element.playWidgetState, this)
    }

}
