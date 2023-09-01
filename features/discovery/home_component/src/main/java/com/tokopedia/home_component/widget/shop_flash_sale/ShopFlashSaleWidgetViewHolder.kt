package com.tokopedia.home_component.widget.shop_flash_sale

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.HomeComponentShopFlashSaleBinding
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.NpaLinearLayoutManager
import com.tokopedia.home_component.widget.common.carousel.CarouselListAdapter
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel.Companion.PAYLOAD_ITEM_LIST_CHANGED
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleItemDecoration
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleItemTypeFactoryImpl
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleShopListener
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopTabListener
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.view.HomeChannelHeaderListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class ShopFlashSaleWidgetViewHolder(
    itemView: View,
    val listener: ShopFlashSaleWidgetListener
): AbstractViewHolder<ShopFlashSaleWidgetDataModel>(itemView), ShopFlashSaleShopListener, ShopTabListener {

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.home_component.R.layout.home_component_shop_flash_sale
    }

    private val binding: HomeComponentShopFlashSaleBinding? by viewBinding()
    private val itemLayoutManager by lazy {
        NpaLinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL
        )
    }
    private val itemAdapter by lazy {
        CarouselListAdapter(
            ShopFlashSaleItemTypeFactoryImpl(listener, this),
            ShopFlashSaleItemDiffUtilCallback()
        )
    }

    private var shopFlashSaleWidgetDataModel: ShopFlashSaleWidgetDataModel? = null

    init {
        initItemAdapter()
    }

    override fun bind(element: ShopFlashSaleWidgetDataModel) {
        shopFlashSaleWidgetDataModel = element
        setChannelDivider(element.channelModel)
        setHeaderComponent(element.channelHeader, element.channelModel.trackingAttributionModel)
        updateTab(element.tabList)
        updateContent(element)
        renderTimer(element.timer)
    }

    override fun bind(element: ShopFlashSaleWidgetDataModel, payloads: MutableList<Any>) {
        shopFlashSaleWidgetDataModel = element
        val payload = payloads.firstOrNull() as? Bundle
        if(payload?.getBoolean(PAYLOAD_ITEM_LIST_CHANGED) == true) {
            updateContent(element)
            renderTimer(element.timer)
        } else {
            bind(element)
        }
    }

    private fun setChannelDivider(channelModel: ChannelModel) {
        binding?.let {
            ChannelWidgetUtil.validateHomeComponentDivider(
                channelModel = channelModel,
                dividerTop = it.homeComponentShopFlashSaleDividerHeader,
                dividerBottom = it.homeComponentShopFlashSaleDividerFooter
            )
        }
    }

    private fun setHeaderComponent(
        channelHeader: ChannelHeader,
        trackingAttributionModel: TrackingAttributionModel,
    ) {
        binding?.homeComponentShopFlashSaleHeaderView?.bind(
            channelHeader,
            object: HomeChannelHeaderListener {
                override fun onSeeAllClick(link: String) {
                    listener.onSeeAllClick(trackingAttributionModel, link)
                }
            }
        )
    }

    private fun updateTab(tabList: List<ShopFlashSaleTabDataModel>) {
        binding?.homeComponentShopFlashSaleTab?.submitList(tabList, this)
    }

    private fun updateContent(model: ShopFlashSaleWidgetDataModel) {
        itemAdapter.submitList(model.itemList as? List<Visitable<CommonCarouselProductCardTypeFactory>>)
    }

    private fun renderTimer(model: ShopFlashSaleTimerDataModel?) {
        try {
            if(model != null) {
                if (model.isLoading) {
                    binding?.shimmerTimerShopFlashSale?.show()
                    binding?.timerShopFlashSale?.invisible()
                } else {
                    binding?.timerShopFlashSale?.targetDate = ShopFlashSaleHelper.getCountdownTimer(model.expiredTime)
                    binding?.timerShopFlashSale?.show()
                    binding?.shimmerTimerShopFlashSale?.invisible()
                }
            } else {
                binding?.shimmerTimerShopFlashSale?.hide()
                binding?.timerShopFlashSale?.hide()
            }
        } catch (_: Exception) { }
    }

    private fun renderShimmerTimer() {
        if(shopFlashSaleWidgetDataModel?.timer != null) {
            binding?.shimmerTimerShopFlashSale?.show()
            binding?.timerShopFlashSale?.invisible()
        } else {
            binding?.shimmerTimerShopFlashSale?.hide()
            binding?.timerShopFlashSale?.hide()
        }
    }

    private fun initItemAdapter() {
        binding?.homeComponentShopFlashSaleItemRv?.apply {
            layoutManager = itemLayoutManager
            if(itemDecorationCount == 0) {
                addItemDecoration(ShopFlashSaleItemDecoration)
            }
            itemAnimator = null
            adapter = itemAdapter
        }
    }

    override fun onShopTabClick(element: ShopFlashSaleTabDataModel) {
        activateShopTab(element)
        renderShimmerTimer()
        shopFlashSaleWidgetDataModel?.let {
            listener.onShopTabClicked(it, element.trackingAttributionModel, element.channelGrid)
        }
    }

    override fun onRefreshClick() {
        renderShimmerTimer()
        shopFlashSaleWidgetDataModel?.let { model ->
            model.getActivatedTab()?.let { tab ->
                listener.onRefreshClick(model, tab.channelGrid)
            }
        }
    }

    private fun activateShopTab(element: ShopFlashSaleTabDataModel) {
        val widgetDataModel = shopFlashSaleWidgetDataModel ?: return
        val newList = widgetDataModel.tabList.map {
            it.copy(isActivated = it.channelGrid.id == element.channelGrid.id)
        }
        shopFlashSaleWidgetDataModel = widgetDataModel.copy(tabList = newList)
        updateTab(newList)
    }
}
