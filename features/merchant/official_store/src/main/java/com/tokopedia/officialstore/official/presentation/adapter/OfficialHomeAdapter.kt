package com.tokopedia.officialstore.official.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.officialstore.base.diffutil.OfficialAdapter
import com.tokopedia.officialstore.official.presentation.adapter.typefactory.OfficialHomeTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialHomeVisitable
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationVisitable

class OfficialHomeAdapter(private val adapterTypeFactory: OfficialHomeTypeFactory) :
    OfficialAdapter<Visitable<*>, OfficialHomeTypeFactory>(
        adapterTypeFactory,
        OfficialDiffCallback
    ) {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun bind(holder: AbstractViewHolder<Visitable<*>>, item: Visitable<*>) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = item !is ProductRecommendationDataModel
        holder.bind(item)
    }

    override fun bind(
        holder: AbstractViewHolder<Visitable<*>>,
        item: Visitable<*>,
        payloads: MutableList<Any>
    ) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.isFullSpan = item !is ProductRecommendationDataModel
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position) is HomeComponentVisitable -> {
                (getItem(position) as HomeComponentVisitable).type(adapterTypeFactory)
            }
            getItem(position) is OfficialHomeVisitable -> {
                (getItem(position) as OfficialHomeVisitable).type(adapterTypeFactory)
            }
            getItem(position) is RecommendationVisitable -> {
                (getItem(position) as RecommendationVisitable).type(adapterTypeFactory)
            }
            else -> {
                -1
            }
        }
    }
}