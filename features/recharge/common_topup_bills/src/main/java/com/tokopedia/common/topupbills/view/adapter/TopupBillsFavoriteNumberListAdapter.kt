package com.tokopedia.common.topupbills.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoritNumberBinding
import com.tokopedia.common.topupbills.databinding.ItemTopupBillsFavoriteNumberEmptyStateBinding
import com.tokopedia.common.topupbills.view.listener.FavoriteNumberEmptyStateListener
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.common.topupbills.view.model.FavoriteNumberDataView
import com.tokopedia.common.topupbills.view.model.FavoriteNumberEmptyDataView
import com.tokopedia.common.topupbills.view.typefactory.FavoriteNumberTypeFactory
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberEmptyViewHolder
import com.tokopedia.common.topupbills.view.viewholder.FavoriteNumberViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class TopupBillsFavoriteNumberListAdapter (
        var visitables: List<Visitable<FavoriteNumberTypeFactory>>,
        private val typeFactory: FavoriteNumberTypeFactory
): RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
//        return if (viewType == VIEW_TYPE_FAVORITE_NUMBER) {
//            val binding = ItemTopupBillsFavoritNumberBinding.inflate(
//                    LayoutInflater.from(parent.context), parent, false)
//            FavoriteNumberViewHolder(binding, favoriteNumberListener)
//        } else {
//            val binding = ItemTopupBillsFavoriteNumberEmptyStateBinding.inflate(
//                    LayoutInflater.from(parent.context), parent, false)
//            FavoriteNumberEmptyViewHolder(binding, emptyStateListener)
//        }
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
//        val viewType = getItemViewType(position)
//        when (viewType) {
//            VIEW_TYPE_FAVORITE_NUMBER -> {
//                val data = clientNumbers[position] as TopupBillsSeamlessFavNumberItem
//                (holder as FavoriteNumberViewHolder).bind(data)
//            }
//            VIEW_TYPE_EMPTY_STATE -> {
//                (holder as FavoriteNumberEmptyViewHolder).bind()
//            }
//        }
        holder.bind(visitables[position])
    }

    override fun getItemViewType(position: Int): Int {
//        return if (visitables.isEmpty()) {
//            VIEW_TYPE_EMPTY_STATE
//        } else {
//            VIEW_TYPE_FAVORITE_NUMBER
//        }
        return visitables[position].type(typeFactory)
    }

    override fun getItemCount(): Int {
        return if (visitables.isEmpty()) 1 else visitables.size
    }

    fun setNumbers(clientNumbers: List<FavoriteNumberDataView>) {
        this.visitables = clientNumbers
        notifyDataSetChanged()
    }

    fun setEmptyState(emptyStateDataView: List<FavoriteNumberEmptyDataView>) {
        this.visitables = emptyStateDataView
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_FAVORITE_NUMBER = 1
        private const val VIEW_TYPE_EMPTY_STATE = 2
    }
}