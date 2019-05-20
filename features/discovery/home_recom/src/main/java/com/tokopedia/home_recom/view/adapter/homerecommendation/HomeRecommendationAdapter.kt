package com.tokopedia.home_recom.view.adapter.homerecommendation

import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_recom.model.dataModel.BaseHomeRecommendationDataModel
import com.tokopedia.home_recom.model.dataModel.ProductInfoDataModel
import com.tokopedia.home_recom.model.dataModel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.view.viewHolder.ProductInfoViewHolder

class HomeRecommendationAdapter(
        private val adapterTypeFactory: HomeRecommendationTypeFactoryImpl
) : BaseListAdapter<BaseHomeRecommendationDataModel, HomeRecommendationTypeFactoryImpl>(adapterTypeFactory) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when(getItemViewType(position)){
            ProductInfoDataModel.LAYOUT -> layout.isFullSpan = true
            RecommendationCarouselDataModel.LAYOUT -> layout.isFullSpan = true
        }
        holder.bind(visitables[position])
    }

    override fun getItemCount(): Int = visitables.size

    override fun getItemViewType(position: Int): Int = visitables[position].type(adapterTypeFactory)

    fun getItem(position: Int) = visitables[position]
}