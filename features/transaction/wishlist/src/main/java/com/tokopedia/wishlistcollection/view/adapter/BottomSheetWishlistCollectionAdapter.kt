package com.tokopedia.wishlistcollection.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.databinding.AddWishlistCollectionAdditionalSectionTextItemBinding
import com.tokopedia.wishlist.databinding.AddWishlistCollectionCreateNewItemBinding
import com.tokopedia.wishlist.databinding.AddWishlistCollectionItemBinding
import com.tokopedia.wishlist.databinding.AddWishlistCollectionMainSectionTextItemBinding
import com.tokopedia.wishlistcollection.data.model.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionHostBottomSheetFragment
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ADDITIONAL_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_COLLECTION_MAIN_SECTION
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TYPE_CREATE_NEW_COLLECTION

class BottomSheetWishlistCollectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var actionListener: ActionListener? = null
    private var listTypeData = mutableListOf<BottomSheetWishlistCollectionTypeLayoutData>()
    private var source = ""

    companion object {
        const val LAYOUT_MAIN_SECTION = 0
        const val LAYOUT_ADDITIONAL_SECTION = 1
        const val LAYOUT_COLLECTION_ITEM = 2
        const val LAYOUT_CREATE_NEW_COLLECTION = 3
    }

    interface ActionListener {
        fun onCollectionItemClicked(name: String, id: String)
        fun onCreateNewCollectionClicked(dataObject: GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet.Data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LAYOUT_MAIN_SECTION  -> {
                val binding = AddWishlistCollectionMainSectionTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionMainItemViewHolder(
                    binding
                )
            }
            LAYOUT_ADDITIONAL_SECTION -> {
                val binding = AddWishlistCollectionAdditionalSectionTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionAdditionalItemViewHolder(
                    binding
                )
            }
            LAYOUT_COLLECTION_ITEM -> {
                val binding = AddWishlistCollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionItemViewHolder(
                    binding
                )
            }
            LAYOUT_CREATE_NEW_COLLECTION -> {
                val binding = AddWishlistCollectionCreateNewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionCreateItemViewHolder(
                    binding
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val element = listTypeData[position]
        when (element.typeLayout) {
            TYPE_COLLECTION_MAIN_SECTION -> {
                (holder as com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionMainItemViewHolder).bind(element)
            }
            TYPE_COLLECTION_ADDITIONAL_SECTION -> {
                (holder as com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionAdditionalItemViewHolder).bind(element)
            }
            TYPE_COLLECTION_ITEM -> {
                (holder as com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionItemViewHolder).bind(element, actionListener, source)
            }
            TYPE_CREATE_NEW_COLLECTION -> {
                (holder as com.tokopedia.wishlistcollection.view.adapter.viewholder.BottomSheetWishlistCollectionCreateItemViewHolder).bind(element, actionListener)
            }
        }
    }

    override fun getItemCount(): Int {
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

    fun setActionListener(fragment: WishlistCollectionHostBottomSheetFragment) {
        this.actionListener = fragment
    }

    fun setActionListener(fragment: WishlistCollectionDetailFragment) {
        this.actionListener = fragment
    }

    fun setSource(pageOrigin: String) {
        source = pageOrigin
    }
}
