package com.tokopedia.wishlist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.WishlistV2TypeLayoutData
import com.tokopedia.wishlist.databinding.*
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_NOT_FOUND
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_EMPTY_STATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LOADER_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LOADER_LIST
import com.tokopedia.wishlist.view.adapter.viewholder.*
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment

/**
 * Created by fwidjaja on 14/10/21.
 */
class WishlistV2Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<WishlistV2TypeLayoutData>()
    private var isShowCheckbox = false

    companion object {
        const val LAYOUT_LOADER_LIST = 0
        const val LAYOUT_LOADER_GRID = 1
        const val LAYOUT_LIST = 2
        const val LAYOUT_GRID = 3
        const val LAYOUT_EMPTY_STATE = 4
        const val LAYOUT_RECOMMENDATION_TITLE = 5
        const val LAYOUT_RECOMMENDATION_LIST = 6
        const val LAYOUT_EMPTY_NOT_FOUND = 7
    }

    interface ActionListener {
//        fun onCariBarangClicked()
//        fun onNotFoundButtonClicked()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_LOADER_LIST -> {
                val binding = WishlistV2LoaderListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2ListLoaderViewHolder(binding)
            }
            LAYOUT_LOADER_GRID -> {
                // TODO : create loader grid
                val binding = WishlistV2LoaderListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2ListLoaderViewHolder(binding)
            }
            LAYOUT_LIST -> {
                val binding = WishlistV2ListItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2ListItemViewHolder(binding, actionListener)
            }
            LAYOUT_GRID -> {
                val binding = WishlistV2GridItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2GridItemViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_STATE -> {
                val binding = WishlistV2EmptyStateBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2EmptyStateViewHolder(binding, actionListener)
            }
            LAYOUT_EMPTY_NOT_FOUND -> {
                val binding = WishlistV2EmptyStateNotFoundItemBinding.inflate(LayoutInflater.from(parent.context), null, false)
                WishlistV2EmptyStateNotFoundViewHolder(binding, actionListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = listTypeData[position]
        when (holder) {
            is WishlistV2ListItemViewHolder-> {
                holder.bind(element, holder.adapterPosition, isShowCheckbox)
            }
            is WishlistV2GridItemViewHolder -> {
                holder.bind(element, holder.adapterPosition)
            }
            is WishlistV2EmptyStateViewHolder -> {
                holder.bind(element)
            }
            is WishlistV2EmptyStateNotFoundViewHolder -> {
                holder.bind(element)
            }
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            TYPE_LOADER_LIST -> LAYOUT_LOADER_LIST
            TYPE_LOADER_GRID -> LAYOUT_LOADER_GRID
            TYPE_LIST -> LAYOUT_LIST
            TYPE_GRID -> LAYOUT_GRID
            TYPE_EMPTY_STATE -> LAYOUT_EMPTY_STATE
            TYPE_EMPTY_NOT_FOUND -> LAYOUT_EMPTY_NOT_FOUND
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun addList(list: List<WishlistV2TypeLayoutData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun appendList(list: List<WishlistV2TypeLayoutData>) {
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(v2Fragment: WishlistV2Fragment) {
        this.actionListener = v2Fragment
    }

    fun showLoader() {
        listTypeData.clear()
        for (x in 0 until 5) {
            listTypeData.add(WishlistV2TypeLayoutData("", TYPE_LOADER_LIST))
        }
        notifyDataSetChanged()
    }

    fun showCheckbox() {
        isShowCheckbox = true
        notifyDataSetChanged()
    }

    fun hideCheckbox() {
        isShowCheckbox = false
        notifyDataSetChanged()
    }
}