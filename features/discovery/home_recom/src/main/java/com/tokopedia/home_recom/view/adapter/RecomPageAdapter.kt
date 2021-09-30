package com.tokopedia.home_recom.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.home_recom.util.RecomPageConstant
import com.tokopedia.home_recom.view.viewholder.FirstLoadViewHolder
import com.tokopedia.home_recom.view.viewholder.LoadMoreViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationCPMViewHolder
import com.tokopedia.home_recom.view.viewholder.RecommendationShimmeringViewHolder
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener

/**
 * Created by yfsx on 01/09/21.
 */
class RecomPageAdapter(asyncDifferConfig: AsyncDifferConfig<HomeRecommendationDataModel>,
                       private val listener: RecommendationListener?,
                       private val adapterTypeFactory: HomeRecommendationTypeFactoryImpl) :
        ListAdapter<HomeRecommendationDataModel, AbstractViewHolder<*>>(asyncDifferConfig) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = onCreateViewItem(parent, viewType)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        bind(holder as AbstractViewHolder<HomeRecommendationDataModel>, getItem(position), position)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            bind(holder as AbstractViewHolder<HomeRecommendationDataModel>, getItem(position), payloads, position)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(viewType, parent, false)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= currentList.size) {
            HideViewHolder.LAYOUT
        } else currentList[position]?.type(adapterTypeFactory) ?: HideViewHolder.LAYOUT
    }

    fun bind(holder: AbstractViewHolder<HomeRecommendationDataModel>, item: HomeRecommendationDataModel, position: Int) {
        holder.configureSpan(position)
        holder.bind(item)
    }

    fun bind(holder: AbstractViewHolder<HomeRecommendationDataModel>, item: HomeRecommendationDataModel, payloads: MutableList<Any>, position: Int) {
        val payloadInt = (payloads.firstOrNull() as? Bundle)?.getInt(RecomPageConstant.DIFFUTIL_PAYLOAD)
        holder.configureSpan(position)
        if (payloads.isNotEmpty() && payloads.firstOrNull() != null && payloadInt != null) {
            holder.bind(item, listOf(payloadInt))
        } else {
            holder.bind(item)
        }
    }

    private fun AbstractViewHolder<HomeRecommendationDataModel>.configureSpan(position: Int) {
        val layout = this.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when (getItemViewType(position)) {
            ProductInfoDataModel.LAYOUT -> layout.isFullSpan = true
            RecommendationCarouselDataModel.LAYOUT -> layout.isFullSpan = true
            RecommendationCPMViewHolder.LAYOUT -> layout.isFullSpan = true
            TitleDataModel.LAYOUT -> layout.isFullSpan = true
            EmptyViewHolder.LAYOUT -> layout.isFullSpan = true
            RecommendationShimmeringViewHolder.LAYOUT -> layout.isFullSpan = true
            RecommendationErrorDataModel.LAYOUT -> layout.isFullSpan = true
            LoadMoreViewHolder.LAYOUT -> layout.isFullSpan = true
            FirstLoadViewHolder.LAYOUT -> layout.isFullSpan = true
        }
    }

    fun showFirstLoading() {
        if (!isLoading()) {
            submitList(listOf(FirstLoadDataModel()))
        }
    }

    fun removeFirstLoading() {
        submitList(mutableListOf())
    }

    fun appendLoadingForLoadMore() {
        if (!isLoading()) {
            val newList = mutableListOf(currentList)
            newList.add(listOf(LoadMoreDataModel()))
            submitList(currentList)
        }
    }

    fun removeLoadingForLoadMore() {
        val newList = mutableListOf<HomeRecommendationDataModel>()
        currentList.forEach { data -> if (data !is LoadMoreDataModel) newList.add(data) }
        submitList(newList)
    }

    fun showError(data: RecommendationErrorDataModel) {
        submitList(listOf(data))
    }

    fun showEmpty(data: RecommendationErrorDataModel) {
        submitList(listOf(data))
    }

    fun clearAllElements() {
        submitList(listOf())
    }

    private fun isLoading(): Boolean {
        val lastIndex = if (currentList.size == 0) -1 else currentList.size - 1
        return if (lastIndex > -1) {
            currentList[lastIndex] is LoadingModel ||
                    currentList[lastIndex] is LoadingMoreModel
        } else {
            false
        }
    }
}