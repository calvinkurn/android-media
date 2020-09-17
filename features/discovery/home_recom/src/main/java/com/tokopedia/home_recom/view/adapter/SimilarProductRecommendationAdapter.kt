package com.tokopedia.home_recom.view.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.*
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.SimilarProductRecommendationDataModel
import com.tokopedia.home_recom.view.viewholder.SimilarProductLoadMoreViewHolder
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.recommendation_filter_chip.view.*

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

class SimilarRecommendationFilterAdapter(private val listener: FilterChipListener): RecyclerView.Adapter<SimilarRecommendationFilterAdapter.SimilarRecommendationFilterViewHolder>(){
    private val filters = mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarRecommendationFilterViewHolder {
        return SimilarRecommendationFilterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recommendation_filter_chip, parent, false), listener)
    }

    override fun getItemCount(): Int = filters.size

    override fun onBindViewHolder(holder: SimilarRecommendationFilterViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    fun submitFilter(list: List<RecommendationFilterChipsEntity.RecommendationFilterChip>){
        val callback = DiffUtil.calculateDiff(FilterDiffUtilCallback(filters, list))
        callback.dispatchUpdatesTo(this)
        filters.clear()
        filters.addAll(list)
    }

    class SimilarRecommendationFilterViewHolder(itemView: View, private val listener: FilterChipListener): RecyclerView.ViewHolder(itemView) {
        fun bind(filterChip: RecommendationFilterChipsEntity.RecommendationFilterChip){
            itemView.annotation_chip?.chipText = filterChip.name
            itemView.annotation_chip?.chipType = if(filterChip.isActivated) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
            itemView.setOnClickListener {
                listener.onFilterAnnotationClicked(filterChip, adapterPosition)
            }
        }
    }

    interface FilterChipListener{
        fun onFilterAnnotationClicked(filterChip: RecommendationFilterChipsEntity.RecommendationFilterChip, position: Int)
    }

    class FilterDiffUtilCallback (private val oldList: List<RecommendationFilterChipsEntity.RecommendationFilterChip>, private val newList: List<RecommendationFilterChipsEntity.RecommendationFilterChip>): DiffUtil.Callback(){
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].value == newList[newItemPosition].value && oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].isActivated == newList[newItemPosition].isActivated && oldList[oldItemPosition].value == newList[newItemPosition].value
        }

    }
}