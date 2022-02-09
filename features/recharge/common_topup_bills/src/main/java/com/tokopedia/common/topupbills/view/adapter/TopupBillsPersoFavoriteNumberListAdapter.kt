package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberEmptyDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberErrorDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberNotFoundDataView
import com.tokopedia.common.topupbills.view.model.favoriteperso.TopupBillsPersoFavNumberShimmerDataView
import com.tokopedia.common.topupbills.view.typefactory.PersoFavoriteNumberTypeFactory

class TopupBillsPersoFavoriteNumberListAdapter(
    var visitables: List<Visitable<PersoFavoriteNumberTypeFactory>>,
    private val typeFactory: PersoFavoriteNumberTypeFactory
): RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<Visitable<*>> {
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

    fun setNumbers(clientNumbers: List<TopupBillsPersoFavNumberDataView>) {
        this.visitables = clientNumbers
        notifyDataSetChanged()
    }

    fun setEmptyState(emptyStateDataView: List<TopupBillsPersoFavNumberEmptyDataView>) {
        this.visitables = emptyStateDataView
        notifyDataSetChanged()
    }

    fun setShimmer(shimmerDataView: List<TopupBillsPersoFavNumberShimmerDataView>) {
        this.visitables = shimmerDataView
        notifyDataSetChanged()
    }

    fun setNotFound(notFoundDataView: List<TopupBillsPersoFavNumberNotFoundDataView>) {
        this.visitables = notFoundDataView
        notifyDataSetChanged()
    }

    fun setErrorState(errorDataView: List<TopupBillsPersoFavNumberErrorDataView>) {
        this.visitables = errorDataView
        notifyDataSetChanged()
    }
}