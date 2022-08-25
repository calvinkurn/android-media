package com.tokopedia.wishlistcollection.view.adapter.viewholder

import android.widget.GridLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.wishlist.R
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.databinding.CollectionWishlistItemBinding
import com.tokopedia.wishlist.util.WishlistV2Consts.SPEC_0
import com.tokopedia.wishlist.util.WishlistV2Consts.SPEC_2
import com.tokopedia.wishlist.util.WishlistV2Utils
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionItemViewHolder(
    private val binding: CollectionWishlistItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
    var isAllWishlist = false
        fun bind(item: WishlistCollectionTypeLayoutData) {
            if (item.dataObject is GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.CollectionsItem) {
                binding.root.setOnClickListener {
                    actionListener?.onCollectionItemClicked(item.dataObject.id)
                }
                binding.cardCollectionItem.setOnClickListener {
                    actionListener?.onCollectionItemClicked(item.dataObject.id)
                }
                binding.cardCollectionItem.cardType = CardUnify2.TYPE_SHADOW
                binding.collectionTitle.text = item.dataObject.name
                binding.collectionDesc.text = "${item.dataObject.totalItem} ${item.dataObject.itemText}"
                if (item.dataObject.name == SEMUA_WISHLIST) {
                    isAllWishlist = true
                    binding.collectionKebabMenu.gone()
                }
            else {
                binding.collectionKebabMenu.visible()
                binding.collectionKebabMenu.setOnClickListener {
                    actionListener?.onKebabMenuClicked(
                        item.dataObject.id,
                        item.dataObject.name
                    )
                }
            }
            if (item.dataObject.images.isEmpty()) {
                binding.glCollectionItem.gone()
                binding.singleCollectionItem.apply {
                    visible()
                    clearImage()
                    loadImage(R.drawable.placeholder_img) {
                        setCacheStrategy(MediaCacheStrategy.NONE)
                    }
                }
            } else {
                when (item.dataObject.images.size) {
                    TOTAL_IMG_4 -> {
                        binding.singleCollectionItem.gone()
                        binding.glCollectionItem.visible()
                        binding.imgCollection1.apply {
                            visible()
                            loadImage(item.dataObject.images[0]) {
                                overrideSize(Resize(binding.imgCollection1.width, binding.imgCollection1.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        binding.imgCollection2.apply {
                            visible()
                            loadImage(item.dataObject.images[1]) {
                                overrideSize(Resize(binding.imgCollection2.width, binding.imgCollection2.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        binding.imgCollection3.apply {
                            visible()
                            loadImage(item.dataObject.images[2]) {
                                overrideSize(Resize(binding.imgCollection3.width, binding.imgCollection3.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        binding.imgCollection4.apply {
                            visible()
                            loadImage(item.dataObject.images[3]) {
                                overrideSize(Resize(binding.imgCollection4.width, binding.imgCollection4.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                    }
                    TOTAL_IMG_3 -> {
                        binding.singleCollectionItem.gone()
                        binding.glCollectionItem.visible()
                        val params1: GridLayout.LayoutParams =
                            GridLayout.LayoutParams(binding.imgCollection1.layoutParams)
                        params1.rowSpec = GridLayout.spec(SPEC_0, SPEC_2, 1.0F)
                        params1.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                        params1.setMargins(0, 0, WishlistV2Utils.toDp(3), 0)
                        binding.imgCollection1.apply {
                            visible()
                            layoutParams = params1
                            loadImage(item.dataObject.images[0]) {
                                overrideSize(Resize(binding.imgCollection1.width, binding.imgCollection1.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val params2: GridLayout.LayoutParams =
                            GridLayout.LayoutParams(binding.imgCollection2.layoutParams)
                        params2.rowSpec = GridLayout.spec(SPEC_0, 1, 1.0F)
                        params2.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                        params2.setMargins(0, 0, 0, WishlistV2Utils.toDp(3))
                        binding.imgCollection2.apply {
                            visible()
                            layoutParams = params2
                            loadImage(item.dataObject.images[1]) {
                                overrideSize(Resize(binding.imgCollection2.width, binding.imgCollection2.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        val params3: GridLayout.LayoutParams =
                            GridLayout.LayoutParams(binding.imgCollection2.layoutParams)
                        params3.rowSpec = GridLayout.spec(SPEC_0, 1, 1.0F)
                        params3.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                        binding.imgCollection3.apply {
                            visible()
                            layoutParams = params3
                            loadImage(item.dataObject.images[2]) {
                                overrideSize(Resize(binding.imgCollection3.width, binding.imgCollection3.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        binding.imgCollection4.gone()
                    }
                    TOTAL_IMG_2 -> {
                        binding.singleCollectionItem.gone()
                        binding.glCollectionItem.visible()
                        val params1: GridLayout.LayoutParams =
                            GridLayout.LayoutParams(binding.imgCollection1.layoutParams)
                        params1.rowSpec = GridLayout.spec(SPEC_0, SPEC_2, 1.0F)
                        params1.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                        params1.setMargins(0, 0, WishlistV2Utils.toDp(3), 0)
                        binding.imgCollection1.apply {
                            visible()
                            layoutParams = params1
                            loadImage(item.dataObject.images[0]) {
                                overrideSize(Resize(binding.imgCollection1.width, binding.imgCollection1.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }

                        val params2: GridLayout.LayoutParams =
                            GridLayout.LayoutParams(binding.imgCollection2.layoutParams)
                        params2.rowSpec = GridLayout.spec(SPEC_0, SPEC_2, 1.0F)
                        params2.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1.0F)
                        binding.imgCollection2.apply {
                            visible()
                            layoutParams = params2
                            loadImage(item.dataObject.images[1]) {
                                overrideSize(Resize(binding.imgCollection2.width, binding.imgCollection2.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                        binding.imgCollection3.gone()
                        binding.imgCollection4.gone()
                    }
                    TOTAL_IMG_1 -> {
                        binding.glCollectionItem.gone()
                        binding.singleCollectionItem.apply {
                            visible()
                            loadImage(item.dataObject.images[0]) {
                                overrideSize(Resize(binding.singleCollectionItem.width, binding.singleCollectionItem.height))
                                setCacheStrategy(MediaCacheStrategy.NONE)
                                setPlaceHolder(R.drawable.placeholder_img)
                            }
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
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
        const val SEMUA_WISHLIST = "Semua Wishlist"
    }
}