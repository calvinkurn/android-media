package com.tokopedia.home_recom.view.adapter

import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.home_recom.model.datamodel.HomeRecommendationDataModel
import com.tokopedia.home_recom.model.datamodel.ProductInfoDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.datamodel.TitleDataModel

class HomeRecommendationAdapter(
        private val adapterTypeFactory: HomeRecommendationTypeFactoryImpl
) : BaseListAdapter<HomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(adapterTypeFactory) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when(getItemViewType(position)){
            ProductInfoDataModel.LAYOUT -> layout.isFullSpan = true
            RecommendationCarouselDataModel.LAYOUT -> layout.isFullSpan = true
            TitleDataModel.LAYOUT -> layout.isFullSpan = true
            EmptyViewHolder.LAYOUT -> layout.isFullSpan = true
            ErrorNetworkViewHolder.LAYOUT -> layout.isFullSpan = true
        }
        holder.bind(visitables[position])
    }

    override fun getItemCount(): Int = visitables.size

    override fun getItemViewType(position: Int): Int = visitables[position].type(adapterTypeFactory)
}