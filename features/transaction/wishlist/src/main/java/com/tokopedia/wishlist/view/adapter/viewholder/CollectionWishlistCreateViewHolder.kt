package com.tokopedia.wishlist.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlist.data.model.CollectionWishlistTypeLayoutData
import com.tokopedia.wishlist.data.model.response.CollectionWishlistResponse
import com.tokopedia.wishlist.databinding.CollectionWishlistCreateItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.CREATE_NEW_COLLECTION_BG_IMAGE
import com.tokopedia.wishlist.view.adapter.CollectionWishlistAdapter

class CollectionWishlistCreateViewHolder(
    private val binding: CollectionWishlistCreateItemBinding,
    private val actionListener: CollectionWishlistAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CollectionWishlistTypeLayoutData) {
            if (item.dataObject is CollectionWishlistResponse.Data.GetWishlistCollections.WishlistCollectionResponseData.Placeholder) {
                binding.labelNewCollection.text = item.dataObject.text
                if (item.dataObject.action == CREATE_COLLECTION) {
                    binding.wishlistCollectionCreateNew.setImageUrl(CREATE_NEW_COLLECTION_BG_IMAGE)
                    binding.root.setOnClickListener { actionListener?.onCreateNewCollection() }
                }
            }
        }

    companion object {
        const val CREATE_COLLECTION = "CREATE_COLLECTION"
    }
}