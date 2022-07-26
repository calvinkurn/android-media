package com.tokopedia.wishlistcollection.view.adapter.viewholder

import android.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.databinding.CollectionWishlistCreateItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.CREATE_NEW_COLLECTION_BG_IMAGE
import com.tokopedia.wishlist.util.WishlistV2Utils
import com.tokopedia.wishlistcollection.data.response.WishlistCollectionResponse
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionCreateItemViewHolder(
    private val binding: CollectionWishlistCreateItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WishlistCollectionTypeLayoutData) {
            if (item.dataObject is WishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Placeholder) {
                val params: GridLayout.LayoutParams = GridLayout.LayoutParams(binding.rlCreateWishlistCollection.layoutParams)
                params.width = WishlistV2Utils.toDp(154)
                params.height = WishlistV2Utils.toDp(154)
                binding.rlCreateWishlistCollection.layoutParams = params

                binding.labelNewCollection.text = item.dataObject.text
                if (item.dataObject.action == CREATE_COLLECTION) {
                    binding.wishlistCollectionCreateNew.setImageUrl(CREATE_NEW_COLLECTION_BG_IMAGE)
                    binding.rlCreateWishlistCollection.setOnClickListener { actionListener?.onCreateNewCollectionClicked() }
                }
            }
        }

    companion object {
        const val CREATE_COLLECTION = "CREATE_COLLECTION"
    }
}