package com.tokopedia.wishlistcollection.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.databinding.CollectionWishlistCreateItemBinding
import com.tokopedia.wishlist.databinding.CollectionWishlistItemBinding
import com.tokopedia.wishlist.databinding.CollectionWishlistTickerItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_CREATE
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_COLLECTION_TICKER
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionCreateItemViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionItemViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionTickerItemViewHolder
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionFragment

class WishlistCollectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<WishlistCollectionTypeLayoutData>()
    private var isTickerCloseClicked = false

    companion object {
        const val LAYOUT_COLLECTION_TICKER = 0
        const val LAYOUT_COLLECTION_ITEM = 1
        const val LAYOUT_CREATE_COLLECTION = 2
    }

    interface ActionListener {
        fun onCloseTicker()
        fun onKebabMenuClicked()
        fun onCreateNewCollectionClicked()
        fun onCollectionItemClicked(id: String)
    }

    fun setActionListener(collectionWishlistFragment: WishlistCollectionFragment) {
        this.actionListener = collectionWishlistFragment
    }

    /*init { setHasStableIds(true) }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_COLLECTION_TICKER -> {
                val binding = CollectionWishlistTickerItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionTickerItemViewHolder(binding, actionListener)
            }
            LAYOUT_COLLECTION_ITEM -> {
                val binding = CollectionWishlistItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionItemViewHolder(binding, actionListener)
            }
            LAYOUT_CREATE_COLLECTION -> {
                val binding = CollectionWishlistCreateItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WishlistCollectionCreateItemViewHolder(binding, actionListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemView.layoutParams is GridLayoutManager.LayoutParams) {
            val element = listTypeData[position]
            when (element.typeLayout) {
                TYPE_COLLECTION_TICKER -> {
                    (holder as WishlistCollectionTickerItemViewHolder).bind(element, isTickerCloseClicked)
                }
                TYPE_COLLECTION_ITEM -> {
                    (holder as WishlistCollectionItemViewHolder).bind(element)
                }
                TYPE_COLLECTION_CREATE -> {
                    (holder as WishlistCollectionCreateItemViewHolder).bind(element)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            TYPE_COLLECTION_TICKER -> LAYOUT_COLLECTION_TICKER
            TYPE_COLLECTION_ITEM -> LAYOUT_COLLECTION_ITEM
            TYPE_COLLECTION_CREATE -> LAYOUT_CREATE_COLLECTION
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun addList(list: List<WishlistCollectionTypeLayoutData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun hideTicker() {
        isTickerCloseClicked = true
        notifyItemChanged(0)
    }

    fun resetTicker() {
        isTickerCloseClicked = false
    }
}