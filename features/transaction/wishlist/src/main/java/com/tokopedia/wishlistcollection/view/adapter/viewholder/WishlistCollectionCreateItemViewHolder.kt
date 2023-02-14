package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.CollectionWishlistCreateItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.CREATE_NEW_COLLECTION_BG_IMAGE
import com.tokopedia.wishlistcollection.analytics.WishlistCollectionAnalytics
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionCreateItemViewHolder(
    private val binding: CollectionWishlistCreateItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: WishlistCollectionTypeLayoutData) {
        if (item.dataObject is GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Placeholder) {
            binding.run {
                labelNewCollection.text = item.dataObject.text
                if (item.dataObject.action == CREATE_COLLECTION) {
                    wishlistCollectionCreateNew.apply {
                        loadImage(CREATE_NEW_COLLECTION_BG_IMAGE) {
                            setCacheStrategy(MediaCacheStrategy.NONE)
                            setPlaceHolder(R.drawable.placeholder_img)
                        }
                    }
                    clCreateCollection.setOnClickListener {
                        actionListener?.onCreateNewCollectionClicked()
                        WishlistCollectionAnalytics.sendCreateNewCollectionOnWishlistPageEvent()
                    }
                }
            }
        }
    }

    companion object {
        const val CREATE_COLLECTION = "CREATE_COLLECTION"
    }
}
