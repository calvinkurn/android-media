package com.tokopedia.wishlistcommon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionAdditionalSectionTextItemBinding
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionCreateNewItemBinding
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionItemBinding
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionMainSectionTextItemBinding
import com.tokopedia.wishlistcommon.data.AddToWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ADDITIONAL_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_MAIN_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_CREATE_NEW_COLLECTION
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetCollectionWishlistAdditionalItemViewHolder
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetCollectionWishlistCreateItemViewHolder
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetCollectionWishlistItemViewHolder
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetCollectionWishlistMainItemViewHolder
import com.tokopedia.wishlistcommon.view.bottomsheet.BottomSheetAddCollectionWishlist

class BottomSheetCollectionWishlistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<AddToWishlistCollectionTypeLayoutData>()

    companion object {
        const val LAYOUT_MAIN_SECTION = 0
        const val LAYOUT_ADDITIONAL_SECTION = 1
        const val LAYOUT_COLLECTION_ITEM = 2
        const val LAYOUT_CREATE_NEW_COLLECTION = 3
    }

    interface ActionListener {
        fun onCollectionItemClicked()
        fun onCreateNewCollectionClicked()
    }

    init { setHasStableIds(true) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_MAIN_SECTION  -> {
                val binding = AddWishlistCollectionMainSectionTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetCollectionWishlistMainItemViewHolder(binding)
            }
            LAYOUT_ADDITIONAL_SECTION -> {
                val binding = AddWishlistCollectionAdditionalSectionTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetCollectionWishlistAdditionalItemViewHolder(binding)
            }
            LAYOUT_COLLECTION_ITEM -> {
                val binding = AddWishlistCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetCollectionWishlistItemViewHolder(binding, actionListener)
            }
            LAYOUT_CREATE_NEW_COLLECTION -> {
                val binding = AddWishlistCollectionCreateNewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetCollectionWishlistCreateItemViewHolder(binding, actionListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val element = listTypeData[position]
            when (element.typeLayout) {
                TYPE_COLLECTION_MAIN_SECTION -> {
                    (holder as BottomSheetCollectionWishlistMainItemViewHolder).bind(element)
                }
                TYPE_COLLECTION_ADDITIONAL_SECTION -> {
                    (holder as BottomSheetCollectionWishlistAdditionalItemViewHolder).bind(element)
                }
                TYPE_COLLECTION_ITEM -> {
                    (holder as BottomSheetCollectionWishlistItemViewHolder).bind(element)
                }
                TYPE_CREATE_NEW_COLLECTION -> {
                    (holder as BottomSheetCollectionWishlistCreateItemViewHolder).bind(element)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            TYPE_COLLECTION_MAIN_SECTION -> LAYOUT_MAIN_SECTION
            TYPE_COLLECTION_ADDITIONAL_SECTION -> LAYOUT_ADDITIONAL_SECTION
            TYPE_COLLECTION_ITEM -> LAYOUT_COLLECTION_ITEM
            TYPE_CREATE_NEW_COLLECTION -> LAYOUT_CREATE_NEW_COLLECTION
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun addList(list: List<AddToWishlistCollectionTypeLayoutData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    fun setActionListener(bottomsheet: BottomSheetAddCollectionWishlist) {
        this.actionListener = bottomsheet
    }
}