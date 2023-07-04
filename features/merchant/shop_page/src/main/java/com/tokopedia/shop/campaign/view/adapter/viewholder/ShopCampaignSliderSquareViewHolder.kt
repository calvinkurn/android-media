package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.customview.ShopCampaignTabWidgetHeaderView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.databinding.WidgetShopPageCampaignSliderSquareBinding
import com.tokopedia.shop.home.view.adapter.PaddingItemDecorationShopPage
import com.tokopedia.shop.home.view.adapter.ShopHomeSliderSquareAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignSliderSquareViewHolder(
    itemView: View,
    private val listener: ShopHomeDisplayWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_page_campaign_slider_square
    }
    private val viewBinding: WidgetShopPageCampaignSliderSquareBinding? by viewBinding()
    private var shopHomeSliderSquareAdapter: ShopHomeSliderSquareAdapter? = null
    private var rvCarouselShopPageHome: RecyclerView? = viewBinding?.rvCarouselShopPageHome
    private var headerView: ShopCampaignTabWidgetHeaderView? = viewBinding?.headerView

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {
        setHeader(element)
        val linearLayoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        shopHomeSliderSquareAdapter = ShopHomeSliderSquareAdapter(listener)
        rvCarouselShopPageHome?.apply {
            isNestedScrollingEnabled = false
            layoutManager = linearLayoutManager
            if (itemDecorationCount == 0) {
                addItemDecoration(PaddingItemDecorationShopPage(element.name))
            }
            adapter = shopHomeSliderSquareAdapter
        }
        shopHomeSliderSquareAdapter?.displayWidgetUiModel = element
        shopHomeSliderSquareAdapter?.heightRatio = getHeightRatio(element)
        shopHomeSliderSquareAdapter?.parentPosition = adapterPosition
        shopHomeSliderSquareAdapter?.submitList(element.data)
        setWidgetImpressionListener(element)
    }

    private fun getIndexRatio(data: ShopHomeDisplayWidgetUiModel, index: Int): Int {
        return data.header.ratio.split(":").getOrNull(index).toIntOrZero()
    }

    private fun getHeightRatio(data: ShopHomeDisplayWidgetUiModel): Float {
        val indexZero = getIndexRatio(data, 0).toFloat()
        val indexOne = getIndexRatio(data, 1).toFloat()
        return (indexOne / indexZero)
    }

    private fun setHeader(uiModel: ShopHomeDisplayWidgetUiModel) {
        val title = uiModel.header.title
        if (title.isEmpty()) {
            headerView?.hide()
        } else {
            headerView?.show()
            headerView?.setTitle(title)
            headerView?.configColorMode(shopCampaignInterface.isCampaignTabDarkMode())
        }
    }

    private fun setWidgetImpressionListener(model: ShopHomeDisplayWidgetUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            listener.onDisplayWidgetImpression(model, bindingAdapterPosition)
        }
    }
}
