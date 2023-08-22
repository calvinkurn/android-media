package com.tokopedia.home_component.widget.shop_flash_sale

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.HomeComponentShopFlashSaleBinding
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.home_component.util.NpaLinearLayoutManager
import com.tokopedia.home_component.widget.common.CarouselListAdapter
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil
import com.tokopedia.home_component.widget.shop_flash_sale.ShopFlashSaleWidgetDataModel.Companion.PAYLOAD_ITEM_LIST_CHANGED
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleItemDecoration
import com.tokopedia.home_component.widget.shop_flash_sale.item.ShopFlashSaleItemTypeFactoryImpl
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabAdapter
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDataModel
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabDiffUtil
import com.tokopedia.home_component.widget.shop_flash_sale.tab.ShopFlashSaleTabListener
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.view.HomeChannelHeaderListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class ShopFlashSaleWidgetViewHolder(
    itemView: View,
    val listener: ShopFlashSaleListener
): AbstractViewHolder<ShopFlashSaleWidgetDataModel>(itemView), ShopFlashSaleTabListener {

    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.home_component.R.layout.home_component_shop_flash_sale
    }

    private val binding: HomeComponentShopFlashSaleBinding? by viewBinding()
    private val tabLayoutManager by lazy {
        NpaLinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL
        )
    }
    private val itemLayoutManager by lazy {
        NpaLinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL
        )
    }
    private val tabAdapter by lazy {
        ShopFlashSaleTabAdapter(
            ShopFlashSaleTabDiffUtil(),
            this
        )
    }
    private val itemAdapter by lazy {
        CarouselListAdapter(
            ShopFlashSaleItemTypeFactoryImpl(listener),
            HomeComponentCarouselDiffUtil()
        )
    }

    private var shopFlashSaleWidgetDataModel: ShopFlashSaleWidgetDataModel? = null

    init {
        initShopTabAdapter()
        initItemAdapter()
    }

    override fun bind(element: ShopFlashSaleWidgetDataModel) {
        shopFlashSaleWidgetDataModel = element
        setChannelDivider(element.channelModel)
        setHeaderComponent(element.channelHeader, element.channelModel.trackingAttributionModel)
        updateTab(element)
        updateContent(element)
        updateShopHeader(element)
    }

    override fun bind(element: ShopFlashSaleWidgetDataModel, payloads: MutableList<Any>) {
        shopFlashSaleWidgetDataModel = element
        val payload = payloads.firstOrNull() as? Bundle
        if(payload?.getBoolean(PAYLOAD_ITEM_LIST_CHANGED) == true) {
            updateContent(element)
            updateShopHeader(element)
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

    private fun updateTab(model: ShopFlashSaleWidgetDataModel) {
        tabAdapter.submitList(model.tabList)
    }

    private fun updateContent(model: ShopFlashSaleWidgetDataModel) {
        itemAdapter.submitList(model.itemList)
    }

    private fun updateShopHeader(model: ShopFlashSaleWidgetDataModel) {
        if(model.useShopHeader) {
            binding?.containerShopFlashSaleShopHeader?.show()
            renderShopName()
            if(!model.isLoadingState()) renderTimer(model.timer)
        } else binding?.containerShopFlashSaleShopHeader?.hide()
    }

    private fun renderShopName() {
        if(shopFlashSaleWidgetDataModel?.useShopHeader == true) {
            val shopTab = getActivatedTab() ?: return
            binding?.txtShopFlashSaleShopName?.text = shopTab.channelGrid.name
            val shopNameClickListener = View.OnClickListener {
                listener.onShopNameClicked(shopTab.trackingAttributionModel, shopTab.channelGrid)
            }
            binding?.txtShopFlashSaleShopName?.setOnClickListener(shopNameClickListener)
            binding?.ctaShopFlashSaleShopName?.setOnClickListener(shopNameClickListener)
        }
    }

    private fun renderTimer(model: ShopFlashSaleTimerDataModel) {
        try {
            if(shopFlashSaleWidgetDataModel?.useShopHeader == true) {
                if(model.endDate.isEmpty()) {
                    hideTimer()
                } else {
                    binding?.timerShopFlashSale?.run {
                        if (!DateHelper.isExpired(expiredTime = model.expiredTime)) {
                            targetDate = DateHelper.getCountdownTimer(model.expiredTime)
                            show()
                        } else hide()
                    }
                }
            }
        } catch (_: Exception) { }
    }

    private fun hideTimer() {
        binding?.timerShopFlashSale?.hide()
    }

    private fun initShopTabAdapter() {
        binding?.homeComponentShopFlashSaleTabRv?.apply {
            layoutManager = tabLayoutManager
            itemAnimator = null
            adapter = tabAdapter
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
        renderShopName()
        hideTimer()
        shopFlashSaleWidgetDataModel?.let {
            listener.onShopTabClicked(it, element.trackingAttributionModel, element.channelGrid)
        }
    }

    private fun activateShopTab(element: ShopFlashSaleTabDataModel) {
        val widgetDataModel = shopFlashSaleWidgetDataModel ?: return
        val newList = widgetDataModel.tabList.map {
            it.copy(isActivated = it.channelGrid.id == element.channelGrid.id)
        }
        shopFlashSaleWidgetDataModel = widgetDataModel.copy(tabList = newList)
        tabAdapter.submitList(newList)
    }

    private fun getActivatedTab(): ShopFlashSaleTabDataModel? {
        return shopFlashSaleWidgetDataModel?.tabList?.firstOrNull { it.isActivated }
    }
}
