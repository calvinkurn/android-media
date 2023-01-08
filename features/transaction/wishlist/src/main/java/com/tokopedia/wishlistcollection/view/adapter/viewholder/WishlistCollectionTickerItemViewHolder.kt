package com.tokopedia.wishlistcollection.view.adapter.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.databinding.CollectionWishlistTickerItemBinding
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.BG_TICKER
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter

class WishlistCollectionTickerItemViewHolder(
    private val binding: CollectionWishlistTickerItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WishlistCollectionTypeLayoutData, isTickerClosed: Boolean) {
            if (item.dataObject is GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.Ticker) {
                if (item.dataObject.title.isNotEmpty() && !isTickerClosed) {
                    binding.root.visible()
                    val params = (binding.root.layoutParams as GridLayoutManager.LayoutParams).apply {
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                        width = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                    binding.root.layoutParams = params
                    binding.wishlistCollectionTickerTitle.text = item.dataObject.title
                    binding.wishlistCollectionTickerDesc.text = item.dataObject.description
                    binding.icCloseTickerCollectionWishlist.bringToFront()
                    binding.icCloseTickerCollectionWishlist.setOnClickListener { actionListener?.onCloseTicker() }
                    binding.wishlistCollectionTickerBg.loadImage(BG_TICKER)
                } else {
                    binding.root.gone()
                    val params = (binding.root.layoutParams as GridLayoutManager.LayoutParams).apply {
                        height = 0
                        width = 0
                    }
                    binding.root.layoutParams = params
                }
            }
        }
}