package com.tokopedia.home_recom.view.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.*
import com.tokopedia.home_recom.model.datamodel.SimilarProductRecommendationDataModel
import com.tokopedia.home_recom.view.viewholder.SimilarProductLoadMoreViewHolder

/**
 * Created by Lukas on 26/08/19
 *
 * A Class of HomeRecommendationAdapter.
 *
 * This class for handling adapter recommendation page
 *
 * @property adapterTypeFactory the type factory for recommendationPage
 */
class SimilarProductRecommendationAdapter(
        private val adapterTypeFactory: SimilarProductRecommendationTypeFactoryImpl
) : BaseListAdapter<SimilarProductRecommendationDataModel, SimilarProductRecommendationTypeFactoryImpl>(adapterTypeFactory) {

    /**
     * This override function from [BaseListAdapter]
     * It return viewHolder
     * @param parent the parent of the view
     * @param viewType the type of view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    /**
     * This override void from [BaseListAdapter]
     * It handling binding viewHolder
     * @param holder the viewHolder on bind
     * @param position the position of the viewHolder
     */
    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when(getItemViewType(position)){
            EmptyViewHolder.LAYOUT -> layout.isFullSpan = true
            ErrorNetworkViewHolder.LAYOUT -> layout.isFullSpan = true
            LoadingShimmeringGridViewHolder.LAYOUT -> layout.isFullSpan = true
            SimilarProductLoadMoreViewHolder.LAYOUT -> layout.isFullSpan = true
        }
        holder.bind(visitables[position])
    }

    /**
     * This override function from [BaseListAdapter]
     * It return item count
     */
    override fun getItemCount(): Int = visitables.size

    /**
     * This override function from [BaseListAdapter]
     * It return viewType of the viewHolder
     */
    override fun getItemViewType(position: Int): Int = visitables[position].type(adapterTypeFactory)
}