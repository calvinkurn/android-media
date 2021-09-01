package com.tokopedia.home_recom.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationErrorDataModel
import com.tokopedia.home_recom.util.RecomPageConstant
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
        bind(holder as AbstractViewHolder<HomeRecommendationDataModel>, getItem(position))
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            bind(holder as AbstractViewHolder<HomeRecommendationDataModel>, getItem(position), payloads)
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

    fun bind(holder: AbstractViewHolder<HomeRecommendationDataModel>, item: HomeRecommendationDataModel) {
        holder.bind(item)
    }

    fun bind(holder: AbstractViewHolder<HomeRecommendationDataModel>, item: HomeRecommendationDataModel, payloads: MutableList<Any>) {
        val payloadInt = (payloads.firstOrNull() as? Bundle)?.getInt(RecomPageConstant.DIFFUTIL_PAYLOAD)
        if (payloads.isNotEmpty() && payloads.firstOrNull() != null && payloadInt != null) {
            holder.bind(item, listOf(payloadInt))
        } else {
            holder.bind(item)
        }
    }

//    fun showLoading() {
//        if (!isLoading()) {
//            submitList(listOf(LoadingMoreModel()))
//        }
//    }

    fun showError(data: RecommendationErrorDataModel) {
        submitList(listOf(data))
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