package com.tokopedia.wishlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.RevampedWishlistTypeData
import com.tokopedia.wishlist.databinding.RevampedWishlistGridItemBinding
import com.tokopedia.wishlist.databinding.RevampedWishlistListItemBinding
import com.tokopedia.wishlist.util.RevampedWishlistConsts
import com.tokopedia.wishlist.view.adapter.viewholder.RevampWishlistItemListViewHolder
import com.tokopedia.wishlist.view.adapter.viewholder.RevampedWishlistItemGridViewHolder
import com.tokopedia.wishlist.view.fragment.RevampedWishlistFragment

/**
 * Created by fwidjaja on 14/10/21.
 */
class RevampedWishlistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    var listTypeData = mutableListOf<RevampedWishlistTypeData>()

    companion object {
        const val LAYOUT_LOADER = 0
        const val LAYOUT_LIST = 2
        const val LAYOUT_GRID = 2
        const val LAYOUT_EMPTY_STATE = 3
        const val LAYOUT_RECOMMENDATION_TITLE = 4
        const val LAYOUT_RECOMMENDATION_LIST = 5
    }

    interface ActionListener {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_LIST -> {
                val binding = RevampedWishlistListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                RevampWishlistItemListViewHolder(binding, actionListener)
            }
            LAYOUT_GRID -> {
                val binding = RevampedWishlistGridItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                RevampedWishlistItemGridViewHolder(binding, actionListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = listTypeData[position]
        when (holder) {
            is RevampWishlistItemListViewHolder-> {
                holder.bind(element, holder.adapterPosition)
            }
            is RevampedWishlistItemGridViewHolder -> {
                holder.bind(element, holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            RevampedWishlistConsts.TYPE_LIST -> LAYOUT_LIST
            RevampedWishlistConsts.TYPE_GRID -> LAYOUT_GRID
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun addList(list: List<RevampedWishlistTypeData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(fragment: RevampedWishlistFragment) {
        this.actionListener = fragment
    }
}