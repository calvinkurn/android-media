package com.tokopedia.wishlistcollection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.databinding.AddWishlistCollectionItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlistcollection.data.model.BottomSheetWishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.SRC_WISHLIST_COLLECTION_BULK_ADD
import com.tokopedia.wishlistcollection.view.adapter.BottomSheetCollectionWishlistAdapter

class BottomSheetWishlistCollectionItemViewHolder(
    private val binding: AddWishlistCollectionItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: BottomSheetWishlistCollectionTypeLayoutData,
        actionListener: BottomSheetCollectionWishlistAdapter.ActionListener?,
        source: String
    ) {
        if (item.dataObject is GetWishlistCollectionsBottomSheetResponse.GetWishlistCollectionsBottomsheet.Data.MainSection.CollectionsItem) {
            binding.run {
                if (item.dataObject.label.isNotEmpty()) {
                    labelCollectionItem.visible()
                    labelCollectionItem.text = item.dataObject.label
                } else {
                    labelCollectionItem.gone()
                }
                if (item.dataObject.imageUrl.isNotEmpty()) {
                    collectionItemImage.apply {
                        loadImage(item.dataObject.imageUrl) {
                            setCacheStrategy(MediaCacheStrategy.NONE)
                            setPlaceHolder(R.drawable.placeholder_img)
                        }
                    }
                } else {
                    collectionItemImage.apply {
                        clearImage()
                        loadImage(R.drawable.placeholder_img) {
                            setCacheStrategy(MediaCacheStrategy.NONE)
                        }
                    }
                }

                mainCollectionItemName.text = item.dataObject.name
                mainCollectionTotalItem.text =
                    "${item.dataObject.totalItem} ${item.dataObject.itemText}"

                if (item.dataObject.isContainProduct && source != SRC_WISHLIST_COLLECTION_BULK_ADD) {
                    icCheck.visible()
                } else {
                    icCheck.gone()
                }

                root.setOnClickListener { actionListener?.onCollectionItemClicked(item.dataObject.name, item.dataObject.id) }
            }
        }
    }
}