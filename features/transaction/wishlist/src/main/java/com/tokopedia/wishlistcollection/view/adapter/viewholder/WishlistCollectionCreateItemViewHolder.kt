package com.tokopedia.wishlistcollection.view.adapter.viewholder

import android.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.databinding.CollectionWishlistCreateItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.CREATE_NEW_COLLECTION_BG_IMAGE
import com.tokopedia.wishlist.util.WishlistV2Utils
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionCreateItemViewHolder(
    private val binding: CollectionWishlistCreateItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WishlistCollectionTypeLayoutData) {
            if (item.dataObject is GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Placeholder) {
                val params: GridLayout.LayoutParams = GridLayout.LayoutParams(binding.rlCreateWishlistCollection.layoutParams)
                params.width = WishlistV2Utils.toDp(154)
                params.height = WishlistV2Utils.toDp(154)

                binding.run {
                    rlCreateWishlistCollection.layoutParams = params

                    labelNewCollection.text = item.dataObject.text
                    if (item.dataObject.action == CREATE_COLLECTION) {
                        wishlistCollectionCreateNew.apply {
                            loadImage(CREATE_NEW_COLLECTION_BG_IMAGE) {
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                        }
                        rlCreateWishlistCollection.setOnClickListener { actionListener?.onCreateNewCollectionClicked() }
                    }
                }
            }
        }

    companion object {
        const val CREATE_COLLECTION = "CREATE_COLLECTION"
    }
}