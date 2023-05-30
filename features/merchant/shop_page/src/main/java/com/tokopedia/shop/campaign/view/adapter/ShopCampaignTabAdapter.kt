package com.tokopedia.shop.campaign.view.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.common.data.model.ShopPageWidgetLayoutUiModel
import com.tokopedia.shop.common.util.ShopUtil.setElement
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderBannerViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop_widget.common.util.WidgetState

class ShopCampaignTabAdapter(
    shopCampaignTabAdapterTypeFactory: ShopCampaignTabAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, AdapterTypeFactory>(shopCampaignTabAdapterTypeFactory),
    DataEndlessScrollListener.OnDataEndlessScrollListener {

    private var recyclerView: RecyclerView? = null

    fun setCampaignLayoutData(data: List<Visitable<*>>) {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.addAll(
            data.onEach {
                if (it is BaseShopHomeWidgetUiModel) {
                    it.widgetState = WidgetState.PLACEHOLDER
                }
            }
        )
        submitList(newList)
    }

    fun isAllWidgetLoading(): Boolean {
        // remove voucher visitable widget since it's loaded from different gql and finished earlier
        val filteredVisitables = visitables.filter { it !is ShopHomeVoucherUiModel }
        return filteredVisitables.filterIsInstance<Visitable<*>>().all {
            when (it) {
                is BaseShopHomeWidgetUiModel -> it.widgetState == WidgetState.PLACEHOLDER || it.widgetState == WidgetState.LOADING
                else -> false
            }
        }
    }

    override fun getEndlessDataSize(): Int {
        return Int.ZERO
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

    private fun submitList(newList: List<Visitable<*>>) {
        val diffCallback = ShopPageCampaignDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        newList.forEach {
            when (it) {
                is BaseShopHomeWidgetUiModel -> it.isNewData = false
            }
        }
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
        super.onBindViewHolder(holder, position)
    }

    fun removeShopHomeWidget(listShopWidgetLayout: List<ShopPageWidgetLayoutUiModel>) {
        val newList = getNewVisitableItems()
        listShopWidgetLayout.onEach { shopWidgetLayout ->
            newList.filterIsInstance<Visitable<*>>().indexOfFirst {
                when (it) {
                    is BaseShopHomeWidgetUiModel -> {
                        shopWidgetLayout.widgetId == it.widgetId
                    }

                    else -> {
                        false
                    }
                }
            }.let { position ->
                newList.removeAt(position)
            }
        }
        submitList(newList)
    }

    fun isLoadNextCampaignWidgetData(position: Int): Boolean {
        return visitables.filterIsInstance<BaseShopHomeWidgetUiModel>()
            .getOrNull(position)?.widgetState == WidgetState.PLACEHOLDER
    }

    fun updateShopCampaignWidgetStateToLoading(listWidgetLayout: MutableList<ShopPageWidgetLayoutUiModel>) {
        listWidgetLayout.onEach { widgetLayout ->
            visitables.filterIsInstance<Visitable<*>>().firstOrNull {
                when (it) {
                    is BaseShopHomeWidgetUiModel -> {
                        widgetLayout.widgetId == it.widgetId
                    }

                    else -> {
                        false
                    }
                }
            }?.let {
                when (it) {
                    is BaseShopHomeWidgetUiModel -> {
                        it.widgetState = WidgetState.LOADING
                    }
                }
            }
        }
    }

    fun isLoadFirstWidgetContentData(): Boolean {
        return visitables.filterIsInstance<Visitable<*>>().none {
            when (it) {
                is BaseShopHomeWidgetUiModel -> it.widgetState == WidgetState.LOADING || it.widgetState == WidgetState.FINISH
                else -> false
            }
        }
    }

    fun pauseSliderBannerAutoScroll() {
        val listSliderBannerViewModel = visitables.filterIsInstance<ShopHomeDisplayWidgetUiModel>().filter {
            it.name == WidgetName.SLIDER_BANNER
        }
        listSliderBannerViewModel.forEach {
            (recyclerView?.findViewHolderForAdapterPosition(visitables.indexOf(it)) as? ShopHomeSliderBannerViewHolder)?.pauseTimer()
        }
    }
    fun resumeSliderBannerAutoScroll() {
        val listSliderBannerViewModel = visitables.filterIsInstance<ShopHomeDisplayWidgetUiModel>().filter {
            it.name == WidgetName.SLIDER_BANNER
        }
        listSliderBannerViewModel.forEach {
            (recyclerView?.findViewHolderForAdapterPosition(visitables.indexOf(it)) as? ShopHomeSliderBannerViewHolder)?.resumeTimer()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    fun updateShopCampaignWidgetContentData(listWidgetContentData: Map<Pair<String, String>, Visitable<*>?>) {
        val newList = getNewVisitableItems()
        listWidgetContentData.onEach { widgetContentData ->
            newList.filterIsInstance<Visitable<*>>().indexOfFirst {
                when (it) {
                    is BaseShopHomeWidgetUiModel -> {
                        widgetContentData.key.first == it.widgetId
                    }
                    else -> {
                        false
                    }
                }
            }.let { position ->
                if (position >= 0 && position < newList.size) {
                    when (widgetContentData.value) {
                        null -> {
                            newList.removeAt(position)
                        }
                        is BaseShopHomeWidgetUiModel -> {
                            (widgetContentData.value as BaseShopHomeWidgetUiModel).widgetState = WidgetState.FINISH
                            (widgetContentData.value as BaseShopHomeWidgetUiModel).isNewData = true
                            newList.setElement(position, widgetContentData.value)
                        }
                    }
                }
            }
        }
        submitList(newList)
    }
}
