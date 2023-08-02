package com.tokopedia.shop.campaign.view.adapter

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignSliderBannerViewHolder
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.util.ShopUtil.setElement
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeSliderBannerViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeVoucherUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel

class ShopCampaignTabAdapter(
    shopCampaignTabAdapterTypeFactory: ShopCampaignTabAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, AdapterTypeFactory>(shopCampaignTabAdapterTypeFactory),
    DataEndlessScrollListener.OnDataEndlessScrollListener {

    companion object{
        private const val INVALID_INDEX = -1
    }

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

    fun getNewVisitableItems() = visitables.toMutableList()

    fun submitList(newList: List<Visitable<*>>) {
        val currentRecyclerViewState: Parcelable? = recyclerView?.layoutManager?.onSaveInstanceState()
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
        currentRecyclerViewState?.let {
            recyclerView?.layoutManager?.onRestoreInstanceState(it)
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = true
        }
        super.onBindViewHolder(holder, position)
    }

    fun removeShopHomeWidget(listShopWidgetLayout: List<ShopPageWidgetUiModel>) {
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

    fun updateShopCampaignWidgetStateToLoading(listWidgetLayout: MutableList<ShopPageWidgetUiModel>) {
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
        val listSliderBannerViewModel =
            visitables.filterIsInstance<ShopHomeDisplayWidgetUiModel>().filter {
                it.name == WidgetName.SLIDER_BANNER
            }
        listSliderBannerViewModel.forEach {
            (recyclerView?.findViewHolderForAdapterPosition(visitables.indexOf(it)) as? ShopCampaignSliderBannerViewHolder)?.pauseTimer()
        }
    }

    fun resumeSliderBannerAutoScroll() {
        val listSliderBannerViewModel =
            visitables.filterIsInstance<ShopHomeDisplayWidgetUiModel>().filter {
                it.name == WidgetName.SLIDER_BANNER
            }
        listSliderBannerViewModel.forEach {
            (recyclerView?.findViewHolderForAdapterPosition(visitables.indexOf(it)) as? ShopCampaignSliderBannerViewHolder)?.resumeTimer()
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
                            (widgetContentData.value as BaseShopHomeWidgetUiModel).widgetState =
                                WidgetState.FINISH
                            (widgetContentData.value as BaseShopHomeWidgetUiModel).isNewData = true
                            newList.setElement(position, widgetContentData.value)
                        }
                    }
                }
            }
        }
        submitList(newList)
    }

    fun getVoucherSliderUiModel(): ShopWidgetVoucherSliderUiModel? {
        return visitables.filterIsInstance<ShopWidgetVoucherSliderUiModel>().firstOrNull()
    }

    fun setHomeYouTubeData(widgetId: String, data: YoutubeVideoDetailModel) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopHomeDisplayWidgetUiModel>()
            .find {
                it.widgetId == widgetId
            }?.let {
                it.data?.firstOrNull()?.youTubeVideoDetail = data
                it.isNewData = true
            }
        submitList(newList)
    }

    fun getPlayWidgetUiModel(): CarouselPlayWidgetUiModel? {
        return visitables.filterIsInstance<CarouselPlayWidgetUiModel>().firstOrNull()
    }

    fun updatePlayWidget(playWidgetState: PlayWidgetState?) {
        val newList = getNewVisitableItems()
        newList.indexOfFirst { it is CarouselPlayWidgetUiModel }.let { position ->
            if (position == -1) return@let
            if (playWidgetState == null || playWidgetState.isLoading || isPlayWidgetEmpty(playWidgetState.model)) {
                newList.removeAt(position)
            } else {
                (newList.getOrNull(position) as? CarouselPlayWidgetUiModel)?.copy(playWidgetState = playWidgetState)?.apply {
                    widgetState = WidgetState.FINISH
                    isNewData = true
                }?.also {
                    newList.setElement(position, it)
                }
            }
        }
        submitList(newList)
    }

    private fun isPlayWidgetEmpty(widget: PlayWidgetUiModel): Boolean {
        return widget.items.isEmpty()
    }

    fun removeWidget(model: Visitable<*>) {
        val newList = getNewVisitableItems()
        val modelIndex = newList.indexOf(model)
        if (modelIndex != INVALID_INDEX) {
            newList.remove(model)
            submitList(newList)
        }
    }

    override fun showLoading() {
        if (!isLoading) {
            val newList = getNewVisitableItems()
            if (isShowLoadingMore) {
                newList.add(loadingMoreModel)
            } else {
                newList.clear()
                newList.add(loadingModel)
            }
            submitList(newList)
        }
    }

}
