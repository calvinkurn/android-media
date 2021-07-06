package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.model.TopupBillsFavNumberShimmerDataView
import com.tokopedia.common.topupbills.view.typefactory.FavoriteNumberTypeFactory

class TopupBillsFavoriteNumberListAdapter (
        var visitables: List<Visitable<FavoriteNumberTypeFactory>>,
        private val typeFactory: FavoriteNumberTypeFactory
): RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(visitables[position])
    }

    override fun getItemViewType(position: Int): Int {
        return visitables[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return visitables.size
    }

    fun setNumbers(clientNumbers: List<TopupBillsFavNumberDataView>) {
        this.visitables = clientNumbers
        notifyDataSetChanged()
    }

    fun setEmptyState(emptyStateDataView: List<TopupBillsFavNumberEmptyDataView>) {
        this.visitables = emptyStateDataView
        notifyDataSetChanged()
    }

    fun setShimmer(shimmerDataView: List<TopupBillsFavNumberShimmerDataView>) {
        this.visitables = shimmerDataView
        notifyDataSetChanged()
    }

    fun setNotFound(notFoundDataView: List<TopupBillsFavNumberNotFoundDataView>) {
        this.visitables = notFoundDataView
        notifyDataSetChanged()
    }
}