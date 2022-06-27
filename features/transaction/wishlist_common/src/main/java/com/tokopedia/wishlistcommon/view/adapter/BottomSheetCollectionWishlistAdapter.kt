package com.tokopedia.wishlistcommon.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionAdditionalSectionTextItemBinding
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionCreateNewItemBinding
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionItemBinding
import com.tokopedia.wishlist_common.databinding.AddWishlistCollectionMainSectionTextItemBinding
import com.tokopedia.wishlistcommon.data.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ADDITIONAL_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_MAIN_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_CREATE_NEW_COLLECTION
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetWishlistCollectionAdditionalItemViewHolder
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetWishlistCollectionCreateItemViewHolder
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetWishlistCollectionItemViewHolder
import com.tokopedia.wishlistcommon.view.adapter.viewholder.BottomSheetWishlistCollectionMainItemViewHolder

class BottomSheetCollectionWishlistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<BottomSheetWishlistCollectionTypeLayoutData>()

    companion object {
        const val LAYOUT_MAIN_SECTION = 0
        const val LAYOUT_ADDITIONAL_SECTION = 1
        const val LAYOUT_COLLECTION_ITEM = 2
        const val LAYOUT_CREATE_NEW_COLLECTION = 3
    }

    /*interface ActionListener {
        fun onCollectionItemClicked()
        fun onCreateNewCollectionClicked()
    }*/

    /*init { setHasStableIds(true) }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_MAIN_SECTION  -> {
                val binding = AddWishlistCollectionMainSectionTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetWishlistCollectionMainItemViewHolder(binding)
            }
            LAYOUT_ADDITIONAL_SECTION -> {
                val binding = AddWishlistCollectionAdditionalSectionTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetWishlistCollectionAdditionalItemViewHolder(binding)
            }
            LAYOUT_COLLECTION_ITEM -> {
                val binding = AddWishlistCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetWishlistCollectionItemViewHolder(binding)
            }
            LAYOUT_CREATE_NEW_COLLECTION -> {
                val binding = AddWishlistCollectionCreateNewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BottomSheetWishlistCollectionCreateItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = listTypeData[position]
        when (element.typeLayout) {
            TYPE_COLLECTION_MAIN_SECTION -> {
                (holder as BottomSheetWishlistCollectionMainItemViewHolder).bind(element)
            }
            TYPE_COLLECTION_ADDITIONAL_SECTION -> {
                (holder as BottomSheetWishlistCollectionAdditionalItemViewHolder).bind(element)
            }
            TYPE_COLLECTION_ITEM -> {
                (holder as BottomSheetWishlistCollectionItemViewHolder).bind(element)
            }
            TYPE_CREATE_NEW_COLLECTION -> {
                (holder as BottomSheetWishlistCollectionCreateItemViewHolder).bind(element)
            }
        }
    }

    override fun getItemCount(): Int {
        println("++ size = ${listTypeData.size}")
        return listTypeData.size
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

    fun addList(list: List<BottomSheetWishlistCollectionTypeLayoutData>) {
        listTypeData.clear()
        listTypeData.addAll(list)
        notifyDataSetChanged()
    }

    /*fun setActionListener(bottomsheet: BottomSheetAddCollectionWishlist) {
        this.actionListener = bottomsheet
    }*/
}