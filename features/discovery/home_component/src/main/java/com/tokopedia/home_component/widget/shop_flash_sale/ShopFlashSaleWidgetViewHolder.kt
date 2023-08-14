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
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

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
        LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }
    private val itemLayoutManager by lazy {
        LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
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
            ShopFlashSaleItemTypeFactoryImpl(),
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
    }

    override fun bind(element: ShopFlashSaleWidgetDataModel, payloads: MutableList<Any>) {
        shopFlashSaleWidgetDataModel = element
        if((payloads.firstOrNull() as? Bundle)?.getBoolean(PAYLOAD_ITEM_LIST_CHANGED) == true) {
            updateContent(element)
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

    private fun renderShopHeader(shop: ShopFlashSaleTabDataModel) {
        binding?.txtShopFlashSaleShopName?.text = shop.channelGrid.name
        val shopNameClickListener = View.OnClickListener {
            listener.onShopNameClicked(shop.trackingAttributionModel, shop.channelGrid)
        }
        binding?.txtShopFlashSaleShopName?.setOnClickListener(shopNameClickListener)
        binding?.ctaShopFlashSaleShopName?.setOnClickListener(shopNameClickListener)
    }

    private fun renderTimer(model: ShopFlashSaleWidgetDataModel) {
        val expiredTime = DateHelper.getExpiredTime(model.endTime)
        if (!DateHelper.isExpired(0, expiredTime)) {
            binding?.timerShopFlashSale?.run {
                // calculate date diff
                targetDate = Calendar.getInstance().apply {
                    val currentDate = Date()
                    val currentMillisecond: Long = currentDate.time + 0
                    val timeDiff = expiredTime.time - currentMillisecond
                    add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                    add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                    add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
                }
                onFinish = {
                }
            }
        }
    }

    private fun updateTab(model: ShopFlashSaleWidgetDataModel) {
        tabAdapter.submitList(model.tabList)
    }

    private fun updateContent(model: ShopFlashSaleWidgetDataModel) {
        itemAdapter.submitList(model.itemList)
        getActivatedTab(model.tabList)?.let { renderShopHeader(it) }
        renderTimer(model)
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

    private fun getActivatedTab(list: List<ShopFlashSaleTabDataModel>) : ShopFlashSaleTabDataModel? {
        return list.firstOrNull { it.isActivated }
    }

    override fun onShopTabClick(element: ShopFlashSaleTabDataModel) {
//        if(shopFlashSaleWidgetDataModel?.findShopTab(element.channelGrid.id)?.isActivated == false) {
//
//        }
        shopFlashSaleWidgetDataModel?.let { activateShopTab(it, element) }
        renderShopHeader(element)
        shopFlashSaleWidgetDataModel?.let {
            listener.onShopTabClicked(it, element.trackingAttributionModel, element.channelGrid)
        }
    }

    private fun activateShopTab(widgetDataModel: ShopFlashSaleWidgetDataModel, element: ShopFlashSaleTabDataModel) {
        val newList = widgetDataModel.tabList.map {
            it.copy(isActivated = it.channelGrid.id == element.channelGrid.id)
        }
        shopFlashSaleWidgetDataModel = widgetDataModel.copy(tabList = newList)
        tabAdapter.submitList(newList)
    }
}
