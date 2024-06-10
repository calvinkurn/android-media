package com.tokopedia.home.beranda.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.HomeRecommendationUtil.isFullSpan
import com.tokopedia.recommendation_widget_common.infinite.foryou.recom.RecommendationCardModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.EmptyStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ErrorStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.LoadMoreStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.RetryButtonStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.state.model.ShimmeringStateModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.ForYouRecommendationDiffUtil

class GlobalHomeRecommendationAdapter(
    private val adapterTypeFactory: ForYouRecommendationTypeFactoryImpl
) : ListAdapter<ForYouRecommendationVisitable, AbstractViewHolder<Visitable<*>>>(
    AsyncDifferConfig.Builder(ForYouRecommendationDiffUtil())
        .build()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType) as AbstractViewHolder<Visitable<*>>
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<Visitable<*>>,
        position: Int
    ) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when (val item = getItem(position)) {
            is ShimmeringStateModel,
            is ErrorStateModel,
            is EmptyStateModel,
            is LoadMoreStateModel -> layout.isFullSpan = true
            is RetryButtonStateModel -> layout.isFullSpan = true
            is RecommendationCardModel -> layout.isFullSpan = item.isFullSpan()
        }
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<Visitable<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(getItem(holder.bindingAdapterPosition), payloads)
        } else {
            super.onBindViewHolder(holder, holder.bindingAdapterPosition, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type(adapterTypeFactory)
    }
}
