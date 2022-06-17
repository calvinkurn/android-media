package com.tokopedia.wishlist.view.adapter.viewholder

import android.widget.GridLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.wishlist.data.model.CollectionWishlistTypeLayoutData
import com.tokopedia.wishlist.data.model.response.CollectionWishlistResponse
import com.tokopedia.wishlist.databinding.CollectionWishlistItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.SPEC_0
import com.tokopedia.wishlist.util.WishlistV2Consts.SPEC_2
import com.tokopedia.wishlist.util.WishlistV2Utils
import com.tokopedia.wishlist.view.adapter.CollectionWishlistAdapter

class CollectionWishlistItemViewHolder(
    private val binding: CollectionWishlistItemBinding,
    private val actionListener: CollectionWishlistAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CollectionWishlistTypeLayoutData) {
            if (item.dataObject is CollectionWishlistResponse.Data.GetWishlistCollections.WishlistCollectionResponseData.CollectionsItem) {
                binding.collectionTitle.text = item.dataObject.name
                binding.collectionDesc.text = "${item.dataObject.totalItem} ${item.dataObject.itemText}"
                when (item.dataObject.images.size) {
                    TOTAL_IMG_4 -> {
                        binding.singleCollectionItem.gone()
                        binding.glCollectionItem.visible()
                        binding.imgCollection1.setImageUrl(item.dataObject.images[0])
                        binding.imgCollection2.setImageUrl(item.dataObject.images[1])
                        binding.imgCollection3.setImageUrl(item.dataObject.images[2])
                        binding.imgCollection4.setImageUrl(item.dataObject.images[3])
                    }
                    TOTAL_IMG_3 -> {
                        binding.singleCollectionItem.gone()
                        binding.glCollectionItem.visible()
                        val params: GridLayout.LayoutParams = GridLayout.LayoutParams(binding.imgCollection1.layoutParams)
                        params.rowSpec = GridLayout.spec(SPEC_0, SPEC_2)
                        params.height = WishlistV2Utils.toDp(MERGE_SIZE)
                        binding.imgCollection1.apply {
                            layoutParams = params
                            setImageUrl(item.dataObject.images[0])
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        binding.imgCollection2.setImageUrl(item.dataObject.images[1])
                        binding.imgCollection3.setImageUrl(item.dataObject.images[2])
                        binding.imgCollection4.gone()
                    }
                    TOTAL_IMG_2 -> {
                        binding.singleCollectionItem.gone()
                        binding.glCollectionItem.visible()
                        val params1: GridLayout.LayoutParams = GridLayout.LayoutParams(binding.imgCollection1.layoutParams)
                        params1.rowSpec = GridLayout.spec(SPEC_0, SPEC_2)
                        params1.height = WishlistV2Utils.toDp(MERGE_SIZE)
                        binding.imgCollection1.apply {
                            layoutParams = params1
                            setImageUrl(item.dataObject.images[0])
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }

                        val params2: GridLayout.LayoutParams = GridLayout.LayoutParams(binding.imgCollection2.layoutParams)
                        params2.rowSpec = GridLayout.spec(SPEC_0, SPEC_2)
                        params2.height = WishlistV2Utils.toDp(MERGE_SIZE)
                        binding.imgCollection2.apply {
                            layoutParams = params2
                            setImageUrl(item.dataObject.images[1])
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        binding.imgCollection3.gone()
                        binding.imgCollection4.gone()
                    }
                    TOTAL_IMG_1 -> {
                        binding.glCollectionItem.gone()
                        binding.singleCollectionItem.apply {
                            visible()
                            setImageUrl(item.dataObject.images[0])
                        }
                    }
                }
            }
        }

    companion object {
        const val TOTAL_IMG_1 = 1
        const val TOTAL_IMG_2 = 2
        const val TOTAL_IMG_3 = 3
        const val TOTAL_IMG_4 = 4
        const val MERGE_SIZE = 154
    }
}